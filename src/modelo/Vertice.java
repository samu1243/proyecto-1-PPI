/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Objects;

/**
 *
 * @author Samuel Djekki
 */
public class Vertice {
    private String id;
    private String nombre;
    private int grado;
    
    public Vertice(String id){
        this.id = id;
        this.nombre = id;
        this.grado = 0;
    }
    
    public Vertice(String id, String nombre){
        this.id = id;
        this.nombre = nombre;
        this.grado = 0;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public int getGrado() { return grado; }
    public void setGrado(int grado) { this.grado = grado; }
    //ai me dice que override ayuda a solucionar errores. Por ejemplo,
    //este override en especifico ayudara a que no esten dos nodos de la misma proteina.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vertice vertice = (Vertice) obj;
        return id.equals(vertice.id);
    }

    @Override //esto lo hizo el solucionador de net beans
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }
    @Override
    public String toString() {
        return id;
    }
}
