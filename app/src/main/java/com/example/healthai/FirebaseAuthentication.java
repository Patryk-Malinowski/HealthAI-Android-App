// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthentication {

    private static final String TAG = "EmailPassword";

    // this method attempts to sign in a user with the provided email and password using Firebase Authentication
    static void signIn(String email, String password, FirebaseAuth mAuth, LoginActivity activity, Context context) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(context, "Authentication Successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            activity.updateUI(user);
                            Log.d("FIREBASEAUTH", "CODE 1");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication Failed.",
                                    Toast.LENGTH_SHORT).show();
                            activity.updateUI(null);
                            Log.d("FIREBASEAUTH", "CODE 2");
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
                                Log.d("FIREBASEAUTH", "CODE 3");

                            } else {
                                // handle the error
                                Log.e(TAG, "reload:failure", task.getException());
                                Toast.makeText(context, "Failed to reload user data.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }



}
