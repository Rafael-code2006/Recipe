package com.example.recipe.activities;

import static android.view.View.INVISIBLE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipe.model.Descriptions;
import com.example.recipe.R;
import com.example.recipe.setting.MyApp;
import com.example.recipe.viewmodel.RecipeShowViewModel;
import com.example.recipe.model.Recipes;

import java.util.Arrays;
import java.util.List;

public class ShowRecipe extends AppCompatActivity {

    // TextView
    private TextView RecipeTextViewShow;
    private TextView textViewInstructionContent;

    private TextView title;
    private TextView ingredientsTitle;
    private TextView instructionTitle;


    // Button
    private Button EditButton;


    // LinearLayout
    private LinearLayout linearLayout;



    // ViewModel
    private RecipeShowViewModel recipeShowViewModel;
    private View textViewIngredients;

    private MyApp myApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_show);

        Recipes recipes = (Recipes) getIntent().getSerializableExtra("Recipe");
        initVies(); // Инициализация

        showRecipe(recipes.getId()); // Показ ингридиентов

        EditButtonClick(recipes);// Слушатель клика на редактирование

        if(myApp.getBaseLanguage().equals("Рус")){
            title.setText("Рецепт");
            ingredientsTitle.setText("Ингредиенты");
            instructionTitle.setText("Инструкция");
        }
        if(myApp.getBaseLanguage().equals("Eng")){
            title.setText("Recipe");
            ingredientsTitle.setText("Ingredients");
            instructionTitle.setText("Instruction");
        }
        if(myApp.getBaseLanguage().equals("Каз")){
            title.setText("Рецепт");
            ingredientsTitle.setText("Құрамы");
            instructionTitle.setText("Нұсқаулық");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initVies(){
        RecipeTextViewShow = findViewById(R.id.RecipeTextViewShow);
        textViewIngredients = findViewById(R.id.TextViewIngredients);
        EditButton = findViewById(R.id.RecipeEditButton);
        recipeShowViewModel = new ViewModelProvider(this).get(RecipeShowViewModel.class);
        textViewInstructionContent = findViewById(R.id.TextViewInstructionContent);
        linearLayout = findViewById(R.id.ingredientsContainer);
        myApp = MyApp.getInstance();
        title = findViewById(R.id.YourRecipeTextView);
        ingredientsTitle = findViewById(R.id.TextViewIngredients);
        instructionTitle = findViewById(R.id.TextViewInstruction);

    }

    private void showRecipe(long recipe_id) {
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

                if(descriptions.size() == 0){
                    textViewIngredients.setVisibility(INVISIBLE);
                }
                // очищаем контейнер, чтобы не дублировать элементы при обновлении
                linearLayout.removeAllViews();

                for (Descriptions x : descriptions) {
                    View view = LayoutInflater.from(ShowRecipe.this).inflate(
                            R.layout.show_ingredient_item,
                            linearLayout,
                            false
                    );

                    TextView nameIngredient = view.findViewById(R.id.ingredientText);
                    TextView weightIngredient = view.findViewById(R.id.ingredientWeight);
                    TextView unitIngredient = view.findViewById(R.id.ingredientWeightType);


                    List<String> units = myApp.unit();

                    String unit = x.getUnit();
                    unit = checkLanguage(unit, units);

                    Log.d("RecipeShow11", "unit:" + unit);

                    nameIngredient.setText(x.getName());
                    float weight = x.getWeight();

                    weightIngredient.setText(String.valueOf(weight));
                    unitIngredient.setText(unit);

                    
                    linearLayout.addView(view);
                }
            }
        });
    }

    private String checkLanguage(String unit, List<String> units) {
        for(int i=0; i<3; i++){
            List<String> current;
            if(i == 0){
                current = myApp.getRusUnit();
            } else if(i == 1){
                current = myApp.getEngUnit();
            } else{
                current = myApp.getKazUnit();
            }
            for(int j=0; j < current.size(); j++){
                if(unit.equals(current.get(j))){
                    unit = units.get(j);
                }
            }
        }
        return unit;
    }

    private void EditButtonClick(Recipes recipe){
        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EditRecipe.getIntent(ShowRecipe.this, recipe);
                startActivity(intent);
                finish();
            }
        });
    }

    public static Intent newIntent(Context context, Recipes recipe){
        Intent intent = new Intent(context, ShowRecipe.class);
        intent.putExtra("Recipe", recipe);
        return intent;
    }
}