package com.example.mymovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mymovie.OnBoardingAdapter;
import com.example.mymovie.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import java.util.Arrays;

public class OnBoardingActivity extends AppCompatActivity {

     ViewPager2 viewPager;
    OnBoardingAdapter adapter;
     DotsIndicator dotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        viewPager = findViewById(R.id.viewPager);
        dotsIndicator = findViewById(R.id.dotsIndicator);

        adapter = new OnBoardingAdapter(
                this,
                Arrays.asList(
                        R.layout.onboarding_1,
                        R.layout.onboarding_2,
                        R.layout.onboarding_3
                )
        );
        viewPager.setAdapter(adapter);

        // Set up the Dots Indicator
        dotsIndicator.setViewPager2(viewPager);

    }
}
