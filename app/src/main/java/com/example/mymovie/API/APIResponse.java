package com.example.mymovie.API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APIResponse {
    @SerializedName("results")
    private List<MovieModelResponse> results;

    public List<MovieModelResponse> getResults() {
        return results;
    }


}

