package com.example.recipe;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface recipesDAO{

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipes>> getRecipes();

    @Query("SELECT * FROM recipes WHERE id = :id")
    Recipes getRecipe(int id);

    @Query("SELECT r.name AS recipe_name, d.name_description, d.type, d.weight " +
            "FROM recipes r INNER JOIN descriptions d " +
            "ON r.id_description = d.id_description")
    LiveData<List<RecipeWithDescription>> getRecipesWithDetails();


    @Insert
    Completable add(Recipes recipe);

    @Query("DELETE FROM recipes WHERE id = :id")
    Completable remove(long id);

}
