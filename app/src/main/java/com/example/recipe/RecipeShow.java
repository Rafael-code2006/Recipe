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

import java.util.ArrayList;
import java.util.List;

public class RecipeShow extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());

    private RecipeDataBase recipeDataBase;

    private TextView RecipeTextViewShow;


    private Button RecipeEditButton, RecipeSaveButton;

    private LinearLayout linearLayoutDescription;

    private TextView TextViewName_descr, TextViewWeight, TextViewType;

    private RecipeShowViewModel recipeShowViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_show);
        initVies(); // Инициализация
        setTitleRecipe(); //


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initVies(){
        recipeDataBase = RecipeDataBase.getInstance(getApplication());
        RecipeTextViewShow = findViewById(R.id.RecipeTextViewShow);
        RecipeEditButton = findViewById(R.id.RecipeEditButton);
        RecipeSaveButton = findViewById(R.id.RecipeSaveButton);
        RecipeSaveButton = findViewById(R.id.RecipeSaveButton);
        linearLayoutDescription = findViewById(R.id.LinearLayoutShowRecipes);
        recipeShowViewModel = new ViewModelProvider(this).get(RecipeShowViewModel.class);
    }




    private void setTitleRecipe() {
        recipeShowViewModel.refreshRecipes();
        recipeShowViewModel.getDesriptions();
        int position = getIntent().getIntExtra("IdRecipe", 0);
        recipeShowViewModel.getRecipes().observe(this, new Observer<List<Recipes>>() {
            @Override
            public void onChanged(List<Recipes> recipes) {
                for(Recipes obj : recipes){
                    if(obj.getId() == position){
                        RecipeTextViewShow.setText(obj.getName());
                        recipeShowViewModel.getDesriptions().observe(RecipeShow.this, new Observer<List<Descriptions>>() {
                            @Override
                            public void onChanged(List<Descriptions> descriptions) {
                                for(Descriptions objDesc : descriptions) {
                                    if (obj.getId_description() == objDesc.getId_description()){
                                        showDescriptions(
                                                objDesc.getName_description(),
                                                objDesc.getWeight());
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    public void getRecipeByIdAsync(int id, RecipeCallback callback) {
        new Thread(() -> {
            Recipes recipe = recipeDataBase.recipesDAO().getRecipe(id);
            handler.post(() -> {
                if (recipe != null) {
                    callback.onRecipeLoaded(recipe);
                } else {
                    callback.onRecipeLoaded(null); // или обработай ошибку
                }
            });
        }).start();
    }

    @SuppressLint("MissingInflatedId")
    private void showDescriptions(
            String name_description,
            float weight
    ){
        View view = getLayoutInflater().inflate(R.layout.descriptions_show_item, linearLayoutDescription, false);
        TextViewName_descr = view.findViewById(R.id.TextViewName_descr);
        TextViewName_descr.setText(name_description);
        TextViewWeight = view.findViewById(R.id.TextViewWeight);
        TextViewWeight.setText(String.valueOf(weight));
        TextViewType = view.findViewById(R.id.TextViewType);
        linearLayoutDescription.addView(view);
    }


    public static Intent newIntent(Context context, int id_Recipe){
        Intent intent = new Intent(context, RecipeShow.class);
        intent.putExtra("IdRecipe", id_Recipe);
        return intent;
    }
}