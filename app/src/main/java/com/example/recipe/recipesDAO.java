package com.example.recipe;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface recipesDAO{

    @Query("SELECT * FROM recipes")
    List<Recipes> getRecipes();

    @Query("SELECT * FROM recipes WHERE id = :id")
    Recipes getRecipe(int id);

    @Insert
    void add(Recipes recipe);

    @Query("DELETE FROM recipes WHERE id = :id")
    void remove(long id);

}
