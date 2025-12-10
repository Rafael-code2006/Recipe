package com.example.recipe;
// cd /d/1AndroidStudioProjects/Recipe
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DescriptionDao{


    @Query("SELECT * FROM descriptions")
    Single<List<Descriptions>> getDescriptions();


    @Query("SELECT * FROM descriptions WHERE recipe_id = :recipe_id")
    Single<List<Descriptions>> getDescriptionWithRecipe(int recipe_id);


    @Query("SELECT * FROM descriptions WHERE id_description = :id_description")
    Descriptions getDescription(int id_description);

    @Query("DELETE FROM descriptions WHERE id_description = :id_description")
    Completable remove(int id_description);

    @Insert
    Completable insertDescription(Descriptions description);





}