package com.example.pantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.app.AppCompatActivity;

public class PantrySettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        // Switch for expiry alerts
        SwitchCompat alertsSwitch = findViewById(R.id.alertsSwitch);

        boolean alertsEnabled = getPreferences(MODE_PRIVATE)
            .getBoolean("alerts", true);

        alertsSwitch.setChecked(alertsEnabled);

        alertsSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
            getPreferences(MODE_PRIVATE).edit().putBoolean("alerts", isChecked).apply()
        );

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);

        // Switch to default view - sort by name
        SwitchCompat nameSwitch = findViewById(R.id.switchSortName);

        // Load saved value
        boolean nameSortEnabled = prefs.getBoolean("sort_name", false);
        nameSwitch.setChecked(nameSortEnabled);

        // Save value
        nameSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean("sort_name", isChecked).apply()
        );


        // Switch to sort by expiry date
        SwitchCompat sortSwitch = findViewById(R.id.switchSortExpiry);

        // Load saved value
        boolean sortEnabled = prefs.getBoolean("sort_expiry", false);
        sortSwitch.setChecked(sortEnabled);

        // Save value
        sortSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean("sort_expiry", isChecked).apply()
        );


        Button back = findViewById(R.id.btnBack);
        back.setOnClickListener(view -> finish());
    }
}