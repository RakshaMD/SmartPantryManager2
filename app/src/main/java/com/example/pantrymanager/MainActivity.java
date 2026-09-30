package com.example.pantrymanager;

import android.annotation.SuppressLint; // Allows suppressing warnings like "NotifyDataSetChanged"
import android.content.Intent; // Used to move between screens
import android.content.SharedPreferences; // Used to load and save user settings (sorting, alerts)
import android.os.Bundle; // Gives access to Activity lifecycle and screen setup
import android.widget.TextView; // Used to update text on the screen

import androidx.appcompat.app.AlertDialog; // Used to show confirmation dialogs
import androidx.appcompat.app.AppCompatActivity; // Base class for Activities with modern UI support
import androidx.recyclerview.widget.LinearLayoutManager; // Lays items in a vertical scrolling list
import androidx.recyclerview.widget.RecyclerView; // Displays the pantry list using an adapter

import java.text.SimpleDateFormat; // Used to format dates (expiry comparison)
import java.util.ArrayList; // Used for storing pantry items in a list
import java.util.Date; // Represents a date object (today’s date)
import java.util.List; // Used for storing pantry items in a list
import java.util.Locale; // Ensures consistent formatting (e.g., date, lowercase)


public class MainActivity extends AppCompatActivity implements PantryRecycler.Listener {

    private DatabaseHelper databaseHelper;
    private PantryRecycler adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing the database
        databaseHelper = new DatabaseHelper(this);

        // Loading pantry items from the database
        List<PantryItems> items = databaseHelper.getPantry();
        if (items == null) {
            items = new ArrayList<>();
        }

        // Setting up RecyclerView adapter
        adapter = new PantryRecycler(items, this);

        RecyclerView recyclerView = findViewById(R.id.recyclerPantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Navigating to Add Ingredient screen
        findViewById(R.id.btnAdd).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UpdateIngredients.class);
            startActivity(intent);
        });

        // Navigating to Suggested Recipe screen
        findViewById(R.id.btnSuggestions).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SuggestedRecipe.class);
            startActivity(intent);
        });

        // Navigating to Settings screen
        findViewById(R.id.btnSettings).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PantrySettings.class);
            startActivity(intent);
        });

        // Initial UI refresh
        refresh();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Refresh list when returning to screen
        if (databaseHelper != null) {
            refresh();
        }
        // Calling checkExpiryAlerts method
        checkExpiryAlerts();
    }

    private void checkExpiryAlerts() {
        // Read the setting from PantrySettings
        boolean alertsEnabled = getPreferences(MODE_PRIVATE)
                .getBoolean("alerts", true);

        if (!alertsEnabled) return;  // User turned alerts OFF

        // Today’s date in the same format you store expiry
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                .format(new Date());

        // Loop through pantry items
        for (PantryItems item : databaseHelper.getPantry()) {
            if (item.expiry != null && item.expiry.trim().equals(today)) {

                new AlertDialog.Builder(this)
                        .setTitle("Expiry Alert")
                        .setMessage(item.name + " expires today!")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }

//
//    private Date parseDate(String expiry) {
//        if (expiry == null) return null;
//        expiry = expiry.trim();
//        if (expiry.isEmpty()) return null;
//
//        try {
//            return new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(expiry);
//        } catch (Exception e) {
//            return null;
//        }
//    }

    @SuppressLint("NotifyDataSetChanged")
    private void refresh() {

        // Loading pantry items
        List<PantryItems> items = databaseHelper.getPantry();

        // Loading sorting preferences
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean sortExpiry = prefs.getBoolean("sort_expiry", false);
        boolean sortName = prefs.getBoolean("sort_name", false);

        // Sort by expiry date method rules
        if (sortExpiry) {
            items.sort((a, b) -> {
                String ea = (a.expiry == null) ? "" : a.expiry.trim();
                String eb = (b.expiry == null) ? "" : b.expiry.trim();

                if (a.expiry == null) return 1;
                if (b.expiry == null) return -1;

                return ea.compareTo(eb);
                //return a.expiry.compareTo(b.expiry);
            });
        }

        // Sort alphabetically rule
        if (sortName) {
            items.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
        }

        // Updating adapter data
        adapter.setData(items);
        adapter.notifyDataSetChanged();

        // Updating pantry count text
        int count = items.size();
        TextView countText = findViewById(R.id.tvCount);
        countText.setText(getString(R.string.pantry_count, count)
        );
    }

    @Override
    public void edit(PantryItems item) {
        // Opening edit screen with selected item ID
        Intent intent = new Intent(this, UpdateIngredients.class);
        intent.putExtra("id", item.id);
        startActivity(intent);
    }

    @Override
    public void delete(PantryItems item) {
        // Confirming delete dialog box
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient?")
                .setMessage("Remove " + item.name + " from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    databaseHelper.deletePantry(item.id);
                    refresh();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}