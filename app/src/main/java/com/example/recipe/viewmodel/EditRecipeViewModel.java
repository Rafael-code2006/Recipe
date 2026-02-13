package com.example.recipe.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipe.database.RecipeDataBase;
import com.example.recipe.model.Descriptions;
import com.example.recipe.model.Recipes;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditRecipeViewModel extends AndroidViewModel {

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());
    private MutableLiveData<List<Descriptions>> ingredients = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<List<Descriptions>> getIngredients() {
        return ingredients;
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


    public void editRecipe(Recipes recipe){
       Disposable disposable = recipeDataBase.recipesDAO().update(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("EditRecipeViewModel1", throwable.getMessage());
                    }
                })
                .subscribe();
        compositeDisposable.add(disposable);
    }

    public void editIngredient(long recipe_id, Descriptions descriptions){
        Disposable disposable = recipeDataBase.descriptionDao().updateAllByRecipeId(
                recipe_id,descriptions.getName()
                        , descriptions.getWeight()
                        , descriptions.getUnit())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("EditRecipeViewModel1", throwable.getMessage());
                    }
                })
                .subscribe();
        compositeDisposable.add(disposable);
    }

    @SuppressLint("CheckResult")
    public void updateAllIngredients(List<Descriptions> ingredients) {
        Completable.fromAction(() -> recipeDataBase.descriptionDao().updateIngredients(ingredients))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.d("DB", "All ingredients updated"),
                        throwable -> Log.e("DB", "Update error", throwable));
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
