package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class SignUpActivity5 extends AppCompatActivity {
    private SeekBar[] seekBars;
    private TextView[] textViewSeekBarValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up5);

        // Initialize SeekBars and TextViews
        seekBars = new SeekBar[7];
        textViewSeekBarValues = new TextView[7];

        for (int i = 1; i <= 7; i++) {
            int seekBarId = getResources().getIdentifier("seekBar" + i, "id", getPackageName());
            int textViewId = getResources().getIdentifier("textViewSeekBarValue" + i, "id", getPackageName());

            seekBars[i - 1] = findViewById(seekBarId);
            textViewSeekBarValues[i - 1] = findViewById(textViewId);

            setSeekBarListener(i - 1);
        }
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