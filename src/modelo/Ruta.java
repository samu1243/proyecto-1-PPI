/**
 *
 * @author Juan Ferreira, 
 *      
 */

package modelo;

public class Ruta {
    private ListaEnlazada camino;
    private double distancia;

    public Ruta(ListaEnlazada camino, double distancia) {
        this.camino = camino;
        this.distancia = distancia;
    }

    public ListaEnlazada getCamino() { return camino; }
    public double getDistancia() { return distancia; }
}

