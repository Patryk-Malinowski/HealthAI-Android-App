package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;


public class MainActivity extends AppCompatActivity {

    String TAG = "ProfileMenuClick";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            ImageButton profileIcon = findViewById(R.id.profileIcon);

            profileIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v);
                }
        });

    }

    private void showPopupMenu (View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.profile_menu, popupMenu.getMenu());

        // set up click listener for profile menu items
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_my_details) {
                    handleMyDetails();
                    Log.d(TAG, "My Details clicked");
                    return true;
                } else if (item.getItemId() == R.id.menu_settings) {
                    handleSettings();
                    Log.d(TAG, "Settings clicked");
                    return true;
                } else if (item.getItemId() == R.id.menu_sign_out) {
                    handleSignOut();
                    Log.d(TAG, "Sign Out clicked");
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();  // display pop up menu
    }

    // method for handling My Details click
    public void handleMyDetails() {
    }

    // method for handling Settings click
    public void handleSettings() {
    }

    // method for handling Sign Out click
    public void handleSignOut() {
    FirebaseAuthentication.SignOut(this);
    }


    }
