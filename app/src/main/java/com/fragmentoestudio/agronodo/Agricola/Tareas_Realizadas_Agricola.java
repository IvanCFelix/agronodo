package com.fragmentoestudio.agronodo.Agricola;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentoestudio.agronodo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tareas_Realizadas_Agricola extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tareas_realizadas_agricola, container, false);
        return view;
    }

}
