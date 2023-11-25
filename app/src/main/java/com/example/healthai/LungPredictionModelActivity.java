package com.example.healthai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LungPredictionModelActivity extends AppCompatActivity {
    private static final String TAG = "LungPredictionModelActivity";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String url = "https://healthiai-predict.onrender.com/predict_lung";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lung_prediction_model);

        retrieveDataFromFirebase();

    }


    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    void retrieveDataFromFirebase() {
        db.collection("users")
                .document(userUid)  // Specify the document for the current user
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve data relevant to the Lung Prediction Model
                                String gender = document.getString("gender").toString();
                                String dob = document.getString("dob").toString();
                                int genderInteger = convertGenderToInteger(gender);
                                int age = convertDOBToAge(dob);
                                int airPollution = document.getLong("air_pollution").intValue();
                                int alcoholConsumption = document.getLong("alcohol_consumption").intValue();
                                int dustExposure = document.getLong("dust_exposure").intValue();
                                int geneticRisk = document.getLong("genetic_risk").intValue();
                                int balancedDiet = document.getLong("balanced_diet").intValue();
                                int obesity = document.getLong("obesity").intValue();
                                int smoker = document.getLong("smoker").intValue();
                                int passiveSmoker = document.getLong("passive_smoker").intValue();
                                int chestPain = document.getLong("chest_pain").intValue();
                                int coughingBlood = document.getLong("coughing_blood").intValue();
                                int fatigue = document.getLong("fatigue").intValue();
                                int weightLoss = document.getLong("weight_loss").intValue();
                                int shortnessBreath = document.getLong("shortness_breath").intValue();
                                int wheezing = document.getLong("wheezing").intValue();
                                int swallowDifficulty = document.getLong("swallow_difficulty").intValue();
                                int clubbingNails = document.getLong("clubbing_nails").intValue();
                                int snore = document.getLong("Snore").intValue();

                                sendDataToPredictionModel(genderInteger, age, airPollution, alcoholConsumption, dustExposure, geneticRisk, balancedDiet,
                                        obesity, smoker, passiveSmoker, chestPain, coughingBlood, fatigue, weightLoss, shortnessBreath,
                                        wheezing, swallowDifficulty, clubbingNails, snore);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    int convertDOBToAge(String dob) {
        // Define the date format for the DOB string
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            // Parse the DOB string to a Date object
            Date dateOfBirth = dateFormat.parse(dob);

            // Get the current date
            Date currentDate = new Date();

            // Calculate the age
            int age = calculateAge(dateOfBirth, currentDate);

            return age;


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

     int calculateAge(Date birthDate, Date currentDate) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);

        int age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

        return age;
    }

    int convertGenderToInteger(String gender){
        int res = -1;
        if ("Male".equals(gender)) {
            res = 0;
        } else if ("Female".equals(gender)) {
            res = 1;
        }

        return res;
    }


    void sendDataToPredictionModel(int genderInteger, int age, int airPollution, int alcoholConsumption, int dustExposure,
                                   int geneticRisk, int balancedDiet, int obesity, int smoker, int passiveSmoker,
                                   int chestPain, int coughingBlood, int fatigue, int weightLoss, int shortnessBreath,
                                   int wheezing, int swallowDifficulty, int clubbingNails, int snore) {



    }

}