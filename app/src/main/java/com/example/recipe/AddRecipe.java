package com.example.recipe;
// cd /d/1AndroidStudioProjects/Recipe
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddRecipe extends AppCompatActivity {

    private TextView TextViewDescription;
    private EditText EditTextRecipe, edit_recipe_description;
    private Button RecipeSaveButton;

    private Spinner SpinnerWeight;

    private AddRecipeModelView addRecipeModelView;

    private LinearLayout linearLayoutDescription;

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe);

        InitViews(); // Инициализация

        FloatingClickButton(); // Нажатие кнопки добавления состава

        SaveButtonClick(); // Нажатие кнопки сохранения рецепта

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void InitViews() {
        SpinnerWeight = findViewById(R.id.SpinnerWeight);
        TextViewDescription = findViewById(R.id.TextViewDescription);
        EditTextRecipe = findViewById(R.id.EditTextRecipe);
        edit_recipe_description = findViewById(R.id.EditTextIngredient);
        RecipeSaveButton = findViewById(R.id.RecipeSaveButton);
        linearLayoutDescription = findViewById(R.id.linearLayoutDescription);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        addRecipeModelView = new ViewModelProvider(this).get(AddRecipeModelView.class);
    }

    private void FloatingClickButton(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescriptions(); // просто добавляет поля на экран
            }
        });
    }

    private void SaveButtonClick(){
        RecipeSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FieldCheck(); // пока сохраняет только название и описание рецепта
            }
        });
    }
    private void setRecipe() {
        String text = EditTextRecipe.getText().toString().trim();
        String description = edit_recipe_description.getText().toString();
        if (!text.isEmpty() && !description.isEmpty()) {
            Recipes recipes = new Recipes(text, description);
            addRecipeModelView.addRecipe(recipes);
            Log.d("DB_TEST", "Рецепт добавлен в базу: " + description);
        }
    }

    private void FieldCheck() {
        if (!EditTextRecipe.getText().toString().isEmpty() &&
                !edit_recipe_description.getText().toString().isEmpty()) {
            setRecipe();
            addRecipeModelView.getShouldCloseScreen().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean close) {
                    if(close){
                        finish();
                    }
                }
            });
        } else if (EditTextRecipe.getText().toString().isEmpty() &&
                edit_recipe_description.getText().toString().isEmpty()) {
            Toast.makeText(AddRecipe.this, "Введите все данные", Toast.LENGTH_SHORT).show();
        } else if (EditTextRecipe.getText().toString().isEmpty()) {
            Toast.makeText(AddRecipe.this, "Введите название рецепта", Toast.LENGTH_SHORT).show();
        } else if (edit_recipe_description.getText().toString().isEmpty()) {
            Toast.makeText(AddRecipe.this, "Введите состав", Toast.LENGTH_SHORT).show();
        }
    }


    private void showDescriptions() {
        View view = getLayoutInflater().inflate(R.layout.description_item, linearLayoutDescription, false);
        linearLayoutDescription.addView(view); // просто добавляет блок на экран
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddRecipe.class);
    }
}