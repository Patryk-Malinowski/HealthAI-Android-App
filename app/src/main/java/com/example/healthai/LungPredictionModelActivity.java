// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LungPredictionModelActivity extends AppCompatActivity {
    private static final String TAG = "LungPredictionModelActivity";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    private TextView tvResult;
    ImageButton homeBtn, backBtn;
    FloatingActionButton logoutBtn;

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


    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // Method to retrieve and process user data from Firebase and initiate a prediction
    void processUserDataAndInitiatePrediction() {
        db.collection("users")
                .document(userUid)  // Specify the document for the current user
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data relevant to the Lung Prediction Model
                                String gender = document.getString("gender").toString();
                                String dob = document.getString("dob").toString();
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

                                callURL(genderInteger, age, airPollution, alcoholConsumption, dustExposure, geneticRisk, balancedDiet,
                                        obesity, smoker, passiveSmoker, chestPain, coughingBlood, fatigue, weightLoss, shortnessBreath,
                                        wheezing, swallowDifficulty, clubbingNails, snore);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
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
    int convertGenderToInteger(String gender){
        int res = -1;
        if ("Male".equals(gender)) {
            res = 0;
        } else if ("Female".equals(gender)) {
            res = 1;
        }

        return res;
    }

    // Method to make a call to the server with the user's data to retrieve a prediction
    void callURL(int genderInteger, int age, long airPollution, long alcoholConsumption, long dustExposure, long geneticRisk,
                 long balancedDiet, long obesity, long smoker, long passiveSmoker, long chestPain, long coughingBlood, long fatigue,
                 long weightLoss, long shortnessBreath, long wheezing, long swallowDifficulty, long clubbingNails, long snore) {
        String url = "https://healthiai-predict.onrender.com/predict_lung";


        // Construct the JSON body for the API request
        String jsonInputString = String.format(
                "[%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d]",
                genderInteger, age, airPollution, alcoholConsumption, dustExposure, geneticRisk, balancedDiet,
                obesity, smoker, passiveSmoker, chestPain, coughingBlood, fatigue, weightLoss, shortnessBreath,
                wheezing, swallowDifficulty, clubbingNails, snore);

        // Construct the JSON body for the API request
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("data", new JSONArray(jsonInputString));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the request and enqueue it
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

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
                    String result = response.body().string();

                    try {
                        // Assuming the response is a JSON object with a key "prediction"
                        JSONObject jsonResponse = new JSONObject(result);
                        String answer = jsonResponse.getString("prediction");

                        String tvAnswer;

                        if ("L".equals(answer)) {
                            tvAnswer = "Our prediction model indicates you have Low risk of lung cancer.";
                        } else if ("M".equals(answer)) {
                            tvAnswer = "Our prediction model indicates you have Medium risk of lung cancer.";
                        } else if ("H".equals(answer)) {
                            tvAnswer = "Our prediction model indicates you have High risk of lung cancer.";
                        } else {
                            tvAnswer = "Error";
                        }

                        // Update UI on the main thread
                        runOnUiThread(() -> tvResult.setText(tvAnswer));

                        // Log the response
                        Log.d(TAG, "Prediction: " + answer.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                        Log.e(TAG, "Failed to parse prediction: " + result);
                    }
                } else {
                    // If the URL call is not successful, log the error
                    Log.e(TAG, "Failed to load prediction due to " + response.body().string());
                }
            }
        });
    }


}