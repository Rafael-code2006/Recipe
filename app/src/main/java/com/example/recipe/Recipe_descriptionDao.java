package com.example.recipe;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface Recipe_descriptionDao {

    @Insert
    long add(RecipeIngredient recipeIngredient);

    @Query("SELECT * FROM recipe_ingredient")
    Single<RecipeIngredient> getRecipeIngredient();

    @Delete
    Completable remove(RecipeIngredient recipeIngredient);


    @Query("SELECT * FROM recipe_ingredient WHERE recipe_id = :recipe_id")
    Single<List<RecipeIngredient>> getRecipeDescriptions(long recipe_id);
}
