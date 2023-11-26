// Patryk Malinowski
// R00210173
// HealthAI Android App

package com.example.healthai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

public class SignUpActivity6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up6);


        // Set up filters for EditTexts
        setEditTextFilter(R.id.editText1, 6.98, 28.1);
        setEditTextFilter(R.id.editText2, 9.71, 39.3);
        setEditTextFilter(R.id.editText3, 43.8, 189);
        setEditTextFilter(R.id.editText4, 144, 2500);
        setEditTextFilter(R.id.editText5, 0.05, 0.16);
        setEditTextFilter(R.id.editText6, 0.02, 0.35);
        setEditTextFilter(R.id.editText7, 0, 0.43);
        setEditTextFilter(R.id.editText8, 0, 0.2);
    }

    // This method sets up an InputFilterMinMax for an EditText, restricting input to the specified range
    private void setEditTextFilter(int editTextId, double minValue, double maxValue) {
        EditText editText = findViewById(editTextId);
        InputFilterMinMax inputFilterMinMax = new InputFilterMinMax(minValue, maxValue);
        editText.setFilters(new InputFilter[]{inputFilterMinMax});
    }


    // This static inner class implements InputFilter to restrict input within a specified numeric range
    private static class InputFilterMinMax implements InputFilter {
        private final double minValue;
        private final double maxValue;

        // Initialize min and max values in constructor
        public InputFilterMinMax(double minValue, double maxValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        // This method filters input to allow only numeric values within the specified range (limits to 2 decimal points)
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String input = dest.toString() + source.toString();
                if (input.matches("^\\d*\\.?\\d{0,2}$")) {
                    double inputValue = Double.parseDouble(input);
                    if (isInRange(minValue, maxValue, inputValue))
                        return null;
                }
            } catch (NumberFormatException nfe) {
                // Handle the exception if the input cannot be parsed to a double
            }
            return "";
        }

        // This method checks if a value is within the specified range.
        private boolean isInRange(double a, double b, double input) {
            return b > a && input >= a && input <= b || b <= a && input >= b && input <= a;
        }
    }




}






