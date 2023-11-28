// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import static java.lang.Double.parseDouble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private boolean isInputValid = false;

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

        });

        updateContinueButtonState();
        inputChanged();

        Intent intent = new Intent(SignUpActivity6.this, SignUpActivity7.class);
        startActivity(intent);

    }


    // Method to check if values are withing a certain range
    private boolean isValueInRange(double value, double minValue, double maxValue) {
        return value >= minValue && value <= maxValue;
    }


    // this method checks if the inputs entered is valid for all editText fields
    private void inputValidation() {
        try {
            double value0 = parseDoubleOrZero(editTexts[0].getText().toString());
            double value1 = parseDoubleOrZero(editTexts[1].getText().toString());
            double value2 = parseDoubleOrZero(editTexts[2].getText().toString());
            double value3 = parseDoubleOrZero(editTexts[3].getText().toString());
            double value4 = parseDoubleOrZero(editTexts[4].getText().toString());
            double value5 = parseDoubleOrZero(editTexts[5].getText().toString());
            double value6 = parseDoubleOrZero(editTexts[6].getText().toString());
            double value7 = parseDoubleOrZero(editTexts[7].getText().toString());

            if (isValueInRange(value0, 6.98, 28.1) &&
                    isValueInRange(value1, 9.71, 39.3) &&
                    isValueInRange(value2, 43.8, 189) &&
                    isValueInRange(value3, 144, 2500) &&
                    isValueInRange(value4, 0.05, 0.16) &&
                    isValueInRange(value5, 0.02, 0.35) &&
                    isValueInRange(value6, 0, 0.43) &&
                    isValueInRange(value7, 0, 0.2)) {
                isInputValid = true;
            } else {
                isInputValid = false;
            }
        } catch (NumberFormatException e) {
            isInputValid = false;
        }
    }

    private double parseDoubleOrZero(String s) {
        if (s.isEmpty()) {
            return 0;
        } else {
            return Double.parseDouble(s);
        }
    }



    // this method updates the state of the Continue button based on validation results
    private void updateContinueButtonState() {
        if (isInputValid) {
            continueButton.setEnabled(true);
            continueButton.setBackgroundColor(getColor(R.color.defaultButtonColor));
        } else {
            continueButton.setEnabled(false);
            continueButton.setBackgroundColor(getColor(R.color.unclickableButtonGray));
        }
    }


    // this method checks if the input in the fields has changed
    private void inputChanged() {
        // add a single TextWatcher for multiple EditText fields
        TextWatcher commonTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputValidation();
                updateContinueButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        for (int i = 0; i <= 7; i++) {
            // we add the text watcher to each editText
            editTexts[i].addTextChangedListener(commonTextWatcher);
        }
    }

}






