// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity7 extends AppCompatActivity {
    private static final String TAG = "Sign Up Page 5";
    private EditText editText2, editText3, editText6, editText8, editText10;
    private Button continueButton;
    private boolean isInputValid = false;
    private Spinner chestPainSpinner, fastingBloodSugarSpinner, restingECGSpinner, exerciseInducedAnginaSpinner, slopePeakExerciseSpinner, thalassemiaSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up7);

        continueButton = findViewById(R.id.buttonContinueRegistration);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialize Spinners
        chestPainSpinner = findViewById(R.id.chestPainSpinner);
        fastingBloodSugarSpinner = findViewById(R.id.fastingBloodSugarSpinner);
        restingECGSpinner = findViewById(R.id.restingECGSpinner);
        exerciseInducedAnginaSpinner = findViewById(R.id.exerciseInducedAnginaSpinner);
        slopePeakExerciseSpinner = findViewById(R.id.slopePeakExerciseSpinner);
        thalassemiaSpinner = findViewById(R.id.thalassemiaSpinner);

        // Initialize HashMaps to map spinner values to their corresponding database values
        // Chest Pain Type
        Map<String, Integer> chestPainMap = new HashMap<>();
        chestPainMap.put("Typical", 0);
        chestPainMap.put("Atypical", 1);
        chestPainMap.put("Non-anginal", 2);
        chestPainMap.put("Asymptomatic", 3);

        // Fasting Blood Sugar
        Map<String, Integer> fastingBloodSugarMap = new HashMap<>();
        fastingBloodSugarMap.put("+ 120 mg/dL", 1);
        fastingBloodSugarMap.put("- 120 mg/dL", 0);

        // Resting ECG
        Map<String, Integer> restingECGMap = new HashMap<>();
        restingECGMap.put("Normal", 0);
        restingECGMap.put("Abnormality", 1);
        restingECGMap.put("Hypertrophy", 2);

        // Exercise Induced Angina
        Map<String, Integer> exerciseInducedAnginaMap = new HashMap<>();
        exerciseInducedAnginaMap.put("Yes", 1);
        exerciseInducedAnginaMap.put("No", 0);

        // Slope Peak Exercise
        Map<String, Integer> slopePeakExerciseMap = new HashMap<>();
        slopePeakExerciseMap.put("Upsloping", 0);
        slopePeakExerciseMap.put("Flat", 1);
        slopePeakExerciseMap.put("Downsloping", 2);

        // Thalassemia
        Map<String, Integer> thalassemiaMap = new HashMap<>();
        thalassemiaMap.put("Normal", 0);
        thalassemiaMap.put("Fixed", 1);
        thalassemiaMap.put("Reversible", 2);


        // Initialize EditTexts
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText6 = findViewById(R.id.editText6);
        editText8 = findViewById(R.id.editText8);
        editText10 = findViewById(R.id.editText10);


        continueButton.setOnClickListener(v -> {
            // Create new medical_info object
            Map<String, Object> medical_info = new HashMap<>();

            // Retrieve selected values from spinners/EditText and put them into medical_info
            // Chest Pain Type
            String selectedChestPain = chestPainSpinner.getSelectedItem().toString();
            int chestPainValue = chestPainMap.get(selectedChestPain);
            medical_info.put("chest_pain_type", chestPainValue);

            medical_info.put("resting_blood_pressure", parseInt(editText2.getText().toString()));
            medical_info.put("serum_cholesterol", parseInt(editText3.getText().toString()));

            // Fasting Blood Sugar
            String selectedFastingBloodSugar = fastingBloodSugarSpinner.getSelectedItem().toString();
            int fastingBloodSugarValue = fastingBloodSugarMap.get(selectedFastingBloodSugar);
            medical_info.put("fasting_blood_sugar", fastingBloodSugarValue);

            // Resting ECG
            String selectedRestingECG = restingECGSpinner.getSelectedItem().toString();
            int restingECGValue = restingECGMap.get(selectedRestingECG);
            medical_info.put("resting_electrocardiographic_results", restingECGValue);

            // Max Heart Rate
            medical_info.put("max_heart_rate_achieved", parseInt(editText6.getText().toString()));

            // Exercise Induced Angina
            String selectedExerciseInducedAngina = exerciseInducedAnginaSpinner.getSelectedItem().toString();
            int exerciseInducedAnginaValue = exerciseInducedAnginaMap.get(selectedExerciseInducedAngina);
            medical_info.put("exercise_induced_angina", exerciseInducedAnginaValue);

            // ST Depression
            medical_info.put("oldpeak", parseDouble(editText8.getText().toString()));

            // Slope Peak Exercise
            String selectedSlope = slopePeakExerciseSpinner.getSelectedItem().toString();
            int slopeValue = slopePeakExerciseMap.get(selectedSlope);
            medical_info.put("slope_of_peak_exercise_ST_segment", slopeValue);

            // Number of Major Vessels
            medical_info.put("num_major_vessels", parseInt(editText10.getText().toString()));

            // Thalassemia
            String selectedThalassemia = thalassemiaSpinner.getSelectedItem().toString();
            int thalassemiaValue = thalassemiaMap.get(selectedThalassemia);
            medical_info.put("thal", thalassemiaValue);


            // Get the current user's UID
            String userUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


            // Add to the document with the user's UID as the document ID
            db.collection("Patient").document(userUid).update(medical_info).addOnSuccessListener(aVoid -> {
                Log.d(TAG, "DocumentSnapshot added with ID: " + userUid);
                startActivity(new Intent(SignUpActivity7.this, MainActivity.class));

            }).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

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
            double value0 = parseInt(editText2.getText().toString());
            double value1 = parseInt(editText3.getText().toString());
            double value2 = parseInt(editText6.getText().toString());
            double value3 = parseDoubleOrZero(editText8.getText().toString());
            double value4 = parseInt(editText10.getText().toString());

            isInputValid = isValueInRange(value0, 94, 200) && isValueInRange(value1, 126, 564) && isValueInRange(value2, 71, 202) && isValueInRange(value3, 0, 6.2) && isValueInRange(value4, 0, 4);
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
        // Add a single TextWatcher for multiple EditText fields
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

        // Initialize an array of EditText fields
        EditText[] editTexts = {editText2, editText3, editText6, editText8, editText10};

        // Attach the text watcher to each editText
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(commonTextWatcher);
        }
    }


}
