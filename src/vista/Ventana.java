package vista;
/**
 *
 * @author Juan Ferreira, Valeria Torres
 */
import controlador.Controlador;
import modelo.*;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swing_viewer.*;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.View;
import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {
    private Controlador controlador;
    private Graph visualGraph;
    private Viewer viewer;
    private JPanel panelGrafo;
    private JTextArea txtResultados;
    private JTextField txtEntrada;  // Campo de texto para comandos rápidos

    public Ventana() {
        controlador = new Controlador();
        controlador.cargarDefault(this); // Carga el CSV inicial

        // config de GraphStream 
        visualGraph = new SingleGraph("RedPPI");
        visualGraph.setAttribute("ui.stylesheet",
            "node { size: 20px; fill-color: #3498db; text-size: 14px; text-alignment: at-right; }" +
            "edge { fill-color: #bdc3c7; stroke-width: 2px; }");

        initComponents();
        actualizarVisualizacion();
    }

    private void initComponents() {
        setTitle("Red PPI - Visualizador de Interacciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

       // menu
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemCargar = new JMenuItem("Cargar CSV");
        JMenuItem itemGuardar = new JMenuItem("Guardar");
        JMenuItem itemGuardarComo = new JMenuItem("Guardar como...");
        JMenuItem itemSalir = new JMenuItem("Salir");
        menuArchivo.add(itemCargar);
        menuArchivo.add(itemGuardar);
        menuArchivo.add(itemGuardarComo);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);

        JMenu menuEdicion = new JMenu("Edición");
        JMenuItem itemAgregarProteina = new JMenuItem("Agregar proteína");
        JMenuItem itemEliminarProteina = new JMenuItem("Eliminar proteína");
        JMenuItem itemAgregarInteraccion = new JMenuItem("Agregar interacción");
        JMenuItem itemEliminarInteraccion = new JMenuItem("Eliminar interacción");
        menuEdicion.add(itemAgregarProteina);
        menuEdicion.add(itemEliminarProteina);
        menuEdicion.add(itemAgregarInteraccion);
        menuEdicion.add(itemEliminarInteraccion);
        menuBar.add(menuEdicion);

        JMenu menuAnalisis = new JMenu("Análisis");
        JMenuItem itemComponentes = new JMenuItem("Mostrar componentes");
        JMenuItem itemRutaCorta = new JMenuItem("Ruta más corta (Dijkstra)");
        JMenuItem itemHubs = new JMenuItem("Identificar hubs");
        menuAnalisis.add(itemComponentes);
        menuAnalisis.add(itemRutaCorta);
        menuAnalisis.add(itemHubs);
        menuBar.add(menuAnalisis);

        setJMenuBar(menuBar);

        
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 5, 5)); // 0 filas = tantas como necesite
        panelBotones.setPreferredSize(new Dimension(150, 0));

        JButton btnCargar = new JButton("Cargar CSV");
        JButton btnAnalizar = new JButton("Analizar Red");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnComponentes = new JButton("Componentes");
        JButton btnRuta = new JButton("Ruta corta");
        JButton btnHubs = new JButton("Hubs");

        panelBotones.add(btnCargar);
        panelBotones.add(btnAnalizar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnComponentes);
        panelBotones.add(btnRuta);
        panelBotones.add(btnHubs);

        
        panelGrafo = new JPanel(new BorderLayout());
        viewer = new SwingViewer(visualGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        View view = viewer.addDefaultView(false);
        panelGrafo.add((Component) view, BorderLayout.CENTER);

        
        JPanel panelInferior = new JPanel(new BorderLayout());

        txtResultados = new JTextArea(6, 40);
        txtResultados.setEditable(false);
        panelInferior.add(new JScrollPane(txtResultados), BorderLayout.CENTER);

        JPanel panelEntrada = new JPanel(new FlowLayout());
        txtEntrada = new JTextField(20);
        JButton btnEjecutar = new JButton("Ejecutar");
        panelEntrada.add(new JLabel("Comando:"));
        panelEntrada.add(txtEntrada);
        panelEntrada.add(btnEjecutar);
        panelInferior.add(panelEntrada, BorderLayout.SOUTH);

        
        add(panelBotones, BorderLayout.WEST);
        add(panelGrafo, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        
        btnCargar.addActionListener(e -> cargarArchivo());
        btnAnalizar.addActionListener(e -> mostrarEstadisticas());
        btnGuardar.addActionListener(e -> controlador.guardarArchivo(this));
        btnComponentes.addActionListener(e -> mostrarComponentes());
        btnRuta.addActionListener(e -> pedirRuta());
        btnHubs.addActionListener(e -> mostrarHubs());

        itemCargar.addActionListener(e -> cargarArchivo());
        itemGuardar.addActionListener(e -> controlador.guardarArchivo(this));
        itemGuardarComo.addActionListener(e -> {
            controlador.setArchivoActual(null);
            controlador.guardarArchivo(this);
        });
        itemSalir.addActionListener(e -> System.exit(0));

        itemAgregarProteina.addActionListener(e -> agregarProteina());
        itemEliminarProteina.addActionListener(e -> eliminarProteina());
        itemAgregarInteraccion.addActionListener(e -> agregarInteraccion());
        itemEliminarInteraccion.addActionListener(e -> eliminarInteraccion());

        itemComponentes.addActionListener(e -> mostrarComponentes());
        itemRutaCorta.addActionListener(e -> pedirRuta());
        itemHubs.addActionListener(e -> mostrarHubs());

        btnEjecutar.addActionListener(e -> ejecutarComando(txtEntrada.getText()));

        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    

    private void cargarArchivo() {
        if (!controlador.confirmarGuardar(this)) return;
        if (controlador.cargarArchivo(this)) {
            actualizarVisualizacion();
            txtResultados.append("Archivo cargado. Proteínas: " + controlador.getGrafo().getVertices().getMagnitud() + "\n");
        }
    }

    private void mostrarEstadisticas() {
        txtResultados.setText("Analizando red...\n");
        txtResultados.append("Proteínas totales: " + controlador.getGrafo().getVertices().getMagnitud() + "\n");
        txtResultados.append("Componentes conexos: " + controlador.getComponentesBFS().getMagnitud() + "\n");
    }

    private void mostrarComponentes() {
        ListaEnlazada componentes = controlador.getComponentesBFS();
        txtResultados.setText("Componentes conexos: " + componentes.getMagnitud() + "\n");
        for (int i = 0; i < componentes.getMagnitud(); i++) {
            ListaEnlazada comp = (ListaEnlazada) componentes.obtener(i);
            txtResultados.append("Componente " + (i+1) + ": ");
            for (int j = 0; j < comp.getMagnitud(); j++) {
                txtResultados.append(comp.obtener(j) + " ");
            }
            txtResultados.append("\n");
        }
        colorearComponentes(componentes);
    }

    private void colorearComponentes(ListaEnlazada componentes) {
        String[] colores = {"#3498db", "#e74c3c", "#2ecc71", "#f39c12", "#9b59b6",
                            "#1abc9c", "#e67e22", "#34495e", "#d35400", "#27ae60"};
        for (int i = 0; i < componentes.getMagnitud(); i++) {
            ListaEnlazada comp = (ListaEnlazada) componentes.obtener(i);
            String color = colores[i % colores.length];
            for (int j = 0; j < comp.getMagnitud(); j++) {
                String id = (String) comp.obtener(j);
                Node node = visualGraph.getNode(id);
                if (node != null) {
                    node.setAttribute("ui.style", "fill-color: " + color + ";");
                }
            }
        }
    }

    private void pedirRuta() {
        String origen = JOptionPane.showInputDialog(this, "Ingrese proteína origen:");
        if (origen == null) return;
        String destino = JOptionPane.showInputDialog(this, "Ingrese proteína destino:");
        if (destino == null) return;

        Ruta ruta = controlador.getGrafo().dijkstra(origen, destino);
        if (ruta == null) {
            txtResultados.append("No hay ruta entre " + origen + " y " + destino + "\n");
        } else {
            StringBuilder sb = new StringBuilder();
            ListaEnlazada camino = ruta.getCamino();
            for (int i = 0; i < camino.getMagnitud(); i++) {
                if (i > 0) sb.append(" → ");
                sb.append(camino.obtener(i));
            }
            txtResultados.append("Ruta más corta: " + sb.toString() + " (distancia: " + ruta.getDistancia() + ")\n");
        }
    }

    private void mostrarHubs() {
        ListaEnlazada hubs = controlador.getGrafo().getHubs();
        txtResultados.setText("Proteínas ordenadas por grado (hub más importante primero):\n");
        for (int i = 0; i < hubs.getMagnitud(); i++) {
            Vertice v = (Vertice) hubs.obtener(i);
            txtResultados.append(v.getId() + " (grado " + v.getGrado() + ")\n");
        }
    }

    private void agregarProteina() {
        String id = JOptionPane.showInputDialog(this, "Ingrese ID de la nueva proteína:");
        if (id == null || id.trim().isEmpty()) return;
        if (controlador.getGrafo().buscarVertice(id) != -1) {
            JOptionPane.showMessageDialog(this, "La proteína ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        controlador.getGrafo().agregarVertice(id);
        controlador.setModificado(true);
        actualizarVisualizacion();
        txtResultados.append("Proteína " + id + " agregada.\n");
    }

    private void eliminarProteina() {
        String id = JOptionPane.showInputDialog(this, "Ingrese ID de la proteína a eliminar:");
        if (id == null) return;
        if (controlador.eliminarProteina(id)) {
            actualizarVisualizacion();
            txtResultados.append("Proteína " + id + " eliminada.\n");
        } else {
            JOptionPane.showMessageDialog(this, "Proteína no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarInteraccion() {
        String origen = JOptionPane.showInputDialog(this, "Proteína origen:");
        if (origen == null) return;
        String destino = JOptionPane.showInputDialog(this, "Proteína destino:");
        if (destino == null) return;
        String pesoStr = JOptionPane.showInputDialog(this, "Peso (número):");
        if (pesoStr == null) return;
        try {
            double peso = Double.parseDouble(pesoStr);
            controlador.agregarInteraccion(origen, destino, peso);
            actualizarVisualizacion();
            txtResultados.append("Interacción " + origen + "-" + destino + " agregada con peso " + peso + "\n");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Peso inválido. Debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarInteraccion() {
        String origen = JOptionPane.showInputDialog(this, "Proteína origen:");
        if (origen == null) return;
        String destino = JOptionPane.showInputDialog(this, "Proteína destino:");
        if (destino == null) return;
        if (controlador.eliminarInteraccion(origen, destino)) {
            actualizarVisualizacion();
            txtResultados.append("Interacción " + origen + "-" + destino + " eliminada.\n");
        } else {
            JOptionPane.showMessageDialog(this, "Interacción no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método opcional para probar comandos rápidos
    private void ejecutarComando(String comando) {
        // Aquí podrías implementar comandos simples, por ejemplo: "dijkstra P1 P8"
        String[] partes = comando.trim().split("\\s+");
        if (partes.length == 0) return;
        if (partes[0].equalsIgnoreCase("dijkstra") && partes.length == 3) {
            Ruta ruta = controlador.getGrafo().dijkstra(partes[1], partes[2]);
            if (ruta == null) {
                txtResultados.append("No hay ruta entre " + partes[1] + " y " + partes[2] + "\n");
            } else {
                StringBuilder sb = new StringBuilder();
                ListaEnlazada camino = ruta.getCamino();
                for (int i = 0; i < camino.getMagnitud(); i++) {
                    if (i > 0) sb.append(" → ");
                    sb.append(camino.obtener(i));
                }
                txtResultados.append("Ruta: " + sb + " (distancia: " + ruta.getDistancia() + ")\n");
            }
        } else {
            txtResultados.append("Comando no reconocido.\n");
        }
    }

    private void actualizarVisualizacion() {
        visualGraph.clear();

        GrafoPPI g = controlador.getGrafo();
        ListaEnlazada vertices = g.getVertices();

        if (vertices == null || vertices.getMagnitud() == 0) return;

        // dibujar nodos
        for (int i = 0; i < vertices.getMagnitud(); i++) {
            Vertice v = (Vertice) vertices.obtener(i);
            Node n = visualGraph.addNode(v.getId());
            n.setAttribute("ui.label", v.getId());
        }

        // dibujar aristas 
        for (int i = 0; i < vertices.getMagnitud(); i++) {
            ListaEnlazada ady = g.getAdyacencia()[i];
            for (int j = 0; j < ady.getMagnitud(); j++) {
                Arista a = (Arista) ady.obtener(j);
                String id1 = a.getOrigen().getId();
                String id2 = a.getDestino().getId();
                if (visualGraph.getEdge(id1 + "-" + id2) == null && visualGraph.getEdge(id2 + "-" + id1) == null) {
                    visualGraph.addEdge(id1 + "-" + id2, id1, id2);
                }
            }
        }

      
        colorearComponentes(controlador.getComponentesBFS());
    }
}