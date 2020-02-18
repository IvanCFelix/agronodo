package com.fragmentoestudio.agronodo;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Servicios.Authentification;
import com.fragmentoestudio.agronodo.Utilidades.Datos;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;
import com.fragmentoestudio.agronodo.Utilidades.Uris;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    EditText txtUsuario, txtContraseña, txtRecuperarCorreo;
    Button btnLogin, btnOlvido, btnRecuperar;
    ScrollView scrolLogin;

    LinearLayout lyLogin;

    Animation aparece, desaparece;

    ProgressDialog iniciando;

    Dialog dialogo_Recuperar;

    public static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 3;
    public static final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

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

        btnOlvido.setText(subrayarTexto(getResources().getString(R.string.olvido_contrasena)));

        dialogo_Recuperar = new Dialog(Login.this);
        dialogo_Recuperar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo_Recuperar.setContentView(R.layout.dialogo_recuperar_contrasena);
        dialogo_Recuperar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogo_Recuperar.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogo_Recuperar.getWindow().setAttributes(lp);
        dialogo_Recuperar.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id

        txtRecuperarCorreo = dialogo_Recuperar.findViewById(R.id.txt_recuperar_Correo);
        btnRecuperar = dialogo_Recuperar.findViewById(R.id.btnRecuperarContraseña);

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtRecuperarCorreo.getText().toString().trim().isEmpty()) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            iniciando = ProgressDialog.show(Login.this, "", getString(R.string.verificando_correo), true);
                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject postData = new JSONObject();
                            try {
                                postData.put("email", txtRecuperarCorreo.getText().toString().trim());
                                final String resultado = new Authentification.RecuperarContraseña().execute(postData.toString()).get();
                                final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Login.this);
                                switch (resultado){
                                    case "1":
                                        dialogo1.setMessage(getResources().getString(R.string.correo_enviado));
                                        break;
                                    case "2":
                                        dialogo1.setMessage(getResources().getString(R.string.correo_no_existe));
                                        break;
                                }
                                dialogo1.setPositiveButton(getResources().getString(R.string.enterado), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                        if (resultado.equals("1")) {
                                            txtRecuperarCorreo.setText("");
                                            dialogo_Recuperar.dismiss();
                                        }
                                    }
                                });
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (iniciando.isShowing())
                                            iniciando.dismiss();
                                        dialogo1.show();
                                    }
                                });
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (iniciando.isShowing())
                                            iniciando.dismiss();
                                    }
                                });
                            }
                        }
                    }).start();
                }else{
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Login.this);
                    dialogo1.setTitle(getString(R.string.datos_incompletos));
                    dialogo1.setMessage(getString(R.string.complete_datos_correctamente));
                    dialogo1.setPositiveButton(getString(R.string.enterado), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                        }
                    });
                    dialogo1.show();
                }
            }
        });

        PedirPermisos();

        if (SQLITE.obtenerTamañoTabla(Login.this, SQLITE.tablaPerfil) == 1) {
            startActivity(new Intent(Login.this, Menu_Agricola.class));
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsuario.getText().toString().isEmpty() || txtContraseña.getText().toString().isEmpty()) {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Login.this);
                    dialogo1.setTitle(getResources().getString(R.string.datos_incompletos));
                    dialogo1.setMessage(getResources().getString(R.string.complete_datos_correctamente));
                    dialogo1.setPositiveButton(getString(R.string.enterado), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                        }
                    });
                    dialogo1.show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (Datos.existeInternet(Login.this, Login.this)) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        iniciando = ProgressDialog.show(Login.this, "", getResources().getString(R.string.iniciando_sesion), true);
                                    }
                                });

                                JSONObject postData = new JSONObject();
                                try {

                                    postData.put("username", txtUsuario.getText().toString().trim());
                                    postData.put("password", txtContraseña.getText().toString().trim());
                                    final String resultado = new Authentification.IniciarSesion().execute(postData.toString()).get();

                                    final JSONObject datos = new JSONObject(resultado);

                                    try {
                                        if (datos.getString("token").length() > 0 && (datos.getInt("user_type") == 4 || datos.getInt("user_type") == 5 || datos.getInt("user_type") == 6 || datos.getInt("user_type") == 7)) {
                                            switch (datos.getInt("user_type")){
                                                case 4:
                                                    String url = datos.getJSONObject("profile").getString("photo");
                                                    try {
                                                        String formato = url.substring(url.indexOf(".") + 1);
                                                        Bitmap imagen = null;
                                                        imagen = new Datos.imagendeWEB().execute(Uris.ENDPOINT_AGRONODO + url).get();
                                                        if (imagen == null) {
                                                            imagen = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                                                            formato = "png";
                                                        }
                                                        SQLITE.ingresarSesion(Login.this, resultado, imagen, formato);
                                                        runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                Toast.makeText(Login.this, getResources().getString(R.string.sesion_iniciada), Toast.LENGTH_LONG).show();
                                                                startActivity(new Intent(Login.this, Menu_Agricola.class));
                                                                finish();
                                                            }
                                                        });
                                                    } catch (ExecutionException e) {
                                                        e.printStackTrace();
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                            }
                                        } else {
                                            final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Login.this);
                                            dialogo1.setTitle(getString(R.string.cuenta_no_soportada));
                                            dialogo1.setMessage(getString(R.string.cuenta_de_tipo) + datos.getString("user_type_name") + getString(R.string.no_es_soportada));
                                            dialogo1.setPositiveButton(getString(R.string.enterado), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogo1, int id) {
                                                }
                                            });
                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    dialogo1.show();
                                                }
                                            });
                                        }

                                    } catch (final Exception e) {
                                        final AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Login.this);
                                        dialogo1.setTitle(getString(R.string.error_datos));
                                        dialogo1.setMessage(getString(R.string.cuenta_no_encontrada));
                                        dialogo1.setPositiveButton(getString(R.string.enterado), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogo1, int id) {
                                            }
                                        });
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                if (iniciando.isShowing())
                                                    iniciando.dismiss();
                                                dialogo1.show();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    //Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    //Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    //Toast.makeText(Login.this, e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (iniciando.isShowing())
                                            iniciando.dismiss();
                                    }
                                });
                            }
                        }
                    }).start();
                }

            }

        });

        btnOlvido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogo_Recuperar.show();
            }
        });
    }

    public SpannableString subrayarTexto(String texto) {
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

    public void PedirPermisos() {
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            //Si alguno de los permisos no esta concedido lo solicita
            ActivityCompat.requestPermissions(this, permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);
        }
    }
}
