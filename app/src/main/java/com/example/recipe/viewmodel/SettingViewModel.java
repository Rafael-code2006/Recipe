package com.example.recipe.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipe.database.RecipeDataBase;
import com.example.recipe.database.RecipeDataBase_Impl;
import com.example.recipe.model.Descriptions;
import com.example.recipe.model.Recipes;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingViewModel extends AndroidViewModel {

    private MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>();

    private MutableLiveData<List<Descriptions>> ingredients = new MutableLiveData<>();

    public LiveData<List<Recipes>> getRecipes() {
        return recipes;
    }

    public LiveData<List<Descriptions>> getIngredients() {
        return ingredients;
    }

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());
    public SettingViewModel(@NonNull Application application) {
        super(application);
    }


    public void updateAllRecipe(){
        Disposable disposable = recipeDataBase.recipesDAO().getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Recipes>>() {
                    @Override
                    public void accept(List<Recipes> recipesSetting) throws Throwable {
                        recipes.setValue(recipesSetting);
                    }
                });
    }

    public void updateAllDescription() {
        Disposable disposable = recipeDataBase.descriptionDao().getDescriptions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Descriptions>>() {
                    @Override
                    public void accept(List<Descriptions> descriptions) throws Throwable {
                        ingredients.setValue(descriptions);
                    }
                });
    }

    public void saveAllRecipe(List<Recipes> recipes) {
        Completable.fromAction(() -> {
                    recipeDataBase.recipesDAO().addList(recipes);
                })
                .subscribeOn(Schedulers.io())      // выполняем в фоне
                .observeOn(AndroidSchedulers.mainThread()) // результат на UI-потоке
                .subscribe(() -> {
                    Log.d("DB", "Рецепты сохранены");
                }, throwable -> {
                    Log.e("DB", "Ошибка сохранения", throwable);
                });
    }






}
