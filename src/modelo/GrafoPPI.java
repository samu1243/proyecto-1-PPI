/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Samuel Djekki
 */
public class GrafoPPI {
    private ListaEnlazada vertices;      
    private ListaEnlazada[] adyacencia;  
    
    public GrafoPPI() {
        this.vertices = new ListaEnlazada();
        this.adyacencia = new ListaEnlazada[500]; 
        inicializarAdyacencia();
    }
    
    private void inicializarAdyacencia() {
        for (int i = 0; i < adyacencia.length; i++) {
            adyacencia[i] = new ListaEnlazada();
        }
    }
    
    public boolean agregarVertice(String id) {
        if (buscarVertice(id) != -1) {
            return false; 
        }
        
        Vertice nuevo = new Vertice(id);
        vertices.agregar(nuevo);
        
        if (vertices.getMagnitud() > adyacencia.length) {
            expandirArreglo();
        }
        
        return true;
    }
    
    public boolean agregarVertice(String id, String nombre) {
        if (buscarVertice(id) != -1) {
            return false;
        }
        
        Vertice nuevo = new Vertice(id, nombre);
        vertices.agregar(nuevo);
        
        if (vertices.getMagnitud() > adyacencia.length) {
            expandirArreglo();
        }
        
        return true;
    }
        public int buscarVertice(String id) {
        for (int i = 0; i < vertices.getMagnitud(); i++) {
            Vertice v = (Vertice) vertices.obtener(i);
            if (v.getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }
    private void expandirArreglo() {
        ListaEnlazada[] nuevaAdyacencia = new ListaEnlazada[adyacencia.length * 2];
        for (int i = 0; i < adyacencia.length; i++) {
            nuevaAdyacencia[i] = adyacencia[i];
        }
        for (int i = adyacencia.length; i < nuevaAdyacencia.length; i++) {
            nuevaAdyacencia[i] = new ListaEnlazada();
        }
        adyacencia = nuevaAdyacencia;
    }   
}
