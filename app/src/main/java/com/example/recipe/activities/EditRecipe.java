package com.example.recipe.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.R;
import com.example.recipe.adapters.AdapterEditRecipes;
import com.example.recipe.model.Descriptions;
import com.example.recipe.model.Recipes;
import com.example.recipe.viewmodel.EditRecipeViewModel;

import java.io.Serializable;
import java.util.List;

public class EditRecipe extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextRecipe;
    private Button saveButton;
    private EditRecipeViewModel viewModel;
    private  AdapterEditRecipes adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_recipe);
        initView();

        Recipes recipes = (Recipes) getIntent().getSerializableExtra("Recipe");
        editTextRecipe.setText(recipes.getName());
        viewModel.loadIngredients(recipes);
        viewModel.getIngredients().observe(this, new Observer<List<Descriptions>>() {
            @Override
            public void onChanged(List<Descriptions> descriptions) {
                adapter.setIngredient(descriptions);
                recyclerView.post(() -> setRecyclerViewHeightBasedOnChildren(recyclerView));
            }
        });



        adapter.setCheckRecipes(ingredient -> {
                    Log.d("EditRecipe11", "name: " + ingredient.getName());
                    Log.d("EditRecipe11", "weight" + ingredient.getWeight());
                    Log.d("EditRecipe11", "unit: " + ingredient.getUnit());
                });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Descriptions> descriptions = adapter.getIngredients();
                viewModel.updateAllIngredients(descriptions);
                Intent intent = ShowRecipe.newIntent(EditRecipe.this, recipes);
                startActivity(intent);
                finish();


            }
        });
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

    private void initView(){
        recyclerView = findViewById(R.id.RecyclerViewIngredients);
        editTextRecipe = findViewById(R.id.EditTextRecipe);
        adapter = new AdapterEditRecipes();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(EditRecipeViewModel.class);
        saveButton = findViewById(R.id.RecipeEditButton);
    }

    private void setRecyclerViewHeightBasedOnChildren(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) return;

        int totalHeight = 0;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
            adapter.onBindViewHolder(holder, i);
            holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.UNSPECIFIED
            );
            totalHeight += holder.itemView.getMeasuredHeight();

            // Если в item layout есть margin, можно добавить здесь, например 8dp в px:
            int marginPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 8, recyclerView.getResources().getDisplayMetrics());
            totalHeight += marginPx + 50;
        }


        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = totalHeight;
        recyclerView.setLayoutParams(params);
        recyclerView.requestLayout();
    }





    public static Intent getIntent(Context context, Recipes recipes){
     Intent intent = new Intent(context, EditRecipe.class);
        intent.putExtra("Recipe", (Serializable) recipes);
     return intent;
    }
}