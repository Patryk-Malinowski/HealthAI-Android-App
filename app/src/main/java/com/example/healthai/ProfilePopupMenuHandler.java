// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;

public class ProfilePopupMenuHandler {
    static String TAG = "ProfileMenuClick";

    static void showPopupMenu(View v, Context context) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.profile_menu, popupMenu.getMenu());

        // set up click listener for profile menu items
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_my_details) {
                Log.d(TAG, "My Details clicked");
                handleMyDetails(context);
                return true;
            } else if (item.getItemId() == R.id.menu_settings) {
                Log.d(TAG, "Settings clicked");
                handleSettings(context);
                return true;
            } else if (item.getItemId() == R.id.menu_sign_out) {
                Log.d(TAG, "Sign Out clicked");
                handleSignOut(context);
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();  // display pop up menu
    }

    // method for handling My Details click
    static void handleMyDetails(Context context) {
        context.startActivity(new Intent(context, UserDetailsActivity.class));
    }

    // method for handling Settings click
    static void handleSettings(Context context) {
        context.startActivity(new Intent(context, UserSettingsActivity.class));
    }

    // method for handling Sign Out click
    static void handleSignOut(Context context) {
        FirebaseAuthentication.SignOut(context);
    }
}
