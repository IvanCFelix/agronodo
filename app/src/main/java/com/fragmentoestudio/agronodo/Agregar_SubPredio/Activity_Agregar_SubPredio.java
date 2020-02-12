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
import com.fragmentoestudio.agronodo.R;

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

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cerrar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Agregar SubPredio");

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
                        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Activity_Agregar_SubPredio.this);
                        dialogo1.setTitle("Atención");
                        dialogo1.setMessage("El SubPredio tomará todo el tamaño");
                        dialogo1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                mapa_agregar_subPredio.coordenadas = mapa_agregar_subPredio.coordenadas_padre;
                                mapa_agregar_subPredio.dibujarCampo();
                            }
                        });
                        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                dialogo1.dismiss();
                            }
                        });
                        dialogo1.show();
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
