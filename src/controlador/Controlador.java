package controlador;

import modelo.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;


 //(carga y guardado de archivos CSV) y la comunicacion
 //entre la GUI y grafo ppi.
  
 //@author Juan Ferreira
 
public class Controlador {
    private GrafoPPI grafo;          
    private File archivoActual;       
    private boolean modificado;       

    
    //constructor 
    
    public Controlador() {
        this.grafo = new GrafoPPI();
        this.modificado = false;
    }

    public GrafoPPI getGrafo() { return grafo; }

    
    //si el grafo a sido modificado desde la ultima carga
     
    public boolean isModificado() { return modificado; }

    
    public void setModificado(boolean m) { this.modificado = m; }

    //validaciones a la hora con csv
    public boolean cargarArchivo(JFrame parent) {
        if (modificado) {
            int opcion = JOptionPane.showConfirmDialog(parent,
                    "Hay cambios no guardados. ¿Desea guardarlos antes de cargar un nuevo archivo?",
                    "Confirmar", JOptionPane.YES_NO_CANCEL_OPTION);
            if (opcion == JOptionPane.CANCEL_OPTION) return false;
            if (opcion == JOptionPane.YES_OPTION) {
                guardarArchivo(parent);
            }
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));
        int result = chooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (cargarDesdeFile(file)) {
                archivoActual = file;
                modificado = false;
                return true;
            } else {
                JOptionPane.showMessageDialog(parent, "Error al cargar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    
    //load del contenido csv en un nuevo grafo y lo asigna como el grafo actual
     
    private boolean cargarDesdeFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            GrafoPPI nuevo = new GrafoPPI();
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) {
                    primera = false;
                    if (linea.startsWith("Proteína_Origen")) continue;
                }
                String[] partes = linea.split(",");
                if (partes.length >= 3) {
                    String origen = partes[0].trim();
                    String destino = partes[1].trim();
                    double peso;
                    try {
                        peso = Double.parseDouble(partes[2].trim());
                    } catch (NumberFormatException e) {
                        continue; // valida para solo peso numerico
                    }
                    nuevo.agregarArista(origen, destino, peso);
                }
            }
            this.grafo = nuevo;
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    
    public boolean guardarArchivo(JFrame parent) {
        if (archivoActual == null) {
            return guardarComo(parent);
        } else {
            return guardarEnFile(archivoActual);
        }
    }

    public boolean guardarComo(JFrame parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));
        int result = chooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            if (guardarEnFile(file)) {
                archivoActual = file;
                modificado = false;
                return true;
            } else {
                JOptionPane.showMessageDialog(parent, "Error al guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    private boolean guardarEnFile(File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("Proteína_Origen,Proteína_Destino,Peso");
            bw.newLine();

            ListaEnlazada vertices = grafo.getVertices();
            boolean[][] guardadas = new boolean[vertices.getMagnitud()][vertices.getMagnitud()];

            for (int i = 0; i < vertices.getMagnitud(); i++) {
                Vertice v = (Vertice) vertices.obtener(i);
                ListaEnlazada aristas = grafo.getAdyacencia()[i];
                for (int j = 0; j < aristas.getMagnitud(); j++) {
                    Arista a = (Arista) aristas.obtener(j);
                    int idxDest = grafo.buscarVertice(a.getDestino().getId());
                    if (i < idxDest && !guardadas[i][idxDest]) {
                        bw.write(v.getId() + "," + a.getDestino().getId() + "," + a.getPeso());
                        bw.newLine();
                        guardadas[i][idxDest] = true;
                    }
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //Intenta cargar el archivo por default "dataset.csv" desde el directorio del programa
    
    public void cargarDefault() {
        File defaultFile = new File("dataset.csv");
        if (defaultFile.exists()) {
            cargarDesdeFile(defaultFile);
            archivoActual = defaultFile;
            modificado = false;
        }
    }
  
     //Agg nueva proteina al grafo
     
    public boolean agregarProteina(String id) {
        boolean ok = grafo.agregarVertice(id);
        if (ok) modificado = true;
        return ok;
    }

    //delete proteína del grafo junto las interacciones
     
    public boolean eliminarProteina(String id) {
        boolean ok = grafo.eliminarVertice(id);
        if (ok) modificado = true;
        return ok;
    }


    //Agg arista entre dos proteínas. Si alguna de las
  
    public boolean agregarInteraccion(String origen, String destino, double peso) {
        grafo.agregarArista(origen, destino, peso);
        modificado = true;
        return true;
    }


    //delete una interaccion entre dos proteina
   
    public boolean eliminarInteraccion(String origen, String destino) {
        boolean ok = grafo.eliminarArista(origen, destino);
        if (ok) modificado = true;
        return ok;
    }


    //hace un get los componentes conexos del grafo

    public ListaEnlazada getComponentes() {
        return grafo.getComponentesConexos();
    }

    //get de la ruta mas corta
    public Ruta getRutaCorta(String origen, String destino) {
        return grafo.dijkstra(origen, destino);
    }

    //get de la lista de proteínas ordenadas por su grado, mayor-menor
     
    public ListaEnlazada getHubs() {
        return grafo.getHubs();
    }
}