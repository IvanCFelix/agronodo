package com.fragmentoestudio.agronodo.Editar_SubPredio;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.fragmentoestudio.agronodo.Agregar_SubPredio.Formulario_Agregar_SubPredio;
import com.fragmentoestudio.agronodo.Clases.SubCampos;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Formulario_Editar_SubPredio extends Fragment {

    List<String> listaAgricultura = new ArrayList<>();
    List<String> listaCultivos = new ArrayList<>();

    Spinner spnAgricultura, spnCultivos;
    TextInputLayout tilCultivo;

    public static EditText txtFechaHoy, txtFechaFin;

    EditText txtNombre, txtCultivo;

    SubCampos subCampo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_formulario_editar_sub_predio, container, false);

        int ID = getArguments().getInt("ID");

        subCampo = SQLITE.obtenerSubCampo(getContext(), ID);

        spnCultivos = view.findViewById(R.id.spn_AgregarCampo_Cultivo);
        spnAgricultura = view.findViewById(R.id.spn_AgregarCampo_Agricultura);
        tilCultivo = view.findViewById(R.id.til_AgregarSubCampo_Cultivo);

        txtNombre = view.findViewById(R.id.txt_AgregarSubCampo_Nombre);
        txtCultivo = view.findViewById(R.id.txt_AgregarSubCampo_Cultivo);
        txtFechaHoy = view.findViewById(R.id.txtSubCampoFechaHoy);
        txtFechaFin = view.findViewById(R.id.txtSubCampoFechaFin);

        txtFechaHoy.setText(getFecha());

        ArrayList<String> cultivos = SQLITE.obtenerCultivos(getContext());

        for(String nombre : cultivos){
            listaCultivos.add(nombre);
        }

        listaCultivos.add(getContext().getString(R.string.seleccionar_cultivo));

        listaAgricultura.add(getContext().getString(R.string.seleccionar_agricultura));
        listaAgricultura.add("Tradicional");
        listaAgricultura.add("Protegida");

        spnCultivos.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_item, listaCultivos));
        spnAgricultura.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_item, listaAgricultura));

        txtNombre.setText(subCampo.getNombre());
        txtFechaHoy.setText(subCampo.getFecha_Inicio());
        txtFechaFin.setText(subCampo.getFecha_Final());
        spnCultivos.setSelection(listaCultivos.indexOf(subCampo.getTipo_Cultivo()));
        spnAgricultura.setSelection(listaAgricultura.indexOf(subCampo.getTipo_Agricultura()));

        spnCultivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnCultivos.getSelectedItemPosition()==listaCultivos.size()-1){
                    tilCultivo.setVisibility(View.VISIBLE);
                }else{
                    tilCultivo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtFechaHoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirfechaInicio(view);
            }
        });

        txtFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirfechaFin(view);
            }
        });

        return view;
    }

    public static String getHora() {
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedDate=dateFormat.format(date);
        return formattedDate;
    }

    public static String getFecha(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month= month+1;
            if(getTag().equals("Inicio"))
                txtFechaHoy.setText(day + "/" + month + "/" + year);

            if(getTag().equals("Fin"))
                txtFechaFin.setText(day + "/" + month + "/" + year);
        }
    }

    public void abrirfechaInicio(View v) {
        DialogFragment newFragment = new Formulario_Editar_SubPredio.DatePickerFragment();
        newFragment.show(getFragmentManager(), "Inicio");
    }

    public void abrirfechaFin(View v) {
        DialogFragment newFragment = new Formulario_Editar_SubPredio.DatePickerFragment();
        newFragment.show(getFragmentManager(), "Fin");
    }

}
