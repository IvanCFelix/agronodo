package com.fragmentoestudio.agronodo.Agricola;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Menu_Agricola;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Servicios.Authentification;
import com.fragmentoestudio.agronodo.Utilidades.Datos;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import com.fragmentoestudio.agronodo.Utilidades.Uris;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Mi_Usuario_Agricola extends Fragment {

    CircleImageView civPerfil;

    SwipeRefreshLayout swipeRefreshLayout;

    String Usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mi_usuario_agricola, container, false);

        civPerfil = view.findViewById(R.id.civ_perfil);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorGreen);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                try {
                    PrimeThread p = new PrimeThread(140);
                    p.start();
                } catch (Exception e) {
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        getActivity().setTitle(" Mi Usuario");

        return view;
    }

    class PrimeThread extends Thread {
        long minPrime;

        PrimeThread(long minPrime) {
            this.minPrime = minPrime;
        }

        public void run() {
            leerUsuario();
        }
    }

    void leerUsuario() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject usuarioJSON = new JSONObject(Usuario);
                    String token = usuarioJSON.getString("token");
                    final String resultado = new Authentification.RefrescarPerfil().execute(token).get();
                    final JSONObject datos = new JSONObject(resultado);
                    String url = datos.getJSONObject("profile").getString("photo");
                    String formato = url.substring(url.indexOf(".") + 1);
                    Bitmap imagen = new Datos.imagendeWEB().execute(Uris.ENDPOINT_AGRONODO + url).get();
                    if (imagen == null) {
                        imagen = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                        formato = "png";
                    }
                    SQLITE.ingresarSesion(getContext(), resultado, imagen, formato);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            dibujarUsuario();
                        }
                    });
                } catch (
                        JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "Ha ocurrido un error con los datos", Toast.LENGTH_SHORT).show();
                            if (swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    e.printStackTrace();
                } catch (
                        InterruptedException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "Ha ocurrido un error con los datos", Toast.LENGTH_SHORT).show();
                            if (swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    e.printStackTrace();
                } catch (
                        ExecutionException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "Ha ocurrido un error con los datos", Toast.LENGTH_SHORT).show();
                            if (swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        dibujarUsuario();
    }

    void dibujarUsuario() {
        try {
            Bitmap imagen = SQLITE.obtenerImagen(getContext());
            civPerfil.setImageBitmap(imagen);
            Menu_Agricola.imagenPerfil.setImageBitmap(imagen);
            Usuario = SQLITE.obtenerUsuario(getContext());
            final JSONObject usuarioJSON = new JSONObject(Usuario);
            Menu_Agricola.txtNombre.setText(usuarioJSON.getJSONObject("profile").getString("agricola"));
            Menu_Agricola.txtCorreo.setText(usuarioJSON.getString("email"));
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        }
    }
}
