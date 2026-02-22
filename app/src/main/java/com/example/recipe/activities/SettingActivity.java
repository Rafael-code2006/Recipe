package com.example.recipe.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.recipe.R;
import com.example.recipe.model.Descriptions;
import com.example.recipe.model.ExportData;
import com.example.recipe.model.Recipes;
import com.example.recipe.setting.MyApp;
import com.example.recipe.viewmodel.SettingViewModel;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingActivity extends AppCompatActivity {

    private TextView title, buttonImport, buttonExport, languageTitle;
    private Button recipeSaveButton;
    private ImageView flag, nonHaveFile, haveFile;
    private Spinner language;

    private ImageView imageCountry;

    private SettingViewModel viewModel;

    private List<Recipes> importRecipes;
    private List<Descriptions> importIngredients;

    private MyApp myApp;

    private static final int PICK_JSON_FILE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        initView();

        exportButton();
        importButton();
        clickSaveButton();
        changeLanguage();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void changeLanguage(){
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();

                if(selectedLanguage.equals("Рус")){
                    myApp.setBaseLanguage("Рус");
                    Log.d("Setting111", myApp.toString());
                    title.setText("Настройки");
                    Log.d("Setting111", title.getText().toString());
                    imageCountry.setImageResource(R.drawable.russia_test);
                    Log.d("Setting111", imageCountry.toString());
                    buttonExport.setText("Экспорт");
                    Log.d("Setting111", buttonExport.toString());
                    buttonImport.setText("Импорт");
                    Log.d("Setting111", buttonImport.toString());
                    languageTitle.setText("Язык");
                    Log.d("Setting111", languageTitle.toString());
                    recipeSaveButton.setText("Сохранить");
                } else if(selectedLanguage.equals("Eng")){
                    myApp.setBaseLanguage("Eng");
                    title.setText("Settings");
                    imageCountry.setImageResource(R.drawable.america);
                    buttonExport.setText("Export");
                    buttonImport.setText("Import");
                    languageTitle.setText("Language");
                    recipeSaveButton.setText("Save");
                } else {
                    myApp.setBaseLanguage("Рус");
                    title.setText("Параметрлер");
                    imageCountry.setImageResource(R.drawable.kazakhstan);
                    buttonExport.setText("Экспорт");
                    buttonImport.setText("Импорт");
                    languageTitle.setText("Тіл");
                    recipeSaveButton.setText("Сақтау");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // ===================== EXPORT =====================

    private void exportButton() {
        buttonExport.setOnClickListener(v -> {

            viewModel.updateAllRecipe();
            viewModel.updateAllDescription();

            viewModel.getRecipes().observe(this, recipes -> {
                viewModel.getIngredients().observe(this, ingredients -> {

                    // Кодируем изображения в Base64
                    for (Recipes recipe : recipes) {
                        if (recipe.getImage() != null) {
                            String base64 = encodeImage(recipe.getImage());
                            recipe.setImage(base64);
                        }
                    }

                    ExportData exportData = new ExportData(recipes, ingredients);
                    String json = new Gson().toJson(exportData);

                    try {
                        File file = new File(getFilesDir(), "recipes_export.json");
                        FileWriter writer = new FileWriter(file);
                        writer.write(json);
                        writer.close();

                        Uri uri = FileProvider.getUriForFile(
                                this,
                                getPackageName() + ".provider",
                                file
                        );

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("application/json");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, "Share recipes"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        });
    }

    private String encodeImage(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) return null;

            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            fis.close();

            return Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===================== IMPORT =====================

    private void importButton() {
        buttonImport.setOnClickListener(v -> openFilePicker());
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

            try (InputStream is = getContentResolver().openInputStream(uri);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

                StringBuilder builder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                String json = builder.toString();

                ExportData exportData = new Gson().fromJson(json, ExportData.class);

                importRecipes = exportData.getRecipes();
                importIngredients = exportData.getIngredients();

                haveFile.setVisibility(View.VISIBLE);
                nonHaveFile.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String decodeAndSaveImage(String base64) {
        try {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);

            File file = new File(getFilesDir(),
                    "imported_" + System.currentTimeMillis() + ".jpg");

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();

            return file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===================== SAVE IMPORTED =====================

    private void clickSaveButton() {
        recipeSaveButton.setOnClickListener(v -> {

            // Создаём список рецептов для сохранения
            List<Recipes> recipesToSave = new ArrayList<>();

            // Если импортированные есть, добавляем их
            if (importRecipes != null) {
                recipesToSave.addAll(importRecipes);
            }

            // Сохраняем каждый рецепт
            for (Recipes recipe : recipesToSave) {

                long oldId = recipe.getId();

                // Восстанавливаем фото
                if (recipe.getImage() != null && recipe.getImage().length() > 100) {
                    String newPath = decodeAndSaveImage(recipe.getImage());
                    recipe.setImage(newPath);
                }

                List<Descriptions> ingredientsForRecipe = new ArrayList<>();
                if (importIngredients != null) {
                    for (Descriptions ing : importIngredients) {
                        if (ing.getRecipe_id() == oldId) {
                            ing.setId_description(0);
                            ingredientsForRecipe.add(ing);
                        }
                    }
                }

                recipe.setId(0);

                viewModel.addRecipeRX(recipe)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(newId -> {
                            for (Descriptions ing : ingredientsForRecipe) {
                                ing.setRecipe_id(newId);
                            }
                            viewModel.saveAllIngredients(ingredientsForRecipe);
                        }, throwable -> {
                            Log.e("IMPORT", "Ошибка", throwable);
                        });
            }

            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            startActivity(MainActivity.getIntent(this));
            finish();
        });
    }

    // ===================== INIT =====================

    private void initView() {
        title = findViewById(R.id.main_RecipeTextView);
        buttonExport = findViewById(R.id.RecipeExportButton);
        buttonImport = findViewById(R.id.RecipeImportButton);
        flag = findViewById(R.id.LanguageIcon);
        language = findViewById(R.id.SpinnerUnit);
        languageTitle = findViewById(R.id.TextSettingName);
        recipeSaveButton = findViewById(R.id.RecipeSaveButton);
        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        nonHaveFile = findViewById(R.id.ImageNonHaveFile);
        haveFile = findViewById(R.id.ImageHaveFile);
        myApp = MyApp.getInstance();
        imageCountry = findViewById(R.id.LanguageIcon);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }
}