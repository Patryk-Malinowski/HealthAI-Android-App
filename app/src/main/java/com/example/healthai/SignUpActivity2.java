// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity2 extends AppCompatActivity {
    private Button continueButton;
    private EditText firstName, lastName, phoneNumber, postalCode, dateOfBirth;
    private boolean isInputValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        continueButton = findViewById(R.id.buttonContinueRegistration);

        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        phoneNumber = findViewById(R.id.editTextPhoneNumber);
        postalCode = findViewById(R.id.editTextPostalCode);
        dateOfBirth = findViewById(R.id.editTextDateOfBirth);



        continueButton.setOnClickListener(v -> {

        });




        inputChanged();
    }


    // this method checks if the inputs entered is valid for all editText fields
    private void inputValidation() {
        String firstName = this.firstName.getText().toString().trim();
        String lastName = this.lastName.getText().toString().trim();
        String phoneNumber = this.phoneNumber.getText().toString().trim();
        String postalCode = this.postalCode.getText().toString().trim();
        String dob = this.dateOfBirth.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || postalCode.isEmpty() || dob.isEmpty()) {
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
        firstName.addTextChangedListener(commonTextWatcher);
        lastName.addTextChangedListener(commonTextWatcher);
        phoneNumber.addTextChangedListener(commonTextWatcher);
        postalCode.addTextChangedListener(commonTextWatcher);
        dateOfBirth.addTextChangedListener(commonTextWatcher);
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
