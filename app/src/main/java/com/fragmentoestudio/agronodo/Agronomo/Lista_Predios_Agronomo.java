package com.fragmentoestudio.agronodo.Agronomo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Adaptadores.Predios_Encabezado;
import com.fragmentoestudio.agronodo.Cines;
import com.fragmentoestudio.agronodo.Movie;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Servicios.Authentification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Lista_Predios_Agronomo extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Cines> cines = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    Predios_Encabezado movieAdapter;
    FloatingActionButton fabAgregar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_predios_agronomo, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        fabAgregar = view.findViewById(R.id.fab_agregar);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        getActivity().setTitle("Mis Predios");
        initRecyclerView();
        //initData();
        PrimeThread p = new PrimeThread(143);
        p.start();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==0){
                    swipeRefreshLayout.setEnabled(true);
                }else{
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });


        swipeRefreshLayout.setColorSchemeResources(R.color.colorGreen);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PrimeThread p = new PrimeThread(143);
                p.start();
            }
        });

        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void leerComposiciones() throws ExecutionException, InterruptedException, JSONException {
        JSONArray composiciones = new JSONArray(new Authentification.ObtenerComposiciones().execute().get());
        cines.clear();
        for (int i = 0; i<composiciones.length(); i++) {
            JSONObject composicion = (composiciones.getJSONObject(i));
            if (cines.size() > 0) {
                if (listaindexOf(composicion.getJSONObject("profile").getString("email")) == -1) {
                    ArrayList<Movie> pelicula = new ArrayList<>();
                    pelicula.add(new Movie(composicion.getString("name")));
                    cines.add(new Cines(composicion.getJSONObject("profile").getString("email"), pelicula));
                } else {
                    cines.get(listaindexOf(composicion.getJSONObject("profile").getString("email"))).insertarPelicula(new Movie(composicion.getString("name")));
                }
            } else {
                ArrayList<Movie> pelicula = new ArrayList<>();
                pelicula.add(new Movie(composicion.getString("name")));
                cines.add(new Cines(composicion.getJSONObject("profile").getString("email"), pelicula));
            }
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                    movieAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    class PrimeThread extends Thread {
        long minPrime;
        PrimeThread(long minPrime) {
            this.minPrime = minPrime;
        }
        public void run() {
            try {
                leerComposiciones();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int listaindexOf(String nombre){
        for (int i = 0; i<cines.size(); i++){
            if(cines.get(i).getNombre().equals(nombre))
                return i;
        }
        return -1;
    }

    private void initRecyclerView() {
        movieAdapter = new Predios_Encabezado(cines, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(movieAdapter);
    }

    private void initData() {
        cines = new ArrayList<>();
        ArrayList<Movie> movies= new ArrayList<>();
        movies.add(new Movie("Cuadra de mi casa"));
        movies.add(new Movie("Tec de Los Mochis"));
        movies.add(new Movie("Cuadra de mi abuela"));
        cines.add(new Cines("Cultivo de Maíz", movies));
        ArrayList<Movie> movies2= new ArrayList<>();
        movies2.add(new Movie("SML"));
        cines.add(new Cines("Cultivo de Aguacate", movies2));
        ArrayList<Movie> movies3= new ArrayList<>();
        movies3.add(new Movie("Corerepe"));
        movies3.add(new Movie("Univafu"));
        cines.add(new Cines("Cultivo de Cacahuate", movies3));
        ArrayList<Movie> movies4= new ArrayList<>();
        movies4.add(new Movie("Casa Daniel"));
        cines.add(new Cines("Cultivo de Trigo", movies4));

    }


}
