package com.example.mymovie.API;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mymovie.MovieAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitResponse {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "c3957c0341f6ceb7b221d37abf80c151";
    private static final String TAG = "RetrofitService";

    private final Context context;
    private final ArrayList<MovieModelResponse> movieModelArrayList;
    private final MovieAdapter movieAdapter;
    private static Retrofit retrofit;
    private final ProgressBar progressBar;




    public RetrofitResponse(Context context, ArrayList<MovieModelResponse> movieModelArrayList, MovieAdapter movieAdapter, ProgressBar progressBar) {
        this.context = context;
        this.movieModelArrayList = movieModelArrayList;
        this.movieAdapter = movieAdapter;
        this.progressBar = progressBar;
    }

    public static void getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }



    public void fetchPopularMovies() {
        movieModelArrayList.clear();
        getRetrofitInstance();
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<APIResponse> call = apiInterface.getPopularMovies(API_KEY);

        call.enqueue(new Callback<APIResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<APIResponse> call, @NonNull Response<APIResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    APIResponse apiResponse = response.body();

                    movieModelArrayList.addAll(apiResponse.getResults());
                    movieAdapter.notifyDataSetChanged();

                    if (response.isSuccessful() && response.body() != null) {
                        movieModelArrayList.addAll(response.body().getResults());
                        movieAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Failed to load movies", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<APIResponse> call, @NonNull Throwable t) {
                hideProgressBar();
                Log.e(TAG, "Error fetching movies", t);
                Toast.makeText(context, "Error Loading movies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch top-rated movies
    public void fetchTopRatedMovies() {
        movieModelArrayList.clear();
        showProgressBar();
        getRetrofitInstance();
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<APIResponse> call = apiInterface.getTopRatedMovies(API_KEY);

        call.enqueue(new Callback<APIResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<APIResponse> call, @NonNull Response<APIResponse> response) {
                hideProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    movieModelArrayList.addAll(response.body().getResults());
                    movieAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Failed to load movies", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse> call, @NonNull Throwable t) {
                hideProgressBar();
                Log.e(TAG, "Error fetching movies", t);
                Toast.makeText(context, "Error Loading movies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch upcoming movies
    public void fetchUpcomingMovies() {
        movieModelArrayList.clear();
        showProgressBar();
        getRetrofitInstance();
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<APIResponse> call = apiInterface.getUpcomingMovies(API_KEY);

        call.enqueue(new Callback<APIResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<APIResponse> call, @NonNull Response<APIResponse> response) {
                hideProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    movieModelArrayList.addAll(response.body().getResults());
                    movieAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Failed to load movies", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse> call, @NonNull Throwable t) {
                hideProgressBar();
                Log.e(TAG, "Error fetching movies", t);
                Toast.makeText(context, "Error Loading movies", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch movie details
    public void fetchMovieDetails(int movieId, final MovieDetailsCallback callback) {
        getRetrofitInstance();
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        Call<MovieModelResponse> call = apiInterface.getMovieDetails(movieId, API_KEY);

        call.enqueue(new Callback<MovieModelResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieModelResponse> call, @NonNull Response<MovieModelResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to load movie details");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieModelResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Error fetching movie details", t);
                callback.onError("Error loading movie details");
            }
        });
    }

    public interface MovieDetailsCallback {
        void onSuccess(MovieModelResponse movieDetails);
        void onError(String errorMessage);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
