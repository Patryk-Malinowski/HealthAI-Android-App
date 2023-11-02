package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signOut = findViewById(R.id.buttonSignOut);

        signOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Log.d("SignOut", "Sign Out was successful.");

            // revoke Google access token (clears the Google Sign-In state)
            GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).revokeAccess().addOnCompleteListener(task -> {
                // then we send user back to login screen
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            });


        });
    }
}