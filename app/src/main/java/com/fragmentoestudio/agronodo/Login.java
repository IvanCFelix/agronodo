package com.fragmentoestudio.agronodo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Servicios.Authentification;
import com.fragmentoestudio.agronodo.Utilidades.Datos;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import com.fragmentoestudio.agronodo.Utilidades.Uris;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    Button btnLogin;
    EditText txtUsuario, txtContraseña;
    Button btnOlvido;
    ScrollView scrolLogin;

    LinearLayout lyLogin;

    Animation aparece, desaparece;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btnLogin = findViewById(R.id.btnIniciarSesion);
        txtUsuario = findViewById(R.id.txt_Login_Usuario);
        txtContraseña = findViewById(R.id.txt_Login_Contraseña);
        btnOlvido = findViewById(R.id.btnOlvideContraseña);
        scrolLogin = findViewById(R.id.src_login);
        lyLogin = findViewById(R.id.lyLogin);

        desaparece = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.desaparecer);
        aparece = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.aparecer);

        btnOlvido.setText(subrayarTexto("¿Olvidó su Contraseña?"));


        if(SQLITE.obtenerTamañoTabla(Login.this, SQLITE.tablaPerfil)==1){
            startActivity(new Intent(Login.this, Menu_Agronomo.class));
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUsuario.getText().toString().isEmpty() || txtContraseña.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "Complete los datos", Toast.LENGTH_LONG).show();
                }else {
                    if(Datos.existeInternet(Login.this)) {
                        JSONObject postData = new JSONObject();
                        try {
                            postData.put("username", txtUsuario.getText().toString().trim());
                            postData.put("password", txtContraseña.getText().toString().trim());
                            String resultado = new Authentification.IniciarSesion().execute(postData.toString()).get();
                            JSONObject datos = new JSONObject(resultado);
                            try {
                                if (datos.getBoolean("status") == true) {
                                    SQLITE.ingresarSesion(Login.this, resultado, new Datos.imagendeWEB().execute("http://159.65.69.22:8000" + datos.getJSONObject("data").getJSONObject("profile").get("pic")).get());
                                    Toast.makeText(Login.this, "Sesión Iniciada", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Login.this, Menu_Agronomo.class));
                                    finish();
                                }
                            } catch (Exception e) {
                                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Login.this);
                                dialogo1.setTitle("Datos Incorrectos");
                                dialogo1.setMessage("Introduzca los datos correctamente");
                                dialogo1.setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                    }
                                });
                                dialogo1.show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public SpannableString subrayarTexto(String texto){
        SpannableString textoSubrayado = new SpannableString(texto);
        textoSubrayado.setSpan(new UnderlineSpan(), 0, texto.length(), 0);
        return textoSubrayado;
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrolLogin.setAnimation(aparece);
    }

    @Override
    protected void onStop() {
        super.onStop();
        scrolLogin.clearAnimation();
    }
}
