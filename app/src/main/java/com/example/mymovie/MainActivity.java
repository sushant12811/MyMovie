package com.example.mymovie;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import Authentication.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        // Setup Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        LinearLayout movie1Layout = findViewById(R.id.movie1Layout);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (R.id.navigation_home == item.getItemId()) {
                    Toast.makeText(MainActivity.this, "Home Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (R.id.navigation_favourite == item.getItemId()) {

                    Toast.makeText(MainActivity.this, "Favourite Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (R.id.navigation_addToWatchlist == item.getItemId()) {
                    makeText(MainActivity.this, "Watchlist Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (R.id.navigation_logout == item.getItemId()) {
                    {
                        performLogout();
                        return true;
                    }
                }
                return true;

            }


        });

        movie1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the movie ID (replace with how you store/access movie IDs)
                int movieId = 123;

                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra("MOVIE_ID", movieId);
                startActivity(intent);
            }
        });
    }


    //perform logout
    private void performLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Logout Confirmation");
        builder.setMessage("Are you sure you want to log out?");

        // Set the positive button with a click listener
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fAuth.signOut();

                Toast.makeText(MainActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set the negative button with a click listener
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
