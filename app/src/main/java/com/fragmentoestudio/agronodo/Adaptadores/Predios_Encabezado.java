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

import com.fragmentoestudio.agronodo.Cines;
import com.fragmentoestudio.agronodo.R;

import java.util.List;

public class Predios_Encabezado extends RecyclerView.Adapter<Predios_Encabezado.MovieVH> {

    private static final String TAG = "MovieAdapter";
    List<Cines> movieList;
    Context context;

    public Predios_Encabezado(List<Cines> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_predio_titulo, parent, false);
        return new MovieVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {
        Cines cine = movieList.get(position);
        holder.txtContador.setText(cine.getPeliculas().size() + "");
        holder.titleTextView.setText(cine.getNombre());
        Predios_Contenido prediosContenido = new Predios_Contenido(cine.getPeliculas(), context);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(prediosContenido);
        boolean isExpanded = movieList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.imvFlecha.setRotation(isExpanded ? -90f : 0);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MovieVH extends RecyclerView.ViewHolder {
        private static final String TAG = "MovieVH";
        ConstraintLayout expandableLayout;
        TextView titleTextView, txtContador;
        RecyclerView recyclerView;
        LinearLayout layout;
        ImageView imvFlecha;

        public MovieVH(@NonNull final View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            txtContador = itemView.findViewById(R.id.rv_txtcontador);
            recyclerView = itemView.findViewById(R.id.rvCine);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            imvFlecha =itemView.findViewById(R.id.rv_ivFlecha);
            layout = itemView.findViewById(R.id.rv_lyTitulo);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cines cine = movieList.get(getAdapterPosition());
                    cine.setExpanded(!cine.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
