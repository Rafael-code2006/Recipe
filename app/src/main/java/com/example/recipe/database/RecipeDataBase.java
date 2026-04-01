package com.example.recipe.database;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.recipe.model.Descriptions;
import com.example.recipe.model.Recipes;

@Database(entities = {Recipes.class, Descriptions.class}, version = 4, exportSchema = false)
public abstract class RecipeDataBase extends RoomDatabase {

    private static final String DB_NAME = "recipe.db";
    private static RecipeDataBase instance = null;

    public static synchronized RecipeDataBase getInstance(Application app) {
        if (instance == null) {
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