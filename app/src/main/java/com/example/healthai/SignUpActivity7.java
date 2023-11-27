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
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity7 extends AppCompatActivity {
    private EditText[] editTexts;
    private Button continueButton;
    private static final String TAG = "Sign Up Page 5";
    private boolean isInputValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up7);

        continueButton = findViewById(R.id.buttonContinueRegistration);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Initialize EditTexts
        editTexts = new EditText[11];

        for (int i = 0; i <= 10; i++) {
            int editTextId = getResources().getIdentifier("editText" + (i + 1), "id", getPackageName());

            editTexts[i] = findViewById(editTextId);


        }


        continueButton.setOnClickListener(v -> {
            // Create new medical_info object
            Map<String, Object> medical_info = new HashMap<>();
            medical_info.put("chest_pain_type", parseDouble(editTexts[0].getText().toString()));
            medical_info.put("resting_blood_pressure", parseDouble(editTexts[1].getText().toString()));
            medical_info.put("serum_cholesterol", parseDouble(editTexts[2].getText().toString()));
            medical_info.put("fasting_blood_sugar", parseDouble(editTexts[3].getText().toString()));
            medical_info.put("resting_electrocardiographic_results", parseDouble(editTexts[4].getText().toString()));
            medical_info.put("max_heart_rate_achieved", parseDouble(editTexts[5].getText().toString()));
            medical_info.put("exercise_induced_angina", parseDouble(editTexts[6].getText().toString()));
            medical_info.put("oldpeak", parseDouble(editTexts[7].getText().toString()));
            medical_info.put(" slope_of_peak_exercise_ST_segment", parseDouble(editTexts[7].getText().toString()));
            medical_info.put("num_major_vessels", parseDouble(editTexts[7].getText().toString()));
            medical_info.put("thal", parseDouble(editTexts[7].getText().toString()));

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
                            startActivity(new Intent(SignUpActivity7.this, MainActivity.class));

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
            double value8 = parseDoubleOrZero(editTexts[8].getText().toString());
            double value9 = parseDoubleOrZero(editTexts[9].getText().toString());
            double value10 = parseDoubleOrZero(editTexts[10].getText().toString());
            double value11 = parseDoubleOrZero(editTexts[11].getText().toString());

            if (isValueInRange(value0, 0, 1) &&
                    isValueInRange(value1, 0, 3) &&
                    isValueInRange(value2, 94, 200) &&
                    isValueInRange(value3, 126, 564) &&
                    isValueInRange(value4, 0, 1) &&
                    isValueInRange(value5, 0, 2) &&
                    isValueInRange(value6, 71, 202) &&
                    isValueInRange(value7, 0, 1) &&
                    isValueInRange(value8, 0, 6.2) &&
                    isValueInRange(value9, 0, 2) &&
                    isValueInRange(value10, 0, 4) &&
                    isValueInRange(value11, 1, 3)) {
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

        for (int i = 0; i <= 10; i++) {
            // we add the text watcher to each editText
            editTexts[i].addTextChangedListener(commonTextWatcher);
        }
    }


}