package com.example.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeShow extends AppCompatActivity {

    // TextView
    private TextView RecipeTextViewShow;
    private TextView textViewInstructionContent;


    // LinearLayout
    private LinearLayout linearLayout;



    // ViewModel
    private RecipeShowViewModel recipeShowViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_show);

        initVies(); // Инициализация

        showRecipe(); // Показ ингридиентов



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initVies(){
        RecipeTextViewShow = findViewById(R.id.RecipeTextViewShow);
        recipeShowViewModel = new ViewModelProvider(this).get(RecipeShowViewModel.class);
        textViewInstructionContent = findViewById(R.id.TextViewInstructionContent);
        linearLayout = findViewById(R.id.ingredientsContainer);
    }

    private void showRecipe() {
        long recipe_id = getIntent().getLongExtra("IdRecipe", 0);
        recipeShowViewModel.loadRecipe(recipe_id);
        recipeShowViewModel.getRecipe().observe(this, new Observer<Recipes>() {
            @Override
            public void onChanged(Recipes recipes) {
                RecipeTextViewShow.setText(recipes.getName());
                textViewInstructionContent.setText(recipes.getInsctruction());
            }
        });
        recipeShowViewModel.refreshDescriptions(recipe_id);

        recipeShowViewModel.getDesriptions().observe(this, new Observer<List<Descriptions>>() {
            @Override
            public void onChanged(List<Descriptions> descriptions) {
                // очищаем контейнер, чтобы не дублировать элементы при обновлении
                linearLayout.removeAllViews();

                for (Descriptions x : descriptions) {
                    View view = LayoutInflater.from(RecipeShow.this).inflate(
                            R.layout.show_ingredient_item,
                            linearLayout,
                            false
                    );

                    TextView nameIngredient = view.findViewById(R.id.ingredientText);
                    TextView weightIngredient = view.findViewById(R.id.ingredientWeight);
                    TextView unitIngredient = view.findViewById(R.id.ingredientWeightType);

                    nameIngredient.setText(x.getName());
                    float weight = x.getWeight();
                    Log.d("testTest", ""+weight);
                    String unit = "g";

                    if (weight >= 1000) {
                        weight = weight / 1000;
                        unit = "kg";
                    }

                    weightIngredient.setText(String.valueOf(weight));
                    unitIngredient.setText(unit);

                    // вот этого не хватало!
                    linearLayout.addView(view);
                }
            }
        });
    }

    public static Intent newIntent(Context context, long id_Recipe){
        Intent intent = new Intent(context, RecipeShow.class);
        intent.putExtra("IdRecipe", id_Recipe);
        return intent;
    }
}