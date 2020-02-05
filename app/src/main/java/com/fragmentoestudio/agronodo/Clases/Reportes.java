package com.fragmentoestudio.agronodo.Clases;

import android.graphics.Bitmap;

public class Reportes {
    private String Titulo;
    private int id;
    private String Descripcion;
    private String Coordenadas;
    private Bitmap Imagen;

    public Reportes(String titulo, int id, String descripcion, String coordenadas, Bitmap imagen) {
        Titulo = titulo;
        this.id = id;
        Descripcion = descripcion;
        Coordenadas = coordenadas;
        Imagen = imagen;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCoordenadas() {
        return Coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        Coordenadas = coordenadas;
    }

    public Bitmap getImagen() {
        return Imagen;
    }

    public void setImagen(Bitmap imagen) {
        Imagen = imagen;
    }
}
