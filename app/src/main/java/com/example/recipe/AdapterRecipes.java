package com.example.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecipes extends RecyclerView.Adapter<AdapterRecipes.RecipesViewHolder> {



    private CountIngredients countIngredients;

    public void setCountIngredients(CountIngredients countIngredients) {
        this.countIngredients = countIngredients;
    }

    private RecipeOnClickListener recipeOnClickListener;
    private List<Recipes> recipes = new ArrayList<>();

    private Recipes selectedRecipe;

    public void setSelectedRecipe(Recipes recipe) {
        this.selectedRecipe = recipe;
    }

    public Recipes getSelectedRecipe() {
        return selectedRecipe;
    }

    // Устанавливаем список рецептов
    public void setRecipes(List<Recipes> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    // Получаем список рецептов
    public List<Recipes> getRecipes() {
        return new ArrayList<>(recipes);
    }

    // Устанавливаем обработчик клика
    public void setRecipeOnClickListener(RecipeOnClickListener recipeOnClickListener) {
        this.recipeOnClickListener = recipeOnClickListener;
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

        if(countIngredients != null){
            countIngredients.OnClickRecipe(recipe);
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

    // ViewHolder
    static class RecipesViewHolder extends RecyclerView.ViewHolder {
        private final TextView recipeTitle;
        private final TextView ingredientsCount;
        private final ConstraintLayout recipeCard;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeCard = itemView.findViewById(R.id.recipeCard);
            ingredientsCount = itemView.findViewById(R.id.ingredientsCount);
        }
    }

    // Интерфейс для клика
    public interface RecipeOnClickListener {
        void OnClickRecipe(Recipes recipe);
    }

    private interface CountIngredients{
        int OnClickRecipe(Recipes recipe);
    }
}