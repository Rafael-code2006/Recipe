package com.example.recipe.activities;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddRecipe extends AppCompatActivity {

    private static final String TAG = "AddRecipe1";

    private TextView title;
    private TextView nameRecipeTitle;
    private TextView instructionTitle;

    private ImageView imageRecipe;

    private TextView titleIngredients;


    // EditText
    private EditText editTextRecipe;
    private EditText editTextInsctructionRecipe;

    private String image;


    // Button
    private Button RecipeSaveButton;


    // FloatingActionButton
    private FloatingActionButton floatingActionButton;


    // LinearLayout
    private LinearLayout linearLayoutDescription;

    static final int PICK_IMAGE_REQUEST = 101;
    private static final int REQUEST_PERMISSION = 102;

    private static int counter_ingredients = 0;




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

        imageRecipe.setOnClickListener(v -> openGallery());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Открываем галерею
    private void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
                return;
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                return;
            }
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Выберите фото"), PICK_IMAGE_REQUEST);
    }

    // Получаем результат запроса разрешения
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Нужно разрешение на доступ к галерее", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Получаем результат выбора изображения
    // В onActivityResult после выбора изображения
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                // Создаём файл в internal storage
                File file = new File(getFilesDir(), String.format("temp_%s_image.jpg", System.currentTimeMillis()));

                try (InputStream input = getContentResolver().openInputStream(imageUri);
                     OutputStream output = new FileOutputStream(file)) {

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }
                }

                // Сохраняем путь к файлу в базе или переменной
                image = file.getAbsolutePath();

                // Ставим картинку в ImageView
                imageRecipe.setImageURI(Uri.fromFile(file));

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Не удалось сохранить изображение", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeLanguage() {
        if(myApp.getBaseLanguage().equals("Рус")){
            title.setText("Добавить Рецепт");
            nameRecipeTitle.setText("Имя рецепта:");
            titleIngredients.setText("Ингредиенты");
            instructionTitle.setText("Инструкция:");
            editTextRecipe.setHint("имя");
            editTextInsctructionRecipe.setHint("инструкция");
        }
        if(myApp.getBaseLanguage().equals("Eng")){
            title.setText("Add Recipe");
            nameRecipeTitle.setText("Recipe name");
            titleIngredients.setText("Ingredients");
            instructionTitle.setText("Insctruction");
            editTextRecipe.setHint("name");
            editTextInsctructionRecipe.setHint("instruction");
        }
        if(myApp.getBaseLanguage().equals("Каз")){
            title.setText("Рецепт Қосыңыз");
            nameRecipeTitle.setText("Рецепт атауы");
            titleIngredients.setText("Ингредиенттер");
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
        imageRecipe = findViewById(R.id.recipeImageView);
        titleIngredients = findViewById(R.id.TextViewIngredient);
    }

    private void FloatingClickButton(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter_ingredients += 1;
                showDescriptions();
            }
        });
    }

    private void showDescriptions() {
        if(titleIngredients.getVisibility() == INVISIBLE){
            titleIngredients.setVisibility(VISIBLE);
        }
        Log.d("AddRecipe2123", "Сработал флоатинг");
        View view = getLayoutInflater().inflate(R.layout.ingredient_item, linearLayoutDescription, false);
        Button test = view.findViewById(R.id.ButtonDelete);
        Spinner spinnerUnit = view.findViewById(R.id.SpinnerUnit);
        TextView UnitTextView = view.findViewById(R.id.TextViewUnit);
        test.setId(View.generateViewId());

        List<String> list = checkLanguage();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerUnit.setAdapter(adapter);

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
            new MaterialAlertDialogBuilder(AddRecipe.this)
                    .setTitle("Удалить ингредиент?")
                    .setMessage("Вы уверены, что хотите удалить этот ингредиент?")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton("Да", (dialog, which) -> {
                        // удаляем родительский элемент (строку ингредиента)
                        View parentRow = (View) v.getParent();
                        counter_ingredients -= 1;
                        linearLayoutDescription.removeView(parentRow);
                        if(counter_ingredients == 0){
                            if(titleIngredients.getVisibility() == VISIBLE){
                                titleIngredients.setVisibility(INVISIBLE);
                            }
                        }
                    })
                    .setNegativeButton("Нет", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        linearLayoutDescription.addView(view);
    }

    private List<String> checkLanguage() {
        List<String> result = new ArrayList<>();
        if(myApp.getBaseLanguage().equals("Рус")){
            result.add("кг");
            result.add("гр");
            result.add("л");
            result.add("мл");
            result.add("мсл");
            result.add("мчл");
        } else if(myApp.getBaseLanguage().equals("Eng")){
            result.add("kg");
            result.add("gr");
            result.add("l");
            result.add("ml");
            result.add("tbsp");
            result.add("tsp");
        } else {
            result.add("кг");
            result.add("гр");
            result.add("л");
            result.add("мл");
            result.add("өақ");
            result.add("өшк");
        }
        return result;
    }


    private void SaveButtonClick() {
        RecipeSaveButton.setOnClickListener(v -> saveRecipe());
    }

    private void saveRecipe() {

        String title = editTextRecipe.getText().toString().trim();
        String instruction = editTextInsctructionRecipe.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Введите название рецепта", Toast.LENGTH_SHORT).show();
            return;
        }

        // Сначала проверяем все ингредиенты
        List<Descriptions> descriptionsList = new ArrayList<>();

        for (int i = 0; i < linearLayoutDescription.getChildCount(); i++) {

            View item = linearLayoutDescription.getChildAt(i);

            EditText nameIngredient = item.findViewById(R.id.EditTextNameIngredient);
            EditText weightIngredient = item.findViewById(R.id.EditTextWeight);
            Spinner spinnerUnit = item.findViewById(R.id.SpinnerUnit);

            String name = nameIngredient.getText().toString().trim();
            String weightText = weightIngredient.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Введите название ингредиента", Toast.LENGTH_SHORT).show();
                return;
            }

            if (weightText.isEmpty()) {
                Toast.makeText(this, "Введите вес у ингредиента", Toast.LENGTH_SHORT).show();
                return;
            }

            float weight;
            try {
                weight = Float.parseFloat(weightText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Ошибка формата веса", Toast.LENGTH_SHORT).show();
                return;
            }

            String unit = spinnerUnit.getSelectedItem().toString();

            // Пока recipeId неизвестен — ставим 0
            descriptionsList.add(new Descriptions(0, name, weight, unit));
        }

        // Если ингредиентов нет
        if (descriptionsList.isEmpty()) {
            Toast.makeText(this, "Добавьте хотя бы один ингредиент", Toast.LENGTH_SHORT).show();
            return;
        }

        if(image == null){
            image = "";
        }
        // Создаем рецепт
        Recipes recipe = new Recipes(title, instruction, image);
        recipe.setIngredient_count(descriptionsList.size());

        // Сохраняем рецепт
        addRecipeModelView.addRecipe(recipe);


        // Получаем ID один раз
        addRecipeModelView.getIdRecipes().observe(this, recipeId -> {
            Log.d("TesTest", "Выполняю переход");
            startActivity(MainActivity.getIntent(AddRecipe.this));
            finish();
            if (recipeId == null) return;

            // Сохраняем ингредиенты с правильным recipeId
            for (Descriptions desc : descriptionsList) {
                desc.setRecipe_id(recipeId);
                addRecipeModelView.addDescription(desc);
            }

            // Удаляем observer
            addRecipeModelView.getIdRecipes().removeObservers(this);
            Log.d("TesTest", "Выполняю переход");

            Toast.makeText(this, "Рецепт сохранён", Toast.LENGTH_SHORT).show();

            Log.d("TesTest", "Выполняю переход");
            // Переход
        });

    }


    public static Intent newIntent(Context context) {
        return new Intent(context, AddRecipe.class);
    }


}