package com.example.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeShow extends AppCompatActivity {

    private RecipeDataBase recipeDataBase;
    private AdapterRecipes adapterRecipes;

    private TextView RecipeTextViewShow, RecipeDiscriptionTVShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_show);
        initVies();
        setTitleRecipe();



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initVies(){
        recipeDataBase = RecipeDataBase.getInstance(getApplication());
        adapterRecipes = new AdapterRecipes();
        RecipeTextViewShow = findViewById(R.id.RecipeTextViewShow);
        RecipeDiscriptionTVShow = findViewById(R.id.RecipeDiscriptionTVShow);
    }

    private void setTitleRecipe(){
        int position = getIntent().getIntExtra("IdRecipe", 0);
        Recipes recipe = recipeDataBase.recipesDAO().getRecipe(position);
        RecipeTextViewShow.setText(recipe.getName());
        Log.d("ShowRecipeTest", "name: " + recipe.getName());
        RecipeDiscriptionTVShow.setText(recipe.getDescription());
        Log.d("ShowRecipeTest", "dicription: " + recipe.getDescription());
        Log.d("ShowRecipeTest", "position: " + position);
    }


    public static Intent newIntent(Context context, int id){
        Intent intent = new Intent(context, RecipeShow.class);
        intent.putExtra("IdRecipe", id);
        return intent;
    }
}