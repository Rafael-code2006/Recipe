package com.example.recipe.activities;

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

import com.example.recipe.setting.MyApp;
import com.example.recipe.viewmodel.AddRecipeModelView;
import com.example.recipe.model.Descriptions;
import com.example.recipe.R;
import com.example.recipe.model.Recipes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AddRecipe extends AppCompatActivity {

    private static final String TAG = "AddRecipe1";

    private TextView title;
    private TextView nameRecipeTitle;
    private TextView instructionTitle;


    // EditText
    private EditText editTextRecipe;
    private EditText editTextInsctructionRecipe;


    // Button
    private Button RecipeSaveButton;


    // FloatingActionButton
    private FloatingActionButton floatingActionButton;

    // LinearLayout
    private LinearLayout linearLayoutDescription;


    // ViewModel
    private AddRecipeModelView addRecipeModelView;

    private MyApp myApp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe);

        InitViews(); // Инициализация

        changeLanguage();

        FloatingClickButton(); // Добавление поле для ингридиента

        SaveButtonClick(); // Нажатие кнопки сохранения рецепта

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void changeLanguage() {
        if(myApp.getBaseLanguage().equals("Рус")){
            title.setText("Добавить Рецепт");
            nameRecipeTitle.setText("Имя рецепта:");
            instructionTitle.setText("Инструкция:");
            editTextRecipe.setHint("имя");
            editTextInsctructionRecipe.setHint("инструкция");
        }
        if(myApp.getBaseLanguage().equals("Eng")){
            title.setText("Add Recipe");
            nameRecipeTitle.setText("Recipe name");
            instructionTitle.setText("Insctruction");
            editTextRecipe.setHint("name");
            editTextInsctructionRecipe.setHint("instruction");
        }
        if(myApp.getBaseLanguage().equals("Каз")){
            title.setText("Рецепт Қосыңыз");
            nameRecipeTitle.setText("Рецепт атауы");
            instructionTitle.setText("Нұсқаулық");
            editTextRecipe.setHint("атауы");
            editTextInsctructionRecipe.setHint("нұсқаулық");
        }
    }


    private void InitViews() {
        editTextRecipe = findViewById(R.id.EditTextRecipe);
        RecipeSaveButton = findViewById(R.id.RecipeSaveButton);
        linearLayoutDescription = findViewById(R.id.linearLayoutDescription);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        addRecipeModelView = new ViewModelProvider(this).get(AddRecipeModelView.class);
        editTextInsctructionRecipe = findViewById(R.id.EditTextInsctructionRecipe);
        myApp = MyApp.getInstance();
        title = findViewById(R.id.CreateRecipeTextView);
        nameRecipeTitle = findViewById(R.id.TextViewRecipeName);
        instructionTitle = findViewById(R.id.TextViewInstructionRecipe);

    }

    private void FloatingClickButton(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescriptions();
            }
        });
    }

    private void showDescriptions() {
        View view = getLayoutInflater().inflate(R.layout.ingredient_item, linearLayoutDescription, false);
        Button test = view.findViewById(R.id.ButtonDelete);
        Spinner spinnerUnit = view.findViewById(R.id.SpinnerUnit);
        TextView UnitTextView = view.findViewById(R.id.TextViewUnit);
        test.setId(View.generateViewId());

        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unit = parent.getSelectedItem().toString();
                UnitTextView.setText(unit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        test.setOnClickListener(v -> {
            int clickedId = v.getId();
            linearLayoutDescription.removeView((View) v.getParent());
        });

        linearLayoutDescription.addView(view);
    }


    private void SaveButtonClick(){
        RecipeSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextRecipe.getText().toString().isEmpty()){
                    Toast.makeText(AddRecipe.this, "Введите название рецепта", Toast.LENGTH_SHORT).show();
                } else{
                    setRecipe();
                   Intent intent = MainActivity.getIntent(AddRecipe.this);
                   startActivity(intent);
                }
            }
        });
    }

    private void setRecipe() {

        // Создание рецепта
        String text = editTextRecipe.getText().toString().trim();
        String insctruction = editTextInsctructionRecipe.getText().toString();
        Recipes recipe = new Recipes(text, insctruction);

        addRecipeModelView.addRecipe(recipe);


        addRecipeModelView.getIdRecipes().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long recipeId) {
                if (recipeId == null) return;

                List<Descriptions> listDescriptions = new ArrayList<>();


                // Добавление ингридиентов из LinearLayout
                for (int i = 0; i < linearLayoutDescription.getChildCount(); i++) {
                    View item = linearLayoutDescription.getChildAt(i);

                    EditText nameIngredient = item.findViewById(R.id.EditTextNameIngredient);
                    EditText weightIngredient = item.findViewById(R.id.EditTextWeight);
                    Spinner spinnerUnit = item.findViewById(R.id.SpinnerUnit);

                    String name = nameIngredient.getText().toString().trim();
                    String weightText = weightIngredient.getText().toString().trim();

                    if (name.isEmpty() || weightText.isEmpty()) continue;

                    try {
                        float weight = Float.parseFloat(weightText);
                        String spinnerValue = spinnerUnit.getSelectedItem().toString();

                        Descriptions descriptions = new Descriptions(recipeId, name, weight, spinnerValue);
                        addRecipeModelView.addDescription(descriptions);
                        listDescriptions.add(descriptions);
                        Log.d("AddRecipe1", "Ингредиент: " + name + ", вес: " + weight + " добавлен");
                    } catch (NumberFormatException e) {
                        Toast.makeText(AddRecipe.this, "Ошибка формата веса", Toast.LENGTH_SHORT).show();
                    }
                }

                if (!listDescriptions.isEmpty()) {
                    int count_ingredient = 0;
                    for(Descriptions x : listDescriptions){
                        count_ingredient += 1;
                    }
                    recipe.setIngredient_count(count_ingredient);
                }

                // чтобы не подписываться бесконечно
                addRecipeModelView.getIdRecipes().removeObserver(this);
            }
        });
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, AddRecipe.class);
    }


}