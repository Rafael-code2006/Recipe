package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;


public class RecipeIngredient {

    private long recipe_id;

    private List<Descriptions> descriptions;

    public RecipeIngredient(long recipe_id, List<Descriptions> descriptions) {
        this.recipe_id = recipe_id;
        this.descriptions = descriptions;
    }

    public long getRecipe_id() {
        return recipe_id;
    }

    public List<Descriptions> getDescriptions() {
        return descriptions;
    }
}
