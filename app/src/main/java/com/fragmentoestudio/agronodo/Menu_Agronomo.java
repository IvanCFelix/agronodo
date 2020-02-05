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
import com.fragmentoestudio.agronodo.Agronomo.Intro_Agronomo;
import com.fragmentoestudio.agronodo.Agronomo.Lista_Predios_Agronomo;
import com.fragmentoestudio.agronodo.Agronomo.Mapa_Predios_Agronomo;
import com.fragmentoestudio.agronodo.Agronomo.Mi_Usuario_Agronomo;
import com.fragmentoestudio.agronodo.Agronomo.Notificaciones_Agronomo;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu_Agronomo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Fragment fragment;

    public static Intro_Agronomo intro = new Intro_Agronomo();
    public static Lista_Predios_Agronomo lista_predios = new Lista_Predios_Agronomo();
    public static Mapa_Predios_Agronomo mapa_prediosAgronomo = new Mapa_Predios_Agronomo();
    public static Mi_Usuario_Agronomo mi_usuarioAgronomo = new Mi_Usuario_Agronomo();
    public static Notificaciones_Agronomo notificaciones_agronomo = new Notificaciones_Agronomo();

    public static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 3;
    public static final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_agronomo);
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

        CircleImageView imagenPerfil = headerView.findViewById(R.id.navheader_civ);
        TextView txtNombre = headerView.findViewById(R.id.navheader_Nombre);
        TextView txtCorreo = headerView.findViewById(R.id.navheader_Correo);
        JSONObject usuario= null;
        try {
            usuario = new JSONObject(SQLITE.obtenerUsuario(Menu_Agronomo.this));
        } catch (JSONException e) {
            SQLITE.eliminarBasedeDatos(Menu_Agronomo.this);
            startActivity(new Intent(Menu_Agronomo.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
            e.printStackTrace();
        } catch (Exception e){

        }
        try {
            txtNombre.setText(usuario.getJSONObject("profile").getString("names") + " " + usuario.getJSONObject("profile").getString("lastnames"));
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

        Bitmap imagen = SQLITE.obtenerImagen(Menu_Agronomo.this);
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
                    Drawable icono = getResources().getDrawable(R.drawable.ic_lista);
                    icono.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setIcon(icono);
                } catch (Exception e) {
                }
                break;
            case R.id.nav_Mapa_Predios:
                try {
                    if (ActivityCompat.checkSelfPermission(Menu_Agronomo.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(Menu_Agronomo.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Menu_Agronomo.this, permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);
                        if (ActivityCompat.checkSelfPermission(Menu_Agronomo.this, permissions[0]) == PackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(Menu_Agronomo.this, permissions[1]) == PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(Menu_Agronomo.this, "Aun en Desarrollo", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_salir:
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Menu_Agronomo.this);
                dialogo1.setTitle("Cerrar Sesión");
                dialogo1.setMessage("¿ Desea cerrar sesión ?");
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        SQLITE.eliminarBasedeDatos(Menu_Agronomo.this);
                        startActivity(new Intent(Menu_Agronomo.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
