package Authentication;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovie.MainActivity;
import com.example.mymovie.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextView signInText;
    private FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    private FirebaseFirestore fbStore;

    private EditText emailText, passwordText, fullNameText;

    private Button registerButton;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializer();
        firebaseAuth = FirebaseAuth.getInstance();
        fbStore = FirebaseFirestore.getInstance();
        signInText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        registerButton.setOnClickListener(v -> registerUser());
    }

    // Initialize all views
    private void initializer() {
        emailText = findViewById(R.id.userEmail);
        passwordText = findViewById(R.id.userPassword);
        fullNameText = findViewById(R.id.userFullName);
        registerButton = findViewById(R.id.registerButton);
        signInText = findViewById(R.id.signInText);
    }




    // Registration method
    private void registerUser() {
        String email, password, fullName;
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        fullName = fullNameText.getText().toString();

        // Checking for empty fields
        if (fullName.isEmpty()) {
            fullNameText.setError("Please enter full name!");
            fullNameText.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailText.setError("Please enter email!");
            emailText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordText.setError("Please enter password!");
            passwordText.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        currentUser = firebaseAuth.getCurrentUser();
                        if (currentUser != null) {
                            userID = currentUser.getUid();
                            Map<String, Object> user = new HashMap<>();
                            user.put("fullName", fullName);
                            user.put("email", email);

                            // Saving user information to Firebase Firestore
                            fbStore.collection("users")
                                    .document(userID)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "User profile saved for user ID: " + userID);
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Failed to save user profile.", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });


    }


}
