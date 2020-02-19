package com.fragmentoestudio.agronodo.Editar_SubPredio;

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
import com.fragmentoestudio.agronodo.Agregar_SubPredio.Activity_Agregar_SubPredio;
import com.fragmentoestudio.agronodo.Clases.SubCampos;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Activity_Editar_SubPredio extends AppCompatActivity {

    ViewPager paginas;
    SlideViewPager adaptador;
    FloatingActionButton fabSiguiente, fabAtras;

    List<Fragment> lista = new ArrayList<>();
    Formulario_Editar_SubPredio formulario_editar_subPredio = new Formulario_Editar_SubPredio();
    Mapa_Editar_SubPredio mapa_editar_subPredio = new Mapa_Editar_SubPredio();

    int ID;
    SubCampos subCampo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_sub_predio);

        ID = getIntent().getIntExtra("ID", 1);
        Bundle bundle = new Bundle();
        bundle.putInt("ID", ID);

        subCampo = SQLITE.obtenerSubCampo(Activity_Editar_SubPredio.this, ID);

        mapa_editar_subPredio.setArguments(bundle);
        formulario_editar_subPredio.setArguments(bundle);
        fabSiguiente = findViewById(R.id.fab_siguiente);
        fabAtras = findViewById(R.id.fab_atras);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cerrar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.editar_predio));

        lista.add(mapa_editar_subPredio);
        lista.add(formulario_editar_subPredio);

        paginas = findViewById(R.id.vpIntro);
        adaptador = new SlideViewPager(getSupportFragmentManager(), lista);
        paginas.setAdapter(adaptador);

        fabSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paginas.getCurrentItem() == 0) {
                    if (mapa_editar_subPredio.coordenadas.size() >= 3) {
                        if (lista.size() == 1) {
                            lista.add(formulario_editar_subPredio);
                            adaptador.notifyDataSetChanged();
                        }
                        paginas.setCurrentItem(1);
                    } else {
                        if (mapa_editar_subPredio.coordenadas.size() == 0 && mapa_editar_subPredio.subCampos.size()==0) {
                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Editar_SubPredio.this);
                            dialogo1.setTitle(getString(R.string.atencion));
                            dialogo1.setMessage(getString(R.string.subpredio_tomara_todo_espacio));
                            dialogo1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    for (LatLng coordenada : mapa_editar_subPredio.coordenadas_padre) {
                                        mapa_editar_subPredio.coordenadas.add(coordenada);
                                    }
                                    mapa_editar_subPredio.coordenadas.remove(mapa_editar_subPredio.coordenadas.size() - 1);
                                    mapa_editar_subPredio.dibujarCampo();
                                    if (lista.size() == 1) {
                                        lista.add(formulario_editar_subPredio);
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
                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Editar_SubPredio.this);
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
                    if(mapa_editar_subPredio.coordenadas.size()>=3 || formulario_editar_subPredio.txtNombre.getText().toString().trim().isEmpty() || Formulario_Editar_SubPredio.txtFechaHoy.getText().toString().trim().isEmpty() || Formulario_Editar_SubPredio.txtFechaFin.getText().toString().trim().isEmpty()){
                        if(formulario_editar_subPredio.spnAgricultura.getSelectedItemPosition()!=0){
                            subCampo.setNombre(formulario_editar_subPredio.txtNombre.getText().toString().trim());
                            if(formulario_editar_subPredio.tilCultivo.getVisibility() == View.VISIBLE){
                                if(formulario_editar_subPredio.txtCultivo.getText().toString().trim().isEmpty()){
                                    completarDatos();
                                    return;
                                }else{
                                    subCampo.setTipo_Cultivo(formulario_editar_subPredio.txtCultivo.getText().toString().trim());
                                    SQLITE.agregarCultivo(Activity_Editar_SubPredio.this, formulario_editar_subPredio.txtCultivo.getText().toString().trim());
                                }
                            }else{
                                subCampo.setTipo_Cultivo(formulario_editar_subPredio.spnCultivos.getSelectedItem().toString());
                            }
                            subCampo.setTipo_Agricultura(formulario_editar_subPredio.spnAgricultura.getSelectedItem().toString());

                            String coordenadas = "";
                            mapa_editar_subPredio.coordenadas.add(mapa_editar_subPredio.coordenadas.get(0));
                            for (int i = 0; i < mapa_editar_subPredio.coordenadas.size(); i++) {
                                coordenadas = coordenadas + "{'Latitud': " + mapa_editar_subPredio.coordenadas.get(i).latitude + ", 'Longitud': " + mapa_editar_subPredio.coordenadas.get(i).longitude + "}";
                                if (mapa_editar_subPredio.coordenadas.size() - 1 != i) {
                                    coordenadas = coordenadas + ",";
                                }
                            }
                            subCampo.setCoordenadas(coordenadas);

                            subCampo.setFecha_Inicio(Formulario_Editar_SubPredio.txtFechaHoy.getText().toString().trim());
                            subCampo.setFecha_Final(Formulario_Editar_SubPredio.txtFechaFin.getText().toString().trim());

                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Editar_SubPredio.this);
                            dialogo1.setCancelable(false);
                            switch (SQLITE.editarSubPredio(Activity_Editar_SubPredio.this, subCampo)){
                                case 1:
                                    dialogo1.setMessage(getString(R.string.subpredio_editado));
                                    break;
                                case 2:
                                    dialogo1.setMessage(getString(R.string.no_se_pudo_editar_subpredio));
                                    break;
                            }
                            dialogo1.setPositiveButton(getString(R.string.enterado), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    Formulario_Editar_SubPredio.txtFechaHoy.setText("");
                                    Formulario_Editar_SubPredio.txtFechaFin.setText("");
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
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Editar_SubPredio.this);
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
