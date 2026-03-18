package com.example.recipe.viewmodel;
// cd /d/1AndroidStudioProjects/Recipe
import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipe.model.Descriptions;
import com.example.recipe.model.Recipes;
import com.example.recipe.database.RecipeDataBase;

import java.util.concurrent.Callable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddRecipeModelView extends AndroidViewModel {

    private static final String TAG = "AddRecipeViewModel1";

    // DataBase
    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());


    // MutableLiveData
    private MutableLiveData<Long> IdRecipes = new MutableLiveData<>();
    private MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();


    // CompositeDisposable
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    // Getters
    public LiveData<Long> getIdRecipes() {
        return IdRecipes;
    }




    // Конструктор
    public AddRecipeModelView(@NonNull Application application) {
        super(application);
        shouldCloseScreen.setValue(false);
    }



    // Добавление рецепта
    @SuppressLint("CheckResult")
    public void addRecipe(Recipes recipe){
        Disposable disposable = addRecipeRX(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipeId -> {
                    IdRecipes.setValue(recipeId);
                    Log.d(TAG, "Рецепт добавлен с id=" + recipeId);
                    shouldCloseScreen.setValue(true);
                }, throwable -> {
                    Log.e(TAG, "Ошибка добавления рецепта", throwable);
                });
        compositeDisposable.add(disposable);
    }

    @SuppressLint("CheckResult")
    public Single<Long> TestAddRecipe(Recipes recipe){
        return Single.fromCallable(()-> recipeDataBase.recipesDAO().add(recipe));
    }


    // Single метод для добавления рецепта
    private Single<Long> addRecipeRX(Recipes recipe){
        return Single.fromCallable(() -> recipeDataBase.recipesDAO().add(recipe));
    }


    // Добавление игридиента
    @SuppressLint("CheckResult")
    public void addDescription(Descriptions description) {
        Disposable disposable = recipeDataBase.descriptionDao().add(description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "Ингредиент добавлен: " + description.getName());
                }, throwable -> {
                    Log.e(TAG, "Ошибка добавления ингредиента", throwable);
                });
        compositeDisposable.add(disposable);
    }

    public void deleteRecipe(Recipes recipes){
        recipeDataBase.recipesDAO().remove(recipes.getId())
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
