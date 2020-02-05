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

    public EditText txtNombre, txtCultivo;
    TextInputLayout tilCultivo;
    public Spinner spnCultivo;
    ArrayList<String> cultivos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_formulario_agregar_campo, container, false);

        txtNombre = view.findViewById(R.id.txt_AgregarCampo_Nombre);
        txtCultivo = view.findViewById(R.id.txt_AgregarCampo_Cultivo);
        tilCultivo = view.findViewById(R.id.til_AgregarCampo_Cultivo);
        spnCultivo = view.findViewById(R.id.spn_AgregarCampo_Tipo);

        cultivos = SQLITE.obtenerCultivos(getContext());

        cultivos.add("-Otro-");

        spnCultivo.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_item, cultivos));

        spnCultivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnCultivo.getSelectedItem().equals("-Otro-")){
                    tilCultivo.setVisibility(View.VISIBLE);
                }else{
                    tilCultivo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

}
