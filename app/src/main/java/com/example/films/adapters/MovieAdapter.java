package com.example.films.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.films.R;
import com.example.films.pojo.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.EmployeeViewHolder>{
    private List<Result> movies;
    private OnPosterClickListener onPosterClickListener;
    private OnReachEndListener onReachEndListener;

    public MovieAdapter() { movies = new ArrayList<>();}

    public interface OnPosterClickListener {
        void onPosterClick(int position);

    }
    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }
    public interface OnReachEndListener{
        void onReachEnd();

    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        if(movies.size()>=20 && position > movies.size() -6 && onReachEndListener!=null){
            onReachEndListener.onReachEnd();
        }
        Result result = movies.get(position);
        Picasso.get().load("https://image.tmdb.org/t/p/w185"+result.getPosterPath()).into(holder.imageViewSmallPoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewSmallPoster;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSmallPoster = itemView.findViewById(R.id.imageViewSmallPoster);
            itemView.setOnClickListener(v -> {
                if (onPosterClickListener!=null){
                    onPosterClickListener.onPosterClick(getAdapterPosition());
                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear(){
        this.movies.clear();
        notifyDataSetChanged();
    }

    public List<Result> getMovies() {
        return movies;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMovies(List<Result> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

}
