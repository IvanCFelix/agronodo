package com.fragmentoestudio.agronodo.Utilidades;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Base64;

import com.fragmentoestudio.agronodo.Login;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Datos {
    Bitmap base64aImagen(String imagen){
        byte[] decodedString = Base64.decode(imagen, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static class imagendeWEB extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch(IOException e) {
                return null;
            }
        }

        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
        }

    }

    public static boolean existeInternet(final Context context, Activity activity){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }else{
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                    dialogo1.setTitle("Sin Conexión");
                    dialogo1.setMessage("Compruebe su conexión a internet");
                    dialogo1.setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                        }
                    });
                    dialogo1.show();
                }
            });
        }
        return false;
    }
}
