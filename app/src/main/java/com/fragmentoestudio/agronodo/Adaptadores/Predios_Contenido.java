package com.fragmentoestudio.agronodo.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Clases.Campos;
import com.fragmentoestudio.agronodo.R;

import java.util.ArrayList;
import java.util.List;

public class Predios_Contenido extends RecyclerView.Adapter<Predios_Contenido.MovieVH> {

    ArrayList<Campos> campos_source;
    ArrayList<Campos> campos_filtrados;
    Context context;

    public Predios_Contenido(ArrayList<Campos> lista, Context context) {
        this.campos_source = lista;
        this.campos_filtrados = lista;
        this.context = context;
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
        holder.txtNombre.setText(campo.getNombre());
        holder.txtNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, campo + "", Toast.LENGTH_LONG).show();
            }
        });
        Animation aparece = AnimationUtils.loadAnimation(context, R.anim.aparecer_lista);
        holder.txtNombre.setAnimation(aparece);
}

    @Override
    public int getItemCount() {
        return campos_filtrados.size();
    }

    class MovieVH extends RecyclerView.ViewHolder {

        TextView txtNombre;

        public MovieVH(@NonNull final View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.rvNombrePeli);
        }
    }
}
