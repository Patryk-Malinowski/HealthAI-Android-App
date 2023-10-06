package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    Button continueButton;
    EditText editTextEmail, editTextPassword, editTextConfirmPassword;

    CardView cardSymbol, cardUppercase, cardNumber, cardMinimumCharacters;

    private boolean is10Char, hasUpper, hasNumber, hasSpecialSymbol, passwordMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        continueButton = findViewById(R.id.buttonContinueRegistration);

        editTextEmail = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextPasswordConfirm);

        cardMinimumCharacters = findViewById(R.id.cardMinimumTenCharacters);
        cardSymbol = findViewById(R.id.cardOneSymbol);
        cardNumber = findViewById(R.id.cardOneNumber);
        cardUppercase = findViewById(R.id.cardOneUppercase);


        // If the user presses "Already have an account? Login here" text it will bring them to the Main activity
        TextView txtAlreadyHaveAccount = findViewById(R.id.textViewAlreadyHaveAnAccount);

        txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        inputChanged();



    }



    // this method checks if the inputs entered are valid
    private void inputValidation() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Please enter an email address.");
        }

        // if password length is minimum 10 characters
        if (password.length() >= 10) {
            is10Char = true;
            cardMinimumCharacters.setCardBackgroundColor(getColor(R.color.greenCheck));
        } else {
            is10Char = false;
            cardMinimumCharacters.setCardBackgroundColor(getColor(R.color.grayCheck));
        }

        // if password contains at least one digit
        if (password.matches(".*\\d.*")) {
            hasNumber = true;
            cardNumber.setCardBackgroundColor(getColor(R.color.greenCheck));
        } else {
            hasNumber = false;
            cardNumber.setCardBackgroundColor(getColor(R.color.grayCheck));
        }

        // if password contains at least one uppercase
        if (password.matches(".*[A-Z].*")) {
            hasUpper = true;
            cardUppercase.setCardBackgroundColor(getColor(R.color.greenCheck));
        } else {
            hasUpper = false;
            cardUppercase.setCardBackgroundColor(getColor(R.color.grayCheck));
        }

        // if password contains at least one special symbol
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            hasSpecialSymbol = true;
            cardSymbol.setCardBackgroundColor(getColor(R.color.greenCheck));
        } else {
            hasSpecialSymbol = false;
            cardSymbol.setCardBackgroundColor(getColor(R.color.grayCheck));
        }

        if (confirmPassword.matches(password)) {
            passwordMatches = true;
        }
        else {
            passwordMatches = false;
        }
    }


    // this method checks if the input in the password and confirm password fields has changed
    private void inputChanged() {
        // add a TextWatcher for the password field
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputValidation(); // run input validation every time a character is added/changed in the password field
                updateContinueButtonState(); // update the state of the Continue button
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // add a TextWatcher for the confirmPassword field
        editTextConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputValidation(); // run input validation every time a character is added/changed in the confirmPassword field
                updateContinueButtonState(); // update the state of the Continue button
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }







        // update the state of the Continue button based on validation results
        private void updateContinueButtonState() {
            if (is10Char && hasNumber && hasSpecialSymbol && hasUpper && passwordMatches) {
                continueButton.setEnabled(true);
                continueButton.setBackgroundColor(getColor(R.color.defaultButtonColor));
            } else {
                continueButton.setEnabled(false);
                continueButton.setBackgroundColor(getColor(R.color.unclickableButtonGray));
            }
        }



}
