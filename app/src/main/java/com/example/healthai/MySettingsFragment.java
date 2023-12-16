package com.example.healthai;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

public class MySettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Register the listener
        Objects.requireNonNull(getPreferenceManager().getSharedPreferences()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Handle preference changes here
        assert key != null;
        if (key.equals("notifications")) {
            boolean notificationsEnabled = sharedPreferences.getBoolean(key, false);
            // Do something with the new value
            Log.d("SETTINGS", "Notifications enabled: " + notificationsEnabled);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the listener to avoid memory leaks
        Objects.requireNonNull(getPreferenceManager().getSharedPreferences()).unregisterOnSharedPreferenceChangeListener(this);
    }
}
