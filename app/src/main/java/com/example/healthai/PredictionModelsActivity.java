// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PredictionModelsActivity extends AppCompatActivity {
    private static final String TAG = "PredictionModelsActivity";
    ImageButton homeBtn, backBtn;
    FloatingActionButton logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_models);

        Button btnLungPrediction = findViewById(R.id.btnLungPrediction);
        Button btnHeartPrediction = findViewById(R.id.btnHeartPrediction);
        Button btnBreastPrediction = findViewById(R.id.btnBreastPrediction);


        homeBtn = findViewById(R.id.homeBtn);
        backBtn = findViewById(R.id.backBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        backBtn.setOnClickListener(view -> finish());

        homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(PredictionModelsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuthentication.SignOut(this);

            // Navigate back to the LogInActivity
            Intent intent = new Intent(PredictionModelsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });


        btnLungPrediction.setOnClickListener(v -> {
            // Handle Lung Prediction button click
            Log.d(TAG, "Lung Prediction clicked");
            try {
                startActivity(new Intent(PredictionModelsActivity.this, LungPredictionModelActivity.class));
                Log.d(TAG, "LungPredictionModelActivity successfully called");
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "Activity not found: " + e.getMessage());
            }
        });

        btnHeartPrediction.setOnClickListener(v -> {
            // Handle Heart Prediction button click
            Log.d(TAG, "Heart Prediction clicked");
            try {
                startActivity(new Intent(PredictionModelsActivity.this, HeartPredictionModelActivity.class));
                Log.d(TAG, "HeartPredictionModelActivity successfully called");
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "Activity not found: " + e.getMessage());
            }
        });

        btnBreastPrediction.setOnClickListener(v -> {
            // Handle Breast Prediction button click
            Log.d(TAG, "Breast Prediction clicked");
            try {
                startActivity(new Intent(PredictionModelsActivity.this, BreastPredictionModelActivity.class));
                Log.d(TAG, "BreastPredictionModelActivity successfully called");
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "Activity not found: " + e.getMessage());
            }
        });
    }
}