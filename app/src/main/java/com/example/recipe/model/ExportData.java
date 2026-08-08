package com.example.recipe.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExportData {
    @SerializedName("recipes")
    private List<Recipes> recipes;

    @SerializedName("ingredients")
    private List<Ingredient> ingredients;


    public ExportData(List<Recipes> recipes, List<Ingredient> ingredients) {
        this.recipes = recipes;
        this.ingredients = ingredients;
    }

    public List<Recipes> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipes> recipes) {
        this.recipes = recipes;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
