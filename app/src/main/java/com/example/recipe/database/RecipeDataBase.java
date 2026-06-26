package com.example.recipe.database;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.recipe.model.Descriptions;
import com.example.recipe.model.Recipes;

@Database(entities = {Recipes.class, Descriptions.class}, version = 2, exportSchema = false)
public abstract class RecipeDataBase extends RoomDatabase {

    private static final String DB_NAME = "recipe.db";
    private static volatile RecipeDataBase instance = null; // volatile для thread-safety

    public static synchronized RecipeDataBase getInstance(Application app) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            app.getApplicationContext(), // безопаснее использовать applicationContext
                            RecipeDataBase.class,
                            DB_NAME
                    )
                    // .allowMainThreadQueries() — УБРАНО, используй async (LiveData/Flow/suspend)
                    .fallbackToDestructiveMigration() // опционально: при смене версии пересоздаёт БД
                    .build();
        }
        return instance;
    }

    public abstract RecipesDAO recipesDAO();     // RecipesDAO с заглавной R
    public abstract DescriptionDao descriptionDao();
}