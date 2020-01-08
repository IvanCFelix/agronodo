package com.fragmentoestudio.agronodo;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Agronomo.Intro_Agronomo;
import com.fragmentoestudio.agronodo.Servicios.Authentification;
import com.fragmentoestudio.agronodo.Utilidades.Datos;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu_Agronomo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Fragment fragment;

    public static Intro_Agronomo intro = new Intro_Agronomo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_agronomo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.getMenu().getItem(0).setChecked(true);
        View headerView = navigationView.getHeaderView(0);

        CircleImageView imagenPerfil = headerView.findViewById(R.id.navheader_civ);
        TextView txtNombre = headerView.findViewById(R.id.navheader_Nombre);
        TextView txtCorreo = headerView.findViewById(R.id.navheader_Correo);
        JSONObject usuario= null;
        try {
            usuario = new JSONObject(SQLITE.obtenerUsuario(Menu_Agronomo.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            txtNombre.setText(usuario.getJSONObject("data").getJSONObject("profile").getString("first_name") + " " + usuario.getJSONObject("data").getJSONObject("profile").getString("last_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            txtCorreo.setText(usuario.getJSONObject("data").getJSONObject("user").getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imagenPerfil.setImageBitmap(SQLITE.obtenerImagen(Menu_Agronomo.this));

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

        switch (id) {
            case R.id.nav_Intro:
                try {
                    fragmentTransaction.replace(R.id.area_ventana, intro);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
