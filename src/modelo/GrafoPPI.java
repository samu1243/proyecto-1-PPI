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
        
   public void agregarArista(String origen, String destino, double peso) {

    int i = buscarVertice(origen);
    int j = buscarVertice(destino);

    if (i == -1 || j == -1) {
        System.out.println("Vertices no existen");
        return;
    }

    Vertice vOrigen = (Vertice) vertices.obtener(i);
    Vertice vDestino = (Vertice) vertices.obtener(j);
    Arista nueva =new Arista(vOrigen, vDestino, peso);
    adyacencia[i].agregar(nueva);
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
    
    //BFS recursivo 
   private void DFSRec(boolean[] revisado, int indice, ListaEnlazada resultado) {

    revisado[indice] = true; //Es para que el programa no se quede en un bucle

    // obtener vértice actual
    Vertice actual = (Vertice) vertices.obtener(indice);
    resultado.agregar(actual.getId());

    // recorrer lista de adyacencia
    ListaEnlazada vecinos = adyacencia[indice];

    for (int i = 0; i < vecinos.getMagnitud(); i++) {

        Arista arista = (Arista) vecinos.obtener(i);
        Vertice vecino = arista.getDestino();
        int indiceVecino = buscarVertice(vecino.getId());

        if (revisado[indiceVecino] == false){ 
            DFSRec(revisado, indiceVecino, resultado);//Termina la iteracion se vuelve a llamar la funcion
        }
    }
  }
   
   //Este metodo llama al recursivo, es para cuando el grafo esta desconectado
   public ListaEnlazada DFS() {

    int n = vertices.getMagnitud();

    boolean[] revisado = new boolean[n]; //Crea un array de booleanos
    ListaEnlazada resultado = new ListaEnlazada(); //Crea una nueva lista enlazada para guardar los datos que devuelve

    for (int i = 0; i < n; i++) {

        if (revisado[i] == false) {
            DFSRec(revisado, i, resultado); //Se llama a la funcion recursiva
        }
    }

    return resultado;
    }
   
   public ListaEnlazada BFS() {

    int n = vertices.getMagnitud();
    boolean[] revisado = new boolean[n]; //Similar a dfs
    ListaEnlazada resultado = new ListaEnlazada();

    //Se crea una "cola" (la teoria es similar pero no cumple todas las restriciones de una cola) 
    int[] cola = new int[n];
    int frente = 0;
    int fin = 0;

    for (int i = 0; i < n; i++) {

        if (revisado[i] == false) {
            
            revisado[i] = true;
            cola[fin++] = i; // encolar

            while (frente < fin) {

                int indice = cola[frente++]; // desencolar 

                Vertice actual =(Vertice) vertices.obtener(indice);
                resultado.agregar(actual.getId());
                ListaEnlazada vecinos =adyacencia[indice];

                for (int j = 0; j < vecinos.getMagnitud(); j++) {

                    Arista arista = (Arista) vecinos.obtener(j);
                    Vertice vecino = arista.getDestino();

                    int indiceVecino = buscarVertice(vecino.getId());

                    if (revisado[indiceVecino] == false) {
                        
                        revisado[indiceVecino] = true;
                        cola[fin++] = indiceVecino;
                        
                    }
                }
            }
        }
    }

    return resultado; //Devuelve la lista de los datos
 }
   
   public ListaEnlazada Dijkstra(String idOrigen, String idDestino) {

    int n = vertices.getMagnitud();

    int indiceOrigen = buscarVertice(idOrigen);
    int indiceDestino = buscarVertice(idDestino);

    ListaEnlazada camino = new ListaEnlazada();

    if (indiceOrigen == -1 || indiceDestino == -1) {System.out.println("Origen o destino no existen");
        return camino; //Cuando no hay nodo origen o nodo destino
    }

    double[] distancia = new double[n]; //distancia min desde origen
    boolean[] revisado = new boolean[n]; //los que ya se revisaron
    int[] previo = new int[n]; //guarda el veritce anterior

    // Inicialización
    for (int i = 0; i < n; i++) {
        distancia[i] = Double.MAX_VALUE; //Integer.MAX_VALUE es el valor máximo que puede almacenar un int
        previo[i] = -1; //es que no hay anterior
    }

    distancia[indiceOrigen] = 0;
    //Aqui empieza el algoritmo
    for (int i = 0; i < n; i++) {

        int u = -1;
        double menor = Double.MAX_VALUE; 

        // buscar vértice no visitado más cercano
        for (int j = 0; j < n; j++) {
            if (revisado[j] == false && distancia[j] < menor) {
                menor = distancia[j];
                u = j; //  u es el vértice a procesar
            }
        }

        if (u == -1) //ya no hay mas vertices
            break;

        revisado[u] = true;
        if (u == indiceDestino)
            break;

        // recorrer vecinos
       ListaEnlazada vecinos = adyacencia[u]; // Lista de aristas desde u

        for (int k = 0; k < vecinos.getMagnitud(); k++) {
            Arista arista =(Arista) vecinos.obtener(k);
            Vertice vecino = arista.getDestino();
            int v = buscarVertice(vecino.getId());
            if (v == -1) continue;
            double peso = arista.getPeso();

            //si se encuentra un camino mas corto
            if (revisado[v] == false &&  distancia[u] + peso < distancia[v]) {
                distancia[v] = distancia[u] + peso; 
                previo[v] = u;
            }
        }
    }

    // reconstruye el camino
    if (distancia[indiceDestino] == Double.MAX_VALUE) {
        System.out.println("No existe ruta");
        return camino;
    }

    int actual = indiceDestino;
    while (actual != -1) { 
        Vertice v = (Vertice) vertices.obtener(actual);
       camino.insertar(0, v.getId()); //Inserta al inicio
       actual = previo[actual];
    }

    System.out.println( "Distancia total: "+ distancia[indiceDestino]);

    return camino;
}

}

