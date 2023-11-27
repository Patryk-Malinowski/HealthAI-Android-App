package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ContactInsuranceActivity extends AppCompatActivity {

    EditText messageEditText;
    Button submitButton;

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

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        setUpTextWatcher();
        setUpSubmitButtonListener();
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
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(message)) {
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String username = documentSnapshot.getString("name");
                                String insuranceNameValue = documentSnapshot.getString("insurance");
                                String insurancePolicyNumberValue = documentSnapshot.getString("policyNo");

                                Map<String, Object> messageData = new HashMap<>();
                                messageData.put("message", message);
                                messageData.put("username", username);
                                messageData.put("insurance_company", insuranceNameValue);
                                messageData.put("policy_number", insurancePolicyNumberValue);

                                storeMessage(messageData);
                            } else {
                                Toast.makeText(ContactInsuranceActivity.this, "User details not found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ContactInsuranceActivity.this, "Failed to fetch user details.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Please enter a message.", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeMessage(Map<String, Object> messageData) {
        db.collection("insurance_messages")
                .add(messageData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ContactInsuranceActivity.this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
                        messageEditText.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ContactInsuranceActivity.this, "Failed to send message.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

