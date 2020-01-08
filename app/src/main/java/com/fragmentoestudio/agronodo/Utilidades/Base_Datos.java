package com.fragmentoestudio.agronodo.Utilidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class Base_Datos extends SQLiteOpenHelper {

    public static final String nombreBaseDatos = "AgroNodo";

    public Base_Datos(@Nullable Context context) {
        super(context, nombreBaseDatos, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SQLITE.tablaPerfil + "(Datos Text, Imagen Blob);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}