package com.example.recipe;
// cd /d/1AndroidStudioProjects/Recipe
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DescriptionDao{

    @Insert
    Completable add(Descriptions description);

    @Query("DELETE FROM descriptions WHERE id_description = :id_description")
    Completable remove(long id_description);

    @Query("DELETE FROM descriptions WHERE recipe_id = :id_recipe")
    Completable removeForRecipe(long id_recipe);

    @Query("SELECT * FROM descriptions")
    Single<List<Descriptions>> getDescriptions();

    @Query("SELECT * FROM descriptions WHERE recipe_id = :recipe_id")
    Single<List<Descriptions>> getDescriptionForRecipe(long recipe_id);

    @Query("SELECT * FROM descriptions WHERE id_description = :id_description")
    Single<Descriptions> getDescription(long id_description);









}