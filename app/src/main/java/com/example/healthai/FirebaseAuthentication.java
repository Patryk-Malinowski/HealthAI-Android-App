// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthentication {

    private static final String TAG = "EmailPasswordAuth";

    // this method attempts to sign in a user with the provided email and password using Firebase Authentication
    static void signIn(String email, String password, FirebaseAuth mAuth, LoginActivity activity, Context context) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            activity.updateUI(user);
                            Log.d(TAG, "Authentication Successful.");

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
                                activity.updateUI(updatedUser); // update the UI with refreshed user information
                                Log.d(TAG, "Reload Successful.");

                            } else {
                                // handle the error
                                Log.e(TAG, "Reload Failed.");
                            }
                        }
                    });
        }
    }



}
