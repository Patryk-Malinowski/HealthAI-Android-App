// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LungPredictionModelActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String TAG = "LungPredictionModelActivity";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
    ImageButton homeBtn, backBtn;
    FloatingActionButton logoutBtn;
    String userUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lung_prediction_model);

        processUserDataAndInitiatePrediction();

        tvResult = findViewById(R.id.textViewResult);

        homeBtn = findViewById(R.id.homeBtn);
        backBtn = findViewById(R.id.backBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        backBtn.setOnClickListener(view -> finish());

        homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LungPredictionModelActivity.this, MainActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuthentication.SignOut(this);

            // Navigate back to the LogInActivity
            Intent intent = new Intent(LungPredictionModelActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to retrieve and process user data from Firebase and initiate a prediction
    void processUserDataAndInitiatePrediction() {
        db.collection("Patient").document(userUid)  // Specify the document for the current user
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Retrieve data relevant to the Lung Prediction Model
                            String gender = document.getString("gender");
                            String dob = document.getString("dob");
                            int genderInteger = convertGenderToInteger(gender);
                            int age = convertDOBToAge(dob);
                            Long airPollution = document.getLong("air_pollution");
                            Long alcoholConsumption = document.getLong("alcohol_consumption");
                            Long dustExposure = document.getLong("dust_exposure");
                            Long geneticRisk = document.getLong("genetic_risk");
                            Long balancedDiet = document.getLong("balanced_diet");
                            Long obesity = document.getLong("obesity");
                            Long smoker = document.getLong("smoker");
                            Long passiveSmoker = document.getLong("passive_smoker");
                            Long chestPain = document.getLong("chest_pain");
                            Long coughingBlood = document.getLong("coughing_blood");
                            Long fatigue = document.getLong("fatigue");
                            Long weightLoss = document.getLong("weight_loss");
                            Long shortnessBreath = document.getLong("shortness_breath");
                            Long wheezing = document.getLong("wheezing");
                            Long swallowDifficulty = document.getLong("swallow_difficulty");
                            Long clubbingNails = document.getLong("clubbing_nails");
                            Long snore = document.getLong("Snore");

                            callURL(genderInteger, age, airPollution, alcoholConsumption, dustExposure, geneticRisk, balancedDiet, obesity, smoker, passiveSmoker, chestPain, coughingBlood, fatigue, weightLoss, shortnessBreath, wheezing, swallowDifficulty, clubbingNails, snore);
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    // Method to convert users dob to age (integer)
    int convertDOBToAge(String dob) {
        // Define the date format for the DOB string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            // Parse the DOB string to a Date object
            Date dateOfBirth = dateFormat.parse(dob);

            // Get the current date
            Date currentDate = new Date();

            // Calculate the age
            int age = calculateAge(dateOfBirth, currentDate);

            return age;


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Method to calculate age (integer) from user dob
    int calculateAge(Date birthDate, Date currentDate) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);

        int age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

        return age;
    }

    // Method to convert gender to an integer
    int convertGenderToInteger(String gender) {
        int res = -1;
        if ("Male".equals(gender)) {
            res = 0;
        } else if ("Female".equals(gender)) {
            res = 1;
        }

        return res;
    }

    // Method to make a call to the server with the user's data to retrieve a prediction
    void callURL(int genderInteger, int age, long airPollution, long alcoholConsumption, long dustExposure, long geneticRisk, long balancedDiet, long obesity, long smoker, long passiveSmoker, long chestPain, long coughingBlood, long fatigue, long weightLoss, long shortnessBreath, long wheezing, long swallowDifficulty, long clubbingNails, long snore) {
        String url = "https://healthiai-predict.onrender.com/predict_lung";


        // Construct the JSON body for the API request
        @SuppressLint("DefaultLocale") String jsonInputString = String.format("[%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d]", genderInteger, age, airPollution, alcoholConsumption, dustExposure, geneticRisk, balancedDiet, obesity, smoker, passiveSmoker, chestPain, coughingBlood, fatigue, weightLoss, shortnessBreath, wheezing, swallowDifficulty, clubbingNails, snore);

        // Show a Toast for the request
        runOnUiThread(() -> Toast.makeText(LungPredictionModelActivity.this, "Request JSON: " + jsonInputString, Toast.LENGTH_LONG).show());

        // Construct the JSON body for the API request
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("data", new JSONArray(jsonInputString));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the request and enqueue it
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // If the URL call fails, log the error
                Log.e(TAG, "Failed due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Parse the response and extract the content
                    assert response.body() != null;
                    String result = response.body().string();

                    // Show a Toast for the response
                    runOnUiThread(() -> Toast.makeText(LungPredictionModelActivity.this, "Response: " + result, Toast.LENGTH_LONG).show());

                    try {
                        // Assuming the response is a JSON object with a key "prediction"
                        JSONObject jsonResponse = new JSONObject(result);
                        String prediction = jsonResponse.getString("prediction");

                        String lungPrediction, tvAnswer;

                        // Map the prediction result to the desired format
                        switch (prediction) {
                            case "L":
                                lungPrediction = "Low";
                                tvAnswer = "Our prediction model indicates you have Low risk of lung cancer.";
                                break;
                            case "M":
                                lungPrediction = "Medium";
                                tvAnswer = "Our prediction model indicates you have Medium risk of lung cancer.";
                                break;
                            case "H":
                                lungPrediction = "High";
                                tvAnswer = "Our prediction model indicates you have High risk of lung cancer.";
                                break;
                            default:
                                lungPrediction = "Error";
                                tvAnswer = "Error";
                                break;
                        }

                        // Update UI on the main thread
                        runOnUiThread(() -> tvResult.setText(tvAnswer));

                        // Update the Firestore document for the current user with the lung prediction
                        FirebaseFirestore.getInstance().collection("Patient").document(userUid).update("lung_prediction", lungPrediction).addOnSuccessListener(aVoid -> Log.d(TAG, "Lung prediction updated: " + lungPrediction)).addOnFailureListener(e -> Log.e(TAG, "Error updating lung prediction", e));

                        // Log the response
                        Log.d(TAG, "Prediction: " + lungPrediction);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                        Log.e(TAG, "Failed to parse prediction: " + result);
                    }
                } else {
                    // If the URL call is not successful, log the error
                    assert response.body() != null;
                    Log.e(TAG, "Failed to load prediction due to " + response.body().string());
                }
            }

        });
    }


}