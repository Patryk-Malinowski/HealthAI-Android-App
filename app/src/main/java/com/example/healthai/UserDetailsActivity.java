// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserDetailsActivity extends AppCompatActivity {
    TextView tv1;

    private static final String TAG = "User Details Page";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);


        tv1 = findViewById(R.id.textViewName);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        db.collection("users").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {  // Change QueryDocumentSnapshot to DocumentSnapshot
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                StringBuilder userData = new StringBuilder();

                                insertData(userData, document, "name", "Full Name");
                                insertData(userData, document, "gender", "Gender");
                                insertData(userData, document, "dob", "Date of Birth");
                                insertData(userData, document, "phone", "Phone Number");
                                insertData(userData, document, "insurance", "Insurance");
                                insertData(userData, document, "policyNo", "Policy Number");
                                insertData(userData, document, "seekbarValue1", "Seekbar Value 1");
                                insertData(userData, document, "seekbarValue2", "Seekbar Value 2");
                                insertData(userData, document, "seekbarValue3", "Seekbar Value 3");
                                insertData(userData, document, "seekbarValue4", "Seekbar Value 4");
                                insertData(userData, document, "seekbarValue5", "Seekbar Value 5");
                                insertData(userData, document, "seekbarValue6", "Seekbar Value 6");
                                insertData(userData, document, "seekbarValue7", "Seekbar Value 7");

                                tv1.setText(userData.toString());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.w(TAG, "Error getting document.", task.getException());
                        }
                    }
                });




    }


    private void insertData(StringBuilder userData, DocumentSnapshot document, String field, String displayName) {
        // if document contains the specified field
        if (document.contains(field)) {
            // append custom display name, field value, and a newline character to the StringBuilder
            userData.append(displayName).append(": ").append(document.get(field)).append("\n");
        }


    }

}