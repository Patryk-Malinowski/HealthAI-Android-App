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

public class PredictionModelsActivity extends AppCompatActivity {
    private Button btnLungPrediction;
    private Button btnHeartPrediction;
    private Button btnBreastPrediction;
    private static String TAG = "PredictionModelsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_models);

        btnLungPrediction = findViewById(R.id.btnLungPrediction);
        btnHeartPrediction = findViewById(R.id.btnHeartPrediction);
        btnBreastPrediction = findViewById(R.id.btnBreastPrediction);


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