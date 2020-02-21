package com.fragmentoestudio.agronodo.Agricola;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentoestudio.agronodo.R;

public class Intro_Agricola extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_agricola, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        getActivity().setTitle(" AgroNodo");
        swipeRefreshLayout.setColorSchemeResources(R.color.colorGreen);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

}
