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

        txtForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });


        // If the user presses the "Don't have an account? Sign Up" text it will bring them to the Sign Up activity
        TextView txtDontHaveAnAccount = findViewById(R.id.textViewDontHaveAccount);

        txtDontHaveAnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });


        loginButton.setOnClickListener(v -> FirebaseAuthentication.signIn(email.getText().toString(), password.getText().toString(), mAuth, MainActivity.this, MainActivity.this));

        // Invoke the inputChanged method from LoginInputChanged class
        LoginInputChanged.inputChanged(email, password, this::updateLoginButton, this::inputValidation);
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
        validPasswordInput = password.length() >= 10;
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


    // this method will update the users UI after a successful/failed login attempt
    void updateUI(FirebaseUser user) {
        Toast.makeText(MainActivity.this, "UI UPDATE", Toast.LENGTH_SHORT).show(); // temporary for testing purposes
    }
}