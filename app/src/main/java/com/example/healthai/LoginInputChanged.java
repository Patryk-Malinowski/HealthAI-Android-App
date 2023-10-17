// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class LoginInputChanged {
    // this method checks if the input in the email and password fields has changed
    public static void inputChanged(EditText email, EditText password, Runnable updateLoginButton, Runnable inputValidation) {
        // add a TextWatcher for the email field
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputValidation.run(); // run input validation every time a character is added/changed in the email field
                updateLoginButton.run(); // update the state of the Login button
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
                inputValidation.run(); // run input validation every time a character is added/changed in the password field
                updateLoginButton.run(); // update the state of the Login button
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


}
