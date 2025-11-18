package com.example.recipe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Recipes>> getRecipes(){ return recipeDataBase.recipesDAO().getRecipes(); }

    public void remove(Recipes recipe){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                recipeDataBase.recipesDAO().remove(recipe.getId());
            }
        });
    }


}
