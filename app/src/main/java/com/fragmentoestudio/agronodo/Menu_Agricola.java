package com.fragmentoestudio.agronodo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Agricola.Intro_Agricola;
import com.fragmentoestudio.agronodo.Agricola.Lista_Predios_Agricola;
import com.fragmentoestudio.agronodo.Agricola.Mapa_Predios_Agricola;
import com.fragmentoestudio.agronodo.Agricola.Mi_Usuario_Agricola;
import com.fragmentoestudio.agronodo.Agricola.Notificaciones_Agricola;
import com.fragmentoestudio.agronodo.Agricola.Tareas_Agricola;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu_Agricola extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Fragment fragment;

    public Intro_Agricola intro = new Intro_Agricola();
    public Lista_Predios_Agricola lista_predios = new Lista_Predios_Agricola();
    public static Mapa_Predios_Agricola mapa_prediosAgronomo = new Mapa_Predios_Agricola();
    public Mi_Usuario_Agricola mi_usuarioAgronomo = new Mi_Usuario_Agricola();
    public Tareas_Agricola tareas_agricola = new Tareas_Agricola();
    public Notificaciones_Agricola notificaciones_agronomo = new Notificaciones_Agricola();

    public static CircleImageView imagenPerfil;
    public static TextView txtNombre, txtCorreo;

    public static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 3;
    public static final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_agricola);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.getMenu().getItem(0).setChecked(true);
        View headerView = navigationView.getHeaderView(0);

        Drawable icono = getResources().getDrawable(R.drawable.ic_casa);
        icono.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setIcon(icono);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFragmento(mi_usuarioAgronomo);
                navigationView.getMenu().getItem(4).setChecked(true);
            }
        });

        imagenPerfil = headerView.findViewById(R.id.navheader_civ);
        txtNombre = headerView.findViewById(R.id.navheader_Nombre);
        txtCorreo = headerView.findViewById(R.id.navheader_Correo);
        JSONObject usuario = null;
        try {
            usuario = new JSONObject(SQLITE.obtenerUsuario(Menu_Agricola.this));
        } catch (JSONException e) {
            SQLITE.eliminarBasedeDatos(Menu_Agricola.this);
            startActivity(new Intent(Menu_Agricola.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            e.printStackTrace();
        } catch (Exception e) {

        }
        try {
            txtNombre.setText(usuario.getJSONObject("profile").getString("agricola"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        try {
            txtCorreo.setText(usuario.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        Bitmap imagen = SQLITE.obtenerImagen(Menu_Agricola.this);
        if (imagen != null) {
            imagenPerfil.setImageBitmap(imagen);
        }

        fragment = intro;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.area_ventana, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        switch (id) {
            case R.id.nav_Intro:
                try {
                    fragmentTransaction.replace(R.id.area_ventana, intro);
                    drawer.closeDrawer(GravityCompat.START);
                    Drawable icono = getResources().getDrawable(R.drawable.ic_casa);
                    icono.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setIcon(icono);
                } catch (Exception e) {
                }
                break;
            case R.id.nav_Lista_Predios:
                try {
                    fragmentTransaction.replace(R.id.area_ventana, lista_predios);
                    drawer.closeDrawer(GravityCompat.START);
                    Drawable icono = getResources().getDrawable(R.drawable.ic_campo);
                    icono.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setIcon(icono);
                } catch (Exception e) {
                }
                break;
            case R.id.nav_Mapa_Predios:
                try {
                    if (ActivityCompat.checkSelfPermission(Menu_Agricola.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(Menu_Agricola.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Menu_Agricola.this, permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);
                        if (ActivityCompat.checkSelfPermission(Menu_Agricola.this, permissions[0]) == PackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(Menu_Agricola.this, permissions[1]) == PackageManager.PERMISSION_GRANTED) {
                            fragmentTransaction.replace(R.id.area_ventana, mapa_prediosAgronomo);
                            drawer.closeDrawer(GravityCompat.START);
                        }
                    } else {
                        fragmentTransaction.replace(R.id.area_ventana, mapa_prediosAgronomo);
                        drawer.closeDrawer(GravityCompat.START);
                        Drawable icono = getResources().getDrawable(R.drawable.ic_mapa);
                        icono.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                        getSupportActionBar().setIcon(icono);
                    }
                } catch (Exception e) {
                }
                break;
            case R.id.nav_Lista_Tareas:
                try {
                    fragmentTransaction.replace(R.id.area_ventana, tareas_agricola);
                    drawer.closeDrawer(GravityCompat.START);
                    Drawable icono = getResources().getDrawable(R.drawable.ic_tareas);
                    icono.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setIcon(icono);
                } catch (Exception e) {
                }
                break;
            case R.id.nav_Mi_Usuario:
                try {
                    fragmentTransaction.replace(R.id.area_ventana, mi_usuarioAgronomo);
                    drawer.closeDrawer(GravityCompat.START);
                    Drawable icono = getResources().getDrawable(R.drawable.ic_persona);
                    icono.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setIcon(icono);
                } catch (Exception e) {
                }
                break;
            case R.id.nav_Mis_Notificaciones:
                try {
                    fragmentTransaction.replace(R.id.area_ventana, notificaciones_agronomo);
                    drawer.closeDrawer(GravityCompat.START);
                    Drawable icono = getResources().getDrawable(R.drawable.ic_notificacion);
                    icono.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setIcon(icono);
                } catch (Exception e) {
                }
                break;
            case R.id.nav_web:
                Toast.makeText(Menu_Agricola.this, "Aun en Desarrollo", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_salir:
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Menu_Agricola.this);
                dialogo1.setTitle("Cerrar Sesión");
                dialogo1.setMessage("¿ Desea cerrar sesión ?");
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        SQLITE.eliminarBasedeDatos(Menu_Agricola.this);
                        startActivity(new Intent(Menu_Agricola.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        dialogo1.dismiss();
                    }
                });
                dialogo1.show();
                break;
        }
        fragmentTransaction.commit();
        return true;
    }

    public void cambiarFragmento(Fragment fragmento) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        fragmentTransaction.replace(R.id.area_ventana, fragmento);
        drawer.closeDrawer(GravityCompat.START);
        fragmentTransaction.commit();
    }


}
