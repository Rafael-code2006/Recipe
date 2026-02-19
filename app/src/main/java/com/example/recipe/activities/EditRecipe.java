package com.example.recipe.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.example.recipe.setting.MyApp;
import com.example.recipe.viewmodel.EditRecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class EditRecipe extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RecyclerView recyclerView;

    private TextView title;
    private TextView nameRecipeTitle;
    private TextView ingredientsTitle;
    private TextView instructionTitle;

    private FloatingActionButton addIngredient;

    private EditText editTextInsctruction;
    private EditText editTextRecipe;
    private Button saveButton;
    private EditRecipeViewModel viewModel;
    private AdapterEditRecipes adapter;
    private MyApp myApp;

    private static int totalHeight = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_recipe);
        initView();

        Recipes recipes = (Recipes) getIntent().getSerializableExtra("Recipe");

        setStarted(recipes);

        clickSaveButton(recipes);

        changeLanguage();

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Descriptions> newDescriptions = adapter.getIngredients();
                Descriptions desc = new Descriptions();
                desc.setRecipe_id(recipes.getId());
                desc.setName("");
                desc.setUnit("kg");
                desc.setWeight(1f);
                recyclerView.post(() -> setRecyclerViewHeightBasedOnChildren(recyclerView, 25));
                newDescriptions.add(desc);
                adapter.setIngredient(newDescriptions);
                adapter.notifyDataSetChanged();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }


    private void clickSaveButton(Recipes recipes) {
        saveButton.setOnClickListener(v -> {

            List<Descriptions> descriptions = adapter.getIngredients();

            recipes.setName(editTextRecipe.getText().toString());
            recipes.setInsctruction(editTextInsctruction.getText().toString());

            Disposable disposable = viewModel.saveAllIngredients(descriptions)
                    .andThen(viewModel.editRecipe(recipes))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {

                        Intent intent = ShowRecipe.newIntent(EditRecipe.this, recipes);
                        startActivity(intent);
                        finish();

                    }, throwable -> {
                        Log.d("EditRecipe", throwable.getMessage());
                    });

            compositeDisposable.add(disposable);


        });

    }


    private void setStarted(Recipes recipes) {
        editTextRecipe.setText(recipes.getName());
        viewModel.loadIngredients(recipes);
        viewModel.getIngredients().observe(this, new Observer<List<Descriptions>>() {
            @Override
            public void onChanged(List<Descriptions> descriptions) {
                adapter.setIngredient(descriptions);
                recyclerView.post(() -> setRecyclerViewHeightBasedOnChildren(recyclerView ,50));
            }
        });
        editTextInsctruction.setText(recipes.getInsctruction());
    }

    private void changeLanguage() {
        if(myApp.getBaseLanguage().equals("Рус")){
            title.setText("Изменить Рецепт");
            nameRecipeTitle.setText("Имя");
            ingredientsTitle.setText("Ингредиенты");
            instructionTitle.setText("Инструкция");
            editTextRecipe.setHint("имя");
            editTextInsctruction.setHint("инструкция");
        }
        if(myApp.getBaseLanguage().equals("Eng")){
            title.setText("Edit Recipe");
            nameRecipeTitle.setText("Name");
            ingredientsTitle.setText("Ingredients");
            instructionTitle.setText("Instruction");
            editTextRecipe.setHint("name");
            editTextInsctruction.setHint("insctruction");
        }
        if(myApp.getBaseLanguage().equals("Каз")){
            title.setText("Рецептті Өңдеу");
            nameRecipeTitle.setText("Атауы");
            ingredientsTitle.setText("Құрамы");
            instructionTitle.setText("Нұсқаулық");
            editTextRecipe.setHint("атауы");
            editTextInsctruction.setHint("нұсқаулық");
        }
    }

    private void initView(){
        recyclerView = findViewById(R.id.RecyclerViewIngredients);
        editTextRecipe = findViewById(R.id.EditTextRecipe);
        adapter = new AdapterEditRecipes();
        editTextInsctruction = findViewById(R.id.EditTextInsctructionRecipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(EditRecipeViewModel.class);
        saveButton = findViewById(R.id.RecipeEditButton);
        myApp = MyApp.getInstance();
        title = findViewById(R.id.YourRecipeTextView);
        nameRecipeTitle = findViewById(R.id.TextViewNameRecipe);
        ingredientsTitle = findViewById(R.id.TextViewIngredients);
        instructionTitle = findViewById(R.id.TextViewInstruction);
        addIngredient = findViewById(R.id.floatingActionButton);
    }

    /**
     * Обновляет высоту RecyclerView на основе нового элемента.
     * @param recyclerView - сам RecyclerView
     * @param size - дополнительный размер/отступ (например, 30px)
     */
    private void setRecyclerViewHeightBasedOnChildren(RecyclerView recyclerView, int size) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null || adapter.getItemCount() == 0) return;

        int marginPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,  // стандартный отступ между элементами
                recyclerView.getResources().getDisplayMetrics()
        );

        // Если глобальный totalHeight не определён, инициализируем его
        if (totalHeight == -1) {
            totalHeight = 0;
            for (int i = 0; i < adapter.getItemCount(); i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.UNSPECIFIED
                );
                totalHeight += holder.itemView.getMeasuredHeight() + marginPx + size;
            }
        } else {
            // измеряем только последний (новый) элемент
            int position = adapter.getItemCount() - 1;
            RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(position));
            adapter.onBindViewHolder(holder, position);
            holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.UNSPECIFIED
            );
            totalHeight += holder.itemView.getMeasuredHeight() + marginPx + size;
        }

        // применяем новую высоту
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