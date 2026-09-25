package com.example.pantrymanager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class PantrySettings extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        Switch alertsSwitch = findViewById(R.id.switchAlerts);

        boolean alertsEnabled = getPreferences(MODE_PRIVATE)
                .getBoolean("alerts", true);

        alertsSwitch.setChecked(alertsEnabled);

        alertsSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                getPreferences(MODE_PRIVATE)
                        .edit()
                        .putBoolean("alerts", isChecked)
                        .apply()
        );

        Button back = findViewById(R.id.btnBack);
        back.setOnClickListener(view -> finish());
    }
}