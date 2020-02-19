package com.fragmentoestudio.agronodo.Editar_Campo;

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
import android.widget.ArrayAdapter;

import com.fragmentoestudio.agronodo.Adaptadores.SlideViewPager;
import com.fragmentoestudio.agronodo.Agregar_Campo.Activity_Agregar_Campo;
import com.fragmentoestudio.agronodo.Clases.Campos;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import java.util.ArrayList;
import java.util.List;

public class Activity_Editar_Campo extends AppCompatActivity {

    ViewPager paginas;
    SlideViewPager adaptador;
    FloatingActionButton fabSiguiente, fabAtras;

    Mapa_Editar_Campo mapa_editar_campo = new Mapa_Editar_Campo();
    Formulario_Editar_Campo formulario_editar_campo = new Formulario_Editar_Campo();
    ArrayList<Fragment> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_campo);

        int ID = getIntent().getIntExtra("ID", 1);
        Bundle bundle = new Bundle();
        bundle.putInt("ID", ID);
        mapa_editar_campo.setArguments(bundle);
        formulario_editar_campo.setArguments(bundle);
        fabSiguiente = findViewById(R.id.fab_siguiente);
        fabAtras = findViewById(R.id.fab_atras);
        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_atras);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.editar_predio));

        lista.add(mapa_editar_campo);
        lista.add(formulario_editar_campo);

        paginas = findViewById(R.id.vpIntro);
        adaptador = new SlideViewPager(getSupportFragmentManager(), lista);
        paginas.setAdapter(adaptador);

        fabSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paginas.getCurrentItem() == 0) {
                    if (mapa_editar_campo.coordenadas.size() >= 3) {
                        paginas.setCurrentItem(1);
                    } else {
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Editar_Campo.this);
                        dialogo1.setTitle("Error");
                        dialogo1.setMessage(getString(R.string.campo_seleccionado_no_valido));
                        dialogo1.setPositiveButton(getString(R.string.enterado), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                dialogo1.dismiss();
                            }
                        });
                        dialogo1.show();
                    }
                } else {
                    if (formulario_editar_campo.txtNombre.getText().toString().trim().isEmpty() || mapa_editar_campo.coordenadas.size() < 3) {
                        completarDatos();
                        return;
                    } else {
                        Campos campo = new Campos();
                        campo.setID(SQLITE.obtenerValorMaximo(Activity_Editar_Campo.this, SQLITE.tablaCampos, "ID"));
                        campo.setNombre(formulario_editar_campo.txtNombre.getText().toString().trim());
                        String coordenadas = "";
                        mapa_editar_campo.coordenadas.add(mapa_editar_campo.coordenadas.get(0));
                        for (int i = 0; i < mapa_editar_campo.coordenadas.size(); i++) {
                            coordenadas = coordenadas + "{'Latitud': " + mapa_editar_campo.coordenadas.get(i).latitude + ", 'Longitud': " + mapa_editar_campo.coordenadas.get(i).longitude + "}";
                            if (mapa_editar_campo.coordenadas.size() - 1 != i) {
                                coordenadas = coordenadas + ",";
                            }
                        }
                        campo.setCoordenadas(coordenadas);
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Editar_Campo.this);
                        dialogo1.setCancelable(false);
                        switch (SQLITE.editarCampo(Activity_Editar_Campo.this, campo)){
                            case 1:
                                dialogo1.setMessage(getString(R.string.predio_registrado));
                                break;
                            case 2:
                                dialogo1.setMessage(getString(R.string.no_se_pudo_registrar_predio));
                                break;
                        }
                        dialogo1.setPositiveButton(getString(R.string.enterado), new DialogInterface.OnClickListener() {
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
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Editar_Campo.this);
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
