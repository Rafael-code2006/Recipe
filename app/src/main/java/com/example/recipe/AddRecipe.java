package com.example.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddRecipe extends AppCompatActivity {

    private EditText EditTextRecipe, edit_recipe_description;
    private Button RecipeSaveButton;

    private RecipeDataBase recipeDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe);
        InitVies();

        RecipeSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FieldCheck();
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void setRecipe(){
        String text = EditTextRecipe.getText().toString().trim();
        String description = edit_recipe_description.getText().toString();
        if(!text.isEmpty() && !description.isEmpty()) {
            Recipes recipes = new Recipes(text, description);
            recipeDataBase.recipesDAO().add(recipes);
            Log.d("DB_TEST", "Рецепт добавлен в базу :" + description);
        }
        }
        private void FieldCheck(){
            if(!EditTextRecipe.getText().toString().isEmpty() && !edit_recipe_description.getText().toString().isEmpty()) {
                setRecipe();
                Intent intent = MainActivity.newIntent(AddRecipe.this);
                startActivity(intent);
            }else
            if(EditTextRecipe.getText().toString().isEmpty() && edit_recipe_description.getText().toString().isEmpty()){
                Toast.makeText(AddRecipe.this, "Введите все данные", Toast.LENGTH_SHORT).show();
            }
            else
            if(EditTextRecipe.getText().toString().isEmpty()){
                Toast.makeText(AddRecipe.this, "Введите название рецепта", Toast.LENGTH_SHORT).show();
            }
            else
            if(edit_recipe_description.getText().toString().isEmpty()){
                Toast.makeText(AddRecipe.this, "Введите состав", Toast.LENGTH_SHORT).show();
            }
        }

    private void InitVies(){
        EditTextRecipe = findViewById(R.id.EditTextRecipe);
        edit_recipe_description = findViewById(R.id.edit_recipe_description);
        RecipeSaveButton = findViewById(R.id.RecipeSaveButton);
        recipeDataBase = RecipeDataBase.getInstance(getApplication());
    }
    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, AddRecipe.class);
        return intent;
    }
}