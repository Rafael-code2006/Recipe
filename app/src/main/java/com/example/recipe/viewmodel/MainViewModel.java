package com.example.recipe.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipe.model.Descriptions;
import com.example.recipe.model.Recipes;
import com.example.recipe.database.RecipeDataBase;

import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());

    private MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>();
    private MutableLiveData<List<Descriptions>> descriptions = new MutableLiveData<>();
    private MutableLiveData<HashMap<Recipes, Integer>> countIngredients = new MutableLiveData<>();
    private MutableLiveData<Recipes> recipe = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<Recipes> getRecipe() {
        return recipe;
    }

    public LiveData<HashMap<Recipes, Integer>> getCountIngredients() {
        return countIngredients;
    }

    public LiveData<List<Descriptions>> getDescriptions() {
        return descriptions;
    }

    public LiveData<List<Recipes>> getRecipes() {
        return recipes;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        refreshList();
    }

    // ================== Обновление списка рецептов ==================
    public void refreshList() {
        Disposable disposableRefresh = recipeDataBase.recipesDAO().getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        recipesForDB -> {
                            Log.d(TAG, "countRecipe: " + recipesForDB.size());
                            recipes.setValue(recipesForDB);
                        },
                        throwable -> Log.e(TAG, "Ошибка при загрузке рецептов", throwable)
                );
        compositeDisposable.add(disposableRefresh);
    }

    // ================== Удаление рецепта ==================
    public void remove(Recipes recipe) {
        Disposable disposableRemove = recipeDataBase.recipesDAO().remove(recipe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d(TAG, "Рецепт удалён: " + recipe.getName()),
                        throwable -> Log.e(TAG, "Ошибка при удалении рецепта", throwable)
                );
        compositeDisposable.add(disposableRemove);
    }

    // ================== Удаление ингредиентов для рецепта ==================
    public void removeDescriptionForRecipe(Recipes recipe) {
        Disposable disposable = recipeDataBase.descriptionDao().removeForRecipe(recipe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d(TAG, "Ингредиенты для рецепта удалены"),
                        throwable -> Log.e(TAG, "Ошибка при удалении ингредиентов", throwable)
                );
        compositeDisposable.add(disposable);
    }

    // ================== Загрузка ингредиентов ==================
    public void loadIngredients(Recipes recipesItem) {
        Disposable disposable = recipeDataBase.descriptionDao().getDescriptionForRecipe(recipesItem.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        getDescriptions -> {
                            int count = getDescriptions.size();
                            HashMap<Recipes, Integer> map = countIngredients.getValue();
                            if (map == null) map = new HashMap<>();
                            map.put(recipesItem, count);
                            countIngredients.setValue(map);
                        },
                        throwable -> Log.e(TAG, "Ошибка при загрузке ингредиентов", throwable)
                );
        compositeDisposable.add(disposable);
    }

    // ================== Получение рецепта по id ==================
    public void getRecipeForId(Recipes recipeDB) {
        Disposable disposable = recipeDataBase.recipesDAO().getRecipe(recipeDB.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        r -> recipe.setValue(r),
                        throwable -> Log.e(TAG, "Ошибка при получении рецепта по ID", throwable)
                );
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}