package com.fragmentoestudio.agronodo;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.fragmentoestudio.agronodo.Adaptadores.SlideViewPager;

import java.util.ArrayList;
import java.util.List;
import com.fragmentoestudio.agronodo.Intro_Fragmentos.intro_1;
import com.fragmentoestudio.agronodo.Intro_Fragmentos.intro_2;

public class Intro extends AppCompatActivity {

    ViewPager paginas;
    SlideViewPager adaptador;
    TabLayout tabIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        List<Fragment> lista = new ArrayList<>();
        lista.add(new intro_1());
        lista.add(new intro_2());

        tabIndicator = findViewById(R.id.tab_indicator);
        paginas = findViewById(R.id.vpIntro);
        adaptador = new SlideViewPager(getSupportFragmentManager(), lista);

        paginas.setAdapter(adaptador);

        tabIndicator.setupWithViewPager(paginas, true);
    }

    private boolean verificar() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("intro",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;
    }

    private void guardar() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("intro",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("introvisto",true);
        editor.commit();
    }

    private void eliminar(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("intro",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("introvisto",false);
        editor.commit();
    }
}
