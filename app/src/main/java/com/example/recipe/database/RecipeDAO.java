package com.example.recipe.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.recipe.model.Recipe;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface RecipeDAO {

    @Insert
    long add(Recipe recipe);

    @Insert
    List<Long> addList(List<Recipe> recipes);


    @Query("DELETE FROM Recipe WHERE id = :id")
    Completable remove(long id);
    @Query("SELECT * FROM Recipe")
    Single<List<Recipe>> getRecipes();

    @Query("SELECT * FROM Recipe WHERE id = :id")
    Single<Recipe> getRecipe(long id);

    @Update
    Completable update(Recipe recipe);


}
