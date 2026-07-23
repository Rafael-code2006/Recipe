package com.example.recipe.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.recipe.MainHelper;
import com.example.recipe.R;
import com.example.recipe.model.BottomMenu;


public class MainActivity extends AppCompatActivity {




    private MainHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new MainHelper(this);

        helper.managementBottomMenu();

        helper.refreshActivity();

        helper.changeLanguage();

        helper.showRecipes(); // Показ рецептов

        helper.onClickSetting();

        helper.ClickButton(); // Клик кнопок

        helper.Swipe(); // Swipe





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

    @Override
    protected void onResume() {
        super.onResume();
        helper.reloadRecipe();// если есть такой метод
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}