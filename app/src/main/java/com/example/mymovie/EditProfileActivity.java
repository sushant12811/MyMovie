package com.example.mymovie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    private FirebaseFirestore fbStore;
    private FirebaseUser fbUser;
    private ImageButton backButton;
    private EditText fullNameEdit, emailEdit, phoneEdit, locationEdit;
    Button saveButton;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase Auth
        fAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        fbUser = fAuth.getCurrentUser();

        // Initialize views
        fullNameEdit = findViewById(R.id.fullName);
        emailEdit = findViewById(R.id.userEmail);
        phoneEdit = findViewById(R.id.phoneNumber);
        locationEdit = findViewById(R.id.locationText);
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Attach the PhoneNumberFormattingTextWatcher
        phoneEdit.addTextChangedListener(new FormatingPhoneTextWatcher(phoneEdit));

        saveButton.setOnClickListener(v -> saved());

        // Set initial values
        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String phoneNumber = data.getStringExtra("phoneNumber");
        String location = data.getStringExtra("location");

        fullNameEdit.setText(fullName);
        emailEdit.setText(email);
        phoneEdit.setText(phoneNumber);
        locationEdit.setText(location);
    }

    // Function to save edited profile
    private void saved() {

        userID = fbUser.getUid();

        String fullName = fullNameEdit.getText().toString();
        String phoneNumber = phoneEdit.getText().toString();
        String location = locationEdit.getText().toString();

        // Format the location
        location = formatLocation(location);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build();

        String finalLocation = location;
        fbUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Profile updated successfully, update Firestore document
                            DocumentReference dbReference = fbStore.collection("users").document(userID);
                            Map<String, Object> editedProfile = new HashMap<>();
                            editedProfile.put("fullName", fullName);
                            editedProfile.put("phoneNumber", phoneNumber);
                            editedProfile.put("location", finalLocation);

                            dbReference.update(editedProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EditProfileActivity.this, "Account Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfileActivity.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Failed to update profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public String formatLocation(String location) {
        // Trim leading and trailing spaces
        location = location.trim();

        // Capitalize the first letter of each word
        String[] words = location.split("\\s+");
        StringBuilder formattedLocation = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formattedLocation.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase()).append(" ");
            }
        }

        // Remove the trailing space
        return formattedLocation.toString().trim();
    }
}
