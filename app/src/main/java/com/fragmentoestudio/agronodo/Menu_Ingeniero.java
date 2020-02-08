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
import com.fragmentoestudio.agronodo.Ingeniero.Intro_Ingeniero;
import com.fragmentoestudio.agronodo.Ingeniero.Lista_Predios_Ingeniero;
import com.fragmentoestudio.agronodo.Ingeniero.Mapa_Predios_Ingeniero;
import com.fragmentoestudio.agronodo.Ingeniero.Mi_Usuario_Ingeniero;
import com.fragmentoestudio.agronodo.Ingeniero.Notificaciones_Ingeniero;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu_Ingeniero extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Fragment fragment;

    public static Intro_Ingeniero intro = new Intro_Ingeniero();
    public static Lista_Predios_Ingeniero lista_predios = new Lista_Predios_Ingeniero();
    public static Mapa_Predios_Ingeniero mapa_prediosAgronomo = new Mapa_Predios_Ingeniero();
    public static Mi_Usuario_Ingeniero mi_usuarioAgronomo = new Mi_Usuario_Ingeniero();
    public static Notificaciones_Ingeniero notificaciones_agronomo = new Notificaciones_Ingeniero();

    public static CircleImageView imagenPerfil;
    public static TextView txtNombre, txtCorreo;

    public static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 3;
    public static final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ingeniero);
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
                navigationView.getMenu().getItem(3).setChecked(true);
            }
        });

        imagenPerfil = headerView.findViewById(R.id.navheader_civ);
        txtNombre = headerView.findViewById(R.id.navheader_Nombre);
        txtCorreo = headerView.findViewById(R.id.navheader_Correo);
        JSONObject usuario= null;
        try {
            usuario = new JSONObject(SQLITE.obtenerUsuario(Menu_Ingeniero.this));
        } catch (JSONException e) {
            SQLITE.eliminarBasedeDatos(Menu_Ingeniero.this);
            startActivity(new Intent(Menu_Ingeniero.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            e.printStackTrace();
        } catch (Exception e){

        }
        try {
            txtNombre.setText(usuario.getJSONObject("profile").getString("agricola"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){

        }

        try {
            txtCorreo.setText(usuario.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){

        }

        Bitmap imagen = SQLITE.obtenerImagen(Menu_Ingeniero.this);
        if(imagen!=null) {
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
                    if (ActivityCompat.checkSelfPermission(Menu_Ingeniero.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(Menu_Ingeniero.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Menu_Ingeniero.this, permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);
                        if (ActivityCompat.checkSelfPermission(Menu_Ingeniero.this, permissions[0]) == PackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(Menu_Ingeniero.this, permissions[1]) == PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(Menu_Ingeniero.this, "Aun en Desarrollo", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_salir:
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Menu_Ingeniero.this);
                dialogo1.setTitle("Cerrar Sesión");
                dialogo1.setMessage("¿ Desea cerrar sesión ?");
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        SQLITE.eliminarBasedeDatos(Menu_Ingeniero.this);
                        startActivity(new Intent(Menu_Ingeniero.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

    public void cambiarFragmento(Fragment fragmento){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        fragmentTransaction.replace(R.id.area_ventana, fragmento);
        drawer.closeDrawer(GravityCompat.START);
        fragmentTransaction.commit();
    }


}
