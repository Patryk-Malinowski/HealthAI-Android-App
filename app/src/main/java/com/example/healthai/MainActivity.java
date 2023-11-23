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
                Toast.makeText(MainActivity.this, "Contact Insurance clicked", Toast.LENGTH_SHORT).show();
            }
        });

        leaveRatingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for Leave A Rating
                Toast.makeText(MainActivity.this, "Leave A Rating clicked", Toast.LENGTH_SHORT).show();
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


    }





}