/**
 *
 * @author Samuel Djekki, Juan Ferreira, 
 *      
 */
package modelo;


//grafo no dirigido implemetado con lista de adyacencia

public class GrafoPPI {
    private ListaEnlazada vertices;          
    private ListaEnlazada[] adyacencia;      
    private static final int CAPACIDAD_INICIAL = 500;

    public GrafoPPI() {
        this.vertices = new ListaEnlazada();
        this.adyacencia = new ListaEnlazada[CAPACIDAD_INICIAL];
        inicializarAdyacencia();
    }

    private void inicializarAdyacencia() {
        for (int i = 0; i < adyacencia.length; i++) {
            adyacencia[i] = new ListaEnlazada();
        }
    }

    private void expandirArreglo() {
        ListaEnlazada[] nueva = new ListaEnlazada[adyacencia.length * 2];
        System.arraycopy(adyacencia, 0, nueva, 0, adyacencia.length);
        for (int i = adyacencia.length; i < nueva.length; i++) {
            nueva[i] = new ListaEnlazada();
        }
        adyacencia = nueva;
    }

     //agg un vertice si ya no existe
    
    public boolean agregarVertice(String id) {
        if (buscarVertice(id) != -1) return false;
        Vertice nuevo = new Vertice(id);
        vertices.agregar(nuevo);
        if (vertices.getMagnitud() > adyacencia.length) expandirArreglo();
        return true;
    }

    
    //search de un vertice por su id
     
    public int buscarVertice(String id) {
        for (int i = 0; i < vertices.getMagnitud(); i++) {
            Vertice v = (Vertice) vertices.obtener(i);
            if (v.getId().equals(id)) return i;
        }
        return -1;
    }

    public Vertice getVertice(String id) {
        int i = buscarVertice(id);
        return (i != -1) ? (Vertice) vertices.obtener(i) : null;
    }

    
    //return de todos los vertices
    
    public ListaEnlazada getVertices() {
        return vertices;
    }

    //valida si existe una arista entre dos vertices por i.

    private boolean existeArista(int i, int j) {
        ListaEnlazada lista = adyacencia[i];
        for (int k = 0; k < lista.getMagnitud(); k++) {
            Arista a = (Arista) lista.obtener(k);
            if (buscarVertice(a.getDestino().getId()) == j) return true;
        }
        return false;
    }

      //agg una arita no dirigida entre dos protina

    public void agregarArista(String origen, String destino, double peso) {
        int i = buscarVertice(origen);
        int j = buscarVertice(destino);
        if (i == -1 || j == -1) return;
        if (existeArista(i, j)) return;

        Vertice vOrigen = (Vertice) vertices.obtener(i);
        Vertice vDestino = (Vertice) vertices.obtener(j);
        Arista a1 = new Arista(vOrigen, vDestino, peso);
        Arista a2 = new Arista(vDestino, vOrigen, peso);
        adyacencia[i].agregar(a1);
        adyacencia[j].agregar(a2);
        vOrigen.setGrado(vOrigen.getGrado() + 1);
        vDestino.setGrado(vDestino.getGrado() + 1);
    }

    
    //delete d una arista en los 2 lados
     
    public boolean eliminarArista(String origen, String destino) {
        int i = buscarVertice(origen);
        int j = buscarVertice(destino);
        if (i == -1 || j == -1) return false;

        boolean ok = eliminarAristaDesde(i, destino);
        if (ok) {
            eliminarAristaDesde(j, origen);
            Vertice vOrigen = (Vertice) vertices.obtener(i);
            Vertice vDestino = (Vertice) vertices.obtener(j);
            vOrigen.setGrado(vOrigen.getGrado() - 1);
            vDestino.setGrado(vDestino.getGrado() - 1);
        }
        return ok;
    }

    private boolean eliminarAristaDesde(int idx, String destId) {
        ListaEnlazada lista = adyacencia[idx];
        for (int k = 0; k < lista.getMagnitud(); k++) {
            Arista a = (Arista) lista.obtener(k);
            if (a.getDestino().getId().equals(destId)) {
                lista.eliminar(k);
                return true;
            }
        }
        return false;
    }

    
    //delete un vertice y todas sus aritas

    public boolean eliminarVertice(String id) {
        int idx = buscarVertice(id);
        if (idx == -1) return false;

        ListaEnlazada aristas = adyacencia[idx];
        while (aristas.getMagnitud() > 0) {
            Arista a = (Arista) aristas.obtener(0);
            String vecinoId = a.getDestino().getId();
            eliminarArista(id, vecinoId); // update de grados y las 2 listas
        }

        vertices.eliminar(idx);

        for (int i = idx; i < vertices.getMagnitud(); i++) {
            adyacencia[i] = adyacencia[i + 1];
        }
        adyacencia[vertices.getMagnitud()] = new ListaEnlazada(); 

        return true;
    }

    
     //get de los componentes conexos del grafo usando bfs
     //retorna la listaenlazada donde cada elemento es una listaenalazada con los id de un componente
     
