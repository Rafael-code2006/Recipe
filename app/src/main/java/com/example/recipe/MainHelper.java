package com.example.recipe;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.activities.AddRecipe;
import com.example.recipe.activities.MainActivity;
import com.example.recipe.activities.ShowRecipe;
import com.example.recipe.adapters.AdapterRecipes;
import com.example.recipe.model.Recipes;
import com.example.recipe.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;

public class MainHelper {

    private MainViewModel viewModel;

    private AdapterRecipes adapter;

    private ImageView setting;

    private MainActivity context;

    private RecyclerView RecyclerViewRecipes;

    private FloatingActionButton floatingActionButton;



    public MainHelper(MainActivity context){
        this.context = context;
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(MainViewModel.class);
        adapter = new AdapterRecipes();
        floatingActionButton = context.findViewById(R.id.floatingActionButton);
        RecyclerViewRecipes = context.findViewById(R.id.RecyclerViewRecipes);
        setting = context.findViewById(R.id.SettingButton);
        RecyclerViewRecipes.setAdapter(adapter);
    }

    public void onClickFloatingButton(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Rafa", "onClickButtonFloating сработал");
                Intent intent = AddRecipe.newIntent(context);
                context.startActivity(intent);
            }
        });
    }


    public void onClickSetting(){
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SettingActivity.newIntent(context);
                context.startActivity(intent);
            }
        });
    }

    public void showRecipes() {

        // Подписываемся на коллекцию рецептов из базы
        viewModel.getRecipes().observe(context, new Observer<List<Recipes>>() {
            @Override
            public void onChanged(List<Recipes> recipes) {

                loadRecipeToAdapter(recipes);

                // Счетчик ингридиентов для рецепта
                IngredientCounter();


                adapter.setRecipeOnClickListener(new AdapterRecipes.RecipeOnClickListener() {
                    @Override
                    public void OnClickRecipe(Recipes recipe) {
                        clickRecipe(recipe);
                    }
                });

            }
        });



    }

    private void IngredientCounter() {
        adapter.setCountIngredients(new AdapterRecipes.CountIngredients() {
            @Override
            public void CountIngredients(Recipes recipe, TextView targetView) {
                viewModel.loadIngredients(recipe);
                viewModel.getCountIngredients().observe(context, new Observer<HashMap<Recipes, Integer>>() {
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
    }

    private void loadRecipeToAdapter(List<Recipes> recipes) {
        adapter.setRecipes(recipes); // Добавляем коллекцию в адаптер
        adapter.setRecipeOnClickListener(recipe -> {// Запускаем подсчёт
            adapter.setSelectedRecipe(recipe); // Сохраняем выбранный рецепт в адаптер
        });
    }

    public void ClickButton() {
        adapter.setRecipeOnClickListener(new AdapterRecipes.RecipeOnClickListener() {
            @Override
            public void OnClickRecipe(Recipes recipe) {
                clickRecipe(recipe);
            }
        });
    }

    private void clickRecipe(Recipes recipe) {
        Log.d("Rafa", "ClickButton сработал");
        Intent intent = ShowRecipe.newIntent(context, recipe);
        context.startActivity(intent);
    }


    public void Swipe() {
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
                    Recipes recipeDB = adapter.getRecipes().get(position); // Берем рецепт по позиции из адаптера

                    deletedRecipe(position, recipeDB);
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

                deleteIconWithSwipe(c, recyclerView, dX, itemView, paint);
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(RecyclerViewRecipes);
    }

    private static void deleteIconWithSwipe(@NonNull Canvas c, @NonNull RecyclerView recyclerView, float dX, View itemView, Paint paint) {
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

    private void deletedRecipe(int position, Recipes recipeDB) {
        adapter.notifyItemChanged(position); // Указываем перепроверить позицию

        // Диалог подтверждения
        new AlertDialog.Builder(context)
                .setTitle("Удалить рецепт")
                .setMessage("Вы уверены, что хотите удалить этот рецепт?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    viewModel.remove(recipeDB); // Удаляем из базы
                    viewModel.removeDescriptionForRecipe(recipeDB); // Удаляем все ингридиенты
                    adapter.removeRecipe(position); // Удаляем из адаптера
                })
                .setNegativeButton("Отмена", (dialog, which) -> {
                    adapter.notifyItemChanged(position); // Указываем перепроверить позицию
                })
                .show();
    }


}
