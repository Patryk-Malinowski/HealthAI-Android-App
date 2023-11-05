package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;


import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity2 extends AppCompatActivity {

    FirebaseAuth mAuth;
    String TAG = "Sign Up Page 2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Button continueButton = findViewById(R.id.buttonContinueRegistration);

        continueButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                user.reload().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (user.isEmailVerified()) {
                            Log.d(TAG, "Email Is verified. Proceeding to Sign Up Page 3");
                            startActivity(new Intent(SignUpActivity2.this, SignUpActivity3.class));
                        } else {
                            Log.d(TAG, "Email Is not verified. Cannot proceed.");
                            Toast.makeText(this, "Please Verify Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                // No user is signed in.
                Log.d(TAG, "No user logged in.");
            }
        });
    }
}