package com.example.recipe.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipe.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.atomic.AtomicBoolean;

public class AddIngredient extends AppCompatActivity {


    private static final String TAG = "AddIngredient1";

    /**
     * Основная Информация
     */
    private TextInputEditText editIngredientName;
    private AutoCompleteTextView autoCompleteGroup;


    /**
     * Количество
     */

    private TextInputEditText editQuantity;
    private ImageButton buttonQuantityUp;
    private ImageButton buttonQuantityDown;
    private AutoCompleteTextView autoCompleteUnit;
    private MaterialButtonToggleGroup measurementTypeGroup;
    private MaterialButton buttonWeight;
    private MaterialButton buttonVolume;


    private Button saveButton;



    private String name;
    private String group;
    private String quantity;
    private String unit;
    private boolean weight;
    private boolean volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_ingredient);

        setViews();

        getName();

        getGroup();

        getQuantity();

        setupMeasurementType();

        getUnit();

        setOnClickSaveButton();


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

    private void setOnClickSaveButton() {
        saveButton.setOnClickListener((view) -> {
            Log.d(TAG, "Название: " + name);
            Log.d(TAG, "Группа: " + group);
            Log.d(TAG, "Количество: " + quantity);
            Log.d(TAG, "Единица измерения: " + unit);
            Log.d(TAG, "Вес: " + weight);
            Log.d(TAG, "Обьем: " + volume);
        });
    }

    private void getUnit() {
        String unit = autoCompleteUnit.getText().toString();
        this.unit = unit;
    }

    private void getName() {
        String name = editIngredientName.getText().toString();
        this.name = name;
    }

    private void setupMeasurementType() {
        AtomicBoolean weight = new AtomicBoolean(false);
        AtomicBoolean volume = new AtomicBoolean(false);

        measurementTypeGroup.addOnButtonCheckedListener(
                (group, checkedId, isChecked) -> {

                    if (!isChecked) {
                        return;
                    }

                    if (checkedId == R.id.buttonWeight) {
                        setWeightUnits();
                        weight.set(isChecked);
                    } else if (checkedId == R.id.buttonVolume) {
                        setVolumeUnits();
                        volume.set(isChecked);
                    }
                }
        );



        // Значение по умолчанию
        measurementTypeGroup.check(R.id.buttonWeight);

          this.weight = weight.get();
          this.volume = volume.get();
    }

    private void setWeightUnits() {

        String[] units = {
                "кг",
                "гр",
                "мг"
        };

        setUnitAdapter(units);
    }

    private void setVolumeUnits() {

        String[] units = {
                "л",
                "мл"
        };

        setUnitAdapter(units);
    }

    private void setUnitAdapter(String[] units) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                units
        );

        autoCompleteUnit.setAdapter(adapter);

        // Очищаем старую выбранную единицу
        autoCompleteUnit.setText("", false);
    }

    private void getQuantity() {
        String quantity = editQuantity.getText().toString();

        this.quantity = quantity;
    }

    private void getGroup() {


        String[] groups = {
                "Овощи",
                "Фрукты",
                "Мясо",
                "Рыба",
                "Молочные продукты",
                "Крупы",
                "Специи",
                "Соусы"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                groups
        );

        autoCompleteGroup.setAdapter(adapter);

        String group = autoCompleteGroup.getText().toString();
        this.group = group;
    }


    private void setViews(){
        editIngredientName = findViewById(R.id.editIngredientName);
        autoCompleteGroup = findViewById(R.id.autoCompleteGroup);

        editQuantity = findViewById(R.id.editQuantity);
        buttonQuantityUp = findViewById(R.id.buttonQuantityUp);
        buttonQuantityDown = findViewById(R.id.buttonQuantityDown);
        autoCompleteUnit = findViewById(R.id.autoCompleteUnit);
        measurementTypeGroup = findViewById(R.id.measurementTypeGroup);
        buttonWeight = findViewById(R.id.buttonWeight);
        buttonVolume = findViewById(R.id.buttonVolume);

        saveButton = findViewById(R.id.SaveIngredient);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddIngredient.class);
    }
}