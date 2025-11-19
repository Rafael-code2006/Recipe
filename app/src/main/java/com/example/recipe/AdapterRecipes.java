package com.example.recipe;
// cd /d/1AndroidStudioProjects/Recipe

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecipes extends RecyclerView.Adapter<AdapterRecipes.RecipesViewHolder> {

    private RecipeOnClickListener recipeOnClickListener;
    private OnItemTextView onItemTextView;

    private List<Recipes> recipes = new ArrayList<>();

    public void setRecipes(List<Recipes> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public ArrayList<Recipes> getRecipeOnClickListener() {
        return new ArrayList<Recipes>(recipes);
    }

    public void setRecipeOnClickListener(RecipeOnClickListener recipeOnClickListener) {
        this.recipeOnClickListener = recipeOnClickListener;
    }

    public void setOnItemTextView(OnItemTextView onItemTextView) {
        this.onItemTextView = onItemTextView;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recipes_item,
                parent,
                false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        Recipes recipe = recipes.get(position);
        holder.ButtonRecypes.setText(recipe.getName());


        holder.ButtonRecypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeOnClickListener != null) {
                    recipeOnClickListener.OnClickRecipe(recipe);
                }
                if(onItemTextView != null){
                    onItemTextView.onItemTextView(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipesViewHolder extends RecyclerView.ViewHolder{
        private Button ButtonRecypes;
        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButtonRecypes = itemView.findViewById(R.id.ButtonRecypes);
        }
    }

    public interface RecipeOnClickListener{
        void OnClickRecipe(Recipes recipe);
    }

    public interface OnItemTextView{
        void onItemTextView(int position);
    }
}
