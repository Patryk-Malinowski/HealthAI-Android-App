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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BreastPredictionModelActivity extends AppCompatActivity {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    private static final String TAG = "BreastPredictionModelActivity";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    ImageButton homeBtn, backBtn;
    FloatingActionButton logoutBtn;
    String userUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breast_prediction_model);

        processUserDataAndInitiatePrediction();

        tvResult = findViewById(R.id.textViewResult);

        homeBtn = findViewById(R.id.homeBtn);
        backBtn = findViewById(R.id.backBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        backBtn.setOnClickListener(view -> finish());

        homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(BreastPredictionModelActivity.this, MainActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuthentication.SignOut(this);

            // Navigate back to the LogInActivity
            Intent intent = new Intent(BreastPredictionModelActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to retrieve and process user data from Firebase and initiate a prediction
    void processUserDataAndInitiatePrediction() {
        db.collection("Patient")
                .document(userUid)  // Specify the document for the current user
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Retrieve data relevant to the Lung Prediction Model
                            Double radiusMean = document.getDouble("radius_mean");
                            Double textureMean = document.getDouble("texture_mean");
                            Double perimeterMean = document.getDouble("perimeter_mean");
                            Double areaMean = document.getDouble("area_mean");
                            Double smoothnessMean = document.getDouble("smoothness_mean");
                            Double compactnessMean = document.getDouble("compactness_mean");
                            Double concavityMean = document.getDouble("concavity_mean");
                            Double concavePoints = document.getDouble("concave_points");


                            callURL(radiusMean, textureMean, perimeterMean, areaMean, smoothnessMean, compactnessMean, concavityMean, concavePoints);
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }


    // Method to make a call to the server with the user's data to retrieve a prediction
    void callURL(Double radiusMean, Double textureMean, Double perimeterMean, Double areaMean, Double smoothnessMean,
                 Double compactnessMean, Double concavityMean, Double concavePoints) {
        String url = "https://healthiai-predict.onrender.com/predict_breast";


        // Construct the JSON body for the API request
        @SuppressLint("DefaultLocale") String jsonInputString = String.format(
                "[%f, %f, %f, %f, %f, %f, %f, %f]",
                radiusMean, textureMean, perimeterMean, areaMean, smoothnessMean, compactnessMean, concavityMean, concavePoints);

        // Show a Toast for the request
        runOnUiThread(() -> Toast.makeText(BreastPredictionModelActivity.this, "Request JSON: " + jsonInputString, Toast.LENGTH_LONG).show());

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
                assert response.body() != null;
                if (response.isSuccessful()) {
                    // Parse the response and extract the content
                    String result = response.body().string();

                    // Show a Toast for the response
                    runOnUiThread(() -> Toast.makeText(BreastPredictionModelActivity.this, "Response: " + result, Toast.LENGTH_LONG).show());

                    try {
                        // Assuming the response is a JSON object with a key "prediction"
                        JSONObject jsonResponse = new JSONObject(result);
                        String answer = jsonResponse.getString("prediction");

                        String tvAnswer;
                        String breastPrediction;

                        if ("0".equals(answer)) {
                            tvAnswer = "Our prediction model indicates you do not have a risk of breast cancer.";
                            breastPrediction = "Unlikely";
                        } else if ("1".equals(answer)) {
                            tvAnswer = "Our prediction model indicates you have a risk of breast cancer.";
                            breastPrediction = "Likely";
                        } else {
                            tvAnswer = "Error";
                            breastPrediction = "Error";
                        }

                        // Update the Firestore document for the current user with the heart prediction
                        FirebaseFirestore.getInstance().collection("Patient")
                                .document(userUid)
                                .update("breast_prediction", breastPrediction)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Breast prediction updated: " + breastPrediction))
                                .addOnFailureListener(e -> Log.e(TAG, "Error updating breast prediction", e));


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