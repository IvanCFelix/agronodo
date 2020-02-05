package com.fragmentoestudio.agronodo.Clases;

public class Campos {
    private int ID;
    private String Nombre;
    private String Tipo_Cultivo;
    private String Coordenadas;


    public Campos(int ID, String nombre, String tipo_Cultivo, String coordenadas) {
        this.ID = ID;
        Nombre = nombre;
        Tipo_Cultivo = tipo_Cultivo;
        Coordenadas = coordenadas;
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

    public String getTipo_Cultivo() {
        return Tipo_Cultivo;
    }

    public void setTipo_Cultivo(String tipo_Cultivo) {
        Tipo_Cultivo = tipo_Cultivo;
    }

    public String getCoordenadas() {
        return Coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        Coordenadas = coordenadas;
    }
}
