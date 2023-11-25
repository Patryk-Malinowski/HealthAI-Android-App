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
    private String[] seekbarValues;
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
        seekBars = new SeekBar[17];
        textViewSeekBarValues = new TextView[17];

        for (int i = 0; i <= 6; i++) {
            int seekBarId = getResources().getIdentifier("seekBar" + (i+1), "id", getPackageName());
            int textViewId = getResources().getIdentifier("textViewSeekBarValue" + (i+1), "id", getPackageName());

            seekBars[i] = findViewById(seekBarId);
            textViewSeekBarValues[i] = findViewById(textViewId);

            setSeekBarListener(i);
        }



        continueButton.setOnClickListener(v -> {
            // Get the text values from seekbar value TextViews
            seekbarValues = new String[17];
            for (int i = 0; i <= 6; i++) {
                seekbarValues[i] = textViewSeekBarValues[i].getText().toString();
            }


            // Create a new user with a first and last name
            Map<String, Object> medical_info = new HashMap<>();
            medical_info.put("seekbarValue1", seekbarValues[0]);
            medical_info.put("seekbarValue2", seekbarValues[1]);
            medical_info.put("seekbarValue3", seekbarValues[2]);
            medical_info.put("seekbarValue4", seekbarValues[3]);
            medical_info.put("seekbarValue5", seekbarValues[4]);
            medical_info.put("seekbarValue6", seekbarValues[5]);
            medical_info.put("seekbarValue7", seekbarValues[6]);
            medical_info.put("seekbarValue8", seekbarValues[7]);
            medical_info.put("seekbarValue9", seekbarValues[8]);
            medical_info.put("seekbarValue10", seekbarValues[9]);
            medical_info.put("seekbarValue11", seekbarValues[10]);
            medical_info.put("seekbarValue12", seekbarValues[11]);
            medical_info.put("seekbarValue13", seekbarValues[12]);
            medical_info.put("seekbarValue14", seekbarValues[13]);
            medical_info.put("seekbarValue15", seekbarValues[14]);
            medical_info.put("seekbarValue16", seekbarValues[15]);
            medical_info.put("seekbarValue17", seekbarValues[16]);


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