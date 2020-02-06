package com.fragmentoestudio.agronodo.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Clases.Campos;
import com.fragmentoestudio.agronodo.Clases.Cultivos;
import com.fragmentoestudio.agronodo.Editar_Campo.Activity_Editar_Campo;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import java.util.ArrayList;
import java.util.List;

public class Predios_Contenido extends RecyclerView.Adapter<Predios_Contenido.MovieVH> {

    ArrayList<Campos> campos_source;
    ArrayList<Campos> campos_filtrados;
    Context context;
    RecyclerView rvEncabezados;
    Predios_Encabezado adapter;

    public Predios_Contenido(ArrayList<Campos> lista, Context context, Predios_Encabezado adapter, RecyclerView rv) {
        this.campos_source = lista;
        this.campos_filtrados = lista;
        this.context = context;
        this.adapter = adapter;
        rvEncabezados = rv;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_predio_item, parent, false);
        return new MovieVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {
        final Campos campo = campos_filtrados.get(position);
        Animation aparece = AnimationUtils.loadAnimation(context, R.anim.aparecer_lista);
        holder.ly.setAnimation(aparece);
        holder.txtNombre.setText(campo.getNombre());
        holder.txtNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, campo.getNombre(), Toast.LENGTH_LONG).show();
            }
        });
        holder.ivEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                dialogo1.setCancelable(false);
                dialogo1.setMessage("¿Deseas editar el predio " + campo.getNombre() + "?");
                dialogo1.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        context.startActivity(new Intent(context, Activity_Editar_Campo.class).putExtra("ID", campo.getID()));
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.show();
            }
        });

        holder.ivEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                dialogo1.setCancelable(false);
                dialogo1.setMessage("¿Deseas eliminar el predio " + campo.getNombre() + "?");
                dialogo1.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        SQLITE.borrarCampo(context, campo.getID());
                        ArrayList<Cultivos> cultivos = SQLITE.obtenerCultivosLista(context);
                        adapter = new Predios_Encabezado(cultivos, context, rvEncabezados, adapter);
                        rvEncabezados.setAdapter(adapter);
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.show();
            }
        });
}

    @Override
    public int getItemCount() {
        return campos_filtrados.size();
    }

    class MovieVH extends RecyclerView.ViewHolder {

        TextView txtNombre;
        ImageView ivEditar, ivEliminar;
        LinearLayout ly;

        public MovieVH(@NonNull final View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.rvNombre_Predio);
            ivEditar = itemView.findViewById(R.id.rvEditar);
            ivEliminar = itemView.findViewById(R.id.rvEliminar);
            ly = itemView.findViewById(R.id.rv_lyContenido);
        }
    }
}
