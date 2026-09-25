package com.example.pantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetail extends AppCompatActivity {
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipedetail_layout);

        databaseHelper = new DatabaseHelper(this);

        long recipeId = getIntent().getLongExtra("id", -1);

        Recipe selectedRecipe = null;

        // Use try-with-resources to safely close the Cursor
        try (Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(
                "SELECT id, name, ingredients, method FROM recipes WHERE id=?",
                new String[]{String.valueOf(recipeId)}
        )) {
            if (cursor.moveToFirst()) {
                selectedRecipe = new Recipe(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
            }
        }

        if (selectedRecipe != null) {
            TextView title = findViewById(R.id.tvRecipeTitle);
            TextView ingredients = findViewById(R.id.tvIngredients);
            TextView method = findViewById(R.id.tvMethod);

            title.setText(selectedRecipe.name);

            ingredients.setText(
                    getString(
                            R.string.ingredients_title,
                            formatIngredients(selectedRecipe.ingredients)
                    )
            );

            method.setText(
                    getString(
                            R.string.method_title,
                            selectedRecipe.method
                    )
            );
        }

        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
    }

    private String formatIngredients(String ingredients) {
        StringBuilder result = new StringBuilder();
        String[] list = ingredients.split(";");
        for (String ingredient : list) {
            result.append("• ").append(ingredient.trim()).append("\n");
        }
        return result.toString();
    }
}