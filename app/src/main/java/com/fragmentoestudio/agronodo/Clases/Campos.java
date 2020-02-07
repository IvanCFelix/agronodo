package com.fragmentoestudio.agronodo.Clases;

public class Campos {
    private int ID;
    private String Nombre;
    private String Coordenadas;
    private boolean expanded;


    public Campos(int ID, String nombre, String coordenadas) {
        this.ID = ID;
        Nombre = nombre;
        Coordenadas = coordenadas;
        this.expanded = false;
    }

    public Campos() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCoordenadas() {
        return Coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        Coordenadas = coordenadas;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
