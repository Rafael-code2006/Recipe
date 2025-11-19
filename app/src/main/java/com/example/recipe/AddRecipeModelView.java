package com.example.recipe;
// cd /d/1AndroidStudioProjects/Recipe
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AddRecipeModelView extends AndroidViewModel {

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());

    private MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();
    public AddRecipeModelView(@NonNull Application application) {
        super(application);
        shouldCloseScreen.setValue(false);
    }

    public void addRecipe(Recipes recipe){
        recipeDataBase.recipesDAO().add(recipe);
        shouldCloseScreen.setValue(true);
    }

    public LiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }
}
