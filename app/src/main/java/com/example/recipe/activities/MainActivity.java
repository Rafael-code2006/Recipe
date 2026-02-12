package com.example.recipe.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.recipe.MainHelper;
import com.example.recipe.adapters.AdapterRecipes;
import com.example.recipe.viewmodel.MainViewModel;
import com.example.recipe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity1";

    private MainHelper helper;


    // FloatingActionButton
    private FloatingActionButton floatingActionButton;


    // RecyclerView
    private RecyclerView RecyclerViewRecipes;

    // Adapter
    private AdapterRecipes adapterRecipes;


    // ViewModel
    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        helper = new MainHelper(this);

        helper.showRecipes(); // Показ рецептов

        helper.onClickFloatingButton(); // Клик кнопки добавления

        helper.ClickButton(); // Клик кнопок

        helper.Swipe(); // Swipe


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}