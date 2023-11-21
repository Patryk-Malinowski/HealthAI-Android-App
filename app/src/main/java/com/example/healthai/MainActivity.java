// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


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

        // we set OnClickListener for each CardView
        contactGpCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click for Contact GP
                Toast.makeText(MainActivity.this, "Contact GP clicked", Toast.LENGTH_SHORT).show();
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


    }





    }
