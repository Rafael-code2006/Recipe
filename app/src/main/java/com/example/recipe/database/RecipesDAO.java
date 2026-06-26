package com.example.recipe.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.recipe.model.Recipes;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface RecipesDAO {

    @Insert
    long add(Recipes recipe);

    @Insert
    List<Long> addList(List<Recipes> recipes);


    @Query("DELETE FROM recipes WHERE id = :id")
    Completable remove(long id);
    @Query("SELECT * FROM recipes")
    Single<List<Recipes>> getRecipes();

    @Query("SELECT * FROM recipes WHERE id = :id")
    Single<Recipes> getRecipe(long id);

    @Update
    Completable update(Recipes recipe);


}
