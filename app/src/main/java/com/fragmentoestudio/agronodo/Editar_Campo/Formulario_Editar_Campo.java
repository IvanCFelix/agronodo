package com.fragmentoestudio.agronodo.Editar_Campo;


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

import com.fragmentoestudio.agronodo.Clases.Campos;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import java.util.ArrayList;

public class Formulario_Editar_Campo extends Fragment {

    public EditText txtNombre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_formulario__editar__campo, container, false);

        int ID = getArguments().getInt("ID");
        Campos campo = SQLITE.obtenerCampo(getContext(), ID);

        txtNombre = view.findViewById(R.id.txt_AgregarCampo_Nombre);
        txtNombre.setText(campo.getNombre());

        return view;
    }

}
