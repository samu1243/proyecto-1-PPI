package vista;

import controlador.Controlador;
import modelo.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swing_viewer.*;
import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {
    private Controlador controlador;
    private Graph visualGraph;
    private Viewer viewer;
    private JPanel panelGrafo;
    private JTextArea txtResultados;

    public Ventana() {
        controlador = new Controlador();
        controlador.cargarDefault();
        initComponents();
        actualizarVisualizacion();
    }

    private void initComponents() {
        setTitle("Red PPI - Interacciones Proteína-Proteína");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemCargar = new JMenuItem("Cargar...");
        JMenuItem itemGuardar = new JMenuItem("Guardar");
        JMenuItem itemGuardarComo = new JMenuItem("Guardar como...");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemCargar.addActionListener(e -> cargarArchivo());
        itemGuardar.addActionListener(e -> guardarArchivo());
        itemGuardarComo.addActionListener(e -> guardarComo());
        itemSalir.addActionListener(e -> salir());
        menuArchivo.add(itemCargar);
        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemGuardarComo);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 5, 5));
        JButton btnAgregarProteina = new JButton("Agregar Proteína");
        JButton btnEliminarProteina = new JButton("Eliminar Proteína");
        JButton btnAgregarInteraccion = new JButton("Agregar Interacción");
        JButton btnEliminarInteraccion = new JButton("Eliminar Interacción");
        JButton btnComplejos = new JButton("Detectar Complejos");
        JButton btnRutaCorta = new JButton("Ruta más corta");
        JButton btnHubs = new JButton("Identificar Hubs");
        JButton btnActualizarVista = new JButton("Actualizar Vista");
        JButton btnComplejosBFS = new JButton("Complejos (BFS)");
        JButton btnComplejosDFS = new JButton("Complejos (DFS)");
        btnComplejosBFS.addActionListener(e -> detectarComplejosBFS());
        btnComplejosDFS.addActionListener(e -> detectarComplejosDFS());

        btnAgregarProteina.addActionListener(e -> agregarProteina());
        btnEliminarProteina.addActionListener(e -> eliminarProteina());
        btnAgregarInteraccion.addActionListener(e -> agregarInteraccion());
        btnEliminarInteraccion.addActionListener(e -> eliminarInteraccion());
        btnComplejos.addActionListener(e -> detectarComplejos());
        btnRutaCorta.addActionListener(e -> rutaCorta());
        btnHubs.addActionListener(e -> mostrarHubs());
        btnActualizarVista.addActionListener(e -> actualizarVisualizacion());

        panelBotones.add(btnAgregarProteina);
        panelBotones.add(btnEliminarProteina);
        panelBotones.add(btnAgregarInteraccion);
        panelBotones.add(btnEliminarInteraccion);
        panelBotones.add(btnComplejos);
        panelBotones.add(btnRutaCorta);
        panelBotones.add(btnHubs);
        panelBotones.add(btnActualizarVista);

        // Panel de visualización GraphStream
        visualGraph = new SingleGraph("PPI");
        viewer = new Viewer(visualGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        View view = viewer.addDefaultView(false);
        panelGrafo = (JPanel) view;

        // Panel de resultados
        txtResultados = new JTextArea(10, 40);
        txtResultados.setEditable(false);
        JScrollPane scrollResultados = new JScrollPane(txtResultados);

        // Layout
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelBotones, panelGrafo);
        split.setResizeWeight(0.15);
        add(split, BorderLayout.CENTER);
        add(scrollResultados, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void cargarArchivo() {
        if (controlador.cargarArchivo(this)) {
            actualizarVisualizacion();
            txtResultados.append("Archivo cargado.\n");
        }
    }

    private void guardarArchivo() {
        if (controlador.guardarArchivo(this)) {
            txtResultados.append("Archivo guardado.\n");
        }
    }

    private void guardarComo() {
        if (controlador.guardarComo(this)) {
            txtResultados.append("Archivo guardado.\n");
        }
    }

    private void salir() {
        if (controlador.isModificado()) {
            int opt = JOptionPane.showConfirmDialog(this, "Hay cambios sin guardar. ¿Salir de todas formas?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opt != JOptionPane.YES_OPTION) return;
        }
        System.exit(0);
    }

    private void agregarProteina() {
        String id = JOptionPane.showInputDialog(this, "ID de la nueva proteína:");
        if (id != null && !id.trim().isEmpty()) {
            if (controlador.agregarProteina(id.trim())) {
                txtResultados.append("Proteína " + id + " agregada.\n");
                actualizarVisualizacion();
            } else {
                JOptionPane.showMessageDialog(this, "La proteína ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarProteina() {
        String id = JOptionPane.showInputDialog(this, "ID de la proteína a eliminar:");
        if (id != null) {
            if (controlador.eliminarProteina(id.trim())) {
                txtResultados.append("Proteína " + id + " eliminada.\n");
                actualizarVisualizacion();
            } else {
                JOptionPane.showMessageDialog(this, "Proteína no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarInteraccion() {
        String origen = JOptionPane.showInputDialog(this, "Proteína origen:");
        String destino = JOptionPane.showInputDialog(this, "Proteína destino:");
        String pesoStr = JOptionPane.showInputDialog(this, "Peso:");
        if (origen != null && destino != null && pesoStr != null) {
            try {
                double peso = Double.parseDouble(pesoStr.trim());
                controlador.agregarInteraccion(origen.trim(), destino.trim(), peso);
                txtResultados.append("Interacción " + origen + "-" + destino + " agregada.\n");
                actualizarVisualizacion();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Peso inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarInteraccion() {
        String origen = JOptionPane.showInputDialog(this, "Proteína origen:");
        String destino = JOptionPane.showInputDialog(this, "Proteína destino:");
        if (origen != null && destino != null) {
            if (controlador.eliminarInteraccion(origen.trim(), destino.trim())) {
                txtResultados.append("Interacción " + origen + "-" + destino + " eliminada.\n");
                actualizarVisualizacion();
            } else {
                JOptionPane.showMessageDialog(this, "Interacción no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void detectarComplejos() {
        ListaEnlazada componentes = controlador.getComponentes();
        txtResultados.append("--- Complejos proteicos (componentes conexos) ---\n");
        for (int i = 0; i < componentes.getMagnitud(); i++) {
            ListaEnlazada comp = (ListaEnlazada) componentes.obtener(i);
            txtResultados.append("Complejo " + (i+1) + ": ");
            for (int j = 0; j < comp.getMagnitud(); j++) {
                txtResultados.append(comp.obtener(j).toString() + " ");
            }
            txtResultados.append("\n");
        }
    }

    private void rutaCorta() {
        String origen = JOptionPane.showInputDialog(this, "Proteína origen:");
        String destino = JOptionPane.showInputDialog(this, "Proteína destino:");
        if (origen != null && destino != null) {
            Ruta ruta = controlador.getRutaCorta(origen.trim(), destino.trim());
            if (ruta != null) {
                txtResultados.append("Ruta más corta de " + origen + " a " + destino + ": ");
                ListaEnlazada camino = ruta.getCamino();
                for (int i = 0; i < camino.getMagnitud(); i++) {
                    txtResultados.append(camino.obtener(i).toString());
                    if (i < camino.getMagnitud()-1) txtResultados.append(" -> ");
                }
                txtResultados.append(" (distancia total: " + ruta.getDistancia() + ")\n");
            } else {
                txtResultados.append("No hay ruta entre " + origen + " y " + destino + "\n");
            }
        }
    }

    private void mostrarHubs() {
        ListaEnlazada hubs = controlador.getHubs();
        txtResultados.append("--- Hubs (proteínas con mayor grado) ---\n");
        for (int i = 0; i < hubs.getMagnitud(); i++) {
            Vertice v = (Vertice) hubs.obtener(i);
            txtResultados.append(v.getId() + " (grado " + v.getGrado() + ")\n");
        }
    }

    private void actualizarVisualizacion() {
        visualGraph.clear();
        ListaEnlazada vertices = controlador.getGrafo().getVertices();
        for (int i = 0; i < vertices.getMagnitud(); i++) {
            Vertice v = (Vertice) vertices.obtener(i);
            Node node = visualGraph.addNode(v.getId());
            node.setAttribute("ui.label", v.getId());
        }

        boolean[][] agregadas = new boolean[vertices.getMagnitud()][vertices.getMagnitud()];
        for (int i = 0; i < vertices.getMagnitud(); i++) {
            Vertice v = (Vertice) vertices.obtener(i);
            ListaEnlazada aristas = controlador.getGrafo().getAdyacencia()[i];
            for (int j = 0; j < aristas.getMagnitud(); j++) {
                Arista a = (Arista) aristas.obtener(j);
                int idxDest = controlador.getGrafo().buscarVertice(a.getDestino().getId());
                if (i < idxDest && !agregadas[i][idxDest]) {
                    String idArista = v.getId() + "-" + a.getDestino().getId();
                    visualGraph.addEdge(idArista, v.getId(), a.getDestino().getId(), false);
                    agregadas[i][idxDest] = true;
                }
            }
        }
    }
}

