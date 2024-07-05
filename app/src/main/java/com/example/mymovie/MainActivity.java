package com.example.mymovie;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovie.API.MovieModelResponse;
import com.example.mymovie.API.RetrofitResponse;
import com.example.mymovie.Authentication.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TabLayout tabLayout;
    ProgressBar progressBar;
    Toolbar toolbar;
    SearchView searchBar;
    ActionBarDrawerToggle drawerToggle;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fbStore;
    private FirebaseUser currentUser;
    DocumentReference dbReference;
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    MovieAdapter movieAdapter;
    String userID;
    private TextView profileName;
    ArrayList<MovieModelResponse> movieModelArrayList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView
        movieAdapter = new MovieAdapter(this, movieModelArrayList);
        recyclerView = findViewById(R.id.myMovieRecyclerActivityId);
        recyclerView.setAdapter(movieAdapter);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // Initialize Firebase
        fAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();

        // Initialize Navigation View
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        profileName = headerView.findViewById(R.id.profile_name);
        searchBar = findViewById(R.id.searchBar);
        progressBar = findViewById(R.id.progressBar);



         searchBarFunction();
         fetchPopularMovies();

        documentSnapshotHeaderName();

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_density_medium_24);


//      Setup DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Setup tabLayout

        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fetchPopularMovies();
                        break;
                    case 1:
                        fetchTopRatedMovies();
                        break;
                    case 2:
                        fetchUpcomingMovies();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // Set Navigation Item Click Listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.account) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.settings) {
                    return true;
                } else if (id == R.id.account) {
                    return true;
                } else if (id == R.id.favourite) {
                    return true;
                } else if (id == R.id.watchlist) {
                    return true;
                } else if (id == R.id.premium) {
                    return true;
                } else if (id == R.id.logout) {
                    performLogout();
                    return true;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });


     //  Setup Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    return true;
                } else if (id == R.id.navigation_favourite) {
                    return true;
                } else if (id == R.id.navigation_addToWatchlist) {
                    return true;
                } else if (id == R.id.navigation_logout) {
                    performLogout();
                    return true;
                }
                return false;
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


    //Account Name Header
    private void documentSnapshotHeaderName() {
        if (currentUser != null) {
            userID = currentUser.getUid();
            dbReference = fbStore.collection("users").document(userID);

            dbReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.e(TAG, "Error fetching document", error);
                        return;
                    }

                    if (value != null && value.exists()) {
                        String fullName = value.getString("fullName");
                        if (fullName != null) {
                            profileName.setText(fullName);
                        } else {
                            profileName.setText("User Name");
                        }
                    } else {
                        profileName.setText("User Name");
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        } else {
            Log.e(TAG, "No authenticated user found");
        }
    }


    // Perform Logout
    @SuppressLint("SetTextI18n")
    private void performLogout() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialogue_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogView);
        TextView titleHead = dialogView.findViewById(R.id.title_head);
        TextView titleText = dialogView.findViewById(R.id.title);
        Button yesButton = dialogView.findViewById(R.id.btn_yes);
        Button noButton = dialogView.findViewById(R.id.btn_no);

        titleHead.setText("Logout Confirmation");
        titleText.setText("Are you sure you want to log out?");

        AlertDialog dialog = builder.create();

        // Set onClickListeners for buttons
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                Toast.makeText(MainActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }





    // Fetch Popular Movies
    private void fetchPopularMovies() {
        RetrofitResponse retrofitService = new RetrofitResponse(MainActivity.this, movieModelArrayList, movieAdapter, progressBar);
        retrofitService.fetchPopularMovies();

    }


    private void fetchTopRatedMovies() {
        RetrofitResponse retrofitService = new RetrofitResponse(MainActivity.this, movieModelArrayList, movieAdapter, progressBar);
        retrofitService.fetchTopRatedMovies();

    }

    private void fetchUpcomingMovies() {
        RetrofitResponse retrofitService = new RetrofitResponse(MainActivity.this, movieModelArrayList, movieAdapter, progressBar);
        retrofitService.fetchUpcomingMovies();
    }


    private void searchBarFunction(){
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                searchBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }

            private void filterList(String newText) {
                ArrayList<MovieModelResponse> filteredList = new ArrayList<>();
                for (MovieModelResponse item : movieModelArrayList) {
                    if (item.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Sorry, No result", Toast.LENGTH_SHORT).show();
                } else {
                    movieAdapter.setFiltered(filteredList);
                }
            }
        });
        searchBar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchBar.clearFocus();
                }
            }
        });

        findViewById(R.id.drawer_layout).setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchBar.clearFocus();
                return false;
            }
        });


    }
    @Override
    protected void onStop() {
        super.onStop();
        searchBar.clearFocus();
    }

}



