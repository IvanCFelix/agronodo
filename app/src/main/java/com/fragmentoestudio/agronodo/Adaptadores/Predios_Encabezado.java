package com.fragmentoestudio.agronodo.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Agregar_SubPredio.Activity_Agregar_SubPredio;
import com.fragmentoestudio.agronodo.Clases.Campos;
import com.fragmentoestudio.agronodo.Clases.Cultivos;
import com.fragmentoestudio.agronodo.Clases.SubCampos;
import com.fragmentoestudio.agronodo.Editar_Campo.Activity_Editar_Campo;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import java.util.ArrayList;
import java.util.List;

public class Predios_Encabezado extends RecyclerView.Adapter<Predios_Encabezado.ViewHolder> {

    ArrayList<Campos> campos_source;
    ArrayList<Campos> campos_filtrados;
    Context context;
    RecyclerView rvEncabezado;
    TextView txtNoHay;

    public Predios_Encabezado(ArrayList<Campos> lista, Context context, RecyclerView rv, TextView txtNoHay) {
        this.campos_source = lista;
        this.campos_filtrados = lista;
        this.context = context;
        rvEncabezado = rv;
        this.txtNoHay = txtNoHay;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_predio_titulo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Campos campo = campos_filtrados.get(position);

        ArrayList<SubCampos> subCampos = SQLITE.obtenerSubCampos(context, campo.getID());
        holder.txtContador.setText(subCampos.size() + "");
        holder.txtTitulo.setText(campo.getNombre());
        Predios_Contenido prediosContenido = new Predios_Contenido(subCampos, context, rvEncabezado, txtNoHay);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(prediosContenido);
        boolean isExpanded = campos_filtrados.get(position).isExpanded();
        holder.recyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        //holder.imvFlecha.setRotation(isExpanded ? -90f : 0);

        holder.imvFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, Activity_Agregar_SubPredio.class).putExtra("ID_Padre", campo.getID()));
            }
        });

        holder.ivEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                dialogo1.setCancelable(false);
                dialogo1.setMessage(context.getString(R.string.deseas_editar_predio));
                dialogo1.setPositiveButton(context.getString(R.string.editar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        context.startActivity(new Intent(context, Activity_Editar_Campo.class).putExtra("ID", campo.getID()));
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
                dialogo1.setMessage(context.getString(R.string.deseas_eliminar_predio));
                dialogo1.setPositiveButton(context.getString(R.string.eliminar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        SQLITE.borrarCampo(context, campo.getID());
                        ArrayList<Campos> campos = SQLITE.obtenerCampos(context);
                        if(campos.isEmpty()){
                            txtNoHay.setVisibility(View.VISIBLE);
                        }else{
                            txtNoHay.setVisibility(View.GONE);
                        }
                        Predios_Encabezado adapter = new Predios_Encabezado(campos, context, rvEncabezado, txtNoHay);
                        rvEncabezado.setAdapter(adapter);
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
        return campos_filtrados.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitulo, txtContador;
        RecyclerView recyclerView;
        LinearLayout layout;
        ImageView imvFlecha, ivEditar, ivEliminar;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.titleTextView);
            txtContador = itemView.findViewById(R.id.rv_txtcontador);
            recyclerView = itemView.findViewById(R.id.rvContenido);
            ivEditar = itemView.findViewById(R.id.rvEditar);
            ivEliminar = itemView.findViewById(R.id.rvEliminar);
            imvFlecha =itemView.findViewById(R.id.rv_ivFlecha);
            layout = itemView.findViewById(R.id.rv_lyTitulo);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Campos campo = campos_filtrados.get(getAdapterPosition());
                    campo.setExpanded(!campo.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
