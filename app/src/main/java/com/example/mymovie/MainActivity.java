package com.example.mymovie;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mymovie.Authentication.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_density_medium_24);
//         Setup DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("Navigation", "onNavigationItemSelected: ");
                int id = item.getItemId();
                if (id == R.id.settings) {
                    Log.d("Settings", "Settings item clicked");
                    Toast.makeText(MainActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.account) {
                    Toast.makeText(MainActivity.this, "Account Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.favourite) {
                    Toast.makeText(MainActivity.this, "Favourite Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.watchlist) {
                    Toast.makeText(MainActivity.this, "Watch List Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.premium) {
                    Toast.makeText(MainActivity.this, "Premium Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.logout) {
                    Log.d("Logout", "Logout Selected");
                    performLogout();
                    return true;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });

        fAuth = FirebaseAuth.getInstance();

        // Setup Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    Log.d("home", "onNavigationItemSelected: ");
                    Toast.makeText(MainActivity.this, "Home Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.navigation_favourite) {
                    Toast.makeText(MainActivity.this, "Favourite Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.navigation_addToWatchlist) {
                    Toast.makeText(MainActivity.this, "Watchlist Selected", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.navigation_logout) {
                    performLogout();
                    return true;
                }
                return false;
            }
        });

        // Set Movie Layout Click Listener
        LinearLayout movie1Layout = findViewById(R.id.movie1Layout);
        movie1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int movieId = 123;
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra("MOVIE_ID", movieId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            Log.d("Navigation", "Hamburger menu clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Perform Logout
    private void performLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Logout Confirmation");
        builder.setMessage("Are you sure you want to log out?");

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
