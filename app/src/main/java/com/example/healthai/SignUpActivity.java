// Patryk Malinowski
// R00210173
// HealthAI Android App

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

    private Button continueButton;
    private EditText email, password, confirmPassword;
    private CardView cardSymbol, cardUppercase, cardNumber, cardMinimumCharacters;
    private boolean is10Char, hasUpper, hasNumber, hasSpecialSymbol, passwordMatches, validEmailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        continueButton = findViewById(R.id.buttonContinueRegistration);

        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);
        confirmPassword = findViewById(R.id.editTextPasswordConfirm);

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
            }
        });

        inputChanged();



    }


    // this method checks if an email is entered in the correct format using the Android Patterns.EMAIL_ADDRESS matcher
    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    // this method checks if the inputs entered are valid for email, password and password confirm fields
    private void inputValidation() {
        String password = this.password.getText().toString();
        String confirmPassword = this.confirmPassword.getText().toString();

        CharSequence emailCharSequence = email.getText().toString();
        // check if the email is valid using the method isValidEmail
        validEmailInput = isValidEmail(emailCharSequence);

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
        // add a TextWatcher for the email field
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputValidation(); // run input validation every time a character is added/changed in the email field
                updateContinueButtonState(); // update the state of the Continue button
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // add a TextWatcher for the password field
        password.addTextChangedListener(new TextWatcher() {
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
        confirmPassword.addTextChangedListener(new TextWatcher() {
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







        // this method updates the state of the Continue button based on validation results
        private void updateContinueButtonState() {
            if (is10Char && hasNumber && hasSpecialSymbol && hasUpper && passwordMatches && validEmailInput) {
                continueButton.setEnabled(true);
                continueButton.setBackgroundColor(getColor(R.color.defaultButtonColor));
            } else {
                continueButton.setEnabled(false);
                continueButton.setBackgroundColor(getColor(R.color.unclickableButtonGray));
            }
        }



}
