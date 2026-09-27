package com.example.pantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements PantryRecycler.Listener {

    private DatabaseHelper databaseHelper;
    private PantryRecycler adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        List<PantryItems> items = databaseHelper.getPantry();
        if (items == null) {
            items = new ArrayList<>();
        }

        adapter = new PantryRecycler(items, this);

        RecyclerView recyclerView = findViewById(R.id.recyclerPantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnAdd).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UpdateIngredients.class);
            startActivity(intent);
        });

        findViewById(R.id.btnSuggestions).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SuggestedRecipe.class);
            startActivity(intent);
        });

        findViewById(R.id.btnSettings).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PantrySettings.class);
            startActivity(intent);
        });

        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (databaseHelper != null) {
            refresh();
        }
    }

    private Date parseDate(String expiry) {
        if (expiry == null) return null;
        expiry = expiry.trim();
        if (expiry.isEmpty()) return null;

        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(expiry);
        } catch (Exception e) {
            return null;
        }
    }

    private void refresh() {
        List<PantryItems> items = databaseHelper.getPantry();

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean sortExpiry = prefs.getBoolean("sort_expiry", false);
        boolean sortName = prefs.getBoolean("sort_name", false);

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

        if (sortName) {
            items.sort((a, b) -> a.name.compareToIgnoreCase(b.name));
        }

        adapter.setData(items);
        adapter.notifyDataSetChanged();

        int count = items.size();
        TextView countText = findViewById(R.id.tvCount);
        countText.setText(getString(R.string.pantry_count, count)
        );
    }

    @Override
    public void edit(PantryItems item) {
        Intent intent = new Intent(this, UpdateIngredients.class);
        intent.putExtra("id", item.id);
        startActivity(intent);
    }

    @Override
    public void delete(PantryItems item) {
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