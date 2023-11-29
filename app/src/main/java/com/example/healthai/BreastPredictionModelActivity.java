// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BreastPredictionModelActivity extends AppCompatActivity {
    private static final String TAG = "BreastPredictionModelActivity";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();
    private TextView tvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breast_prediction_model);

        processUserDataAndInitiatePrediction();

        tvResult = findViewById(R.id.textViewResult);


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
                    }
                });
    }


    // Method to make a call to the server with the user's data to retrieve a prediction
    void callURL(Double radiusMean, Double textureMean, Double perimeterMean, Double areaMean, Double smoothnessMean,
                 Double compactnessMean, Double concavityMean, Double concavePoints) {
        String url = "https://healthiai-predict.onrender.com/predict_breast";


        // Construct the JSON body for the API request
        String jsonInputString = String.format(
                "[%f, %f, %f, %f, %f, %f, %f, %f]",
                radiusMean, textureMean, perimeterMean, areaMean, smoothnessMean, compactnessMean, concavityMean, concavePoints);

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

                        if ("0".equals(answer)) {
                            tvAnswer = "Our prediction model indicates you do not have a risk of breast cancer.";
                        } else if ("1".equals(answer)) {
                            tvAnswer = "Our prediction model indicates you have a risk of breast cancer.";
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