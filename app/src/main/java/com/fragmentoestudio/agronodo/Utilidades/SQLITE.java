package com.fragmentoestudio.agronodo.Utilidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class SQLITE {
    public static final String tablaPerfil = "Perfil";

    public static int obtenerTamañoTabla(Context contexto, String tabla){
        Base_Datos base_datos = new Base_Datos(contexto);
        SQLiteDatabase db = base_datos.getWritableDatabase();
        if (db != null) {
            Cursor c= db.rawQuery("select * from " + tabla + ";", null);
            int contador= c.getCount();
            db.close();
            return contador;
        }
        return 0;
    }

    public static void ingresarSesion(Context contexto, String JSON, Bitmap imagen, String formato) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if(formato.equals("png")){
            imagen.compress(Bitmap.CompressFormat.PNG, 50, bos);
        }else{
            imagen.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        }
        byte[] img = bos.toByteArray();
        if(obtenerTamañoTabla(contexto, tablaPerfil)==0) {
            Base_Datos base_de_datos = new Base_Datos(contexto);
            SQLiteDatabase db = base_de_datos.getWritableDatabase();
            if (db != null) {
                ContentValues usuario = new ContentValues();
                usuario.put("Datos", JSON);
                usuario.put("Imagen", img);
                db.insert(SQLITE.tablaPerfil, null, usuario);
                db.close();
            }
        }
    }

    public static String obtenerUsuario(Context contexto){
        if(obtenerTamañoTabla(contexto, tablaPerfil)==1) {
            Base_Datos base_de_datos = new Base_Datos(contexto);
            SQLiteDatabase db = base_de_datos.getWritableDatabase();
            if (db != null) {
                Cursor c = db.rawQuery("select * from " + tablaPerfil + ";", null);
                if(c.getCount()>0) {
                    if(c.moveToFirst()){
                        String usuario = c.getString(0);
                        db.close();
                        return usuario;
                    }
                }
            }
        }
        return null;
    }

    public static Bitmap obtenerImagen(Context contexto){
        if(obtenerTamañoTabla(contexto, tablaPerfil)==1) {
            Base_Datos base_de_datos = new Base_Datos(contexto);
            SQLiteDatabase db = base_de_datos.getWritableDatabase();
            if (db != null) {
                Cursor c = db.rawQuery("select * from " + tablaPerfil + ";", null);
                if(c.getCount()>0) {
                    if(c.moveToFirst()){
                        Bitmap imagen = BitmapFactory.decodeByteArray(c.getBlob(1), 0, c.getBlob(1).length);
                        db.close();
                        return imagen;
                    }
                }
            }
        }
        return null;
    }

    public static void eliminarBasedeDatos(Context context){
        context.deleteDatabase(Base_Datos.nombreBaseDatos);
    }
}
