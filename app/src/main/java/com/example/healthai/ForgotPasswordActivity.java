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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";
    private FirebaseAuth auth;
    private EditText email;
    private Button resetPasswordButton;
    private boolean validEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        auth = FirebaseAuth.getInstance();

        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        email = findViewById(R.id.editTextEmailAddress);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call the method to send a password reset email
                sendPasswordResetEmail();
                Intent intent = new Intent(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                startActivity(intent);
            }
        });

        inputChanged();
    }




    // this method sends a password reset email using firebase authentication
    private void sendPasswordResetEmail() {
        auth.sendPasswordResetEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        } else {
                            Log.e(TAG, "Failed to send reset email.", task.getException());
                        }
                    }
                });
    }



    // this method checks if the input in the email field has changed
    private void inputChanged() {
        // add a TextWatcher for the email field
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CharSequence emailCharSequence = email.getText().toString();
                validEmail = isValidEmail(emailCharSequence); // run email validation every time a character is added/changed in the email field
                updateResetButton(); // update the state of the Reset button
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

        // this method checks if an email is entered in the correct format using the Android Patterns.EMAIL_ADDRESS matcher
        public static boolean isValidEmail(CharSequence target) {
            if (target == null) {
                return false;
            } else {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
            }
        }


        private void updateResetButton(){
            if (validEmail) {
                resetPasswordButton.setEnabled(true);
                resetPasswordButton.setBackgroundColor(getColor(R.color.defaultButtonColor));
            } else {
                resetPasswordButton.setEnabled(false);
                resetPasswordButton.setBackgroundColor(getColor(R.color.unclickableButtonGray));
            }
        }

}