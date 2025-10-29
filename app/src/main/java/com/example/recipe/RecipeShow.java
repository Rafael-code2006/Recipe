package com.example.recipe;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeShow extends AppCompatActivity {

    private RecipeDataBase recipeDataBase;

    private TextView RecipeTextViewShow, RecipeDiscriptionTVShow;

    private ScrollView ScrollDiscription;

    private Button RecipeEditButton, RecipeSaveButton;

    private EditText EditRecipeDiscription, EditRecipeShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_show);
        initVies();
        setTitleRecipe();
        onClickEditButton();
        onClickSaveButton();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initVies(){
        recipeDataBase = RecipeDataBase.getInstance(getApplication());
        ScrollDiscription = findViewById(R.id.ScrollDiscription);
        RecipeTextViewShow = findViewById(R.id.RecipeTextViewShow);
        RecipeDiscriptionTVShow = findViewById(R.id.RecipeDiscriptionTVShow);
        RecipeEditButton = findViewById(R.id.RecipeEditButton);
        RecipeSaveButton = findViewById(R.id.RecipeSaveButton);
        EditRecipeDiscription = findViewById(R.id.EditRecipeDiscription);
        RecipeSaveButton = findViewById(R.id.RecipeSaveButton);
        EditRecipeShow = findViewById(R.id.EditRecipeShow);
    }

    private void setTitleRecipe(){
        Recipes recipe = getRecipeById();
        RecipeTextViewShow.setText(recipe.getName());
        Log.d("ShowRecipeTest", "name: " + recipe.getName());
        RecipeDiscriptionTVShow.setText(recipe.getDescription());
        Log.d("ShowRecipeTest", "dicription: " + recipe.getDescription());
    }

    private void onClickEditButton(){
        RecipeEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SetVisibility для view обьектов
                {
                    ScrollDiscription.setVisibility(INVISIBLE);
                    RecipeTextViewShow.setVisibility(INVISIBLE);
                    EditRecipeDiscription.setVisibility(VISIBLE);
                    RecipeEditButton.setVisibility(INVISIBLE);
                    EditRecipeShow.setVisibility(VISIBLE);
                    RecipeSaveButton.setVisibility(VISIBLE);
                }

                Recipes recipe = getRecipeById();
                EditRecipeShow.setText(recipe.getName());
                EditRecipeDiscription.setText(recipe.getDescription());
                Log.d("RafaTest", recipe.getName() + "   " + recipe.getDescription());
            }
        });
    }

    private void onClickSaveButton(){

        // SetVisibility для view обьектов
        {
            ScrollDiscription.setVisibility(VISIBLE);
            RecipeTextViewShow.setVisibility(VISIBLE);
            EditRecipeDiscription.setVisibility(INVISIBLE);
            RecipeEditButton.setVisibility(VISIBLE);
            EditRecipeShow.setVisibility(INVISIBLE);
            RecipeSaveButton.setVisibility(INVISIBLE);
        }
        RecipeSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = EditRecipeShow.getText().toString().trim();
                String description = EditRecipeDiscription.getText().toString();
                int position = getIntent().getIntExtra("IdRecipe", 0);
                recipeDataBase.recipesDAO().changeRecipe(position, name, description);
                Intent intent = MainActivity.newIntent(RecipeShow.this);
                startActivity(intent);
            }
        });
    }


    private Recipes getRecipeById(){
        int position = getIntent().getIntExtra("IdRecipe", 0);
        Recipes recipe = recipeDataBase.recipesDAO().getRecipe(position);
        if(recipe != null) {
            return recipe;
        }
        return null;
    }


    public static Intent newIntent(Context context, int id){
        Intent intent = new Intent(context, RecipeShow.class);
        intent.putExtra("IdRecipe", id);
        return intent;
    }
}