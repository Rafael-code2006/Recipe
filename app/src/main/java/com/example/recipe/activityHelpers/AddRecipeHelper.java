package com.example.recipe.activityHelpers;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipe.R;
import com.example.recipe.activities.AddIngredient;
import com.example.recipe.activities.AddRecipe;
import com.example.recipe.activities.MainActivity;
import com.example.recipe.model.Ingredient;
import com.example.recipe.model.Recipes;
import com.example.recipe.setting.MyApp;
import com.example.recipe.setting.TextKey;
import com.example.recipe.viewmodel.AddRecipeViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddRecipeHelper {

    private final AddRecipe context;

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
    private ImageButton addIngredientButton;


    // LinearLayout
    private LinearLayout linearLayoutDescription;


    // ViewModel
    private AddRecipeViewModel addRecipeViewModel;

    private MyApp myApp;


    static final int PICK_IMAGE_REQUEST = 101;
    private static final int REQUEST_PERMISSION = 102;

    private static int counter_ingredients = 0;



    public AddRecipeHelper(AddRecipe context) {
        this.context = context;
        editTextRecipe = context.findViewById(R.id.EditTextRecipe);
        setTextChanged(editTextRecipe);
        RecipeSaveButton = context.findViewById(R.id.RecipeSaveButton);
        linearLayoutDescription = context.findViewById(R.id.linearLayoutDescription);
        addIngredientButton = context.findViewById(R.id.addIngredientButton);
        addRecipeViewModel = new ViewModelProvider(context).get(AddRecipeViewModel.class);
        editTextInsctructionRecipe = context.findViewById(R.id.EditTextInsctructionRecipe);
        setTextChanged(editTextInsctructionRecipe);
        myApp = MyApp.getInstance();
        title = context.findViewById(R.id.CreateRecipeTextView);
        nameRecipeTitle = context.findViewById(R.id.TextViewRecipeName);
        instructionTitle = context.findViewById(R.id.TextViewInstructionRecipe);
        imageRecipe = context.findViewById(R.id.recipeImageView);
        titleIngredients = context.findViewById(R.id.TextViewIngredient);
    }


    public void AdjustToTheKeyboard(){
        CardView cardView = context.findViewById(R.id.CardView);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            context.getWindow().getDecorView().setWindowInsetsAnimationCallback(
                    new WindowInsetsAnimation.Callback(WindowInsetsAnimation.Callback.DISPATCH_MODE_STOP) {
                        @Override
                        public WindowInsets onProgress(WindowInsets insets, List<WindowInsetsAnimation> runningAnimations) {
                            int imeHeight = insets.getInsets(WindowInsets.Type.ime()).bottom;
                            int navBar = insets.getInsets(WindowInsets.Type.navigationBars()).bottom;
                            int offset = imeHeight - navBar;

                            // Не двигаем вниз — только вверх
                            int translation = offset > 0 ? -offset : 0;

                            addIngredientButton.setTranslationY(translation);
                            cardView.setTranslationY(translation);

                            return insets;
                        }
                    }
            );
        }
    }

    public void setImage() {
        imageRecipe.setOnClickListener(v -> openGallery());
    }

    // Открываем галерею
    public void openGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                context.requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
                return;
            }
        } else {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                context.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                return;
            }
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        context.startActivityForResult(Intent.createChooser(intent, "Выберите фото"), PICK_IMAGE_REQUEST);
    }

    public void changeLanguage() {
        // Заголовки и подписи
        title.setText(MyApp.text(TextKey.ADD));                  // "Добавить Рецепт", "Add Recipe", "Рецепт Қосыңыз"
        nameRecipeTitle.setText(MyApp.text(TextKey.RECIPE_NAME)); // "Имя рецепта:", "Recipe name", "Рецепт атауы"
        titleIngredients.setText(MyApp.text(TextKey.INGREDIENTS));// "Ингредиенты", "Ingredients", "Ингредиенттер"
        instructionTitle.setText(MyApp.text(TextKey.INSTRUCTION));// "Инструкция:", "Instruction", "Нұсқаулық"

        // Хинты для EditText
        editTextRecipe.setHint(MyApp.text(TextKey.NAME));       // "имя", "name", "атауы"
        editTextInsctructionRecipe.setHint(MyApp.text(TextKey.INSTRUCTION)); // "инструкция", "instruction", "нұсқаулық"
    }

    public void FloatingClickButton(){
        addIngredientButton.setOnClickListener(v -> {
//            counter_ingredients += 1;
//            showDescriptions();

            Intent intent = AddIngredient.newIntent(context);
            context.startActivity(intent);
        });

    }

    private void showDescriptions() {
        if(titleIngredients.getVisibility() == INVISIBLE){
            titleIngredients.setVisibility(VISIBLE);
        }
        Log.d("AddRecipe2123", "Сработал флоатинг");
        View view = context.getLayoutInflater().inflate(R.layout.ingredient_item, linearLayoutDescription, false);
        TextView name = view.findViewById(R.id.EditTextNameIngredient);
        Button test = view.findViewById(R.id.ButtonDelete);
        Spinner spinnerUnit = view.findViewById(R.id.SpinnerUnit);
        TextView UnitTextView = view.findViewById(R.id.TextViewUnit);

        setTextChanged(name);

        test.setId(View.generateViewId());

        List<String> list = checkLanguage();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
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
            new MaterialAlertDialogBuilder(context)
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

    private static void setTextChanged(TextView name) {
        name.addTextChangedListener(new TextWatcher() {
            private boolean isEditing = false;

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                if (s.length() == 0) return;

                isEditing = true;

                // Первая буква заглавная
                String firstChar = s.subSequence(0,1).toString().toUpperCase();
                String rest = s.length() > 1 ? s.subSequence(1, s.length()).toString().toLowerCase() : "";

                s.replace(0, s.length(), firstChar + rest);

                isEditing = false;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            }
        });
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


    public void SaveButtonClick() {
        RecipeSaveButton.setOnClickListener(v -> saveRecipe());
    }

    @SuppressLint("CheckResult")
    private void saveRecipe() {

        String title = editTextRecipe.getText().toString().trim();
        String instruction = editTextInsctructionRecipe.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(context, "Введите название рецепта", Toast.LENGTH_SHORT).show();
            return;
        }

        // Сначала проверяем все ингредиенты
        List<Ingredient> descriptionsList = new ArrayList<>();

        for (int i = 0; i < linearLayoutDescription.getChildCount(); i++) {

            View item = linearLayoutDescription.getChildAt(i);

            EditText nameIngredient = item.findViewById(R.id.EditTextNameIngredient);
            EditText weightIngredient = item.findViewById(R.id.EditTextWeight);
            Spinner spinnerUnit = item.findViewById(R.id.SpinnerUnit);

            String name = nameIngredient.getText().toString().trim();
            String weightText = weightIngredient.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(context, "Введите название ингредиента", Toast.LENGTH_SHORT).show();
                return;
            }

            if (weightText.isEmpty()) {
                Toast.makeText(context, "Введите вес у ингредиента", Toast.LENGTH_SHORT).show();
                return;
            }

            if(name.length() > 32){
                setToast("Слишком длинное название ингредиента Позиция: " + i);
                return;
            } else
            if(weightText.length() > 32){
                Toast.makeText(context, "Превышен лимит по значению веса", Toast.LENGTH_SHORT).show();
                return;
            }

            float weight;
            try {
                weight = Float.parseFloat(weightText);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Ошибка формата веса", Toast.LENGTH_SHORT).show();
                return;
            }

            String unit = spinnerUnit.getSelectedItem().toString();

            // Пока recipeId неизвестен — ставим 0
            descriptionsList.add(new Ingredient(0, name, weight, unit));
        }

        // Если ингредиентов нет
        if (descriptionsList.isEmpty()) {
            Toast.makeText(context, "Добавьте хотя бы один ингредиент", Toast.LENGTH_SHORT).show();
            return;
        }

        if(image == null){
            image = "";
        }
        // Создаем рецепт
        Recipes recipe = new Recipes(title, instruction, image);
        recipe.setIngredient_count(descriptionsList.size());

        addRecipeViewModel.TestAddRecipe(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (aLong == null) return;

                    // Сохраняем ингредиенты с правильным recipeId
                    for (Ingredient desc : descriptionsList) {
                        desc.setRecipeId(aLong);
                        addRecipeViewModel.addDescription(desc);
                    }

                    Intent intent = MainActivity.getIntent(context);
                    context.startActivity(intent);
                    context.finish();

                    Toast.makeText(context, "Рецепт сохранён", Toast.LENGTH_SHORT).show();

                });

//        // Получаем ID один раз
//        addRecipeViewModel.getIdRecipes().observe(this, recipeId -> {
//            Log.d("TesTest", "Выполняю переход");
//            startActivity(MainActivity.getIntent(context));
//            finish();
//            if (recipeId == null) return;
//
//            // Сохраняем ингредиенты с правильным recipeId
//            for (Ingredient desc : descriptionsList) {
//                desc.setRecipe_id(recipeId);
//                addRecipeViewModel.addDescription(desc);
//            }
//
//            // Удаляем observer
//            addRecipeViewModel.getIdRecipes().removeObservers(this);
//            Log.d("TesTest", "Выполняю переход");
//
//            Toast.makeText(this, "Рецепт сохранён", Toast.LENGTH_SHORT).show();
//
//            Log.d("TesTest", "Выполняю переход");
//            // Переход
//        });

    }

    private void setToast(String text){
        TextView toastText = new TextView(context);
        toastText.setText(text);
        toastText.setTextColor(Color.WHITE);
        toastText.setTextSize(14);
        toastText.setPadding(40, 24, 40, 24);
        toastText.setBackgroundResource(android.R.drawable.toast_frame);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastText);
        toast.show();
    }



}
