package com.example.pantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry2.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE pantry (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "quantity REAL NOT NULL," +
                        "unit TEXT NOT NULL," +
                        "expiry TEXT)"
        );

        db.execSQL(
                "CREATE TABLE recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "ingredients TEXT NOT NULL," +
                        "method TEXT NOT NULL)"
        );

        defaultRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not needed for this simple student project.
    }

    private void defaultRecipes(SQLiteDatabase db) {
        String[][] recipes = {
                {"Tomato Sauce Pasta", "200 g pasta;2 tomatoes;1 garlic clove;10 ml olive oil",
                        "Boil the pasta. Cook chopped tomatoes and garlic in olive oil. Toss everything together."},
                {"Creamy Sauce Pasta", "200 g pasta;20 g butter;20 g flour;250 ml milk",
                        "Melt butter and add flour. Add milk and cook till thick. Boil pasta and mix. Season."},
                {"Omelette", "2 eggs;1 tomato;20 g onion;30 g cheese",
                        "Beat eggs. Cook onion and tomato. Add eggs and cheese and fold."},
                {"French Toast", "2 bread slices;1 egg;50 ml milk;5 g sugar",
                        "Whisk egg, milk and sugar. Dip bread and fry until golden."},
                {"Garlic Bread", "2 bread slices;1 garlic clove;20 g butter",
                        "Mix crushed garlic with butter. Spread on bread and toast."},
                {"Toasted Cheese and Tomato", "2 bread slices;30 g cheese;1 tomato;20 g butter",
                        "Spread butter, add cheese and tomato, toast on pan."},
                {"Tuna Sandwich", "1 can tuna;2 bread slices;20 g mayonnaise;1 tomato",
                        "Mix tuna and mayonnaise. Spread on bread and add tomato."},
                {"Chicken Mayo Sandwich", "100 g chicken;2 bread slices;1 tomato;20 g mayonnaise",
                        "Cook chicken. Add chicken, tomato and mayonnaise between bread slices."},
                {"Pancakes", "100 g flour;1 egg;150 ml milk;10 g sugar",
                        "Mix into batter. Cook small portions in greased pan."},
                {"Scrambled Eggs", "2 eggs;10 g butter",
                        "Melt butter. Add beaten eggs and stir until cooked."},
                {"Greek Salad", "100 g lettuce;1 tomato;1 cucumber;20 g feta;10 ml olive oil",
                        "Cut ingredients, toss, dress with olive oil, season."},
                {"Chickpea Salad", "1 tin chickpeas;1 cucumber;20 g feta;10 ml olive oil",
                        "Drain chickpeas, dice cucumber and feta, toss and dress."},
                {"Egg Fried Rice", "2 eggs;150 g rice;50 g onion;10 ml olive oil",
                        "Cook onion, add rice and eggs, stir-fry."},
                {"Pizza Toast", "2 bread slices;20 g tomato sauce;20 g cheese;1 garlic clove",
                        "Top toasted bread with sauce, garlic and cheese, grill."},
                {"Vegetable Fried Rice", "150 g rice;1 tomato;50 g onion;1 garlic clove;10 ml olive oil",
                        "Cook rice. Fry vegetables and garlic, mix with rice."}
        };

        for (String[] recipe : recipes) {
            ContentValues values = new ContentValues();
            values.put("name", recipe[0]);
            values.put("ingredients", recipe[1]);
            values.put("method", recipe[2]);
            db.insert("recipes", null, values);
        }
    }

    // Pantry CRUD

    public void insertPantry(PantryItems item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.name);
        values.put("quantity", item.quantity);
        values.put("unit", item.unit);
        values.put("expiry", item.expiry);
        db.insert("pantry", null, values);
    }

    public List<PantryItems> getPantry() {
        List<PantryItems> pantry = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id,name,quantity,unit,expiry FROM pantry ORDER BY name",
                null
        );

        while (cursor.moveToNext()) {
            pantry.add(new PantryItems(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
            ));
        }
        cursor.close();
        return pantry;
    }

    public PantryItems getPantryItem(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id,name,quantity,unit,expiry FROM pantry WHERE id=?",
                new String[]{String.valueOf(id)}
        );

        PantryItems item = null;
        if (cursor.moveToFirst()) {
            item = new PantryItems(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        }
        cursor.close();
        return item;
    }

    public void updatePantry(PantryItems item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", item.name);
        values.put("quantity", item.quantity);
        values.put("unit", item.unit);
        values.put("expiry", item.expiry);
        db.update("pantry", values, "id=?", new String[]{String.valueOf(item.id)});
    }

    public void deletePantry(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("pantry", "id=?", new String[]{String.valueOf(id)});
    }

    // Recipes

    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id,name,ingredients,method FROM recipes ORDER BY name",
                null
        );

        while (cursor.moveToNext()) {
            recipes.add(new Recipe(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            ));
        }
        cursor.close();
        return recipes;
    }

    public List<Recipe> getSuggestedRecipes() {

        Map<String, Double> pantryQuantities = new HashMap<>();

        // Building the pantry mapping
        for (PantryItems item : getPantry()) {
            String ingredient = normalize(item.name);
            Double current = pantryQuantities.get(ingredient);
            if (current == null) current = 0.0;
            pantryQuantities.put(ingredient, current + item.quantity);
        }

        List<Recipe> matches = new ArrayList<>();

        for (Recipe recipe : getRecipes()) {
            boolean recipeMatches = true;
            String[] requiredIngredients = recipe.ingredients.split(";");

            for (String requirement : requiredIngredients) {
                requirement = requirement.trim();
                String[] parts = requirement.split(" ");
                if (parts.length < 2) {
                    recipeMatches = false;
                    break;
                }

                // extracting the exact quantity required by recipe
                double requiredQuantity;

                try {
                    requiredQuantity = Double.parseDouble(parts[0]);
                } catch (NumberFormatException e) {
                    recipeMatches = false;
                    break;
                }

                // Extracting ingredient name properly
                String ingredientName;

                // If second word is a unit → use third word
                if (parts[1].equals("g") ||
                        parts[1].equals("ml") ||
                        parts[1].equals("slice") ||
                        parts[1].equals("slices") ||
                        parts[1].equals("clove") ||
                        parts[1].equals("tin") ||
                        parts[1].equals("can")) {

                    if (parts.length < 3) {
                        recipeMatches = false;
                        break;
                    }
                    ingredientName = parts[2];

                } else {
                    // Otherwise second word is the ingredient
                    ingredientName = parts[1];
                }

                String key = normalize(ingredientName);

                // Ingredient must exist
                if (!pantryQuantities.containsKey(key)) {
                    recipeMatches = false;
                    break;
                }

                // Quantity must be enough
                Double availableBoxed = pantryQuantities.get(key);
                double available = availableBoxed != null ? availableBoxed : 0.0;
                if (available < requiredQuantity) {
                    recipeMatches = false;
                    break;
                }
            }

            if (recipeMatches) {
                matches.add(recipe);
            }
        }

        return matches;
    }

    public static String normalize(String value) {
        value = value.toLowerCase(Locale.US).trim();

        if (value.endsWith("ies")) {
            value = value.substring(0, value.length() - 3) + "y";
        } else {
            String substring = value.substring(0, value.length() - 2);
            if (value.endsWith("oes")) {
                value = substring;
            } else if (value.endsWith("es") && value.length() > 4) {
                value = substring;
            } else if (value.endsWith("s") && !value.endsWith("ss")) {
                value = value.substring(0, value.length() - 1);
            }
        }

        return value.replaceAll("\\s+", " ");
    }
}