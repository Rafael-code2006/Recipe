package com.example.recipe.activities;

import static android.view.View.INVISIBLE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipe.R;
import com.example.recipe.adapters.ShowRecipeAdapter;
import com.example.recipe.model.Recipes;
import com.example.recipe.setting.MyApp;
import com.example.recipe.setting.TextKey;
import com.example.recipe.viewmodel.RecipeShowViewModel;

import java.io.File;

public class ShowRecipe extends AppCompatActivity {

    private TextView title, ingredientsTitle, instructionTitle;
    private TextView recipeNameTextView, instructionTextView;
    private ImageView imageView;
    private Button editButton;
    private RecyclerView ingredientsRecyclerView;

    private Recipes recipe;
    private RecipeShowViewModel recipeShowViewModel;
    private MyApp myApp;
    private ShowRecipeAdapter ingredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_show);

        initViews();

        showRecipe();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showRecipe() {
        recipeShowViewModel.loadRecipe(recipe);

        // Наблюдаем recipe один раз
        recipeShowViewModel.getRecipe().observe(this, recipes1 -> {
            if (recipes1 == null) return;

            recipe = recipes1;
            showImage(recipe);
            showRecipeContent(recipe);
            editButtonClick(recipe);
            changeLanguage();
        });
    }

    private void initViews() {
        recipe = (Recipes) getIntent().getSerializableExtra("Recipe");
        title = findViewById(R.id.YourRecipeTextView);
        ingredientsTitle = findViewById(R.id.TextViewIngredients);
        instructionTitle = findViewById(R.id.TextViewInstruction);
        recipeNameTextView = findViewById(R.id.RecipeTextViewShow);
        instructionTextView = findViewById(R.id.TextViewInstructionContent);
        imageView = findViewById(R.id.RecipeImageView);
        editButton = findViewById(R.id.RecipeEditButton);
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        myApp = MyApp.getInstance();

        recipeShowViewModel = new ViewModelProvider(this).get(RecipeShowViewModel.class);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
                    @Override
                    public boolean canScrollVertically() {
                        return false; // отключаем внутренний скролл
                    }
                };

        ingredientsRecyclerView.setLayoutManager(layoutManager);
        ingredientsRecyclerView.setNestedScrollingEnabled(false);
        ingredientsRecyclerView.setHasFixedSize(false);
        ingredientsAdapter = new ShowRecipeAdapter(myApp);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
    }

    private void changeLanguage() {
        title.setText(MyApp.text(TextKey.SHOW));              // например, "Recipe"
        ingredientsTitle.setText(MyApp.text(TextKey.INGREDIENTS));
        instructionTitle.setText(MyApp.text(TextKey.INSTRUCTION));

        editButton.setText(MyApp.text(TextKey.EDIT));
    }



    private void showImage(Recipes recipe) {
        try {
            String imagePath = recipe.getImage();
            if (imagePath == null || imagePath.isEmpty()) {
                imageView.setImageResource(R.drawable.steak);
                return;
            }

            File file = new File(imagePath);
            if (file.exists()) {
                Glide.with(this)
                        .load(file)
                        .placeholder(R.drawable.steak)
                        .error(R.drawable.steak)
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.steak);
            }
        } catch (Exception e) {
            Log.e("ShowRecipe", "Ошибка показа изображения", e);
            imageView.setImageResource(R.drawable.steak);
        }
    }

    private void showRecipeContent(Recipes recipe) {
        recipeNameTextView.setText(recipe.getName());

        if(instructionTextView.getText() != ""){
            instructionTextView.setText(recipe.getInsctruction());
        } else {
            instructionTitle.setVisibility(INVISIBLE);
        }


        // обновляем ингредиенты
        recipeShowViewModel.refreshDescriptions(recipe);
        recipeShowViewModel.getDesriptions().observe(this, descriptions -> {
            ingredientsAdapter.setData(descriptions);

            // пересчитываем высоту RecyclerView после обновления данных
            ingredientsRecyclerView.post(() -> setRecyclerViewHeightBasedOnChildren(ingredientsRecyclerView));
        });
    }

    /**
     * Вычисляет высоту RecyclerView по всем элементам и устанавливает её
     */
    private void setRecyclerViewHeightBasedOnChildren(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null || adapter.getItemCount() == 0) return;

        int totalHeight = 0;
        int itemCount = adapter.getItemCount();

        for (int i = 0; i < itemCount; i++) {
            RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
            adapter.onBindViewHolder(holder, i);

            // измеряем элемент с учетом padding и wrap_content
            holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );

            totalHeight += holder.itemView.getMeasuredHeight();

            // добавляем margin (если есть)
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            if (lp != null) {
                totalHeight += lp.topMargin + lp.bottomMargin;
            }
        }

        // добавляем padding RecyclerView
        totalHeight += recyclerView.getPaddingTop() + recyclerView.getPaddingBottom();

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = totalHeight;
        recyclerView.setLayoutParams(params);
        recyclerView.requestLayout();
    }

    private void editButtonClick(Recipes recipe) {
        editButton.setOnClickListener(v -> {
            Intent intent = EditRecipe.getIntent(ShowRecipe.this, recipe);
            startActivity(intent);
            finish();
        });
    }

    public static Intent newIntent(Context context, Recipes recipe) {
        Intent intent = new Intent(context, ShowRecipe.class);
        intent.putExtra("Recipe", recipe);
        return intent;
    }
}