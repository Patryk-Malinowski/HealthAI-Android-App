package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class UserDetailsActivity extends AppCompatActivity {
    TextView tv1;

    private static final String TAG = "User Details Page";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);


        tv1 = findViewById(R.id.textViewName);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StringBuilder userData = new StringBuilder();
                            for (QueryDocumentSnapshot document : task.getResult()) {
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
                            }
                            tv1.setText(userData.toString());
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });




    }


    private void insertData(StringBuilder userData, QueryDocumentSnapshot document, String field, String displayName) {
        // if document contains the specified field
        if (document.contains(field)) {
            // append custom display name, field value, and a newline character to the StringBuilder
            userData.append(displayName).append(": ").append(document.get(field)).append("\n");
        }


    }

}