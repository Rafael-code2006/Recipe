package com.example.recipe;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Recipes.class, Descriptions.class}, version = 2, exportSchema = false)
public abstract class RecipeDataBase extends RoomDatabase{

    private static final String DB_NAME = "recipe.db";

    private static RecipeDataBase instance = null;
    public static RecipeDataBase getInstance(Application app){
        if(instance == null){
            instance = Room.databaseBuilder(
                    app,
                    RecipeDataBase.class,
                    DB_NAME
            ).build();
        }
        return instance;
    }

    public abstract recipesDAO recipesDAO();

    public abstract DescriptionDao descriptionDao();
}
