/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Samuel Djekki
 */
public class ListaEnlazada {
    
    //todos estos son basicamente las primitivas, nada nuevo. Se utilizan
    //los setters y getters que colocamos en NodoLista.
    
    private NodoLista cabeza;
    private int magnitud;
    
    public ListaEnlazada() {
        this.cabeza = null;
        this.magnitud = 0;
    }
    
    public void agregar(Object dato) {
        NodoLista nuevoNodo = new NodoLista(dato);
        
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            NodoLista actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        magnitud++;
    }
    
    public void insertar(int indice, Object dato) {
        if (indice < 0 || indice > magnitud) return;
        
        NodoLista nuevoNodo = new NodoLista(dato);
        
        if (indice == 0) {
            nuevoNodo.setSiguiente(cabeza);
            cabeza = nuevoNodo;
        } else {
            NodoLista actual = cabeza;
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.getSiguiente();
            }
            nuevoNodo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevoNodo);
        }
        magnitud++;
    }
    
    public Object obtener(int indice) {
        if (indice < 0 || indice >= magnitud) return null;
        
        NodoLista actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }
    
    public Object eliminar(int indice) {
        if (indice < 0 || indice >= magnitud) return null;
        
        Object datoEliminado;
        
        if (indice == 0) {
            datoEliminado = cabeza.getDato();
            cabeza = cabeza.getSiguiente();
        } else {
            NodoLista actual = cabeza;
            for (int i = 0; i < indice - 1; i++) {
                actual = actual.getSiguiente();
            }
            datoEliminado = actual.getSiguiente().getDato();
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
        }
        magnitud--;
        return datoEliminado;
    }
    
    public int buscar(Object dato) {
        NodoLista actual = cabeza;
        int indice = 0;
        
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return indice;
            }
            actual = actual.getSiguiente();
            indice++;
        }
        return -1;
    }
    
    public boolean estaVacia() {
        return cabeza == null;
    }
    

    public int getTamanio() {
        return magnitud;
    }

    public void vaciar() {
        cabeza = null;
        magnitud = 0;
    }
    
    public void recorrer() {
        NodoLista actual = cabeza;
        while (actual != null) {
            System.out.println(actual.getDato());
            actual = actual.getSiguiente();
        }
    }
}
