package com.fragmentoestudio.agronodo.Servicios;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import com.fragmentoestudio.agronodo.Utilidades.Uris;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Authentification {

    public static class IniciarSesion extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Uris.LOGIN);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(params[0]);
                writer.close();
                BufferedReader br = null;
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }
                StringBuffer jsonString = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
                br.close();
                connection.disconnect();
                return jsonString.toString();
            } catch (Exception e) {
                return "Error";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public static class RecuperarContraseña extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Uris.RECUPERAR_CONTRASEÑA);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(params[0]);
                writer.close();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return "Correo de Recuperación Enviado Exitosamente";
                } else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                    return "El email seleccionado no existe";
                }
                connection.disconnect();
            } catch (Exception e) {
                return "Error";
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public static class RefrescarPerfil extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Uris.REFRESCAR_PERFIL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Authorization", "token " + params[0]);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer jsonString = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
                br.close();
                connection.disconnect();
                return jsonString.toString();
            } catch (Exception e) {
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}
