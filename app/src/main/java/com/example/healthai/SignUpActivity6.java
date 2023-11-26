// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import static java.lang.Double.parseDouble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity6 extends AppCompatActivity {
    private Button continueButton;
    private EditText[] editTexts;
    private final String TAG = "SignUpActivity6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up6);

        continueButton = findViewById(R.id.buttonContinueRegistration);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialize EditTexts
        editTexts = new EditText[8];

        for (int i = 0; i <= 7; i++) {
            int editTextId = getResources().getIdentifier("editText" + (i+1), "id", getPackageName());

            editTexts[i] = findViewById(editTextId);

        }


        continueButton.setOnClickListener(v -> {
            // Check if values are within the specified range
            if (isValueInRange(parseDouble(editTexts[0].getText().toString()), 6.98, 28.1) &&
                    isValueInRange(parseDouble(editTexts[1].getText().toString()), 9.71, 39.3) &&
                    isValueInRange(parseDouble(editTexts[2].getText().toString()), 43.8, 189) &&
                    isValueInRange(parseDouble(editTexts[3].getText().toString()), 144, 2500) &&
                    isValueInRange(parseDouble(editTexts[4].getText().toString()), 0.05, 0.16) &&
                    isValueInRange(parseDouble(editTexts[5].getText().toString()), 0.02, 0.35) &&
                    isValueInRange(parseDouble(editTexts[6].getText().toString()), 0, 0.43) &&
                    isValueInRange(parseDouble(editTexts[7].getText().toString()), 0, 0.2)) {


                // Create new medical_info object
                Map<String, Object> medical_info = new HashMap<>();
                medical_info.put("radius_mean", parseDouble(editTexts[0].getText().toString()));
                medical_info.put("texture_mean", parseDouble(editTexts[1].getText().toString()));
                medical_info.put("perimeter_mean", parseDouble(editTexts[2].getText().toString()));
                medical_info.put("area_mean", parseDouble(editTexts[3].getText().toString()));
                medical_info.put("smoothness_mean", parseDouble(editTexts[4].getText().toString()));
                medical_info.put("compactness_mean", parseDouble(editTexts[5].getText().toString()));
                medical_info.put("concavity_mean", parseDouble(editTexts[6].getText().toString()));
                medical_info.put("concave_mean", parseDouble(editTexts[7].getText().toString()));

                // Get the current user's UID
                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                // Add to the document with the user's UID as the document ID
                db.collection("users")
                        .document(userUid)
                        .update(medical_info)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + userUid);
                                startActivity(new Intent(SignUpActivity6.this, MainActivity.class));

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
            else {
                Log.e(TAG, "Values are not within the required range.");
            }
        });


    }
    // Method to check if values are withing a certain range
    private boolean isValueInRange(double value, double minValue, double maxValue) {
        return value >= minValue && value <= maxValue;
    }



}






