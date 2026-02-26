/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Samuel Djekki
 */
public class Arista {
    private Vertice origen;      // Proteina origen
    private Vertice destino;     // Proteina destino
    private double peso;         // Peso de la interaccion
    
    public Arista(Vertice origen, Vertice destino, double peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }
    
    // Getters y Setters
    public Vertice getOrigen() { return origen; }
    public void setOrigen(Vertice origen) { this.origen = origen; }
    
    public Vertice getDestino() { return destino; }
    public void setDestino(Vertice destino) { this.destino = destino; }
    
    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }
    
    @Override
    public String toString() {
        return origen + " --(" + peso + ")--> " + destino;
    }
}
