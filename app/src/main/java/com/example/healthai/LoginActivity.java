// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText email, password;
    private boolean validEmailInput, validPasswordInput;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.buttonLogin);
        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextPassword);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // If the user presses "Forgot Password?" text it will bring them to the Forgot Password activity
        TextView txtForgotPassword = findViewById(R.id.textViewForgotPassword);

        txtForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });


        // If the user presses the "Don't have an account? Sign Up" text it will bring them to the Sign Up activity
        TextView txtDontHaveAnAccount = findViewById(R.id.textViewDontHaveAccount);

        txtDontHaveAnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });


        loginButton.setOnClickListener(v -> {
            FirebaseAuthentication.signIn(email.getText().toString(), password.getText().toString(), mAuth, LoginActivity.this, LoginActivity.this);
        });

        // Invoke the inputChanged method from LoginInputChanged class
        LoginInputChanged.inputChanged(email, password, this::updateLoginButton, this::inputValidation);



        // GOOGLE SIGN IN
        ImageButton google_sign_in_button = findViewById(R.id.google_sign_in_button);

        google_sign_in_button.setOnClickListener(v -> {
            // create a Google sign in intent
            Intent intent = new Intent(LoginActivity.this, GoogleSignInActivity.class);
            startActivity(intent);
        });


        // TWITTER SIGN IN


        ImageButton twitter_sign_in_button = findViewById(R.id.twitter_sign_in_button);

        twitter_sign_in_button.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, TwitterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });




        // FACEBOOK SIGN IN









    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseAuthentication.reload(mAuth, LoginActivity.this, LoginActivity.this);
        }

    // this method checks if an email is entered in the correct format using the Android Patterns.EMAIL_ADDRESS matcher
    private static boolean isValidEmail(CharSequence target) {
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
        Log.d("updateUI", "UI updated");
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }




}