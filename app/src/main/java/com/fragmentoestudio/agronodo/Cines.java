package com.fragmentoestudio.agronodo;

import java.util.ArrayList;

public class Cines {
    private String Nombre;
    private ArrayList<Movie> Peliculas = new ArrayList<>();
    private boolean expanded;

    public Cines(String nombre, ArrayList<Movie> peliculas) {
        Nombre = nombre;
        Peliculas = peliculas;
        this.expanded = false;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public ArrayList<Movie> getPeliculas() {
        return Peliculas;
    }

    public void setPeliculas(ArrayList<Movie> peliculas) {
        Peliculas = peliculas;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }


    public void insertarPelicula(Movie movie){
        this.Peliculas.add(movie);
    }
}
