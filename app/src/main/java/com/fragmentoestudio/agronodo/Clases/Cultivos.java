package com.fragmentoestudio.agronodo.Clases;

public class Cultivos {
    private String Nombre;
    private boolean expanded;

    public Cultivos(String nombre) {
        Nombre = nombre;
        this.expanded = false;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
