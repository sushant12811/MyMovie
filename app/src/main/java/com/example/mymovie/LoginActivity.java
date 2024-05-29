package com.example.mymovie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextView registerNow, forgotPassword;
    private EditText emailUsers, passwordUsers;

    private Button logInButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializers();
        registerNow.setOnClickListener(v -> setRegisterNow());
        logInButton.setOnClickListener(v -> signIn());
        emailUsers.setShowSoftInputOnFocus(false);
    }

    private void signIn() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void initializers (){

        registerNow = findViewById(R.id.registerNowText);
        emailUsers = findViewById(R.id.userEmail);
        logInButton = findViewById(R.id.signInButton);
    }


    private void setRegisterNow(){
    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
    startActivity(intent);
    }



}