package com.fragmentoestudio.agronodo.Adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class SlideViewPager extends FragmentStatePagerAdapter {

    private List<Fragment> listaFragmentos;

    public SlideViewPager(FragmentManager fm, List<Fragment> listaFragmentos) {
        super(fm);
        this.listaFragmentos = listaFragmentos;
    }

    @Override
    public Fragment getItem(int i) {
        return listaFragmentos.get(i);
    }

    @Override
    public int getCount() {
        return listaFragmentos.size();
    }
}
