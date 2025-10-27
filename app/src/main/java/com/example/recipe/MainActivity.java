package com.example.recipe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private RecyclerView RecyclerViewRecipes;
    private AdapterRecipes adapterRecipes;

    private RecipeDataBase recipeDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initVies();
        RecyclerViewRecipes.setAdapter(adapterRecipes);
        showRecipes();
        onClickFloatingButton();
        adapterRecipes.setRecipeOnClickListener(new AdapterRecipes.RecipeOnClickListener() {
            @Override
            public void OnClickRecipe(Recipes recipe) {
                int idRecipe = recipe.getId();
                Intent intent = RecipeShow.newIntent(MainActivity.this, idRecipe);
                startActivity(intent);
            }
        });

        // Swipe
        {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Удалить рецепт")
                            .setMessage("Вы уверены, что хотите удалить этот рецепт?")
                            .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = viewHolder.getAdapterPosition();
                                    Recipes recipe = adapterRecipes.getRecipeOnClickListener().get(position);
                                    recipeDataBase.recipesDAO().remove(recipe.getId());
                                    showRecipes();
                                }
                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = viewHolder.getAdapterPosition();
                                    adapterRecipes.notifyItemChanged(position);
                                }
                            })
                            .show();
                }
            });
            itemTouchHelper.attachToRecyclerView(RecyclerViewRecipes);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initVies(){
        RecyclerViewRecipes = findViewById(R.id.RecyclerViewRecipes);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        adapterRecipes = new AdapterRecipes();
        recipeDataBase = RecipeDataBase.getInstance(getApplication());
    }

    private void showRecipes(){
        adapterRecipes.setRecipes(recipeDataBase.recipesDAO().getRecipes());
    }


    private void onClickFloatingButton(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddRecipe.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }



    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}