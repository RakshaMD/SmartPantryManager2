package com.example.pantrymanager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateIngredients extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private EditText name;
    private EditText quantity;
    private EditText unit;
    private EditText expiry;

    private long id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addingredient_layout);

        databaseHelper = new DatabaseHelper(this);

        name = findViewById(R.id.etName);
        quantity = findViewById(R.id.etQuantity);
        unit = findViewById(R.id.etUnit);
        expiry = findViewById(R.id.etExpiry);

        id = getIntent().getLongExtra("id", -1);

        if (id != -1) {
            TextView title = findViewById(R.id.tvTitle);
            title.setText("Edit Your Ingredient:");

            PantryItems item = databaseHelper.getPantryItem(id);
            if (item != null) {
                name.setText(item.name);
                quantity.setText(String.valueOf(item.quantity));
                unit.setText(item.unit);
                expiry.setText(item.expiry);
            }
        }

        findViewById(R.id.btnSave).setOnClickListener(view -> saveIngredient());
        findViewById(R.id.btnCancel).setOnClickListener(view -> finish());
    }

    private void saveIngredient() {
        String ingredientName = name.getText().toString().trim();
        String ingredientUnit = unit.getText().toString().trim();
        String expiryDate = expiry.getText().toString().trim();

        if (ingredientName.isEmpty()) {
            name.setError("Ingredient name is required");
            name.requestFocus();
            return;
        }

        double ingredientQuantity;
        try {
            ingredientQuantity = Double.parseDouble(
                    quantity.getText().toString().trim()
            );
        } catch (Exception e) {
            quantity.setError("Enter a valid quantity");
            quantity.requestFocus();
            return;
        }

        if (ingredientQuantity <= 0) {
            quantity.setError("Quantity must be greater than zero");
            quantity.requestFocus();
            return;
        }

        if (ingredientUnit.isEmpty()) {
            unit.setError("Unit is required");
            unit.requestFocus();
            return;
        }

        if (!expiryDate.isEmpty() && !expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            expiry.setError("Use YYYY-MM-DD");
            expiry.requestFocus();
            return;
        }

        PantryItems item = new PantryItems(
                id,
                ingredientName,
                ingredientQuantity,
                ingredientUnit,
                expiryDate
        );

        if (id == -1) {
            databaseHelper.insertPantry(item);
        } else {
            databaseHelper.updatePantry(item);
        }

        finish();
    }
