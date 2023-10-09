// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText email, password;
    private boolean validEmailInput, validPasswordInput;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.buttonLogin);
        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        // If the user presses "Forgot Password?" text it will bring them to the Forgot Password activity
        TextView txtForgotPassword = findViewById(R.id.textViewForgotPassword);

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // If the user presses the "Don't have an account? Sign Up" text it will bring them to the Sign Up activity
        TextView txtDontHaveAnAccount = findViewById(R.id.textViewDontHaveAccount);

        txtDontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuthentication.signIn(email.getText().toString(), password.getText().toString(), mAuth, MainActivity.this, MainActivity.this);
            }
        });



        inputChanged(); // call method to check if input fields have changed

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseAuthentication.reload(mAuth, MainActivity.this, MainActivity.this);
    }

    // this method checks if an email is entered in the correct format using the Android Patterns.EMAIL_ADDRESS matcher
    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    // this method checks if the inputs entered are valid for email and password
    private void inputValidation() {
        CharSequence emailCharSequence = email.getText().toString();
        // check if the email is valid using the method isValidEmail
        validEmailInput = isValidEmail(emailCharSequence);


        if (password.length() >= 10) {
            validPasswordInput = true;
        }
        else {
            validPasswordInput = false;
        }
    }



    // this method checks if the input in the email and password fields has changed
    private void inputChanged() {
        // add a TextWatcher for the email field
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputValidation(); // run input validation every time a character is added/changed in the email field
                updateLoginButton(); // update the state of the Login button
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
                updateLoginButton(); // update the state of the Login button
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    // this method updates the state of the Continue button based on validation results
    private void updateLoginButton() {
        if (validEmailInput && validPasswordInput) {
            loginButton.setEnabled(true);
            loginButton.setBackgroundColor(getColor(R.color.defaultButtonColor));
        } else {
            loginButton.setEnabled(false);
            loginButton.setBackgroundColor(getColor(R.color.unclickableButtonGray));
        }
    }


    void updateUI(FirebaseUser user) {
        Toast.makeText(MainActivity.this, "UI UPDATE", Toast.LENGTH_SHORT).show(); // temporary for testing purposes
    }
}