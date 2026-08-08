package com.example.recipe.database;
// cd /d/1AndroidStudioProjects/Recipe
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.recipe.model.Ingredient;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DescriptionDao{

    String tableName = "ingredients";

    @Insert
    Completable add(Ingredient description);

    @Insert
    Completable addList(List<Ingredient> desc);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable saveIngredient(Ingredient description);

    @Query("DELETE FROM " + tableName + " WHERE id = :id_description")
    Completable remove(long id_description);

    @Query("DELETE FROM " + tableName + " WHERE recipeId = :id_recipe")
    Completable removeForRecipe(long id_recipe);

    @Query("SELECT * FROM " + tableName)
    Single<List<Ingredient>> getDescriptions();

    @Query("SELECT * FROM " + tableName + " WHERE recipeId = :recipe_id")
    Single<List<Ingredient>> getDescriptionForRecipe(long recipe_id);

    @Query("SELECT * FROM " + tableName + " WHERE id = :id_description")
    Single<Ingredient> getDescription(long id_description);

    @Query("UPDATE " + tableName + " SET name = :name, weight = :weight, unit = :unit WHERE recipeId = :recipeId")
    Completable updateAllByRecipeId(long recipeId, String name, float weight, String unit);


    @Update
    void updateAllIngredients(List<Ingredient> ingredients);
    @Update
    Completable updateIngredients(Ingredient ingredients);
}