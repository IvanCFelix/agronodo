package com.fragmentoestudio.agronodo.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fragmentoestudio.agronodo.Cines;
import com.fragmentoestudio.agronodo.Movie;
import com.fragmentoestudio.agronodo.R;

import java.util.List;

public class CineAdapter extends RecyclerView.Adapter<CineAdapter.MovieVH> {

    private static final String TAG = "MovieAdapter";
    List<Cines> movieList;
    Context context;

    public CineAdapter(List<Cines> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cine_row, parent, false);
        return new MovieVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieVH holder, int position) {
        Cines cine = movieList.get(position);
        holder.titleTextView.setText(cine.getNombre());
        MovieAdapter movieAdapter = new MovieAdapter(cine.getPeliculas(), context);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(movieAdapter);
        boolean isExpanded = movieList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MovieVH extends RecyclerView.ViewHolder {
        private static final String TAG = "MovieVH";
        ConstraintLayout expandableLayout;
        TextView titleTextView;
        RecyclerView recyclerView;

        public MovieVH(@NonNull final View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            recyclerView = itemView.findViewById(R.id.rvCine);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            titleTextView.setOnClickListener(new View.OnClickListener() {
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
