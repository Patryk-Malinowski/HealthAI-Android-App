package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RatingsActivity extends AppCompatActivity {

    private RatingBar rating;
    private EditText name, email, review;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        rating = findViewById(R.id.ratingStars);
        review = findViewById(R.id.review);
        Button submitButton = findViewById(R.id.submitRatings);

        submitButton.setOnClickListener(v -> {

            String userName = name.getText().toString();
            String userEmail = email.getText().toString();
            float ratingValue = rating.getRating();
            String reviewText = review.getText().toString();
            long timestamp = System.currentTimeMillis();

            // Create a map to store the data elements
            Map<String, Object> ratingData = new HashMap<>();
            ratingData.put("email", userEmail);
            ratingData.put("name", userName);
            ratingData.put("rating", ratingValue);
            ratingData.put("review", reviewText);
            ratingData.put("subject", "To be filled in");
            ratingData.put("timestamp", timestamp);

            String userUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocRef = db.collection("Rating").document(userUid);
            // Set the document data
            userDocRef.set(ratingData);

            // Clear the input fields after submitting
            name.setText("");
            email.setText("");
            rating.setRating(0);
            review.setText("");
        });
    }


}