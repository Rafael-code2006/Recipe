package com.example.recipe.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.EventLogTags;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipe.database.RecipeDataBase;
import com.example.recipe.model.Descriptions;
import com.example.recipe.model.Recipes;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditRecipeViewModel extends AndroidViewModel {

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());
    private MutableLiveData<List<Descriptions>> ingredients = new MutableLiveData<>();

    private MutableLiveData<Recipes> recipe = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<List<Descriptions>> getIngredients() {
        return ingredients;
    }

    public LiveData<Recipes> getRecipe() {
        return recipe;
    }

    public EditRecipeViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void loadIngredients(Recipes recipes){
       Disposable disposable = recipeDataBase.descriptionDao().getDescriptionForRecipe(recipes.getId())
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
       compositeDisposable.add(disposable);
    }


    public Completable editRecipe(Recipes recipe){
        return recipeDataBase.recipesDAO()
                .update(recipe)
                .subscribeOn(Schedulers.io());
    }

    public void deleteIngredient(Descriptions ingredient){
        Disposable disposable = recipeDataBase.descriptionDao().remove(ingredient.getId_description())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("EditRecipeViewModel", throwable.getMessage());
                    }
                })
                .subscribe();
    }




    public Completable saveAllIngredients(List<Descriptions> list) {

        List<Completable> completableList = new ArrayList<>();

        for (Descriptions desc : list) {
            completableList.add(
                    recipeDataBase.descriptionDao()
                            .saveIngredient(desc)
                            .subscribeOn(Schedulers.io())
            );
        }

        return Completable.merge(completableList);
    }







    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public void loadRecipe(Recipes recipes) {
        recipeDataBase.recipesDAO().getRecipe(recipes.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("EditRecipeViewModel", throwable.getMessage());
                    }
                })
                .subscribe(new Consumer<Recipes>() {
                    @Override
                    public void accept(Recipes recipes) throws Throwable {
                        recipe.setValue(recipes);
                    }
                });
    }
}
