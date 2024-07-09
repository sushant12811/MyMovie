package com.example.mymovie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity"; // Define TAG here

    private TextView fullNameProfileUser, emailProfileUser1, emailProfileUser2, phoneProfileUser, locationProfileUser;
    private Button editProfileButton;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fbStore;
    private FirebaseUser currentUser;
    private DocumentReference dbReference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        fAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();

        initializer();
        documentSnapshotProfileRetrieve();

        // Edit profile button
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditProfileActivity.class);
                intent.putExtra("fullName", fullNameProfileUser.getText().toString());
                intent.putExtra("email", emailProfileUser1.getText().toString());
                intent.putExtra("phoneNumber", phoneProfileUser.getText().toString());
                intent.putExtra("location", locationProfileUser.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        // Set up the bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    Intent homeIntent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                    return true;
                    // Handle other menu items if necessary
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Handle the back button
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializer() {
        fullNameProfileUser = findViewById(R.id.fullNameTextview);
        emailProfileUser1 = findViewById(R.id.emailTextView1);
        emailProfileUser2 = findViewById(R.id.emailTextView2);
        phoneProfileUser = findViewById(R.id.phoneTextView);
        locationProfileUser = findViewById(R.id.locationTextView);
        editProfileButton = findViewById(R.id.editProfileButton);
    }

    private void documentSnapshotProfileRetrieve() {
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

                    if (value != null) {
                        String fullName = value.getString("fullName");
                        String emailId = value.getString("email");
                        String phoneNo = value.getString("phoneNumber");
                        String location = value.getString("location");
                        fullNameProfileUser.setText(fullName);
                        emailProfileUser1.setText(emailId);
                        emailProfileUser2.setText(emailId);
                        phoneProfileUser.setText(phoneNo);
                        locationProfileUser.setText(location);
                    } else {
                        fullNameProfileUser.setText("N/A");
                        emailProfileUser1.setText("N/A");
                        emailProfileUser2.setText("N/A");
                        locationProfileUser.setText("N/A");
                        phoneProfileUser.setText("N/A");
                    }
                }
            });
        } else {
            Log.e(TAG, "No authenticated user found");
        }
    }
}
