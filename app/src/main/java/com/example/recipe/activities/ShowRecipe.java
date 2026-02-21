package com.example.recipe.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe.R;
import com.example.recipe.model.Descriptions;
import com.example.recipe.model.Recipes;
import com.example.recipe.setting.MyApp;
import com.example.recipe.viewmodel.RecipeShowViewModel;

import java.io.File;
import java.util.List;

public class ShowRecipe extends AppCompatActivity {

    private TextView title, ingredientsTitle, instructionTitle;
    private TextView recipeNameTextView, instructionTextView;
    private ImageView imageView;
    private Button editButton;
    private RecyclerView ingredientsRecyclerView;

    private Recipes recipe;
    private RecipeShowViewModel recipeShowViewModel;
    private MyApp myApp;
    private ShowRecipeAdapter ingredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_show);

        initViews();

        recipe = (Recipes) getIntent().getSerializableExtra("Recipe");

        recipeShowViewModel.loadRecipe(recipe);

        // Наблюдаем recipe один раз
        recipeShowViewModel.getRecipe().observe(this, recipes1 -> {
            if (recipes1 == null) return;

            recipe = recipes1;
            showImage(recipe);
            showRecipeContent(recipe);
            editButtonClick(recipe);
            checkLanguage();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        title = findViewById(R.id.YourRecipeTextView);
        ingredientsTitle = findViewById(R.id.TextViewIngredients);
        instructionTitle = findViewById(R.id.TextViewInstruction);
        recipeNameTextView = findViewById(R.id.RecipeTextViewShow);
        instructionTextView = findViewById(R.id.TextViewInstructionContent);
        imageView = findViewById(R.id.RecipeImageView);
        editButton = findViewById(R.id.RecipeEditButton);
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);

        myApp = MyApp.getInstance();

        recipeShowViewModel = new ViewModelProvider(this).get(RecipeShowViewModel.class);

        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsAdapter = new ShowRecipeAdapter(myApp);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
    }

    private void checkLanguage() {
        String lang = myApp.getBaseLanguage();
        if ("Рус".equals(lang)) {
            title.setText("Рецепт");
            ingredientsTitle.setText("Ингредиенты");
            instructionTitle.setText("Инструкция");
        } else if ("Eng".equals(lang)) {
            title.setText("Recipe");
            ingredientsTitle.setText("Ingredients");
            instructionTitle.setText("Instruction");
        } else if ("Каз".equals(lang)) {
            title.setText("Рецепт");
            ingredientsTitle.setText("Құрамы");
            instructionTitle.setText("Нұсқаулық");
        }
    }

    private void showImage(Recipes recipe) {
        try {
            String imagePath = recipe.getImage();
            if (imagePath == null || imagePath.isEmpty()) {
                imageView.setImageResource(R.drawable.steak);
                return;
            }

            File file = new File(imagePath);
            if (file.exists()) {
                Glide.with(this)
                        .load(file)
                        .placeholder(R.drawable.steak)
                        .error(R.drawable.steak)
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.steak);
            }
        } catch (Exception e) {
            Log.e("ShowRecipe", "Ошибка показа изображения", e);
            imageView.setImageResource(R.drawable.steak);
        }
    }

    private void showRecipeContent(Recipes recipe) {
        recipeNameTextView.setText(recipe.getName());
        instructionTextView.setText(recipe.getInsctruction());

        // обновляем ингредиенты
        recipeShowViewModel.refreshDescriptions(recipe);
        recipeShowViewModel.getDesriptions().observe(this, descriptions -> {
            ingredientsAdapter.setData(descriptions);
        });
    }

    private void editButtonClick(Recipes recipe) {
        editButton.setOnClickListener(v -> {
            Intent intent = EditRecipe.getIntent(ShowRecipe.this, recipe);
            startActivity(intent);
            finish();
        });
    }

    public static Intent newIntent(Context context, Recipes recipe) {
        Intent intent = new Intent(context, ShowRecipe.class);
        intent.putExtra("Recipe", recipe);
        return intent;
    }
}