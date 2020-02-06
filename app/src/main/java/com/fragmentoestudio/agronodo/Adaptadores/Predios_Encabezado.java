package com.fragmentoestudio.agronodo.Adaptadores;

import android.content.Context;
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

import com.fragmentoestudio.agronodo.Clases.Cultivos;
import com.fragmentoestudio.agronodo.R;
import com.fragmentoestudio.agronodo.Utilidades.SQLITE;

import java.util.ArrayList;
import java.util.List;

public class Predios_Encabezado extends RecyclerView.Adapter<Predios_Encabezado.ViewHolder> {

    ArrayList<Cultivos> cultivo_source;
    ArrayList<Cultivos> cultivo_filtrados;
    Context context;
    RecyclerView rvEncabezado;
    Predios_Encabezado adapter;

    public Predios_Encabezado(ArrayList<Cultivos> lista, Context context, RecyclerView rv, Predios_Encabezado adapter) {
        this.cultivo_source = lista;
        this.cultivo_filtrados = lista;
        this.context = context;
        this.adapter = adapter;
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
        Cultivos cultivo = cultivo_filtrados.get(position);
        holder.txtContador.setText(SQLITE.obtenerCantidadCamposCultivos(context, cultivo.getNombre()) + "");
        holder.txtTitulo.setText(cultivo.getNombre());
        Predios_Contenido prediosContenido = new Predios_Contenido(SQLITE.obtenerCamposdeunCultivo(context, cultivo.getNombre()), context, adapter, rvEncabezado);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(prediosContenido);
        boolean isExpanded = cultivo_filtrados.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.imvFlecha.setRotation(isExpanded ? -90f : 0);
    }

    @Override
    public int getItemCount() {
        return cultivo_filtrados.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout expandableLayout;
        TextView txtTitulo, txtContador;
        RecyclerView recyclerView;
        LinearLayout layout;
        ImageView imvFlecha;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.titleTextView);
            txtContador = itemView.findViewById(R.id.rv_txtcontador);
            recyclerView = itemView.findViewById(R.id.rvCine);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            imvFlecha =itemView.findViewById(R.id.rv_ivFlecha);
            layout = itemView.findViewById(R.id.rv_lyTitulo);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cultivos cultivo = cultivo_filtrados.get(getAdapterPosition());
                    if(SQLITE.obtenerCantidadCamposCultivos(context, cultivo.getNombre())>0) {
                        cultivo.setExpanded(!cultivo.isExpanded());
                        notifyItemChanged(getAdapterPosition());
                    }else{

                    }
                }
            });
        }
    }
}
