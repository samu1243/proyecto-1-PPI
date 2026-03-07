package controlador;
/**
 *
 * @author Juan Ferreira,Valeria Torres
 *      
 */
import modelo.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class Controlador {
    private GrafoPPI grafo;
    private File archivoActual;
    private boolean modificado;

    public Controlador() {
        this.grafo = new GrafoPPI();
        this.modificado = false;
    }

    public GrafoPPI getGrafo() { return grafo; }
    public boolean isModificado() { return modificado; }
    public void setModificado(boolean m) { this.modificado = m; }
    public File getArchivoActual() { return archivoActual; }
    public void setArchivoActual(File archivo) { this.archivoActual = archivo; }
    
    // load maestro.csv si existe si no pregunta si quiere buscarlo
    public void cargarDefault(JFrame parent) {
        File archivo = new File("maestro.csv");
        if (archivo.exists()) {
            this.archivoActual = archivo;
            this.grafo = new GrafoPPI();
            leerArchivo(archivo);
            this.modificado = false;
            JOptionPane.showMessageDialog(parent,
                "Archivo maestro.csv cargado correctamente.\nProteínas: " + grafo.getVertices().getMagnitud(),
                "Carga exitosa",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            int opcion = JOptionPane.showConfirmDialog(parent,
                "No se encontró el archivo maestro.csv.\n¿Desea seleccionar un archivo manualmente?",
                "Archivo no encontrado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                cargarArchivo(parent);
            } else {
                this.grafo = new GrafoPPI();
                this.modificado = false;
            }
        }
    }

    public void leerArchivo(File archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 2) {
                    String p1 = partes[0].trim();
                    String p2 = partes[1].trim();
                    double peso = (partes.length >= 3) ? Double.parseDouble(partes[2].trim()) : 1.0;
                    grafo.agregarVertice(p1);
                    grafo.agregarVertice(p2);
                    grafo.agregarArista(p1, p2, peso);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }

    public boolean cargarArchivo(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));
        int seleccion = fileChooser.showOpenDialog(parent);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            this.archivoActual = fileChooser.getSelectedFile();
            this.grafo = new GrafoPPI();
            leerArchivo(archivoActual);
            this.modificado = false;
            return true;
        }
        return false;
    }
  
    public ListaEnlazada getComponentesBFS() {
        return grafo.getComponentesConexosBFS();
    }

    // Métodos de modificación
    public void agregarInteraccion(String origen, String destino, double peso) {
        grafo.agregarVertice(origen);
        grafo.agregarVertice(destino);
        grafo.agregarArista(origen, destino, peso);
        modificado = true;
    }

    public boolean eliminarProteina(String id) {
        boolean ok = grafo.eliminarVertice(id);
        if (ok) modificado = true;
        return ok;
    }

    public boolean eliminarInteraccion(String origen, String destino) {
        boolean ok = grafo.eliminarArista(origen, destino);
        if (ok) modificado = true;
        return ok;
    }

    // Persistencia
    public void guardarArchivo(JFrame parent) {
        if (archivoActual == null) {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));
            if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                archivoActual = fc.getSelectedFile();
                if (!archivoActual.getName().toLowerCase().endsWith(".csv")) {
                    archivoActual = new File(archivoActual.getAbsolutePath() + ".csv");
                }
            } else {
                return;
            }
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoActual))) {
            ListaEnlazada vertices = grafo.getVertices();
            int n = vertices.getMagnitud();
            boolean[][] guardadas = new boolean[n][n];

            for (int i = 0; i < n; i++) {
                Vertice v = (Vertice) vertices.obtener(i);
                ListaEnlazada ady = grafo.getAdyacencia()[i];
                for (int j = 0; j < ady.getMagnitud(); j++) {
                    Arista a = (Arista) ady.obtener(j);
                    int idxDest = grafo.buscarVertice(a.getDestino().getId());
                    if (!guardadas[i][idxDest] && !guardadas[idxDest][i]) {
                        pw.println(a.getOrigen().getId() + "," + a.getDestino().getId() + "," + a.getPeso());
                        guardadas[i][idxDest] = true;
                    }
                }
            }
            modificado = false;
            JOptionPane.showMessageDialog(parent, "Archivo guardado en: " + archivoActual.getPath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean confirmarGuardar(JFrame parent) {
        if (!modificado) return true;
        int opcion = JOptionPane.showConfirmDialog(parent,
            "Hay cambios sin guardar. ¿Desea guardarlos antes de continuar?",
            "Cambios no guardados",
            JOptionPane.YES_NO_CANCEL_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            guardarArchivo(parent);
            return true;
        } else if (opcion == JOptionPane.NO_OPTION) {
            return true;
        } else {
            return false;
        }
    }
}