package com.example.mymovie.API;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

public class MovieModelResponse {
    @SerializedName("title")
    private String title;

    @SerializedName("vote_average")
    private double rating;

    @SerializedName("poster_path")
    private String imagePath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("original_language")
    private String language;

    @SerializedName("id")
    int movieId;

    @SerializedName("total_pages")
    private int totalPages;


    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public MovieModelResponse(String title, double rating, String imagePath,
                              String overview, int movieId, String releaseDate, String cast, int totalPages) {
        this.title = title;
        this.rating = rating;
        this.imagePath = imagePath;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.language = cast;
        this.movieId = movieId;
        this.totalPages = totalPages;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageUrl() {
        return "https://image.tmdb.org/t/p/w500" + imagePath;
    }

    @SuppressLint("DefaultLocale")
    public String getFormattedRating() {
        return String.format("%.1f", rating);
    }
}
