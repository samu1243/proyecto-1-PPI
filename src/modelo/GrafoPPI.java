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
            return false; // Ya existe
        }
        
        Vertice nuevo = new Vertice(id);
        vertices.agregar(nuevo);
        
        // Expandir si es necesario
        if (vertices.getMagnitud() > adyacencia.length) {
            expandirArreglo();
        }
        
        return true;
    }
    
    //agrega vertice con nombre 
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
    
    //elimina vertice y todas sus aristas
    public boolean eliminarVertice(String id) {
        int indice = buscarVertice(id);
        if (indice == -1) {
            return false;
        }
        
        // Eliminar aristas que conectan con este vértice
        for (int i = 0; i < vertices.getMagnitud(); i++) {
            if (i != indice) {
                eliminarArista(obtenerVertice(i).getId(), id);
            }
        }
        
        // Eliminar el vértice
        vertices.eliminar(indice);
        
        return true;
    }
    

    public boolean agregarArista(String origenId, String destinoId, double peso) {
        int indiceOrigen = buscarVertice(origenId);
        int indiceDestino = buscarVertice(destinoId);
        
        if (indiceOrigen == -1 || indiceDestino == -1) {
            return false; // Algun vértice no existe
        }
        
        if (indiceOrigen == indiceDestino) {
            return false; // No se permite auto-loops
        }
        
        Vertice origen = obtenerVertice(indiceOrigen);
        Vertice destino = obtenerVertice(indiceDestino);
        
        // Crear arista origen -> destino
        Arista arista1 = new Arista(origen, destino, peso);
        adyacencia[indiceOrigen].agregar(arista1);
        
        // Crear arista destino -> origen (NO DIRIGIDO)
        Arista arista2 = new Arista(destino, origen, peso);
        adyacencia[indiceDestino].agregar(arista2);
        
        // Actualizar grados
        origen.setGrado(origen.getGrado() + 1);
        destino.setGrado(destino.getGrado() + 1);
        
        return true;
    }
    
    // elimina en ambas direcciones
    public boolean eliminarArista(String origenId, String destinoId) {
        int indiceOrigen = buscarVertice(origenId);
        int indiceDestino = buscarVertice(destinoId);
        
        if (indiceOrigen == -1 || indiceDestino == -1) {
            return false;
        }
        
        boolean eliminada = false;
        
        // Eliminar origen -> destino
        ListaEnlazada listaOrigen = adyacencia[indiceOrigen];
        for (int i = 0; i < listaOrigen.getMagnitud(); i++) {
            Arista arista = (Arista) listaOrigen.obtener(i);
            if (arista.getDestino().getId().equals(destinoId)) {
                listaOrigen.eliminar(i);
                eliminada = true;
                break;
            }
        }
        
        // Eliminar destino -> origen
        ListaEnlazada listaDestino = adyacencia[indiceDestino];
        for (int i = 0; i < listaDestino.getMagnitud(); i++) {
            Arista arista = (Arista) listaDestino.obtener(i);
            if (arista.getDestino().getId().equals(origenId)) {
                listaDestino.eliminar(i);
                break;
            }
        }
        
        if (eliminada) {
            // Actualizar grados
            Vertice origen = obtenerVertice(buscarVertice(origenId));
            Vertice destino = obtenerVertice(buscarVertice(destinoId));
            origen.setGrado(origen.getGrado() - 1);
            destino.setGrado(destino.getGrado() - 1);
        }
        
        return eliminada;
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
    

    public Vertice obtenerVertice(int indice) {
        return (Vertice) vertices.obtener(indice);
    }
    

    public ListaEnlazada obtenerAdyacencias(String id) {
        int indice = buscarVertice(id);
        if (indice == -1) {
            return null;
        }
        return adyacencia[indice];
    }

    public ListaEnlazada obtenerAdyacencias(int indice) {
        if (indice < 0 || indice >= vertices.getMagnitud()) {
            return null;
        }
        return adyacencia[indice];
    }

    public int getNumVertices() {
        return vertices.getMagnitud();
    }
    

    public int getNumAristas() {
        int total = 0;
        for (int i = 0; i < vertices.getMagnitud(); i++) {
            total += adyacencia[i].getMagnitud();
        }
        return total / 2; // Dividir por 2 porque es no dirigido
    }
    

    public boolean existeConexion(String origenId, String destinoId) {
        int indiceOrigen = buscarVertice(origenId);
        if (indiceOrigen == -1) return false;
        
        ListaEnlazada lista = adyacencia[indiceOrigen];
        for (int i = 0; i < lista.getMagnitud(); i++) {
            Arista arista = (Arista) lista.obtener(i);
            if (arista.getDestino().getId().equals(destinoId)) {
                return true;
            }
        }
        return false;
    }
    

    public double obtenerPeso(String origenId, String destinoId) {
        int indiceOrigen = buscarVertice(origenId);
        if (indiceOrigen == -1) return -1;
        
        ListaEnlazada lista = adyacencia[indiceOrigen];
        for (int i = 0; i < lista.getMagnitud(); i++) {
            Arista arista = (Arista) lista.obtener(i);
            if (arista.getDestino().getId().equals(destinoId)) {
                return arista.getPeso();
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
    
    // imprime el grafo para ver si funciona correctamente
    public void imprimirGrafo() {
        System.out.println("=== GRAFO PPI ===");
        System.out.println("Vértices: " + getNumVertices());
        System.out.println("Aristas: " + getNumAristas());
        System.out.println();
        
        for (int i = 0; i < vertices.getMagnitud(); i++) {
            Vertice v = (Vertice) vertices.obtener(i);
            System.out.print(v.getId() + " -> ");
            
            ListaEnlazada lista = adyacencia[i];
            for (int j = 0; j < lista.getMagnitud(); j++) {
                Arista a = (Arista) lista.obtener(j);
                System.out.print("[" + a.getDestino().getId() + "(" + a.getPeso() + ")] ");
            }
            System.out.println();
        }
    }
    
    //Da todos los vertices en la lista
    public ListaEnlazada getVertices() {
        return vertices;
    }
    //grafo de 0
    public void limpiar() {
        vertices.vaciar();
        inicializarAdyacencia();
    }
    
}
