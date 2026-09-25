package com.example.pantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class RecipeDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipedetail_layout);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        long recipeId = getIntent().getLongExtra("id", -1);

        Recipe selectedRecipe = null;
        for (Recipe recipe : databaseHelper.getRecipes()) {
            if (recipe.id == recipeId) {
                selectedRecipe = recipe;
                break;
            }
        }

        if (selectedRecipe != null) {
            TextView title = findViewById(R.id.tvRecipeTitle);
            TextView ingredients = findViewById(R.id.tvIngredients);
            TextView method = findViewById(R.id.tvMethod);

            title.setText(selectedRecipe.name);
            ingredients.setText("INGREDIENTS:\n\n" + formatIngredients(selectedRecipe.ingredients));
            method.setText("METHOD:\n\n" + selectedRecipe.method);
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