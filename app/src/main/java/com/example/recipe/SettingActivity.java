package com.example.recipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.recipe.activities.MainActivity;
import com.example.recipe.setting.MyApp;

public class SettingActivity extends AppCompatActivity {

    private TextView title;
    private TextView buttonImport;
    private TextView buttonExport;

    private Button recipeSaveButton;

    private TextView languageTitle;

    private ImageView flag;

    private Spinner language;

    private MyApp myApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        initView();

        setStarted();
        changeLanguage();
        clickSaveButton();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setStarted() {
        String lang = MyApp.getInstance().getBaseLanguage();
        Log.d("Setting1", "lang Main: " + lang);
        if(lang.equals("Рус")){language.setSelection(0);}
        if(lang.equals("Eng")){language.setSelection(1);}
        if(lang.equals("Каз")){language.setSelection(2);}
    }

    private void changeLanguage() {
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected = parent.getItemAtPosition(position).toString();

                    if(selected.equals("Рус")){
                        flag.setImageResource(R.drawable.russia_test);
                        changeLanguage("Настройки", "Экспорт", "Импорт", "Язык");
                        MyApp.getInstance().setBaseLanguage("Рус");
                    } else if (selected.equals("Eng")){
                        flag.setImageResource(R.drawable.america);
                        changeLanguage("Settings", "Export", "Import", "Language");
                        MyApp.getInstance().setBaseLanguage("Eng");
                    } else if (selected.equals("Каз")){
                        flag.setImageResource(R.drawable.kazakhstan);
                        changeLanguage("Параметрлер", "Экспорт", "Импорт", "Тіл");
                        MyApp.getInstance().setBaseLanguage("Каз");
                    }

                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void clickSaveButton() {
        recipeSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.getIntent(SettingActivity.this);
                startActivity(intent);
            }
        });
    }

    private void changeLanguage(String title, String exportText, String importText, String language) {
        this.title.setText(title);
        this.buttonExport.setText(exportText);
        this.buttonImport.setText(importText);
        this.languageTitle.setText(language);
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }

    private void initView(){
        title = findViewById(R.id.main_RecipeTextView);
        buttonExport = findViewById(R.id.RecipeExportButton);
        buttonImport = findViewById(R.id.RecipeImportButton);
        flag = findViewById(R.id.LanguageIcon);
        language = findViewById(R.id.SpinnerUnit);
        languageTitle = findViewById(R.id.TextSettingName);
        myApp = MyApp.getInstance();
        recipeSaveButton = findViewById(R.id.RecipeSaveButton);
    }
}