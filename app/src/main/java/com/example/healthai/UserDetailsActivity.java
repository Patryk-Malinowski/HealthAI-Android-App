package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDetailsActivity extends AppCompatActivity {
    private static final String TAG = "User Details Page";

    private TextView tvName;
    private TextView tvGender;
    private TextView tvDob;
    private TextView tvPhone;
    private TextView tvPostCode;
    private TextView tvInsurance;
    private TextView tvPolicy;
    private TextView textViewAirPollution;
    private TextView textViewAlcohol;
    private TextView textViewDust;
    private TextView textViewSwallow;
    private TextView textViewGenetic;
    private TextView textViewDiet;
    private TextView textViewObesity;
    private TextView textViewSmoker;
    private TextView textViewPassive;
    private TextView textViewChest;
    private TextView textViewCoughing;
    private TextView textViewFatigue;
    private TextView textViewWeight;
    private TextView textViewWheezing;
    private TextView textViewSnore;
    private TextView textViewClubbing;
    private TextView textViewChest2;
    private TextView textViewPressure;
    private TextView textViewSerum;
    private TextView textViewFasting;
    private TextView textViewResting;
    private TextView textViewMax;
    private TextView textViewExercise;
    private TextView textViewOldpeak;
    private TextView textViewSlope;
    private TextView textViewVessels;
    private TextView textViewThal;
    private TextView textViewRadius;
    private TextView textViewTexture;
    private TextView textViewPerimeter;
    private TextView textViewArea;
    private TextView textViewSmoothness;
    private TextView textViewCompactness;
    private TextView textViewConcavity;
    private TextView textViewConcave;
    ImageButton homeBtn, backBtn;
    FloatingActionButton logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // Initialize TextViews
        tvName = findViewById(R.id.textViewName);
        tvGender = findViewById(R.id.textViewGender);
        tvDob = findViewById(R.id.textViewDob);
        tvPhone = findViewById(R.id.textViewPhone);
        tvPostCode = findViewById(R.id.textViewPostCode);
        tvInsurance = findViewById(R.id.textViewInsurance);
        tvPolicy = findViewById(R.id.textViewPolicy);
        textViewAirPollution = findViewById(R.id.textViewAirPollution);
        textViewAlcohol = findViewById(R.id.textViewAlcohol);
        textViewDust = findViewById(R.id.textViewDust);
        textViewSwallow = findViewById(R.id.textViewSwallow);
        textViewGenetic = findViewById(R.id.textViewGenetic);
        textViewDiet = findViewById(R.id.textViewDiet);
        textViewObesity = findViewById(R.id.textViewObesity);
        textViewSmoker = findViewById(R.id.textViewSmoker);
        textViewPassive = findViewById(R.id.textViewPassive);
        textViewChest = findViewById(R.id.textViewChest);
        textViewCoughing = findViewById(R.id.textViewCoughing);
        textViewFatigue = findViewById(R.id.textViewFatigue);
        textViewWeight = findViewById(R.id.textViewWeight);
        textViewWheezing = findViewById(R.id.textViewWheezing);
        textViewSnore = findViewById(R.id.textViewSnore);
        textViewClubbing = findViewById(R.id.textViewClubbing);
        textViewChest2 = findViewById(R.id.textViewChest2);
        textViewPressure = findViewById(R.id.textViewPressure);
        textViewSerum = findViewById(R.id.textViewSerum);
        textViewFasting = findViewById(R.id.textViewFasting);
        textViewResting = findViewById(R.id.textViewResting);
        textViewMax = findViewById(R.id.textViewMax);
        textViewExercise = findViewById(R.id.textViewExercise);
        textViewOldpeak = findViewById(R.id.textViewOldpeak);
        textViewSlope = findViewById(R.id.textViewSlope);
        textViewVessels = findViewById(R.id.textViewVessels);
        textViewThal = findViewById(R.id.textViewThal);
        textViewRadius = findViewById(R.id.textViewRadius);
        textViewTexture = findViewById(R.id.textViewTexture);
        textViewPerimeter = findViewById(R.id.textViewPerimeter);
        textViewArea = findViewById(R.id.textViewArea);
        textViewSmoothness = findViewById(R.id.textViewSmoothness);
        textViewCompactness = findViewById(R.id.textViewCompactness);
        textViewConcavity = findViewById(R.id.textViewConcavity);
        textViewConcave = findViewById(R.id.textViewConcave);
        homeBtn = findViewById(R.id.homeBtn);
        backBtn = findViewById(R.id.backBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        backBtn.setOnClickListener(view -> finish());

        homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(UserDetailsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuthentication.SignOut(this);

            // Navigate back to the LogInActivity
            Intent intent = new Intent(UserDetailsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        db.collection("users").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.d("Document", String.valueOf(document));
                            if (document.exists()) {
                                // Set data to corresponding TextViews
                                tvName.setText("Full Name: " + document.getString("name"));
                                tvGender.setText("Gender: " + document.getString("gender"));
                                tvDob.setText("Date of Birth: " + document.getString("dob"));
                                tvPhone.setText("Phone Number: " + document.getString("phone"));
                                tvPostCode.setText("Postcode: " + document.getString("postcode"));
                                tvInsurance.setText("Insurance: " + document.getString("insurance"));
                                tvPolicy.setText("Policy Number: " + document.getString("policyNo"));
                                textViewAirPollution.setText("Air Pollution: " + document.getLong("air_pollution"));
                                textViewAlcohol.setText("Alcohol Consumption: " + document.getLong("alcohol_consumption"));
                                textViewDust.setText("Dust Exposure: " + document.getLong("dust_exposure"));
                                textViewSwallow.setText("Swallow Difficulty: " + document.getLong("swallow_difficulty"));
                                textViewGenetic.setText("Genetic Risk: " + document.getLong("genetic_risk"));
                                textViewDiet.setText("Balanced Diet: " + document.getLong("balanced_diet"));
                                textViewObesity.setText("Obesity: " + document.getLong("obesity"));
                                textViewSmoker.setText("Smoker: " + document.getLong("smoker"));
                                textViewPassive.setText("Passive Smoker: " + document.getLong("passive_smoker"));
                                textViewChest.setText("Chest Pain: " + document.getLong("chest_pain"));
                                textViewCoughing.setText("Coughing Blood: " + document.getLong("coughing_blood"));
                                textViewFatigue.setText("Fatigue: " + document.getLong("fatigue"));
                                textViewWeight.setText("Weight Loss: " + document.getLong("weight_loss"));
                                textViewWheezing.setText("Wheezing: " + document.getLong("wheezing"));
                                textViewSnore.setText("Snore: " + document.getLong("snore"));
                                textViewClubbing.setText("Clubbing Nails: " + document.getLong("clubbing_nails"));
                                textViewChest2.setText("Chest Pain Type: " + document.getLong("chest_pain_type"));
                                textViewPressure.setText("Resting Blood Pressure: " + document.getLong("resting_blood_pressure"));
                                textViewSerum.setText("Serum Cholesterol: " + document.getLong("serum_cholesterol"));
                                textViewFasting.setText("Fasting Blood Sugar: " + document.getLong("fasting_blood_sugar"));
                                textViewResting.setText("Resting Electrocardiographic Results: " + document.getLong("resting_electrocardiographic_results"));
                                textViewMax.setText("Max Heart Rate Achieved: " + document.getLong("max_heart_rate_achieved"));
                                textViewExercise.setText("Exercise-Induced Angina: " + document.getLong("exercise_induced_angina"));
                                textViewOldpeak.setText("Oldpeak: " + document.getLong("oldpeak"));
                                textViewSlope.setText("Slope of Peak Exercise ST Segment: " + document.getLong("slope_of_peak_exercise_ST_segment"));
                                textViewVessels.setText("Number of Major Vessels: " + document.getLong("num_major_vessels"));
                                textViewThal.setText("Thal: " + document.getLong("thal"));
                                textViewRadius.setText("Radius Mean: " + document.getLong("radius_mean"));
                                textViewTexture.setText("Texture Mean: " + document.getLong("texture_mean"));
                                textViewPerimeter.setText("Perimeter Mean: " + document.getLong("perimeter_mean"));
                                textViewArea.setText("Area Mean: " + document.getLong("area_mean"));
                                textViewSmoothness.setText("Smoothness Mean: " + document.getLong("smoothness_mean"));
                                textViewCompactness.setText("Compactness Mean: " + document.getLong("compactness_mean"));
                                textViewConcavity.setText("Concavity Mean: " + document.getLong("concavity_mean"));
                                textViewConcave.setText("Concave Mean: " + document.getLong("concave_mean"));


                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }
                    }
                });
    }
}
