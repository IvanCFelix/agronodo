package com.fragmentoestudio.agronodo.Utilidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class Base_Datos extends SQLiteOpenHelper {

    public static final String nombreBaseDatos = "AgroNodo";

    public static final int Version = 5;

    Context context;

    public Base_Datos(@Nullable Context context) {
        super(context, nombreBaseDatos, null, Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLITE.tablaPerfil + "(Datos Text, Imagen Blob);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLITE.tablaCampos + "(ID Number, Nombre Text, Coordenadas Text);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLITE.tablaSubCampos + "(ID Number, ID_Padre Number, Nombre Text, Tipo_Cultivo Text, Tipo_Agricultura Text, Coordenadas Text, Fecha_Inicio Text, Fecha_Final Text);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLITE.tablaCultivo + "(Nombre Text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*SQLITE.eliminarBasedeDatos(context);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLITE.tablaPerfil + "(Datos Text, Imagen Blob);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLITE.tablaCampos + "(ID Number, Nombre Text, Coordenadas Text);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLITE.tablaSubCampos + "(ID Number, ID_Padre Number, Nombre Text, Tipo_Cultivo Text, Tipo_Agricultura Text, Coordenadas Text, Fecha_Inicio Text, Fecha_Final Text);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLITE.tablaCultivo + "(Nombre Text);");*/
    }
}