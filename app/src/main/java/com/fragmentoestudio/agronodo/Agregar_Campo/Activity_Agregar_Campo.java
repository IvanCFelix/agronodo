package com.fragmentoestudio.agronodo.Agregar_Campo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Adaptadores.SlideViewPager;
import com.fragmentoestudio.agronodo.Clases.Campos;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Activity_Agregar_Campo extends AppCompatActivity {

    ViewPager paginas;
    SlideViewPager adaptador;
    FloatingActionButton fabSiguiente, fabAtras;

    Mapa_Agregar_Campo mapa_agregar_campo = new Mapa_Agregar_Campo();
    Formulario_Agregar_Campo formulario_agregar_campo = new Formulario_Agregar_Campo();
    List<Fragment> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_campo);
        fabSiguiente = findViewById(R.id.fab_siguiente);
        fabAtras = findViewById(R.id.fab_atras);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cerrar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Agregar Predio");

        lista.add(mapa_agregar_campo);

        paginas = findViewById(R.id.vpIntro);
        adaptador = new SlideViewPager(getSupportFragmentManager(), lista);
        paginas.setAdapter(adaptador);

        fabSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paginas.getCurrentItem() == 0) {
                    if (mapa_agregar_campo.coordenadas.size() >= 3) {
                        if (lista.size() == 1) {
                            lista.add(formulario_agregar_campo);
                            adaptador.notifyDataSetChanged();
                        }
                        paginas.setCurrentItem(1);
                    } else {
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Agregar_Campo.this);
                        dialogo1.setTitle("Error");
                        dialogo1.setMessage("El campo seleccionado no es valido");
                        dialogo1.setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                dialogo1.dismiss();
                            }
                        });
                        dialogo1.show();
                    }
                } else {
                    if (formulario_agregar_campo.txtNombre.getText().toString().trim().isEmpty() || mapa_agregar_campo.coordenadas.size() < 3) {
                        completarDatos();
                        return;
                    } else {
                        Campos campo = new Campos();
                        campo.setID(SQLITE.obtenerValorMaximo(Activity_Agregar_Campo.this, SQLITE.tablaCampos, "ID"));
                        campo.setNombre(formulario_agregar_campo.txtNombre.getText().toString().trim());
                        String coordenadas = "";
                        for (int i = 0; i < mapa_agregar_campo.coordenadas.size(); i++) {
                            coordenadas = coordenadas + "{" + mapa_agregar_campo.coordenadas.get(i).latitude + "," + mapa_agregar_campo.coordenadas.get(i).longitude + "}";
                            if (mapa_agregar_campo.coordenadas.size() - 1 != i) {
                                coordenadas = coordenadas + ",";
                            }
                        }
                        campo.setCoordenadas(coordenadas);
                        if (formulario_agregar_campo.spnCultivo.getVisibility() == View.VISIBLE) {
                            if (formulario_agregar_campo.txtCultivo.getText().toString().trim().isEmpty()) {
                                completarDatos();
                                return;
                            } else {
                                campo.setTipo_Cultivo(formulario_agregar_campo.txtCultivo.getText().toString().trim());
                            }
                        } else {
                            if (formulario_agregar_campo.spnCultivo.getSelectedItem().equals("-Otro-")) {
                                completarDatos();
                                return;
                            } else {
                                campo.setTipo_Cultivo(formulario_agregar_campo.txtCultivo.getText().toString().trim());
                            }
                        }

                        if (formulario_agregar_campo.spnCultivo.getVisibility() == View.VISIBLE && !formulario_agregar_campo.txtCultivo.getText().toString().trim().isEmpty()) {
                            SQLITE.agregarCultivo(Activity_Agregar_Campo.this, campo.getTipo_Cultivo());
                        }

                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Agregar_Campo.this);
                        dialogo1.setCancelable(false);
                        dialogo1.setMessage(SQLITE.agregarCampo(Activity_Agregar_Campo.this, campo));
                        dialogo1.setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                finish();
                            }
                        });
                        dialogo1.show();
                    }
                }
            }
        });

        fabAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Agregar_Campo.this);
        dialogo1.setTitle("Datos Incompletos");
        dialogo1.setMessage("Necesita completar todos los datos");
        dialogo1.setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dialogo1.dismiss();
            }
        });
        dialogo1.show();
    }
}
