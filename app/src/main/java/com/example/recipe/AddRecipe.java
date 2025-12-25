package com.example.recipe;

import android.annotation.SuppressLint;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddRecipe extends AppCompatActivity {

    private static final String TAG = "AddRecipe1";


    // EditText
    private EditText editTextRecipe;
    private EditText editTextInsctructionRecipe;


    // Button
    private Button RecipeSaveButton;


    // FloatingActionButton
    private FloatingActionButton floatingActionButton;

    // LinearLayout
    private LinearLayout linearLayoutDescription;


    // Spinner
    private Spinner SpinnerUnit;


    // ViewModel
    private AddRecipeModelView addRecipeModelView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe);

        InitViews(); // Инициализация

        FloatingClickButton(); // Добавление поле для ингридиента

        SaveButtonClick(); // Нажатие кнопки сохранения рецепта

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void InitViews() {
        editTextRecipe = findViewById(R.id.EditTextRecipe);
        RecipeSaveButton = findViewById(R.id.RecipeSaveButton);
        linearLayoutDescription = findViewById(R.id.linearLayoutDescription);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        addRecipeModelView = new ViewModelProvider(this).get(AddRecipeModelView.class);
        editTextInsctructionRecipe = findViewById(R.id.EditTextInsctructionRecipe);
        SpinnerUnit = findViewById(R.id.SpinnerUnit);
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
        test.setId(View.generateViewId());

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

                        if(spinnerValue.equals("kg")){
                            weight = weight * 1000f;
                        } else
                            if(spinnerValue.equals("lb")){
                                weight = weight * 453.59237f;
                            }
                        Descriptions descriptions = new Descriptions(recipeId, name, weight);
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