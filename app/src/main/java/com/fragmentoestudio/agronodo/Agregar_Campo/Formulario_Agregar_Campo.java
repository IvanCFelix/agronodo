package com.fragmentoestudio.agronodo.Agregar_Campo;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Formulario_Agregar_Campo extends Fragment {

    public EditText txtNombre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_formulario_agregar_campo, container, false);

        txtNombre = view.findViewById(R.id.txt_AgregarCampo_Nombre);

        return view;
    }

}
