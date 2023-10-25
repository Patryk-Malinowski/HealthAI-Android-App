// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText email, password;
    private boolean validEmailInput, validPasswordInput;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;

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



        // GOOGLE SIGN IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton google_sign_in_button = findViewById(R.id.google_sign_in_button);

        google_sign_in_button.setOnClickListener(v -> {
            // create a Google sign in intent
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });


        // TWITTER SIGN IN


        Button twitter_sign_in_button = findViewById(R.id.twitter_sign_in_button);

        twitter_sign_in_button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TwitterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });




        // FACEBOOK SIGN IN









    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseAuthentication.reload(mAuth, MainActivity.this, MainActivity.this);
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
        Toast.makeText(MainActivity.this, "UI UPDATE", Toast.LENGTH_SHORT).show(); // temporary for testing purposes
    }


    // this method handles the result of the Google sign in activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle Google Sign In intent
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // you can now use this Google account to sign in to Firebase.
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // sign in failed
            }
        }
    }





    // this method signs in to Firebase with the google account obtained from sign in actiivty
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // sign in was successful; update UI with the user's information
                        updateUI(user);
                    } else {
                        // sign in failed
                    }
                });
    }



}