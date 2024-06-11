package Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymovie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private EditText emailForReset;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        fAuth = FirebaseAuth.getInstance();
        initialization();
        listener();


    }

    private void initialization(){
        emailForReset = findViewById(R.id.userEmail);
        resetButton = findViewById(R.id.forgetButton);

    }

    private void listener(){
        resetButton.setOnClickListener(v -> resetButtonPressed());

    }
    private void resetButtonPressed() {
        String emailFilled = emailForReset.getText().toString();
        if (!emailFilled.isEmpty()){
            resetPassword(emailFilled);

        }else {
            emailForReset.setError("Please enter your email");
        }

    }



    //resetting method
    private void resetPassword(String email){
        fAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(ForgotPasswordActivity.this, "Reset email sent." ,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }else{
                Toast.makeText(ForgotPasswordActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
            }
        });

    }

}