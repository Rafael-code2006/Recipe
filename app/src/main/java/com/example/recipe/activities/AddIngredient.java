package com.example.recipe.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipe.R;
import com.example.recipe.model.Ingredient;
import com.google.android.material.textfield.TextInputEditText;

public class AddIngredient extends AppCompatActivity {


    private static final String LOG_BASIC_INFORMATION = "BasicInformation";
    private static final String LOG_QUANTITY = "Quantity";
    private static final String LOG_NUTRITION = "Nutrition";
    private static final String LOG_ADDITION = "Addition";
    private static final String LOG_SAVE = "SaveIngredient";

    /**
     * Основная Информация
     */
    private TextInputEditText editIngredientName;
    private AutoCompleteTextView autoCompleteGroup;


    /**
     * Количество
     */
    private TextInputEditText editWeight;
    private ImageButton buttonWeightUp;
    private ImageButton buttonWeightDown;
    private AutoCompleteTextView autoCompleteUnit;


    /**
     * Пищевая ценность
     */

    private EditText editCalories;
    private EditText editProtein;
    private EditText editFat;
    private EditText editCarbs;


    /**
     * Дополнительно
     */

    private TextInputEditText editBrand;
    private TextInputEditText editNote;

    /**
     * Сохранение ингредиента
     */
    private Button saveButton;
    private Ingredient result;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_ingredient);

        setViews();
        updateAll();

        clickUpQuantity();
        clickDownQuantity();

        clickSave();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    0
            );

            return insets;
        });
    }

    /**
     * Работа с названием ингредиента.
     * <ul>
     *     <li>{@code editIngredientName} - изменяемое поле актуального названия ингредиента</li>
     * </ul>
     */

    private String name;
    private void updateName(){
        this.name = editIngredientName.getText().toString();
        result.setName(name);
        Log.d(LOG_BASIC_INFORMATION, "Обновил поле {'name'='" + name + "'}");
    }


    /**
     * Работа с группой ингредиента.
     * <ul>
     *     <li>{@code autoCompleteGroup} - поле для выбора групп</li>
     * </ul>
     */

    private String group;
    private void updateGroup() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.ingredient_groups,
                android.R.layout.simple_dropdown_item_1line
        );
        autoCompleteGroup.setAdapter(adapter);

        this.group = autoCompleteGroup.getText().toString();
        this.result.setGroup(this.group);
        Log.d(LOG_BASIC_INFORMATION, "Обновил поле {'group'='" + this.group + "'}");
    }


    /**
     * Работа с установкой веса
     * <ul>
     *     <li>{@code EditWeight} - изменяемое поле показа актуального веса ингредиента</li>
     *     <li>{@code buttonWeightUp} - кнопка для увеличения веса</li>
     *     <li>{@code buttonWeightDown} - кнопка для уменьшения веса</li>
     * </ul>
     */

    private float weight;


    private void updateWeight(){
        String parseWeight = String.valueOf(this.weight);
        editWeight.setText(parseWeight);
        result.setWeight(weight);
        Log.d(LOG_QUANTITY, "Обновил поле {'weight'=" + parseWeight + "}");
    }
    private void updateWeight(float weight){
        String parseWeight = String.valueOf(weight);
        editWeight.setText(parseWeight);
        result.setWeight(weight);
        Log.d(LOG_QUANTITY, "Обновил поле {'weight'=" + parseWeight + "}");
    }
    private void clickUpQuantity(){
        buttonWeightUp.setOnClickListener(view -> {
            this.weight = Float.parseFloat(editWeight.getText().toString());
            this.weight += 0.5f;
            Log.d(LOG_QUANTITY, "Увеличил поле на 0.5 {'weight'=" + this.weight + "}");
            updateWeight(weight);
        });
    }
    private void clickDownQuantity(){
        buttonWeightDown.setOnClickListener(view -> {
            if(weight >= 0.5f){
                this.weight = Float.parseFloat(editWeight.getText().toString());
                this.weight -= 0.5f;
                Log.d(LOG_QUANTITY, "Убавил значение поля на 0.5 {'weight'=" + this.weight + "}");
                updateWeight(weight);
            } else {
                Log.d(LOG_QUANTITY, "Значение поля меньше 0.5 {'weight'=" + this.weight + "}");
            }
        });
    }


    /**
     * Работа с типом веса ингредиента.
     * <ul>
     *     <li>{@code autoCompleteGroup} - поле для выбора групп</li>
     * </ul>
     */

    private String unit;
    private void updateUnit() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.unit_all,                               // массив-ресурс
                android.R.layout.simple_dropdown_item_1line     // разметка строки
        );
        autoCompleteUnit.setAdapter(adapter);

        this.unit = autoCompleteUnit.getText().toString();
        result.setUnit(this.unit);
        Log.d(LOG_QUANTITY, "Обновил поле {'unit'='" + this.unit + "'}");
    }


    /**
     * Работа с пищевой ценностью ингредиента.
     * <ul>
     *     <li>{@code editCalories} - ввод калорий</li>
     *     <li>{@code editProtein} - ввод белка</li>
     *     <li>{@code editFat} - ввод жира</li>
     *     <li>{@code editCarbs} - ввод углеводов</li>
     * </ul>
     */

    private float calories;

    private void updateCalories(){
        this.calories = Float.parseFloat(editCalories.getText().toString());
        this.result.setCalories(this.calories);
        Log.d(LOG_NUTRITION, "Обновил поле {'calories'='" + this.calories + "'}");
    }

    private float protein;
    private void updateProtein(){
        this.protein = Float.parseFloat(editProtein.getText().toString());
        this.result.setProteins(this.protein);
        Log.d(LOG_NUTRITION, "Обновил поле {'protein'='" + this.protein + "'}");
    }

    private float fat;
    private void updateFat(){
        this.fat = Float.parseFloat(editFat.getText().toString());
        this.result.setFats(this.fat);
        Log.d(LOG_NUTRITION, "Обновил поле {'fat'='" + this.fat + "'}");
    }

    private float carbohydrates;
    private void updateCarbs(){
        this.carbohydrates = Float.parseFloat(editCarbs.getText().toString());
        this.result.setCarbohydrates(this.carbohydrates);
        Log.d(LOG_NUTRITION, "Обновил поле {'carbohydrates'='" + this.carbohydrates + "'}");
    }


    /**
     * Работа с доп. информацией об ингредиенте.
     * <ul>
     *     <li>{@code editCalories} - ввод калорий</li>
     *     <li>{@code editProtein} - ввод белка</li>
     *     <li>{@code editFat} - ввод жира</li>
     *     <li>{@code editCarbs} - ввод углеводов</li>
     * </ul>
     */

    private String brand;
    private void updateBrand(){
        this.brand = editBrand.getText().toString();
        this.result.setBrand(this.brand);
        Log.d(LOG_ADDITION, "Обновил поле {'brand'='" + this.brand + "'}");
    }

    private String note;
    private void updateNote(){
        this.note = editNote.getText().toString();
        this.result.setNotes(this.note);
        Log.d(LOG_ADDITION, "Обновил поле {'note'='" + this.note + "'}");
    }

    /**
     * Обновляет весь список атрибутов
     */

    private void updateAll(){
        updateName();       // Обновляем название
        updateGroup();      // Обновляем группу
        updateWeight();     // Обновляем вес
        updateUnit();       // Обновляем тип веса
        updateCalories();   // Обновляем калории
        updateProtein();    // Обновляем белки
        updateFat();        // Обновляем жиры
        updateCarbs();      // Обновляем углеводы
        updateBrand();      // Обновляем бренд
        updateNote();       // Обновляем заметки
    }

    /**
     * Обработчик событий на кнопку сохранения ингредиента
     */

    private void clickSave(){
        saveButton.setOnClickListener((view) -> {
            updateAll();
            Ingredient ingredient = this.result;
            Log.d(LOG_SAVE, "Сохраняем результат: " + ingredient.toString());
        });
    }

    private void setViews(){
        result = new Ingredient();
        editIngredientName = findViewById(R.id.editIngredientName);
        autoCompleteGroup = findViewById(R.id.autoCompleteGroup);

        editWeight = findViewById(R.id.editWeight);
        buttonWeightUp = findViewById(R.id.buttonWeightUp);
        buttonWeightDown = findViewById(R.id.buttonWeightDown);
        autoCompleteUnit = findViewById(R.id.autoCompleteUnit);

        editCalories = findViewById(R.id.editCalories);
        editProtein = findViewById(R.id.editProtein);
        editFat = findViewById(R.id.editFat);
        editCarbs = findViewById(R.id.editCarbs);

        editBrand = findViewById(R.id.editBrand);
        editNote = findViewById(R.id.editNote);

        saveButton = findViewById(R.id.SaveIngredient);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddIngredient.class);
    }
}