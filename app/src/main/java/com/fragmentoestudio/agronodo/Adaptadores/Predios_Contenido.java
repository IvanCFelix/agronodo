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
import com.fragmentoestudio.agronodo.Clases.SubCampos;
import com.fragmentoestudio.agronodo.Editar_Campo.Activity_Editar_Campo;
import com.fragmentoestudio.agronodo.Editar_SubPredio.Activity_Editar_SubPredio;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import java.util.ArrayList;
import java.util.List;

public class Predios_Contenido extends RecyclerView.Adapter<Predios_Contenido.MovieVH> {

    ArrayList<SubCampos> subCampos_source;
    ArrayList<SubCampos> subCampos_filtrados;
    Context context;
    RecyclerView rvEncabezados;
    TextView txtNoHay;

    public Predios_Contenido(ArrayList<SubCampos> lista, Context context, RecyclerView rv, TextView txtNoHay) {
        this.subCampos_filtrados = lista;
        this.subCampos_source = lista;
        this.context = context;
        rvEncabezados = rv;
        this.txtNoHay = txtNoHay;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_predio_item, parent, false);
        return new MovieVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {
        final SubCampos subCampo = subCampos_filtrados.get(position);
        Animation aparece = AnimationUtils.loadAnimation(context, R.anim.aparecer_lista);
        holder.ly.setAnimation(aparece);
        holder.txtNombre.setText(subCampo.getNombre());
        holder.txtNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, subCampo.getNombre(), Toast.LENGTH_LONG).show();
            }
        });
        holder.ivEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                dialogo1.setCancelable(false);
                dialogo1.setMessage(context.getString(R.string.deseas_editar_subpredio));
                dialogo1.setPositiveButton(context.getString(R.string.editar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        context.startActivity(new Intent(context, Activity_Editar_SubPredio.class).putExtra("ID", subCampo.getID()));
                    }
                });
                dialogo1.setNegativeButton(context.getString(R.string.cancelar), new DialogInterface.OnClickListener() {
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
                dialogo1.setMessage(context.getString(R.string.deseas_eliminar_subpredio));
                dialogo1.setPositiveButton(context.getString(R.string.eliminar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        SQLITE.borrarSubCampo(context, subCampo.getID());
                        ArrayList<Campos> campos = SQLITE.obtenerCampos(context);
                        if(campos.isEmpty()){
                            txtNoHay.setVisibility(View.VISIBLE);
                        }else{
                            txtNoHay.setVisibility(View.GONE);
                        }
                        Predios_Encabezado adapter = new Predios_Encabezado(campos, context, rvEncabezados, txtNoHay);
                        rvEncabezados.setAdapter(adapter);
                    }
                });
                dialogo1.setNegativeButton(context.getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.show();
            }
        });
}

    @Override
    public int getItemCount() {
        return subCampos_filtrados.size();
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
