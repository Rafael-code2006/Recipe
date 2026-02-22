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
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel1";


    // DataBase
    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());


    // MutableLiveData

    private MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>();

    private MutableLiveData<List<Descriptions>> descriptions = new MutableLiveData<>();

    private MutableLiveData<HashMap<Recipes, Integer>> countIngredients = new MutableLiveData<>();

    private MutableLiveData<Recipes> recipe = new MutableLiveData<>();


    // CompositeDisposable
    private CompositeDisposable compositeDisposable = new CompositeDisposable();



    // Getters


    public LiveData<Recipes> getRecipe() {
        return recipe;
    }

    public LiveData<HashMap<Recipes, Integer>> getCountIngredients() {
        return countIngredients;
    }

    public LiveData<List<Descriptions>> getDescriptions() {
        return descriptions;
    }



    // Конструктор
    public MainViewModel(@NonNull Application application) {
        super(application);
        refreshList();
    }


    // Возврат коллекции рецептов
    public LiveData<List<Recipes>> getRecipes(){
        Log.d("ViewModelTest", "Количество обьектов: ");
        return recipes;
    }


    // Обновление списка рецептов в recipes
    public void refreshList(){
        Disposable disposableRefresh = recipeDataBase.recipesDAO().getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Recipes>>() {
                    @Override
                    public void accept(List<Recipes> recipesForDB) throws Throwable {
                        Log.d(TAG, "countRecipe: " + recipesForDB.size());
                        recipes.setValue(recipesForDB);
                    }
                });
        compositeDisposable.add(disposableRefresh);
    }


    // Удаление рецепта по его id
    public void remove(Recipes recipe){
      Disposable disposableRemove = recipeDataBase.recipesDAO().remove(recipe.getId())
                .subscribeOn(Schedulers.io())
                .subscribe();
      compositeDisposable.add(disposableRemove);
    }

    public void removeDescriptionForRecipe(Recipes recipe){
        Disposable disposable = recipeDataBase.descriptionDao().removeForRecipe(recipe.getId())
                .subscribeOn(Schedulers.io())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.getMessage().toString());
                    }
                })
                .subscribe();
    }


    public void loadIngredients(Recipes recipes){
        Disposable disposable = recipeDataBase.descriptionDao().getDescriptionForRecipe(recipes.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Descriptions>>() {
                    @Override
                    public void accept(List<Descriptions> getDescriptions) throws Throwable {
                        int count = getDescriptions.size();

                        HashMap<Recipes, Integer> map = countIngredients.getValue();

                        if(map == null){
                           map = new HashMap<>();
                        }

                        map.put(recipes, count);

                        countIngredients.setValue(map);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.getMessage().toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void getRecipeForId(Recipes recipeDB) {
        Disposable disposable = recipeDataBase.recipesDAO().getRecipe(recipeDB.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Recipes>() {
                    @Override
                    public void accept(Recipes recipes) throws Throwable {
                        recipe.setValue(recipes);
                    }
                });
        compositeDisposable.add(disposable);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }


}
