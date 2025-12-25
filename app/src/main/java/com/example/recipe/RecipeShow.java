package com.example.recipe;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeShow extends AppCompatActivity {

    // TextView
    private TextView RecipeTextViewShow;
    private TextView textViewInstructionContent;


    // RecyclerView
    private RecyclerView RecyclerViewRecipes;


    // Adapter
    private RecipeShowAdapter recipeShowAdapter;


    // ViewModel
    private RecipeShowViewModel recipeShowViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_show);

        initVies(); // Инициализация

        showDescription(); // Показ ингридиентов


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initVies(){
        RecipeTextViewShow = findViewById(R.id.RecipeTextViewShow);
        recipeShowViewModel = new ViewModelProvider(this).get(RecipeShowViewModel.class);
        RecyclerViewRecipes = findViewById(R.id.RecyclerViewIngredients);
        recipeShowAdapter = new RecipeShowAdapter();
        textViewInstructionContent = findViewById(R.id.TextViewInstructionContent);
        RecyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this)); // Установка LayoutManager
        RecyclerViewRecipes.setAdapter(recipeShowAdapter); // Установка адаптера
    }

    private void showDescription(){
        long recipe_id = getIntent().getLongExtra("IdRecipe", 0); // Берем рецепт из intent
        recipeShowViewModel.loadRecipe(recipe_id); // Загружаем рецепт по id

        // Возврат рецепта из базы
        recipeShowViewModel.getRecipe().observe(this, new Observer<Recipes>() {
            @Override
            public void onChanged(Recipes recipes) {
                RecipeTextViewShow.setText(recipes.getName()); // Задаем имя рецепта
                textViewInstructionContent.setText(recipes.getInsctruction()); // Задаем его инструкцию
            }
        });

        // Загружаем ингридиенты по id рецепта
        recipeShowViewModel.refreshDescriptions(recipe_id);

        // Возврат коллекции ингридиентов
        recipeShowViewModel.getDesriptions().observe(this, new Observer<List<Descriptions>>() {
            @Override
            public void onChanged(List<Descriptions> descriptions) {
                recipeShowAdapter.setDescriptions(descriptions);
            }
        });

    }


    public static Intent newIntent(Context context, long id_Recipe){
        Intent intent = new Intent(context, RecipeShow.class);
        intent.putExtra("IdRecipe", id_Recipe);
        return intent;
    }
}