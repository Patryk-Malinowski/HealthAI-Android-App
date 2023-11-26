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
    private int[] seekbarValues;
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

        for (int i = 0; i <= 16; i++) {
            int seekBarId = getResources().getIdentifier("seekBar" + (i+1), "id", getPackageName());
            int textViewId = getResources().getIdentifier("textViewSeekBarValue" + (i+1), "id", getPackageName());

            seekBars[i] = findViewById(seekBarId);
            textViewSeekBarValues[i] = findViewById(textViewId);

            setSeekBarListener(i);
        }



        continueButton.setOnClickListener(v -> {
            // Get the text values from seekbar value TextViews
            seekbarValues = new int[17];
            for (int i = 0; i <= 16; i++) {
                seekbarValues[i] = Integer.parseInt(textViewSeekBarValues[i].getText().toString());
            }


            // Create new medical_info object
            Map<String, Object> medical_info = new HashMap<>();
            medical_info.put("air_pollution", seekbarValues[0]);
            medical_info.put("alcohol_consumption", seekbarValues[1]);
            medical_info.put("dust_exposure", seekbarValues[2]);
            medical_info.put("genetic_risk", seekbarValues[3]);
            medical_info.put("balanced_diet", seekbarValues[4]);
            medical_info.put("obesity", seekbarValues[5]);
            medical_info.put("smoker", seekbarValues[6]);
            medical_info.put("passive_smoker", seekbarValues[7]);
            medical_info.put("chest_pain", seekbarValues[8]);
            medical_info.put("coughing_blood", seekbarValues[9]);
            medical_info.put("fatigue", seekbarValues[10]);
            medical_info.put("weight_loss", seekbarValues[11]);
            medical_info.put("shortness_breath", seekbarValues[12]);
            medical_info.put("wheezing", seekbarValues[13]);
            medical_info.put("swallow_difficulty", seekbarValues[14]);
            medical_info.put("clubbing_nails", seekbarValues[15]);
            medical_info.put("Snore", seekbarValues[16]);


            // Get the current user's UID
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            // Add to the document with the user's UID as the document ID
            db.collection("users")
                    .document(userUid)
                    .update(medical_info)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + userUid);
                            startActivity(new Intent(SignUpActivity5.this, SignUpActivity6.class));
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