    public ListaEnlazada getComponentesConexosBFS() {
        int n = vertices.getMagnitud();
        boolean[] visitado = new boolean[n];
        ListaEnlazada componentes = new ListaEnlazada();

        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                ListaEnlazada componente = new ListaEnlazada();
                int[] cola = new int[n];
                int frente = 0, fin = 0;
                visitado[i] = true;
                cola[fin++] = i;

                while (frente < fin) {
                    int u = cola[frente++];
                    Vertice v = (Vertice) vertices.obtener(u);
                    componente.agregar(v.getId());
                    ListaEnlazada vecinos = adyacencia[u];
                    for (int k = 0; k < vecinos.getMagnitud(); k++) {
                        Arista a = (Arista) vecinos.obtener(k);
                        int vIdx = buscarVertice(a.getDestino().getId());
                        if (!visitado[vIdx]) {
                            visitado[vIdx] = true;
                            cola[fin++] = vIdx;
                        }
                    }
                }
                componentes.agregar(componente);
            }
        }
        return componentes;
    }
    
    public ListaEnlazada getComponentesConexosDFS() {
    int n = vertices.getMagnitud();
    boolean[] visitado = new boolean[n];
    ListaEnlazada componentes = new ListaEnlazada();
    for (int i = 0; i < n; i++) {
        if (!visitado[i]) {
            ListaEnlazada componente = new ListaEnlazada();
            dfsRecursivo(i, visitado, componente);
            componentes.agregar(componente);
        }
    }
    return componentes;
}

    private void dfsRecursivo(int i, boolean[] visitado, ListaEnlazada componente) {
        visitado[i] = true;
        Vertice v = (Vertice) vertices.obtener(i);
        componente.agregar(v.getId());
        ListaEnlazada vecinos = adyacencia[i];
        for (int k = 0; k < vecinos.getMagnitud(); k++) {
            Arista a = (Arista) vecinos.obtener(k);
            int vIdx = buscarVertice(a.getDestino().getId());
            if (!visitado[vIdx]) {
                dfsRecursivo(vIdx, visitado, componente);
            }
        }
    }

    
    // ruta más corta (peso) entre dos proteinas usando dijkstra
     
    public Ruta dijkstra(String origen, String destino) {
        int n = vertices.getMagnitud();
        int s = buscarVertice(origen);
        int t = buscarVertice(destino);
        if (s == -1 || t == -1) return null;

        double[] dist = new double[n];
        boolean[] visitado = new boolean[n];
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Double.MAX_VALUE;
            prev[i] = -1;
        }
        dist[s] = 0;

        for (int i = 0; i < n; i++) {
            int u = -1;
            double min = Double.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (!visitado[j] && dist[j] < min) {
                    min = dist[j];
                    u = j;
                }
            }
            if (u == -1) break;
            visitado[u] = true;
            if (u == t) break;

            ListaEnlazada vecinos = adyacencia[u];
            for (int k = 0; k < vecinos.getMagnitud(); k++) {
                Arista a = (Arista) vecinos.obtener(k);
                int v = buscarVertice(a.getDestino().getId());
                if (v == -1) continue;
                double peso = a.getPeso();
                if (!visitado[v] && dist[u] + peso < dist[v]) {
                    dist[v] = dist[u] + peso;
                    prev[v] = u;
                }
            }
        }

        if (dist[t] == Double.MAX_VALUE) return null; //no hay ruta

        //rebuild del camino
        ListaEnlazada camino = new ListaEnlazada();
        int actual = t;
        while (actual != -1) {
            Vertice v = (Vertice) vertices.obtener(actual);
            camino.insertar(0, v.getId());
            actual = prev[actual];
        }
        return new Ruta(camino, dist[t]);
    }


        //calcula la centralidad de los vertices.

    public ListaEnlazada getHubs() {
        //convierte a un array
        int n = vertices.getMagnitud();
        Vertice[] arr = new Vertice[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (Vertice) vertices.obtener(i);
        }

        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j < n; j++) {
                if (arr[i].getGrado() < arr[j].getGrado()) {
                    Vertice tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
        ListaEnlazada ordenados = new ListaEnlazada();
        for (Vertice v : arr) ordenados.agregar(v);
        return ordenados;
    }

    public ListaEnlazada[] getAdyacencia() {
        return adyacencia;
    }
}