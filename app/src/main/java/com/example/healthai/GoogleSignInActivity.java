// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignInActivity extends LoginActivity {

    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    // this method handles the result of the Google sign in activity
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        // Handle Google Sign In intent
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // you can now use this Google account to sign in to Firebase.
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // sign in failed
                finish();
            }
        }
    }


    // this method signs in to Firebase with the google account obtained from sign in activity
    private void firebaseAuthWithGoogle (String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // sign in was successful; update UI with the user's information
                        Log.d("GoogleAuth", "Google sign in successful.");
                        updateUI(user);
                    } else {
                        // sign in failed
                        Log.e("GoogleAuth", "Google sign in failed.");
                        finish();
                    }
                });
    }


    // this method will update the users UI after a successful/failed login attempt
    void updateUI(FirebaseUser user) {
        Log.d("updateUI", "UI updated");
        startActivity(new Intent(GoogleSignInActivity.this, MainActivity.class));
    }


}
