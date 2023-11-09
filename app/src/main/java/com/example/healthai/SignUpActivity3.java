// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity3 extends AppCompatActivity {
    private Button continueButton;
    private EditText fullName, phoneNumber, postalCode, dateOfBirth;
    private Spinner gender;
    private boolean isInputValid;
    private static final String TAG = "Sign Up Page 3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        continueButton = findViewById(R.id.buttonContinueRegistration);

        fullName = findViewById(R.id.editTextFullName);
        phoneNumber = findViewById(R.id.editTextPhoneNumber);
        postalCode = findViewById(R.id.editTextPostalCode);
        dateOfBirth = findViewById(R.id.editTextDateOfBirth);
        gender = findViewById(R.id.spinnerGender);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // If the user presses "Already have an account? Login here" text it will bring them to the Main activity
        TextView txtAlreadyHaveAccount = findViewById(R.id.textViewAlreadyHaveAnAccount);

        txtAlreadyHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity3.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
            startActivity(intent);
        });



        continueButton.setOnClickListener(v -> {
            // Get the text values from EditText fields
            String fullNameValue = fullName.getText().toString();
            String phoneNumberValue = phoneNumber.getText().toString();
            String postalCodeValue = postalCode.getText().toString();
            String dateOfBirthValue = dateOfBirth.getText().toString();
            String formattedDateOfBirth = dateOfBirthValue.replace("/", "-");
            String genderValue = gender.getSelectedItem().toString();


            // Create a new user with a first and last name
            Map<String, Object> patient = new HashMap<>();
            patient.put("name", fullNameValue);
            patient.put("phone", phoneNumberValue);
            patient.put("postcode", postalCodeValue);
            patient.put("dob", formattedDateOfBirth);
            patient.put("gender", genderValue);


            // Get the current user's UID
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            // Add a new document with the user's UID as the document ID
            db.collection("users")
                    .document(userUid)
                    .set(patient)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + userUid);
                            startActivity(new Intent(SignUpActivity3.this, SignUpActivity4.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        });


        inputChanged();
    }


    // this method checks if the inputs entered is valid for all editText fields
    private void inputValidation() {
        String fullName = this.fullName.getText().toString().trim();
        String phoneNumber = this.phoneNumber.getText().toString().trim();
        String postalCode = this.postalCode.getText().toString().trim();
        String dob = this.dateOfBirth.getText().toString().trim();
        String gender = this.gender.getSelectedItem().toString();

        if (fullName.isEmpty() || phoneNumber.isEmpty() || postalCode.isEmpty() || dob.isEmpty() || gender.isEmpty() || !isValidDOB(dob)) {
            isInputValid = false;
        } else {
            isInputValid = true;
        }


    }

    // input validation for DOB field
    private boolean isValidDOB(String dob) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();

        if (dob.length() != 10) {
            return false; // the DOB should be 10 characters long in the format "DD/MM/YYYY"
        }

        try {
            Date date = dateFormat.parse(dob);

            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);

            int minimumYear = 1900;  // minimum year allowed
            int maximumYear = Calendar.getInstance().get(Calendar.YEAR) - 18;  // users must be 18 years old

            if (year < minimumYear || year > maximumYear) {
                return false;
            }

            return true; // the DOB is valid
        } catch (ParseException e) {
            return false; // the DOB does not match the expected format "DD/MM/YYYY"
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

        // we add the text watcher to each editText
        fullName.addTextChangedListener(commonTextWatcher);
        phoneNumber.addTextChangedListener(commonTextWatcher);
        postalCode.addTextChangedListener(commonTextWatcher);
        dateOfBirth.addTextChangedListener(commonTextWatcher);

        // Add a TextWatcher to the dateOfBirth EditText
        dateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Format the date as the user types
                if (s.length() == 2 && before == 0) {
                    // Automatically add "/" after the day
                    dateOfBirth.setText(s + "/");
                    dateOfBirth.setSelection(dateOfBirth.getText().length());
                } else if (s.length() == 5 && before == 0) {
                    // Automatically add "/" after the month
                    dateOfBirth.setText(s + "/");
                    dateOfBirth.setSelection(dateOfBirth.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        });
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

}
