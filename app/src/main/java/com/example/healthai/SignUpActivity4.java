// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignUpActivity4 extends AppCompatActivity {
    private Button continueButton;
    private EditText insuranceName, insurancePolicyNumber;
    private boolean isInputValid;
    private static final String TAG = "Sign Up Page 3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up4);

        continueButton = findViewById(R.id.buttonContinueRegistration);

        insuranceName = findViewById(R.id.editTextInsuranceName);
        insurancePolicyNumber = findViewById(R.id.editTextPolicyNumber);


        FirebaseFirestore db = FirebaseFirestore.getInstance();



        continueButton.setOnClickListener(v -> {
            // Get the text values from EditText fields
            String insuranceNameValue = insuranceName.getText().toString();
            String insurancePolicyNumberValue = insurancePolicyNumber.getText().toString();



            // Create a new user with a first and last name
            Map<String, Object> insurer = new HashMap<>();
            insurer.put("insurance", insuranceNameValue);
            insurer.put("policyNo", insurancePolicyNumberValue);


            // Get the current user's UID
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            // Add a new document with the user's UID as the document ID
            db.collection("users")
                    .document(userUid)
                    .update(insurer)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + userUid);
//                            startActivity(new Intent(SignUpActivity4.this, SignUpActivity5.class));
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
        String insurance = this.insuranceName.getText().toString().trim();
        String POLICY = this.insurancePolicyNumber.getText().toString().trim();

        if (insurance.isEmpty() || POLICY.isEmpty()) {
            isInputValid = false;
        } else {
            isInputValid = true;
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
        insuranceName.addTextChangedListener(commonTextWatcher);
        insurancePolicyNumber.addTextChangedListener(commonTextWatcher);
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
