package com.example.recipe;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface recipesDAO{

    @Query("SELECT * FROM recipes")
    Single<List<Recipes>> getRecipes();

    @Query("SELECT * FROM recipes WHERE id = :id")
    Recipes getRecipe(int id);

    @Insert
    Completable add(Recipes recipe);

    @Query("DELETE FROM recipes WHERE id = :id")
    Completable remove(long id);

}
