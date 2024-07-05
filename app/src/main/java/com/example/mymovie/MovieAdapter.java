package com.example.mymovie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mymovie.API.MovieModelResponse;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    Context context;
    ArrayList<MovieModelResponse> moviesList;

    public MovieAdapter(Context context, ArrayList<MovieModelResponse> movies) {
        this.context = context;
        this.moviesList = movies;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_movie_item_recycler_view, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        MovieModelResponse movie = moviesList.get(position);
        holder.movieName.setText(movie.getTitle());
        holder.rating.setText(movie.getFormattedRating());
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("movieId", movie.getMovieId());
                context.startActivity(intent);
            }
        });

            RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.nopreview)
                .error(R.drawable.nopreview);

        Glide.with(context)
                .load(movie.getImageUrl())
                .apply(requestOptions)
                .into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
       return moviesList.size();

    }

    public void setFiltered(ArrayList<MovieModelResponse> filteredList) {
        this.moviesList = filteredList;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView movieName, rating;
        ImageView movieImage;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.movie1Title);
            rating = itemView.findViewById(R.id.movie1Rating);
            movieImage = itemView.findViewById(R.id.movie1Poster);
        }
    }


}
