package com.fragmentoestudio.agronodo.Agregar_SubPredio;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.fragmentoestudio.agronodo.Adaptadores.SlideViewPager;
import com.fragmentoestudio.agronodo.Agregar_Campo.Activity_Agregar_Campo;
import com.fragmentoestudio.agronodo.Clases.SubCampos;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Activity_Agregar_SubPredio extends AppCompatActivity {

    ViewPager paginas;
    SlideViewPager adaptador;
    FloatingActionButton fabSiguiente, fabAtras;

    List<Fragment> lista = new ArrayList<>();
    Formulario_Agregar_SubPredio formulario_agregar_subPredio = new Formulario_Agregar_SubPredio();
    Mapa_Agregar_SubPredio mapa_agregar_subPredio = new Mapa_Agregar_SubPredio();

    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_subpredio);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_atras);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.agregar_subpredio));

        fabSiguiente = findViewById(R.id.fab_siguiente);
        fabAtras = findViewById(R.id.fab_atras);

        ID = getIntent().getIntExtra("ID_Padre", 1);
        Bundle bundle = new Bundle();
        bundle.putInt("ID_Padre", ID);

        mapa_agregar_subPredio.setArguments(bundle);

        lista.add(mapa_agregar_subPredio);

        paginas = findViewById(R.id.vpIntro);
        adaptador = new SlideViewPager(getSupportFragmentManager(), lista);
        paginas.setAdapter(adaptador);

        fabSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paginas.getCurrentItem() == 0) {
                    if (mapa_agregar_subPredio.coordenadas.size() >= 3) {
                        if (lista.size() == 1) {
                            lista.add(formulario_agregar_subPredio);
                            adaptador.notifyDataSetChanged();
                        }
                        paginas.setCurrentItem(1);
                    } else {
                        if (mapa_agregar_subPredio.coordenadas.size() == 0 && mapa_agregar_subPredio.subCampos.size()==0) {
                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Agregar_SubPredio.this);
                            dialogo1.setTitle(getString(R.string.atencion));
                            dialogo1.setMessage(getString(R.string.subpredio_tomara_todo_espacio));
                            dialogo1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    for (LatLng coordenada : mapa_agregar_subPredio.coordenadas_padre) {
                                        mapa_agregar_subPredio.coordenadas.add(coordenada);
                                    }
                                    mapa_agregar_subPredio.coordenadas.remove(mapa_agregar_subPredio.coordenadas.size() - 1);
                                    mapa_agregar_subPredio.dibujarCampo();
                                    if (lista.size() == 1) {
                                        lista.add(formulario_agregar_subPredio);
                                        adaptador.notifyDataSetChanged();
                                    }
                                    paginas.setCurrentItem(1);
                                }
                            });
                            dialogo1.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    dialogo1.dismiss();
                                }
                            });
                            dialogo1.show();
                        }else{
                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Agregar_SubPredio.this);
                            dialogo1.setTitle("Error");
                            dialogo1.setMessage(getString(R.string.campo_seleccionado_no_valido));
                            dialogo1.setPositiveButton(getString(R.string.enterado), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    dialogo1.dismiss();
                                }
                            });
                            dialogo1.show();
                        }
                    }
                }else{
                    if(mapa_agregar_subPredio.coordenadas.size()>=3 || formulario_agregar_subPredio.txtNombre.getText().toString().trim().isEmpty() || Formulario_Agregar_SubPredio.txtFechaHoy.getText().toString().trim().isEmpty() || Formulario_Agregar_SubPredio.txtFechaFin.getText().toString().trim().isEmpty()){
                        if(formulario_agregar_subPredio.spnAgricultura.getSelectedItemPosition()!=0){
                            SubCampos subCampo = new SubCampos();
                            subCampo.setID(SQLITE.obtenerValorMaximo(Activity_Agregar_SubPredio.this, SQLITE.tablaSubCampos, "ID") + 1);
                            subCampo.setID_Padre(ID);
                            subCampo.setNombre(formulario_agregar_subPredio.txtNombre.getText().toString().trim());
                            if(formulario_agregar_subPredio.tilCultivo.getVisibility() == View.VISIBLE){
                                if(formulario_agregar_subPredio.txtCultivo.getText().toString().trim().isEmpty()){
                                    completarDatos();
                                    return;
                                }else{
                                    subCampo.setTipo_Cultivo(formulario_agregar_subPredio.txtCultivo.getText().toString().trim());
                                    SQLITE.agregarCultivo(Activity_Agregar_SubPredio.this, formulario_agregar_subPredio.txtCultivo.getText().toString().trim());
                                }
                            }else{
                                subCampo.setTipo_Cultivo(formulario_agregar_subPredio.spnCultivos.getSelectedItem().toString());
                            }
                            subCampo.setTipo_Agricultura(formulario_agregar_subPredio.spnAgricultura.getSelectedItem().toString());

                            String coordenadas = "";
                            mapa_agregar_subPredio.coordenadas.add(mapa_agregar_subPredio.coordenadas.get(0));
                            for (int i = 0; i < mapa_agregar_subPredio.coordenadas.size(); i++) {
                                coordenadas = coordenadas + "{'Latitud': " + mapa_agregar_subPredio.coordenadas.get(i).latitude + ", 'Longitud': " + mapa_agregar_subPredio.coordenadas.get(i).longitude + "}";
                                if (mapa_agregar_subPredio.coordenadas.size() - 1 != i) {
                                    coordenadas = coordenadas + ",";
                                }
                            }
                            subCampo.setCoordenadas(coordenadas);

                            subCampo.setFecha_Inicio(Formulario_Agregar_SubPredio.txtFechaHoy.getText().toString().trim());
                            subCampo.setFecha_Final(Formulario_Agregar_SubPredio.txtFechaFin.getText().toString().trim());

                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Agregar_SubPredio.this);
                            dialogo1.setCancelable(false);
                            switch (SQLITE.agregarSubCampo(Activity_Agregar_SubPredio.this, subCampo)){
                                case 1:
                                    dialogo1.setMessage(getString(R.string.subpredio_registrado));
                                    break;
                                case 2:
                                    dialogo1.setMessage(getString(R.string.no_se_pudo_registrar_subpredio));
                                    break;
                            }
                            dialogo1.setPositiveButton(getString(R.string.enterado), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    Formulario_Agregar_SubPredio.txtFechaHoy.setText("");
                                    Formulario_Agregar_SubPredio.txtFechaFin.setText("");
                                    finish();
                                }
                            });
                            dialogo1.show();
                        }else{
                            completarDatos();
                        }
                    }
                }
            }
        });

        fabAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paginas.getCurrentItem() == 1) {
                    paginas.setCurrentItem(0);
                }
            }
        });

        paginas.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    fabSiguiente.setImageResource((R.drawable.ic_siguiente));
                    fabAtras.setVisibility(View.GONE);
                } else {
                    fabSiguiente.setImageResource((R.drawable.ic_finalizar));
                    fabAtras.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void completarDatos() {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Agregar_SubPredio.this);
        dialogo1.setTitle(getString(R.string.datos_incompletos));
        dialogo1.setMessage(getString(R.string.complete_datos_correctamente));
        dialogo1.setPositiveButton(getString(R.string.enterado), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }
}
