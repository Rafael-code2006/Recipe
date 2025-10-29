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

    @Query("UPDATE recipes SET name = :name, description = :description WHERE id = :id")
    void changeRecipe(int id, String name, String description);

    @Insert
    void add(Recipes recipe);

    @Query("DELETE FROM recipes WHERE id = :id")
    void remove(long id);

}
