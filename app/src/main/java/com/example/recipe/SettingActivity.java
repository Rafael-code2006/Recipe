package com.example.recipe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.DeadSystemException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipe.activities.MainActivity;
import com.example.recipe.model.Descriptions;
import com.example.recipe.model.ExportData;
import com.example.recipe.model.Recipes;
import com.example.recipe.setting.MyApp;
import com.example.recipe.viewmodel.SettingViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingActivity extends AppCompatActivity {

    private TextView title;
    private TextView buttonImport;
    private TextView buttonExport;
    private Button recipeSaveButton;
    private TextView languageTitle;
    private ImageView flag;

    private ImageView nonHaveFile;
    private ImageView haveFile;

    private Spinner language;
    private SettingViewModel viewModel;

    private List<Recipes> importRecipes;
    private List<Descriptions> importIngredients;

    private static final int PICK_JSON_FILE = 100;
    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        initView();
        setStarted();
        changeLanguage();

        // Экспорт
        exportButton();
        // Импорт
        importButton();

        clickSaveButton();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void importButton() {
        buttonImport.setOnClickListener(v -> openFilePicker());
    }

    private void exportButton() {
        buttonExport.setOnClickListener(v -> {
            viewModel.updateAllRecipe();
            viewModel.updateAllDescription();

            // наблюдаем сразу за двумя LiveData
            viewModel.getRecipes().observe(SettingActivity.this, recipes -> {
                viewModel.getIngredients().observe(SettingActivity.this, ingredients -> {
                    Gson gson = new Gson();
                    ExportData exportData = new ExportData(recipes, ingredients);
                    String json = gson.toJson(exportData);
                    Log.d("Setting1", json);

                    try {
                        File file = new File(getFilesDir(), "recipes.json");
                        Log.d("Setting1", "path: " + file.getAbsoluteFile());

                        FileWriter writer = new FileWriter(file);
                        // если хочешь кодировать в Base64
                        String encodeJson;
                        Log.d("SettingActivityExport", json);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            encodeJson = Base64.getEncoder().encodeToString(json.getBytes());
                        } else {
                            encodeJson = json;
                        }
                        writer.write(encodeJson);
                        writer.close();

                        // Поделиться файлом
                        Uri uri = FileProvider.getUriForFile(
                                SettingActivity.this,
                                getPackageName() + ".provider",
                                file
                        );

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("application/json");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, "Поделиться рецептом"));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            });
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        startActivityForResult(intent, PICK_JSON_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_JSON_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                try (InputStream is = getContentResolver().openInputStream(uri);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    String encodedJson = builder.toString();
                    String decodedJson = encodedJson;

                    // Если файл закодирован в Base64
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        try {
                            byte[] decodedBytes = Base64.getDecoder().decode(encodedJson);
                            decodedJson = new String(decodedBytes, StandardCharsets.UTF_8);
                        } catch (IllegalArgumentException e) {
                            // если не Base64 — оставляем как есть
                            decodedJson = encodedJson;
                        }
                    }


                    Gson gson = new Gson();
                    ExportData exportData = gson.fromJson(decodedJson, ExportData.class);

                    importRecipes = exportData.getRecipes();
                    importIngredients = exportData.getIngredients();

                    /*if (importRecipes != null) {
                        for (Recipes x : importRecipes) {
                            x.setId(0); // сброс ID
                        }
                    }

                     */


                   /* for (Descriptions x : importIngredients) {
                        x.setId_description(0); // сброс ID
                    }

                    */





                    haveFile.setVisibility(View.VISIBLE);
                    nonHaveFile.setVisibility(View.GONE);

                    // при необходимости сохраняем импортированный файл во внутреннее хранилище
                    File file = new File(getFilesDir(), "recipes_imported.json");
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(decodedJson);
                    }

                    Log.d("SettingTest", "Импорт завершён успешно");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setStarted() {
        String lang = MyApp.getInstance().getBaseLanguage();
        Log.d("Setting1", "lang Main: " + lang);
        if (lang.equals("Рус")) { language.setSelection(0); }
        if (lang.equals("Eng")) { language.setSelection(1); }
        if (lang.equals("Каз")) { language.setSelection(2); }
    }

    private void changeLanguage() {
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                if (selected.equals("Рус")) {
                    flag.setImageResource(R.drawable.russia_test);
                    changeLanguage("Настройки", "Экспорт", "Импорт", "Язык");
                    MyApp.getInstance().setBaseLanguage("Рус");
                } else if (selected.equals("Eng")) {
                    flag.setImageResource(R.drawable.america);
                    changeLanguage("Settings", "Export", "Import", "Language");
                    MyApp.getInstance().setBaseLanguage("Eng");
                } else if (selected.equals("Каз")) {
                    flag.setImageResource(R.drawable.kazakhstan);
                    changeLanguage("Параметрлер", "Экспорт", "Импорт", "Тіл");
                    MyApp.getInstance().setBaseLanguage("Каз");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void clickSaveButton() {
        recipeSaveButton.setOnClickListener(v -> {
            if (importRecipes != null && importIngredients != null) {
                Log.d("SettingImports", "Пришедший список ингредиентов: \n" + importIngredients);

                for (Recipes recipe : importRecipes) {
                    long originalId = recipe.getId(); // сохраняем старый ID из JSON

                    // фильтруем ингредиенты по старому ID
                    List<Descriptions> ingredientsForRecipe = new ArrayList<>();
                    for (Descriptions ing : importIngredients) {
                        if (ing.getRecipe_id() == originalId) {
                            Log.d("SettingImports", "Присваиваем к рецепту " + recipe.getName() + ": " + ing.getName());
                            ing.setId_description(0); // сброс ID для Room
                            ingredientsForRecipe.add(ing);
                        }
                    }

                    recipe.setId(0); // сброс ID для Room

                    // сохраняем рецепт и обрабатываем результат
                    Disposable disposable = viewModel.addRecipeRX(recipe)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(newId -> {
                                // Room вернул новый ID
                                recipe.setId(newId); // обновляем объект
                                for (Descriptions ing : ingredientsForRecipe) {
                                    ing.setRecipe_id(newId); // проставляем новый ID
                                    Log.d("SettingImports", "Присвоили id ингредиенту: " + ing.getName());
                                }
                                viewModel.saveAllIngredients(ingredientsForRecipe);
                                Log.d("SettingImports", "Сохранили все ингредиенты для рецепта " + recipe.getName());
                            }, throwable -> {
                                Log.e("SettingActivity", "Ошибка сохранения рецепта", throwable);
                            });
                }
            }

            Intent intent = MainActivity.getIntent(SettingActivity.this);
            startActivity(intent);
        });
    }

    private void changeLanguage(String title, String exportText, String importText, String language) {
        this.title.setText(title);
        this.buttonExport.setText(exportText);
        this.buttonImport.setText(importText);
        this.languageTitle.setText(language);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    private void initView() {
        title = findViewById(R.id.main_RecipeTextView);
        buttonExport = findViewById(R.id.RecipeExportButton);
        buttonImport = findViewById(R.id.RecipeImportButton);
        flag = findViewById(R.id.LanguageIcon);
        language = findViewById(R.id.SpinnerUnit);
        languageTitle = findViewById(R.id.TextSettingName);
        myApp = MyApp.getInstance();
        recipeSaveButton = findViewById(R.id.RecipeSaveButton);
        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        nonHaveFile = findViewById(R.id.ImageNonHaveFile);
        haveFile = findViewById(R.id.ImageHaveFile);
    }
}