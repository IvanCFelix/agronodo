package com.fragmentoestudio.agronodo.Agronomo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import de.hdodenhof.circleimageview.CircleImageView;

public class Mi_Usuario_Agronomo extends Fragment {

    CircleImageView civPerfil;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_mi_usuario_agronomo, container, false);

        civPerfil = view.findViewById(R.id.civ_perfil);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorGreen);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        civPerfil.setImageBitmap(SQLITE.obtenerImagen(getContext()));

        getActivity().setTitle(" Mi Usuario");
        return view;
    }

}
