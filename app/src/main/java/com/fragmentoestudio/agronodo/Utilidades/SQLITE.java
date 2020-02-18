package com.fragmentoestudio.agronodo.Utilidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fragmentoestudio.agronodo.Clases.Campos;
import com.fragmentoestudio.agronodo.Clases.Cultivos;
import com.fragmentoestudio.agronodo.Clases.SubCampos;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SQLITE {
    public static final String tablaPerfil = "Perfil";
    public static final String tablaCampos = "Campos";
    public static final String tablaSubCampos = "SubCampos";
    public static final String tablaCultivo = "Cultivos";

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
        if(obtenerTamañoTabla(contexto, tablaPerfil)>0) {
            SQLITE.limpiarTabla(contexto, tablaPerfil);
        }
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

    public static int obtenerValorMaximo(Context contexto, String tabla, String campo) {
        Base_Datos bd = new Base_Datos(contexto);
        SQLiteDatabase db = bd.getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("select MAX(" + campo + ") from " + tabla + " ;", null);
            if (c.getCount() == 1) {
                c.moveToFirst();
                int valor = c.getInt(0);
                db.close();
                return valor;
            }
        }
        return 1;
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

    public static int agregarCampo(Context context, Campos campo){
        Base_Datos bd = new Base_Datos(context);
        SQLiteDatabase db = bd.getWritableDatabase();
        if (db != null) {
            ContentValues registro = new ContentValues();
            registro.put("ID", campo.getID());
            registro.put("Nombre", campo.getNombre());
            registro.put("Coordenadas", campo.getCoordenadas());
            db.insert(SQLITE.tablaCampos, null, registro);
            db.close();
            return 1;
        }
        return 2;
    }

    public static int agregarCultivo(Context context, String cultivo){
        Base_Datos bd = new Base_Datos(context);
        SQLiteDatabase db = bd.getWritableDatabase();
        if (db != null) {
            ContentValues registro = new ContentValues();
            registro.put("Nombre", cultivo);
            db.insert(SQLITE.tablaCultivo, null, registro);
            db.close();
            return 1;
        }
        return 2;
    }

    public static int agregarSubCampo(Context context, SubCampos subCampos){
        Base_Datos bd = new Base_Datos(context);
        SQLiteDatabase db = bd.getWritableDatabase();
        if (db != null) {
            ContentValues registro = new ContentValues();
            registro.put("ID", subCampos.getID());
            registro.put("ID_Padre", subCampos.getID_Padre());
            registro.put("Nombre", subCampos.getNombre());
            registro.put("Tipo_Cultivo", subCampos.getTipo_Cultivo());
            registro.put("Tipo_Agricultura", subCampos.getTipo_Agricultura());
            registro.put("Coordenadas", subCampos.getCoordenadas());
            registro.put("Fecha_Inicio", subCampos.getFecha_Inicio());
            registro.put("Fecha_Final", subCampos.getFecha_Final());
            db.insert(SQLITE.tablaSubCampos, null, registro);
            db.close();
            return 1;
        }
        return 2;
    }

    public static ArrayList<String> obtenerCultivos(Context contexto){
        Base_Datos base_datos = new Base_Datos(contexto);
        SQLiteDatabase db = base_datos.getWritableDatabase();
        if (db != null) {
            ArrayList<String> lista = new ArrayList<>();
            Cursor c= db.rawQuery("select * from " + tablaCultivo + ";", null);
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        lista.add(c.getString(0));
                    } while (c.moveToNext());
                }
            }
            return lista;
        }
        return null;
    }

    public static ArrayList<Campos> obtenerCampos(Context contexto) {
        Base_Datos bd = new Base_Datos(contexto);
        SQLiteDatabase db = bd.getWritableDatabase();
        if (db != null) {
            ArrayList<Campos> lista = new ArrayList<>();
            Cursor c = db.rawQuery("select * from " + SQLITE.tablaCampos + ";", null);
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        lista.add(new Campos(c.getInt(0), c.getString(1), c.getString(2)));
                    } while (c.moveToNext());
                }
            }
            return lista;
        }
        return null;
    }

    public static ArrayList<SubCampos> obtenerSubCampos(Context context, int ID_Padre){
        Base_Datos bd = new Base_Datos(context);
        SQLiteDatabase db = bd.getWritableDatabase();
        if (db != null) {
            ArrayList<SubCampos> lista = new ArrayList<>();
            Cursor c = db.rawQuery("select * from " + SQLITE.tablaSubCampos + " where ID_Padre = " + ID_Padre + ";", null);
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        lista.add(new SubCampos(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7)));
                    } while (c.moveToNext());
                }
            }
            return lista;
        }
        return null;
    }

    public static Campos obtenerCampo(Context context, int id) {
        Base_Datos bd = new Base_Datos(context);
        SQLiteDatabase db = bd.getWritableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("select * from " + SQLITE.tablaCampos + " where ID = " + id + "  ;", null);
            if (c.getCount() == 1) {
                c.moveToFirst();
                Campos campo = new Campos(c.getInt(0), c.getString(1), c.getString(2));
                db.close();
                return campo;
            }
        }
        return null;
    }

    public static int editarCampo(Context contexto, Campos campo) {
        Base_Datos bd = new Base_Datos(contexto);
        SQLiteDatabase db = bd.getWritableDatabase();
        if (db != null) {
            ContentValues registro = new ContentValues();
            registro.put("Nombre", campo.getNombre());
            registro.put("Coordenadas", campo.getCoordenadas());
            db.update(SQLITE.tablaCampos, registro, "ID = '" + campo.getID() + "'", null);
            db.close();
            return 1;
        }
        return 2;
    }

    public static int borrarCampo(Context contexto, int id) {
        Base_Datos bd = new Base_Datos(contexto);
        SQLiteDatabase db = bd.getWritableDatabase();
        if (db != null) {
            db.execSQL("DELETE FROM " + SQLITE.tablaCampos + " WHERE ID=" + id + ";");
            db.close();
            return 1;
        }
        return 2;
    }

    public static int borrarSubCampo(Context contexto, int id) {
        Base_Datos bd = new Base_Datos(contexto);
        SQLiteDatabase db = bd.getWritableDatabase();
        if (db != null) {
            db.execSQL("DELETE FROM " + SQLITE.tablaSubCampos + " WHERE ID=" + id + ";");
            db.close();
            return 1;
        }
        return 2;
    }

    public static void limpiarTabla(Context contexto, String tabla) {
        Base_Datos bd = new Base_Datos(contexto);
        SQLiteDatabase db = bd.getWritableDatabase();
        db.execSQL("delete from " + tabla + ";");
        db.close();
    }
}
