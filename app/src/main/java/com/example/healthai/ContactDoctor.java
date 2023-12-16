// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ContactDoctor extends AppCompatActivity {
    private static final String TAG = "UserDetailsActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uid;

    {
        assert currentUser != null;
        uid = currentUser.getUid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDoctor();

    }

    // this method retrieves the patient's assigned doctor's name
    void getDoctor() {
        db.collection("Patient").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "Patient Document id => " + document.getId() + " Data retrieved => " + document.getData());
                    // get value of "doctor" field
                    try {
                        String doctorValue = document.getString("doctor");

                        ringDoctor(doctorValue);
                        Log.d(TAG, "getDoctor method successfully executed");

                    } catch (NullPointerException e) {
                        Log.e(TAG, "Error getting doctor value: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Document does not exist");
                }
            } else {
                Log.e(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    // method to retrieve doctor phone number and call the action_dial method
    void ringDoctor(String doctorValue) {
        db.collection("doctors").whereEqualTo("name", doctorValue).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, "Doctor Document id => " + document.getId() + " Data retrieved => " + document.getData());
                    // get value of "phoneNumber" field
                    String phoneNumber = document.getString("phoneNumber");

                    startActivity(new Intent(ContactDoctor.this, MainActivity.class));

                    action_dial(phoneNumber);
                    Log.d(TAG, "ringDoctor method successfully executed");
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    // method to initiate phone call using ACTION_DIAL intent
    public void action_dial(String phoneNumber) {
        Intent intent_dial = new Intent(Intent.ACTION_DIAL);
        Uri number = Uri.parse("tel:" + phoneNumber);
        intent_dial.setData(number);
        try {
            startActivity(intent_dial);
            Log.d(TAG, "action_dial method successfully executed");
        } catch (ActivityNotFoundException e) {
            // toast for exception
        }
    }
}


