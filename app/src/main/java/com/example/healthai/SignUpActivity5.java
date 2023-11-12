package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity5 extends AppCompatActivity {
    private SeekBar[] seekBars;
    private TextView[] textViewSeekBarValues;
    private Button continueButton;
    private static final String TAG = "Sign Up Page 5";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up5);

        continueButton = findViewById(R.id.buttonContinueRegistration);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Initialize SeekBars and TextViews
        seekBars = new SeekBar[7];
        textViewSeekBarValues = new TextView[7];

        for (int i = 1; i <= 7; i++) {
            int seekBarId = getResources().getIdentifier("seekBar" + i, "id", getPackageName());
            int textViewId = getResources().getIdentifier("textViewSeekBarValue" + i, "id", getPackageName());

            seekBars[i - 1] = findViewById(seekBarId);
            textViewSeekBarValues[i - 1] = findViewById(textViewId);

            setSeekBarListener(i - 1);
        }



        continueButton.setOnClickListener(v -> {
            // Get the text values from seekbar value TextViews
            String seekbarValue1 = textViewSeekBarValues[0].getText().toString();
            String seekbarValue2 = textViewSeekBarValues[1].getText().toString();
            String seekbarValue3 = textViewSeekBarValues[2].getText().toString();
            String seekbarValue4 = textViewSeekBarValues[3].getText().toString();
            String seekbarValue5 = textViewSeekBarValues[4].getText().toString();
            String seekbarValue6 = textViewSeekBarValues[5].getText().toString();
            String seekbarValue7 = textViewSeekBarValues[6].getText().toString();




            // Create a new user with a first and last name
            Map<String, Object> medical_info = new HashMap<>();
            medical_info.put("seekbarValue1", seekbarValue1);
            medical_info.put("seekbarValue2", seekbarValue2);
            medical_info.put("seekbarValue3", seekbarValue3);
            medical_info.put("seekbarValue4", seekbarValue4);
            medical_info.put("seekbarValue5", seekbarValue5);
            medical_info.put("seekbarValue6", seekbarValue6);
            medical_info.put("seekbarValue7", seekbarValue7);


            // Get the current user's UID
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            // Add a new document with the user's UID as the document ID
            db.collection("users")
                    .document(userUid)
                    .update(medical_info)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + userUid);
                            startActivity(new Intent(SignUpActivity5.this, MainActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        });
    }

    private void setSeekBarListener(final int index) {
        seekBars[index].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String labelText = "" + (progress + 1);
                textViewSeekBarValues[index].setText(labelText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}