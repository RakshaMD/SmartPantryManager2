package com.example.pantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuggestedRecipe extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestedrecipe_layout);

        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerRecipes);
        emptyMessage = findViewById(R.id.tvEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBack).setOnClickListener(view -> finish());

        refreshRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (databaseHelper != null) {
            refreshRecipes();
        }
    }

    private void refreshRecipes() {
        List<Recipe> recipes = databaseHelper.getSuggestedRecipes();

        RecipeRecycler adapter = new RecipeRecycler(
                recipes,
                recipe -> {
                    Intent intent = new Intent(this, RecipeDetails.class);
                    intent.putExtra("id", recipe.id);
                    startActivity(intent);
                }
        );

        recyclerView.setAdapter(adapter);

        if (recipes.isEmpty()) {
            emptyMessage.setVisibility(View.VISIBLE);
        } else {
            emptyMessage.setVisibility(View.GONE);
        }
    }
}