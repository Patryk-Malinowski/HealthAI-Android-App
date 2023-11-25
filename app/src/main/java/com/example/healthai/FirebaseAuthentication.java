// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseAuthentication {

    private static final String TAG = "EmailPasswordAuth";

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();


    // this method attempts to sign in a user with the provided email and password using Firebase Authentication
    static void signIn(String email, String password, FirebaseAuth mAuth, LoginActivity activity, Context context) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // if email is not verified, we don't let the user login
                            if(mAuth.getCurrentUser().isEmailVerified()){

                                Log.d(TAG, "Authentication Successful.");

                                RegistrationStatusChecker(mAuth, activity);

                            }
                            else {
                                Toast.makeText(context, "Please Verify Email", Toast.LENGTH_SHORT).show();
                            }




                        } else {
                            // If sign in fails
                            Log.e(TAG, "Authentication Failed.");
                        }
                    }
                });


    }


    // this method is used during registration and attempts to sign in a user with the provided email and password using Firebase Authentication to allow them to verify their email
    static void signIn(String email, String password, FirebaseAuth mAuth, Context context) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // if email is not verified, we don't let the user login
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                Log.d(TAG, "Authentication Successful.");
                            }
                            else {
                                Toast.makeText(context, "Please Verify Email", Toast.LENGTH_SHORT).show();
                            }




                        } else {
                            // If sign in fails
                            Log.e(TAG, "Authentication Failed.");
                        }
                    }
                });


    }





    // this method attempts to reload user's profile to ensure the local (in the app) copy is up-to-date
    static void reload(FirebaseAuth mAuth, LoginActivity activity, Context context) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // user profile has been refreshed
                                // you can access the updated user information here
                                FirebaseUser updatedUser = mAuth.getCurrentUser();
                                RegistrationStatusChecker(mAuth, activity);
                                Log.d(TAG, "Reload Successful.");

                            } else {
                                // handle the error
                                Log.e(TAG, "Reload Failed.");
                            }
                        }
                    });
        }
    }



    static void RegistrationStatusChecker(FirebaseAuth mAuth, LoginActivity activity) {
        FirebaseUser user = mAuth.getCurrentUser();
        String userUid = user.getUid();

        // Reference the user's document in Firestore
        DocumentReference userDocumentRef = db.collection("users").document(userUid);

        // Check if the "first" field is not empty for the user's document
        userDocumentRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                if (name != null && !name.isEmpty()) {
                                    // The user has completed registration page 3
                                    Log.d(TAG, "User has completed registration page 3.");
                                    String policy = document.getString("policyNo");
                                    if (policy != null && !policy.isEmpty()) {
                                        Log.d(TAG, "User has completed registration page 4.");
                                        Long seekbar1 = document.getLong("air_pollution");
                                        if (seekbar1 != null) {
                                            Log.d(TAG, "User has completed registration page 5.");
                                            activity.updateUI(user);
                                        }
                                        else {
                                            Log.d(TAG, "User has not fully completed registration page 5.");
                                            Intent intent = new Intent(activity, SignUpActivity5.class);
                                            activity.startActivity(intent);
                                        }
                                    } else {
                                        // this code is called only if the "policy" field is null
                                        Log.d(TAG, "User has not fully completed registration page 4.");
                                        Intent intent = new Intent(activity, SignUpActivity4.class);
                                        activity.startActivity(intent);
                                    }
                                } else {
                                    // this code is called only if the "first" field is null
                                    Log.d(TAG, "User has not fully completed registration page 3.");
                                    Intent intent = new Intent(activity, SignUpActivity3.class);
                                    activity.startActivity(intent);
                                }

                            } else {
                                Log.d(TAG, "User document does not exist in Firestore (User has not completed registration page 3).");
                                Intent intent = new Intent(activity, SignUpActivity3.class);
                                activity.startActivity(intent);
                            }
                        } else {
                            Log.e(TAG, "Error getting user document: " + task.getException());
                        }
                    }
                });
    }

    static void SignOut(Context context) {
        FirebaseAuth.getInstance().signOut();
        Log.d("SignOut", "Sign Out was successful.");

        // revoke Google access token (clears the Google Sign-In state)
        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).revokeAccess().addOnCompleteListener(task -> {
            // then we send user back to login screen
            context.startActivity(new Intent(context, LoginActivity.class));
        });
    }




}
