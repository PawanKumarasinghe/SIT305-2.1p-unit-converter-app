package com.example.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    EditText inputValue;
    Spinner spinnerFrom, spinnerTo;
    Button convertButton;
    TextView resultText;

    String[] units = {
            "inch", "foot", "yard", "mile", "cm", "km",
            "pound", "ounce", "ton", "kg", "g",
            "Celsius", "Fahrenheit", "Kelvin"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputStr = inputValue.getText().toString();
                if (inputStr.isEmpty()) {
                    resultText.setText("Please enter a value.");
                    return;
                }

                try {
                    double input = Double.parseDouble(inputStr);
                    String fromUnit = spinnerFrom.getSelectedItem().toString();
                    String toUnit = spinnerTo.getSelectedItem().toString();

                    if (fromUnit.equals(toUnit)) {
                        resultText.setText("Source and destination units are the same.");
                        return;
                    }

                    double result = convert(input, fromUnit, toUnit);
                    resultText.setText(String.format("%.4f", result) + " " + toUnit);

                } catch (Exception e) {
                    resultText.setText("Invalid input.");
                }
            }
        });
    }

    private double convert(double value, String from, String to) {
        // Length
        double cm = switch (from) {
            case "inch" -> value * 2.54;
            case "foot" -> value * 30.48;
            case "yard" -> value * 91.44;
            case "mile" -> value * 160934.0;
            case "cm" -> value;
            case "km" -> value * 100000;
            default -> -1;
        };

        if (cm >= 0) {
            return switch (to) {
                case "inch" -> cm / 2.54;
                case "foot" -> cm / 30.48;
                case "yard" -> cm / 91.44;
                case "mile" -> cm / 160934.0;
                case "cm" -> cm;
                case "km" -> cm / 100000;
                default -> -1;
            };
        }

        // Weight
        double kg = switch (from) {
            case "pound" -> value * 0.453592;
            case "ounce" -> value * 0.0283495;
            case "ton" -> value * 907.185;
            case "kg" -> value;
            case "g" -> value / 1000;
            default -> -1;
        };

        if (kg >= 0) {
            return switch (to) {
                case "pound" -> kg / 0.453592;
                case "ounce" -> kg / 0.0283495;
                case "ton" -> kg / 907.185;
                case "kg" -> kg;
                case "g" -> kg * 1000;
                default -> -1;
            };
        }

        // Temperature
        return switch (from + " to " + to) {
            case "Celsius to Fahrenheit" -> (value * 1.8) + 32;
            case "Fahrenheit to Celsius" -> (value - 32) / 1.8;
            case "Celsius to Kelvin" -> value + 273.15;
            case "Kelvin to Celsius" -> value - 273.15;
            case "Fahrenheit to Kelvin" -> ((value - 32) / 1.8) + 273.15;
            case "Kelvin to Fahrenheit" -> ((value - 273.15) * 1.8) + 32;
            default -> -1;
        };
    }
}