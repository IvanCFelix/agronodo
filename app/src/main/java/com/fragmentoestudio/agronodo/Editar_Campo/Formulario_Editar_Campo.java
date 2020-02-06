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

    public EditText txtNombre, txtCultivo;
    TextInputLayout tilCultivo;
    public Spinner spnCultivo;
    public ArrayList<String> cultivos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_formulario__editar__campo, container, false);

        int ID = getArguments().getInt("ID");
        Campos campo = SQLITE.obtenerCampo(getContext(), ID);

        txtNombre = view.findViewById(R.id.txt_AgregarCampo_Nombre);
        txtCultivo = view.findViewById(R.id.txt_AgregarCampo_Cultivo);
        tilCultivo = view.findViewById(R.id.til_AgregarCampo_Cultivo);

        spnCultivo = view.findViewById(R.id.spn_AgregarCampo_Tipo);
        cultivos = SQLITE.obtenerCultivos(getContext());
        cultivos.add("Seleccione un cultivo");
        spnCultivo.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_item, cultivos));

        txtNombre.setText(campo.getNombre());
        spnCultivo.setSelection(cultivos.indexOf(campo.getTipo_Cultivo()));

        spnCultivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnCultivo.getSelectedItem().equals("Seleccione un cultivo")){
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
