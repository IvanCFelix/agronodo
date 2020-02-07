package com.fragmentoestudio.agronodo.Clases;

public class SubCampos {
    private int ID;
    private int ID_Padre;
    private String Nombre;
    private String Tipo_Cultivo;
    private String Tipo_Agricultura;
    private String Coordenadas;
    private String Fecha_Inicio;
    private String Fecha_Final;

    public SubCampos(int ID, int ID_Padre, String nombre, String tipo_Cultivo, String tipo_Agricultura, String coordenadas, String fecha_Inicio, String fecha_Final) {
        this.ID = ID;
        this.ID_Padre = ID_Padre;
        Nombre = nombre;
        Tipo_Cultivo = tipo_Cultivo;
        Tipo_Agricultura = tipo_Agricultura;
        Coordenadas = coordenadas;
        Fecha_Inicio = fecha_Inicio;
        Fecha_Final = fecha_Final;
    }

    public SubCampos() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID_Padre() {
        return ID_Padre;
    }

    public void setID_Padre(int ID_Padre) {
        this.ID_Padre = ID_Padre;
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

    public String getTipo_Agricultura() {
        return Tipo_Agricultura;
    }

    public void setTipo_Agricultura(String tipo_Agricultura) {
        Tipo_Agricultura = tipo_Agricultura;
    }

    public String getCoordenadas() {
        return Coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        Coordenadas = coordenadas;
    }

    public String getFecha_Inicio() {
        return Fecha_Inicio;
    }

    public void setFecha_Inicio(String fecha_Inicio) {
        Fecha_Inicio = fecha_Inicio;
    }

    public String getFecha_Final() {
        return Fecha_Final;
    }

    public void setFecha_Final(String fecha_Final) {
        Fecha_Final = fecha_Final;
    }
}
