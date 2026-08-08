package com.example.recipe.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipe.database.RecipeDataBase;
import com.example.recipe.model.Ingredient;
import com.example.recipe.model.Recipes;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingViewModel extends AndroidViewModel {

    private MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>();

    private MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<List<Recipes>> getRecipes() {
        return recipes;
    }

    public LiveData<List<Ingredient>> getIngredients() {
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
                .subscribe(recipesSetting -> recipes.setValue(recipesSetting));
        compositeDisposable.add(disposable);
    }

    public void updateAllDescription() {
        Disposable disposable = recipeDataBase.descriptionDao().getDescriptions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(descriptions -> ingredients.setValue(descriptions));
        compositeDisposable.add(disposable);
    }


    public void saveAllIngredients(List<Ingredient> ingredient) {
        Disposable disposable = recipeDataBase.descriptionDao().addList(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.d("SettingViewModel1", throwable.getMessage()))
                .subscribe();
        compositeDisposable.add(disposable);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public Single<Long> addRecipeRX(Recipes recipe){
        return Single.fromCallable(() -> recipeDataBase.recipesDAO().add(recipe));
    }
}


