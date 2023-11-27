// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePopupMenuHandler.showPopupMenu(v, MainActivity.this);
            }});


        CardView contactGpCardView = findViewById(R.id.contactGpCardView);
        CardView contactInsuranceCardView = findViewById(R.id.contactInsuranceCardView);
        CardView leaveRatingCardView = findViewById(R.id.leaveRatingCardView);
        CardView AIChatbotCardView = findViewById(R.id.AIChatbotCardView);
        CardView PredictCardView = findViewById(R.id.PredictCardView);

        // we set OnClickListener for each CardView
        contactGpCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for Contact GP
                Log.d(TAG, "Contact GP clicked");
                try {
                    startActivity(new Intent(MainActivity.this, ContactDoctor.class));
                    Log.d(TAG, "ContactDoctor successfully called");
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Activity not found: " + e.getMessage());
                }
            }
        });

        contactInsuranceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for Contact Insurance
                Log.d(TAG, "Contact Insurance clicked");
                try {
                    startActivity(new Intent(MainActivity.this, ContactInsuranceActivity.class));
                    Log.d(TAG, "ContactInsuranceActivity successfully called");
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Activity not found: " + e.getMessage());
                }
            }
        });

        leaveRatingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for Leave A Rating
                Log.d(TAG, "Leave A Rating clicked");
                try {
                    startActivity(new Intent(MainActivity.this, RatingsActivity.class));
                    Log.d(TAG, "RatingsActivity successfully called");
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Activity not found: " + e.getMessage());
                }
            }
        });

        AIChatbotCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for AI Chatbot
                Log.d(TAG, "AI Chatbot clicked");
                try {
                    startActivity(new Intent(MainActivity.this, ChatbotActivity.class));
                    Log.d(TAG, "ChatbotActivity successfully called");
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Activity not found: " + e.getMessage());
                }
            }
        });


        PredictCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for Prediction Models
                Log.d(TAG, "Prediction Models clicked");
                try {
                    startActivity(new Intent(MainActivity.this, PredictionModelsActivity.class));
                    Log.d(TAG, "PredictionModelsActivity successfully called");
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Activity not found: " + e.getMessage());
                }
            }
        });


    }





}