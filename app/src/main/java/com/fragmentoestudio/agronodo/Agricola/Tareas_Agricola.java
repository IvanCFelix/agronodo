package com.fragmentoestudio.agronodo.Agricola;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentoestudio.agronodo.Adaptadores.SlideViewPager;
import com.fragmentoestudio.agronodo.Agregar_Tarea.Activity_Agregar_Tarea;
import com.fragmentoestudio.agronodo.R;

import java.util.ArrayList;

public class Tareas_Agricola extends Fragment {

    FloatingActionButton fabAgregar;
    TabLayout tabIndicator;
    ViewPager paginas;
    SlideViewPager adaptador;

    Tareas_Pendientes_Agricola tareas_pendientes_agricola = new Tareas_Pendientes_Agricola();
    Tareas_Realizadas_Agricola tareas_realizadas_agricola = new Tareas_Realizadas_Agricola();

    ArrayList<Fragment> lista = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //gg
        View view = inflater.inflate(R.layout.fragment_tareas_agricola, container, false);

        lista.add(tareas_pendientes_agricola);
        lista.add(tareas_realizadas_agricola);

        fabAgregar = view.findViewById(R.id.fab_agregar);
        //swipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        paginas = view.findViewById(R.id.vpIntro);
        tabIndicator = view.findViewById(R.id.tab_indicator);
        adaptador = new SlideViewPager(getFragmentManager(), lista);
        paginas.setAdapter(adaptador);
        tabIndicator.setupWithViewPager(paginas, true);

        tabIndicator.getTabAt(0).setText(getString(R.string.pendientes));
        tabIndicator.getTabAt(1).setText(getString(R.string.realizadas));

        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_Agregar_Tarea.class));
            }
        });

        getActivity().setTitle(" " + getString(R.string.tareas));

        PrimeThread p = new PrimeThread(140);
        p.start();

        /*swipeRefreshLayout.setColorSchemeResources(R.color.colorGreen);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PrimeThread p = new PrimeThread(140);
                p.start();
            }
        });*/

        return view;
    }

    private void leerTareas() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                /*if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);*/
            }
        });
    }

    class PrimeThread extends Thread {
        long minPrime;

        PrimeThread(long minPrime) {
            this.minPrime = minPrime;
        }

        public void run() {
            leerTareas();
        }
    }

}
