package com.example.recipe;
// cd /d/1AndroidStudioProjects/Recipe
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface DescriptionDao{


    @Query("SELECT * FROM descriptions")
    LiveData<List<Descriptions>>getDescriptions();


    @Query("SELECT * FROM descriptions WHERE id_description = :id_description")
    Descriptions getDescription(int id_description);

    @Query("DELETE FROM descriptions WHERE id_description = :id_description")
    Completable remove(int id_description);

    @Insert
    long insertDescription(Descriptions description);


}