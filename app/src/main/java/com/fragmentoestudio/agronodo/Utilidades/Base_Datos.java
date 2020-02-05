package com.fragmentoestudio.agronodo.Utilidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class Base_Datos extends SQLiteOpenHelper {

    public static final String nombreBaseDatos = "AgroNodo";

    public static final int Version = 3;

    public Base_Datos(@Nullable Context context) {
        super(context, nombreBaseDatos, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLITE.tablaPerfil + "(Datos Text, Imagen Blob);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLITE.tablaCampos + "(ID Number, Nombre Text, Cultivo Text, Coordenadas Text);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLITE.tablaCultivos + "(Nombre Text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}