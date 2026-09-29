# SmartPantryManager2
Smart Pantry Manager - Mobile App 700 Assignment

Smart Pantry Manager is an Android application designed to help users track pantry ingredients, monitor expiry dates, and generate recipe suggestions based on available items. The app uses an SQLite database for offline storage and provides a clean, intuitive interface for managing household ingredients efficiently.

FEATURES: 
- Add, edit, and delete pantry items
- Tracking quantities, units, and expiry dates
- Suggested Recipes based on strict ingredient matching
- Ingredient normalization for accurate recipe comparison
- SQLite database integration for fast, offline storage
- RecyclerView list display with edit/delete actions
- Settings screen for app preferences
- Responsive UI with banner and footer components

PROJECT STRUCTURE:
app/
 └── src/
     └── main/
         ├── java/com.example.pantrymanager/
         │     ├── MainActivity.java
         │     ├── PantryRecycler.java
         │     ├── DatabaseHelper.java
         │     ├── SuggestedRecipe.java
         │     ├── RecipeDetail.java
         │     ├── PantryItems.java
         │     ├── PantrySettings.java
         │     ├── Recipe.java
         │     ├── RecipeRecycler.java
         │     └── UpdateIngredients.java
         │
         ├── res/
         │     ├── layout/
         │     │     ├── activity_main.xml
         │     │     ├── addingredient_layout.xml
         │     │     ├── banner.xml
         │     │     ├── footer.xml
         │     │     ├── pantryitem_layout.xml
         │     │     ├── recipedetail_layout.xml
         │     │     ├── recipematch_layout.xml
         │     │     ├── settings_layout.xml
         │     │     └── suggestedrecipe_layoutr.xml
         │     ├── drawable/
         │     │     ├── banner.png
         │     │     ├── buttons.xml
         │     │     ├── deleteicon.png
         │     │     ├── editicon.png
         │     │     └── footer_border.xml
         │     ├── values/
         │     │     ├── colors.xml
         │     │     ├── string.xml
         │     │     └── style.xml
         │
         └── AndroidManifest.xml

DATABASE DESIGN:
SQLite is used as the local database.

---Pantry Table---
Column	  Type	   Description
id	      INTEGER 	Primary key
name	    TEXT  	 Ingredient name
quantity	REAL	   Numeric quantity
unit	    TEXT	   Measurement unit
expiry	  TEXT	   Expiry date (YYYY-MM-DD)

---Recipes Tables---
Column	     Type	    Description
id	         INTEGER 	Primary key
name	       TEXT  	  Recipe name
ingredients  TEXT  	  List of Ingredients
method       TEXT  	  Recipe method


KEY COMPONENTS:
1. RecyclerView + Adapter
Displays pantry items dynamically using the below 2 files and handles edit/delete actions per item.
- PantryRecycler.java
- pantryitem_layout.xml

2. SQLite Database
DatabaseConnecter.java manages the table creation, insert/update/delete, recipe matching, returning suggested recipes

3. Ingredient Normalization
Ensures accurate recipe matching by converting plurals to singular forms.

4. Intents Between Screens
Used for navigation to Add Ingredient, Edit Ingredient, Suggested Recipes, Recipe Details


INSTALLATION  & SETUP
- Clone the git repository: git clone https://github.com/RakshaMD/SmartPantryManager2.git
- Open the project in Android Studio.
- Sync Gradle.
- Run on an emulator or physical device.

TESTING:
The app was tested for UI responsiveness, database accuracy, correct recipe matching, safe handling of null/empty fields, scrolling and layout behavior

CREATED BY:
Raksha Manilal Daya
Student number: 402308778


         
