package com.example.recipe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity1";


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

        initVies(); // Инициализация



        showRecipes(); // Показ

        onClickFloatingButton(); // Клик кнопки добавления

        ClickButton(); // Клик кнопок

        Swipe(); // Swipe


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initVies() {
        RecyclerViewRecipes = findViewById(R.id.RecyclerViewRecipes);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        adapterRecipes = new AdapterRecipes();
        RecyclerViewRecipes.setAdapter(adapterRecipes);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    private void ClickButton() {
        adapterRecipes.setRecipeOnClickListener(new AdapterRecipes.RecipeOnClickListener() {
            @Override
            public void OnClickRecipe(Recipes recipe) {
                Log.d("Rafa", "ClickButton сработал");
                long idRecipe = recipe.getId();
                Intent intent = RecipeShow.newIntent(MainActivity.this, idRecipe);
                startActivity(intent);
            }
        });
    }

    private void Swipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d("Rafa", "Swipe сработал");
                int position = viewHolder.getBindingAdapterPosition(); // Получаем позицию рецепта


                // Если позиция существует
                if (position != RecyclerView.NO_POSITION) {
                    Recipes recipeDB = adapterRecipes.getRecipes().get(position); // Берем рецепт по позиции из адаптера

                    adapterRecipes.notifyItemChanged(position); // Указываем перепроверить позицию

                    // Диалог подтверждения
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Удалить рецепт")
                            .setMessage("Вы уверены, что хотите удалить этот рецепт?")
                            .setPositiveButton("Удалить", (dialog, which) -> {
                                mainViewModel.remove(recipeDB); // Удаляем из базы
                                mainViewModel.removeDescriptionForRecipe(recipeDB); // Удаляем все ингридиенты
                                adapterRecipes.removeRecipe(position); // Удаляем из адаптера
                            })
                            .setNegativeButton("Отмена", (dialog, which) -> {
                                adapterRecipes.notifyItemChanged(position); // Указываем перепроверить позицию
                            })
                            .show();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                paint.setColor(Color.RED);

                if (dX < 0) { // свайп влево
                    // фон
                    c.drawRect((float) itemView.getRight() + dX,
                            (float) itemView.getTop(),
                            (float) itemView.getRight(),
                            (float) itemView.getBottom(), paint);

                    // иконка мусорки
                    Drawable icon = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.ic_delete);
                    if (icon != null) {
                        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                        int iconTop = itemView.getTop() + iconMargin;
                        int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                        int iconRight = itemView.getRight() - iconMargin;
                        int iconBottom = iconTop + icon.getIntrinsicHeight();

                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);
                    }
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(RecyclerViewRecipes);
    }

    private void showRecipes() {

        // Подписываемся на коллекцию рецептов из базы
        mainViewModel.getRecipes().observe(this, new Observer<List<Recipes>>() {
            @Override
            public void onChanged(List<Recipes> recipes) {

                adapterRecipes.setRecipes(recipes); // Добавляем коллекцию в адаптер
                adapterRecipes.setRecipeOnClickListener(recipe -> {// Запускаем подсчёт
                adapterRecipes.setSelectedRecipe(recipe); // Сохраняем выбранный рецепт в адаптер
            });


                // Счетчик ингридиентов для рецепта
                adapterRecipes.setCountIngredients(new AdapterRecipes.CountIngredients() {
                    @Override
                    public void CountIngredients(Recipes recipe, TextView targetView) {
                        mainViewModel.loadIngredients(recipe);
                        mainViewModel.getCountIngredients().observe(MainActivity.this, new Observer<HashMap<Recipes, Integer>>() {
                            @Override
                            public void onChanged(HashMap<Recipes, Integer> map) {
                                Integer counter = map.get(recipe);
                                if(counter != null){
                                    targetView.setText(String.valueOf(counter));
                                }
                            }
                        });

                    }
                });


                adapterRecipes.setRecipeOnClickListener(new AdapterRecipes.RecipeOnClickListener() {
                    @Override
                    public void OnClickRecipe(Recipes recipe) {
                       Intent intent = RecipeShow.newIntent(MainActivity.this, recipe.getId());
                       startActivity(intent);
                    }
                });

            }
        });



    }

    private void onClickFloatingButton(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Rafa", "onClickButtonFloating сработал");
                Intent intent = AddRecipe.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}