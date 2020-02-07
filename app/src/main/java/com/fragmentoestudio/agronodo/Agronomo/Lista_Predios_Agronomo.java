package com.fragmentoestudio.agronodo.Agronomo;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Agregar_Campo.Activity_Agregar_Campo;
import com.fragmentoestudio.agronodo.Adaptadores.Predios_Encabezado;
import com.fragmentoestudio.agronodo.Clases.Campos;
import com.fragmentoestudio.agronodo.Clases.Cultivos;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Servicios.Authentification;
import com.fragmentoestudio.agronodo.Utilidades.Datos;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Lista_Predios_Agronomo extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    Predios_Encabezado adapter;
    FloatingActionButton fabAgregar;

    TextView txtNoHay;

    ProgressDialog cargando;

    public static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 3;
    public static final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_predios_agronomo, container, false);

        txtNoHay = view.findViewById(R.id.txtNoHay);
        recyclerView = view.findViewById(R.id.recyclerView);
        fabAgregar = view.findViewById(R.id.fab_agregar);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        getActivity().setTitle(" Mis Predios");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cargando= new ProgressDialog(getContext());

        PrimeThread p = new PrimeThread(140);
        p.start();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });


        swipeRefreshLayout.setColorSchemeResources(R.color.colorGreen);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PrimeThread p = new PrimeThread(140);
                p.start();
            }
        });

        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);
                    if (ActivityCompat.checkSelfPermission(getContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(getContext(), Activity_Agregar_Campo.class));
                    }
                } else {
                    startActivity(new Intent(getContext(), Activity_Agregar_Campo.class));
                }
            }
        });

        return view;
    }

    private void llenarLista() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                ArrayList<Campos> campos = SQLITE.obtenerCampos(getContext());
                adapter = new Predios_Encabezado(campos, getContext(), recyclerView);
                recyclerView.setAdapter(adapter);
                if(campos.isEmpty()){
                    txtNoHay.setVisibility(View.VISIBLE);
                }else{
                    txtNoHay.setVisibility(View.GONE);
                }
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
        /*if (Datos.existeInternet(getContext(), getActivity())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            cargando = ProgressDialog.show(getContext(), "", "Cargando Datos", true);
                        }
                    });
                    cultivos.clear();
                    cultivos = SQLITE.obtenerCultivosLista(getContext());

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), cultivos.size() + "", Toast.LENGTH_SHORT).show();
                            if (swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                            if (cargando.isShowing())
                                cargando.dismiss();
                            adapater.notifyDataSetChanged();
                        }
                    });

                }
            }).start();
        }
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                if (cargando != null && cargando.isShowing())
                    cargando.dismiss();
                adapater.notifyDataSetChanged();
            }
        });*/

    }

    class PrimeThread extends Thread {
        long minPrime;

        PrimeThread(long minPrime) {
            this.minPrime = minPrime;
        }

        public void run() {
            llenarLista();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            PrimeThread p = new PrimeThread(140);
            p.start();
        }catch (Exception e){}
    }
}
