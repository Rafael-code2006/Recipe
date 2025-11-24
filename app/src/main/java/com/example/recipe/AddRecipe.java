package com.example.recipe;

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

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddRecipe extends AppCompatActivity {

    private EditText editTextRecipe, edit_recipe_description;
    private Button RecipeSaveButton;

    private AddRecipeModelView addRecipeModelView;

    private LinearLayout linearLayoutDescription;

    private FloatingActionButton floatingActionButton;


    private EditText editTextIngredient;
    private Spinner spinnerWeight;
    private EditText editTextWeight;



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
        editTextRecipe = findViewById(R.id.EditTextRecipe);
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
    String text = editTextRecipe.getText().toString().trim();
    Log.d("setRecipe" , text);
    String name_description = editTextIngredient.getText().toString();
    Log.d("setRecipe" , name_description);
    String type = spinnerWeight.getSelectedItem().toString();
    Log.d("setRecipe" , type);
    float weight = Float.parseFloat(editTextWeight.getText().toString().trim());
    Log.d("setRecipe" , String.valueOf(weight));
    Descriptions description = new Descriptions(name_description, type, weight);
    Log.d("setRecipe" , String.valueOf(description.getId_description()));

        Recipes recipes = new Recipes(text, 5);

        setRecipeRX(text, recipes, description)
                .subscribeOn(Schedulers.io())
                .subscribe();
}

private Completable setRecipeRX(String text, Recipes recipes, Descriptions description){
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Throwable {
                long descId = addRecipeModelView.addDescription(description);
                Recipes recipes = new Recipes(text, 5);
                addRecipeModelView.addRecipe(recipes);
            }
        });
}

    private void FieldCheck() {
        if (!editTextRecipe.getText().toString().isEmpty()) {
            setRecipe();
            addRecipeModelView.getShouldCloseScreen().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean close) {
                    if (close) {
                        finish();
                    }
                }
            });
        }
    }


    private void showDescriptions() {
        View view = getLayoutInflater().inflate(R.layout.description_item, linearLayoutDescription, false);
        editTextIngredient = view.findViewById(R.id.EditTextIngredient);
        spinnerWeight = view.findViewById(R.id.SpinnerWeight);
        editTextWeight = view.findViewById(R.id.EditTextWeight);
        linearLayoutDescription.addView(view); // просто добавляет блок на экран
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddRecipe.class);
    }


}