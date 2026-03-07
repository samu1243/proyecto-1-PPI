package vista;

/**
 *
 * @author Juan Ferreira
 */
import vista.Ventana;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");
        SwingUtilities.invokeLater(() -> {
            Ventana ventana = new Ventana();
            ventana.setVisible(true);
        });
    }
}
