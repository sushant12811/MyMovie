package com.example.mymovie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mymovie.API.MovieModelResponse;
import com.example.mymovie.API.RetrofitResponse;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailActivity";

    private ImageView moviePoster, movieSmallPoster;

    private ProgressBar progressBar;
    private TextView movieTitle, movieRating, movieOverview, movieReleaseDate, movieLanguage;


    private RetrofitResponse retrofitResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        progressBar = findViewById(R.id.progress_bar);
        moviePoster = findViewById(R.id.movie_poster);
        movieTitle = findViewById(R.id.movie_title);
        movieRating = findViewById(R.id.movie_rating);
        movieSmallPoster  = findViewById(R.id.movie_small_poster);
        movieOverview = findViewById(R.id.movie_overview);
        movieReleaseDate = findViewById(R.id.movie_release_date);
        movieLanguage = findViewById(R.id.movie_language);

        retrofitResponse = new RetrofitResponse(this, null, null, null);

        Intent intent = getIntent();
        int movieId = intent.getIntExtra("movieId", -1);
        if (movieId != -1) {
            fetchMovieDetails(movieId);
        } else {
            Log.e(TAG, "Invalid movie ID");
        }
    }

    private void fetchMovieDetails(int movieId) {

        progressBar.setVisibility(View.VISIBLE);
        retrofitResponse.fetchMovieDetails(movieId, new RetrofitResponse.MovieDetailsCallback() {
            @Override
            public void onSuccess(MovieModelResponse movieDetails) {
                movieTitle.setText(movieDetails.getTitle());
                movieRating.setText(movieDetails.getFormattedRating());
                movieOverview.setText(movieDetails.getOverview());
                movieReleaseDate.setText(movieDetails.getReleaseDate());
                movieLanguage.setText(movieDetails.getLanguage());
                Glide.with(MovieDetailActivity.this)
                        .load(movieDetails.getImageUrl())
                        .into(moviePoster);
                Glide.with(MovieDetailActivity.this)
                        .load(movieDetails.getImageUrl())
                        .into(movieSmallPoster);
            }


            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MovieDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        progressBar.setVisibility(View.GONE);
    }
}
