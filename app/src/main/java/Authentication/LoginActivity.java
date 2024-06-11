package Authentication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovie.MainActivity;
import com.example.mymovie.OnBoardingActivity;
import com.example.mymovie.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN = 1;
    private TextView registerNow, forgotPassword;
    private EditText emailUsers, passwordUsers;

    private FirebaseAuth fAuth;

    private FirebaseFirestore fbStore;

    FirebaseUser currentUser;

    private ImageView googleButton;

    private Button logInButton;

    GoogleSignInOptions gso;

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs = getSharedPreferences("onboarding", MODE_PRIVATE);
        boolean onboardingCompleted = prefs.getBoolean("completed", false);

        if (!onboardingCompleted) {
            startActivity(new Intent(this, OnBoardingActivity.class));
            finish();
            return;
        }

        initializers();

        fAuth = FirebaseAuth.getInstance();

        // Initialize Firestore
        fbStore = FirebaseFirestore.getInstance();

        registerNow.setOnClickListener(v -> setRegisterNow());
        logInButton.setOnClickListener(v -> signIn());
        googleButton.setOnClickListener(v -> googleSignIn());
        forgotPassword.setOnClickListener(v -> forgotPasswordClicked());

        //Google signIn option and client initialization
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("487700650235-g90hk6btvrcet54hg23kue5c7v6rvo6j.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    //Intent googleSignin
    private void googleSignIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(intent);

    }


    //Google SignIn
    private final ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount signInAccount = signInAccountTask.getResult(ApiException.class);
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                        fAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser fbUser = fAuth.getCurrentUser();
                                // Retrieve user information from GoogleSignInAccount
                                String userId = Objects.requireNonNull(fbUser).getUid();
                                String displayName = signInAccount.getDisplayName();
                                String email = signInAccount.getEmail();

                                // Create a user object with the retrieved data
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", email);
                                user.put("fullName", displayName);

                                // Add the user object to Firestore
                                fbStore.collection("users").document(userId)
                                        .set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(getApplicationContext(), "LoggedIn Successfully.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to add user data to Firestore.", Toast.LENGTH_SHORT).show());
                            } else {
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (ApiException e) {
                        Toast.makeText(getApplicationContext(), "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount signInAccount = signInAccountTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                fAuth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = fAuth.getCurrentUser();
                        // Retrieve user information from GoogleSignInAccount
                        String userId = Objects.requireNonNull(fbUser).getUid();
                        String displayName = signInAccount.getDisplayName();
                        String email = signInAccount.getEmail();

                        // Create a user object with the retrieved data
                        Map<String, Object> user = new HashMap<>();
                        user.put("email", email);
                        user.put("fullName", displayName);

                        // Add the user object to Firestore
                        fbStore.collection("users").document(userId)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getApplicationContext(), "LoggedIn Successfully.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                })
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed to add user data to Firestore.", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void forgotPasswordClicked() {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    //Initializers
    private void initializers() {
        registerNow = findViewById(R.id.registerNowText);
        emailUsers = findViewById(R.id.userEmail);
        passwordUsers = findViewById(R.id.userPassword);
        logInButton = findViewById(R.id.signInButton);
        forgotPassword = findViewById(R.id.forgotText);
        googleButton = findViewById(R.id.googleButton);
    }

    // Check if user is signed in or not
    @Override
    public void onStart() {
        super.onStart();
        currentUser = fAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //Sign In
    private void signIn() {
        String email = emailUsers.getText().toString();
        String password = passwordUsers.getText().toString();

        if (email.isEmpty()) {
            emailUsers.setError("Email is required.");
            emailUsers.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordUsers.setError("Password is required.");
            passwordUsers.requestFocus();
            return;
        }

        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Sign in Successful.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Sign in failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setRegisterNow() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
