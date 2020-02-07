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
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import java.util.ArrayList;
import java.util.List;

public class Predios_Encabezado extends RecyclerView.Adapter<Predios_Encabezado.ViewHolder> {

    ArrayList<Campos> campos_source;
    ArrayList<Campos> campos_filtrados;
    Context context;
    RecyclerView rvEncabezado;

    public Predios_Encabezado(ArrayList<Campos> lista, Context context, RecyclerView rv) {
        this.campos_source = lista;
        this.campos_filtrados = lista;
        this.context = context;
        rvEncabezado = rv;
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
        //holder.txtContador.setText(SQLITE.obtenerCantidadCamposCultivos(context, cultivo.getNombre()) + "");
        holder.txtTitulo.setText(campo.getNombre());
        //Predios_Contenido prediosContenido = new Predios_Contenido(SQLITE.obtenerCamposdeunCultivo(context, cultivo.getNombre()), context, rvEncabezado);
        //holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //holder.recyclerView.setAdapter(prediosContenido);
        boolean isExpanded = campos_filtrados.get(position).isExpanded();
        holder.recyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        //holder.imvFlecha.setRotation(isExpanded ? -90f : 0);

        holder.imvFlecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                dialogo1.setCancelable(false);
                dialogo1.setMessage("Registre un SubPredio para comenzar");
                dialogo1.setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        context.startActivity(new Intent(context, Activity_Agregar_SubPredio.class).putExtra("ID_Padre", campo.getID()));
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
        ImageView imvFlecha;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.titleTextView);
            txtContador = itemView.findViewById(R.id.rv_txtcontador);
            recyclerView = itemView.findViewById(R.id.rvContenido);
            imvFlecha =itemView.findViewById(R.id.rv_ivFlecha);
            layout = itemView.findViewById(R.id.rv_lyTitulo);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Campos campo = campos_filtrados.get(getAdapterPosition());
                    campo.setExpanded(!campo.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                   /*if(SQLITE.obtenerCantidadCamposCultivos(context, cultivo.getNombre())>0) {

                    }else{

                    }*/
                }
            });
        }
    }
}
