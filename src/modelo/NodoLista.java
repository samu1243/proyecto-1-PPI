/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Samuel Djekki
 */
public class NodoLista {
    private Object dato;        
    private NodoLista siguiente;
    
    public NodoLista(Object dato) {
        this.dato = dato;
        this.siguiente = null;
    }
    
    public Object getDato() { return dato; }
    public void setDato(Object dato) { this.dato = dato; }
    
    public NodoLista getSiguiente() { return siguiente; }
    public void setSiguiente(NodoLista siguiente) { this.siguiente = siguiente; }
}
