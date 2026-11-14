package com.example.recipe.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipe.database.RecipeDataBase;
import com.example.recipe.model.Ingredient;
import com.example.recipe.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditRecipeViewModel extends AndroidViewModel {

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());
    private MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>();

    private MutableLiveData<Recipe> recipe = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<List<Ingredient>> getIngredients() {
        return ingredients;
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public EditRecipeViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public void loadIngredients(Recipe recipe){
       Disposable disposable = recipeDataBase.ingredientDao().getDescriptionForRecipe(recipe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Ingredient>>() {
                    @Override
                    public void accept(List<Ingredient> descriptions) throws Throwable {
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


    public Completable editRecipe(Recipe recipe){
        return recipeDataBase.recipesDAO()
                .update(recipe)
                .subscribeOn(Schedulers.io());
    }

    public void deleteIngredient(Ingredient ingredient){
        Disposable disposable = recipeDataBase.ingredientDao().remove(ingredient.getId())
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




    public Completable saveAllIngredients(List<Ingredient> list) {

        List<Completable> completableList = new ArrayList<>();

        for (Ingredient desc : list) {
            completableList.add(
                    recipeDataBase.ingredientDao()
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

    public void loadRecipe(Recipe recipe) {
        recipeDataBase.recipesDAO().getRecipe(recipe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("EditRecipeViewModel", throwable.getMessage());
                    }
                })
                .subscribe(new Consumer<Recipe>() {
                    @Override
                    public void accept(Recipe recipe) throws Throwable {
                        EditRecipeViewModel.this.recipe.setValue(recipe);
                    }
                });
    }
}
