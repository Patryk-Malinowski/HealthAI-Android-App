// Patryk Malinowski
// R00210173
// HealthAI Android App


package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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






    }
}