package com.example.mymovie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import Authentication.LoginActivity;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.onBoardingViewHolder> {

 List<Integer> layouts;
     Context context;

    public OnBoardingAdapter(Context context, List<Integer> layouts) {
        this.context = context;
        this.layouts = layouts;
    }

    @NonNull
    @Override
    public onBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new onBoardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull onBoardingViewHolder holder, int position) {
        if (position == layouts.size() - 1) {
            Button btnFinish = holder.itemView.findViewById(R.id.btnFinished);
            btnFinish.setOnClickListener(v -> {
                // Set the onboarding completed flag in SharedPreferences
                context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("completed", true)
                        .apply();

                // Navigate to the MainActivity
                context.startActivity(new Intent(context, LoginActivity.class));

            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return layouts.get(position);
    }

    @Override
    public int getItemCount() {
        return layouts.size();
    }

    static class onBoardingViewHolder extends RecyclerView.ViewHolder {
        onBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
