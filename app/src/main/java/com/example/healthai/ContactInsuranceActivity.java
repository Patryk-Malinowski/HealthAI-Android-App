package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContactInsuranceActivity extends AppCompatActivity {

    EditText messageEditText;
    Button submitButton;
    ImageButton backBtn, homeBtn;
    FloatingActionButton logoutBtn;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_insurance);

        messageEditText = findViewById(R.id.editTextMessage);
        submitButton = findViewById(R.id.buttonSubmit);
        submitButton.setEnabled(false);
        homeBtn = findViewById(R.id.homeBtn3);
        backBtn = findViewById(R.id.backBtn3);
        logoutBtn = findViewById(R.id.logoutBtn3);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        setUpTextWatcher();
        setUpSubmitButtonListener();

        backBtn.setOnClickListener(view -> finish());

        homeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ContactInsuranceActivity.this, MainActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuthentication.SignOut(this);

            // Navigate back to the LogInActivity
            Intent intent = new Intent(ContactInsuranceActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setUpTextWatcher() {
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not used, but required for TextWatcher
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEmpty = s.toString().trim().isEmpty();
                submitButton.setEnabled(!isEmpty);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // not used, but required for TextWatcher
            }
        });
    }

    private void setUpSubmitButtonListener() {
        submitButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(message)) {
            db.collection("Patient").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String username = documentSnapshot.getString("name");
                    String insuranceNameValue = documentSnapshot.getString("insurance_name");
                    String insurancePolicyNumberValue = documentSnapshot.getString("insurance_number");

                    Map<String, Object> messageData = new HashMap<>();
                    messageData.put("message", message);
                    messageData.put("username", username);
                    messageData.put("insurance_name", insuranceNameValue);
                    messageData.put("insurance_number", insurancePolicyNumberValue);

                    storeMessage(messageData);
                } else {
                    Toast.makeText(ContactInsuranceActivity.this, "User details not found.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(ContactInsuranceActivity.this, "Failed to fetch user details.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Please enter a message.", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeMessage(Map<String, Object> messageData) {
        db.collection("insurance_messages").add(messageData).addOnSuccessListener(documentReference -> {
            Toast.makeText(ContactInsuranceActivity.this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
            messageEditText.setText("");
        }).addOnFailureListener(e -> Toast.makeText(ContactInsuranceActivity.this, "Failed to send message.", Toast.LENGTH_SHORT).show());
    }
}
