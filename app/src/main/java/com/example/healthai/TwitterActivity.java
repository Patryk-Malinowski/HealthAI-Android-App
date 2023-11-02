// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

public class TwitterActivity extends LoginActivity {


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        String apiKey = getString(R.string.twitter_api_key);

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
        provider.addCustomParameter("oauth_consumer_key", apiKey);
        provider.addCustomParameter("oauth_nonce", "unique_nonce");
        provider.addCustomParameter("oauth_timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        provider.addCustomParameter("oauth_signature_method", "HMAC-SHA1");
        provider.addCustomParameter("oauth_version", "1.0");


        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    Log.d("TwitterLogin", "Login successful in Twitter version 3");
                                    startActivity(new Intent(TwitterActivity.this, LoginActivity.class));
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    Log.d("TwitterLogin", "Login FAILED in Twitter version 3");

                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
            firebaseAuth
                    .startActivityForSignInWithProvider(/* activity= */ this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    Log.d("TwitterLogin", "Login successful in Twitter version 4");
                                    startActivity(new Intent(TwitterActivity.this, LoginActivity.class));
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    Log.d("TwitterLogin", "Login FAILED in Twitter version 4");
                                }
                            });
        }

    }






}