package com.example.recipe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipe.R;
import com.example.recipe.model.Recipes;
import com.example.recipe.setting.MyApp;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecipes extends RecyclerView.Adapter<AdapterRecipes.RecipesViewHolder> {



    // ArrayList
    private List<Recipes> recipes = new ArrayList<>();

    private MyApp myApp = MyApp.getInstance();


    //Interface object
    private CountIngredients countIngredients;
    private RecipeOnClickListener recipeOnClickListener;


    // Recipes
    private Recipes selectedRecipe;


    // Setters
    public void setCountIngredients(CountIngredients countIngredients) {
        this.countIngredients = countIngredients;
    }
    public void setSelectedRecipe(Recipes recipe) {
        this.selectedRecipe = recipe;
    }
    public void setRecipes(List<Recipes> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }
    public void setRecipeOnClickListener(RecipeOnClickListener recipeOnClickListener) {
        this.recipeOnClickListener = recipeOnClickListener;
    }


    // Getters
    public Recipes getSelectedRecipe() {
        return selectedRecipe;
    }
    public List<Recipes> getRecipes() {
        return recipes;
    }





    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipes_item, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        Recipes recipe = recipes.get(position);

        // Название рецепта
        holder.recipeTitle.setText(recipe.getName());

        holder.ingredientsCount.setText(String.valueOf(recipe.getIngredient_count()));

        if(myApp.getBaseLanguage().equals("Рус")){
            holder.ingredientsLabel.setText("Ингредиенты:");
        }
        if(myApp.getBaseLanguage().equals("Eng")){
            holder.ingredientsLabel.setText("Ingredients:");
        }
        if(myApp.getBaseLanguage().equals("Каз")){
            holder.ingredientsLabel.setText("Құрамы:");
        }

        if(countIngredients != null){
            Recipes recipeCounter = recipes.get(position);
            countIngredients.CountIngredients(recipeCounter, holder.ingredientsCount);
        }

        // Обработка клика
        holder.recipeCard.setOnClickListener(v -> {
            if (recipeOnClickListener != null) {
                recipeOnClickListener.OnClickRecipe(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    static class RecipesViewHolder extends RecyclerView.ViewHolder {
        private final TextView recipeTitle;
        private final TextView ingredientsCount;
        private final ConstraintLayout recipeCard;

        private final TextView ingredientsLabel;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeCard = itemView.findViewById(R.id.recipeCard);
            ingredientsCount = itemView.findViewById(R.id.ingredientsCount);
            ingredientsLabel = itemView.findViewById(R.id.ingredientsLabel);
        }
    }


    // Interface
    public interface RecipeOnClickListener {
        void OnClickRecipe(Recipes recipe);
    }
    public interface CountIngredients{
        void CountIngredients(Recipes recipe, TextView targetView);
    }


    // Удаление рецепта по его id из адаптера
    public void removeRecipe(int position) {
        if (position >= 0 && position < recipes.size()) {
            recipes.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, recipes.size() - position);
        }
    }


}