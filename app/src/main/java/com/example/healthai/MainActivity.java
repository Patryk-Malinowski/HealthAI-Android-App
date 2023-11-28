// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    ImageButton GPBtn, insuranceBtn, ratingBtn, AIBtn, predictBtn, homeBtn, backBtn;
    FloatingActionButton logoutBtn;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(v -> ProfilePopupMenuHandler.showPopupMenu(v, MainActivity.this));

        GPBtn = findViewById(R.id.GPBtn);
        insuranceBtn = findViewById(R.id.insuranceBtn);
        ratingBtn = findViewById(R.id.ratingBtn);
        AIBtn = findViewById(R.id.AIBtn);
        predictBtn = findViewById(R.id.PredictBtn);
        homeBtn = findViewById(R.id.homeBtn);
        backBtn = findViewById(R.id.backBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        GPBtn.setOnClickListener(v -> {
            // Handle the click for Contact GP
            Log.d(TAG, "Contact GP clicked");
            try {
                startActivity(new Intent(MainActivity.this, ContactDoctor.class));
                Log.d(TAG, "ContactDoctor successfully called");
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "Activity not found: " + e.getMessage());
            }
        });

        insuranceBtn.setOnClickListener(v -> {
            // Handle the click for Contact Insurance
            Log.d(TAG, "Contact Insurance clicked");
            try {
                startActivity(new Intent(MainActivity.this, ContactInsuranceActivity.class));
                Log.d(TAG, "ContactInsuranceActivity successfully called");
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "Activity not found: " + e.getMessage());
            }
        });

        ratingBtn.setOnClickListener(v -> {
            // Handle the click for Leave A Rating
            Log.d(TAG, "Leave A Rating clicked");
            try {
                startActivity(new Intent(MainActivity.this, RatingsActivity.class));
                Log.d(TAG, "RatingsActivity successfully called");
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "Activity not found: " + e.getMessage());
            }
        });

        AIBtn.setOnClickListener(v -> {
            // Handle the click for AI Chatbot
            Log.d(TAG, "AI Chatbot clicked");
            try {
                startActivity(new Intent(MainActivity.this, ChatbotActivity.class));
                Log.d(TAG, "ChatbotActivity successfully called");
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "Activity not found: " + e.getMessage());
            }
        });

        predictBtn.setOnClickListener(v -> {
            // Handle the click for Prediction Models
            Log.d(TAG, "Prediction Models clicked");
            try {
                startActivity(new Intent(MainActivity.this, PredictionModelsActivity.class));
                Log.d(TAG, "PredictionModelsActivity successfully called");
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "Activity not found: " + e.getMessage());
            }
        });

        backBtn.setOnClickListener(view -> finish());

        homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuthentication.SignOut(this);

            // Navigate back to the LogInActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}