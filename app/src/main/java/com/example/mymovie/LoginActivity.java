package com.example.mymovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView registerNow, forgotPassword;
    private EditText emailUsers, passwordUsers;

    private FirebaseAuth firebaseAuth;

    FirebaseUser currentUser;

    private Button logInButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs = getSharedPreferences("onboarding", MODE_PRIVATE);
        boolean onboardingCompleted = prefs.getBoolean("completed", false);

        if (!onboardingCompleted) {
            // If onboarding is not completed, navigate to OnboardingActivity
            startActivity(new Intent(this, OnBoardingActivity.class));
            finish();
            return;
        }

        initializers();

        firebaseAuth = FirebaseAuth.getInstance();

        registerNow.setOnClickListener(v -> setRegisterNow());
        logInButton.setOnClickListener(v -> signIn());
        forgotPassword.setOnClickListener(v -> forgotPasswordClicked());

    }

    private void forgotPasswordClicked() {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }


    //Initializers
    private void initializers (){
        registerNow = findViewById(R.id.registerNowText);
        emailUsers = findViewById(R.id.userEmail);
        passwordUsers = findViewById(R.id.userPassword);
        logInButton = findViewById(R.id.signInButton);
        forgotPassword = findViewById(R.id.forgotText);
    }


    // Check if user is signed in or not
    @Override
    public void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
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

        }

        if (password.isEmpty()) {
            passwordUsers.setError("Password is required.");
            passwordUsers.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Sign in Successful.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign in failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }





    private void setRegisterNow(){
    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
    startActivity(intent);
    }



}