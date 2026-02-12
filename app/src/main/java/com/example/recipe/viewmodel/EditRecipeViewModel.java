package com.example.recipe.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipe.database.DescriptionDao;
import com.example.recipe.database.RecipeDataBase;
import com.example.recipe.model.Descriptions;
import com.example.recipe.model.Recipes;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditRecipeViewModel extends AndroidViewModel {

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());
    private MutableLiveData<List<Descriptions>> ingredients = new MutableLiveData<>();

    public LiveData<List<Descriptions>> getIngredients() {
        return ingredients;
    }

    public EditRecipeViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void loadIngredients(Recipes recipes){
        recipeDataBase.descriptionDao().getDescriptionForRecipe(recipes.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Descriptions>>() {
                    @Override
                    public void accept(List<Descriptions> descriptions) throws Throwable {
                        ingredients.setValue(descriptions);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        throw new Throwable(throwable.getMessage());
                    }
                });
    }


}
