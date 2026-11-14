package com.example.recipe.viewmodel;
// cd /d/1AndroidStudioProjects/Recipe
import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipe.model.Ingredient;
import com.example.recipe.model.Recipe;
import com.example.recipe.database.RecipeDataBase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddRecipeViewModel extends AndroidViewModel {

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
    public AddRecipeViewModel(@NonNull Application application) {
        super(application);
        shouldCloseScreen.setValue(false);
    }



    // Добавление рецепта
    @SuppressLint("CheckResult")
    public void addRecipe(Recipe recipe){
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
    public Single<Long> TestAddRecipe(Recipe recipe){
        return Single.fromCallable(()-> recipeDataBase.recipesDAO().add(recipe));
    }


    // Single метод для добавления рецепта
    private Single<Long> addRecipeRX(Recipe recipe){
        return Single.fromCallable(() -> recipeDataBase.recipesDAO().add(recipe));
    }


    // Добавление игридиента
    @SuppressLint("CheckResult")
    public void addDescription(Ingredient description) {
//        Disposable disposable = recipeDataBase.ingredientDao().add(description)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(() -> {
//                    Log.d(TAG, "Ингредиент добавлен: " + description.getName());
//                }, throwable -> {
//                    Log.e(TAG, "Ошибка добавления ингредиента", throwable);
//                });
//        compositeDisposable.add(disposable);
    }

    public void deleteRecipe(Recipe recipe){
        recipeDataBase.recipesDAO().remove(recipe.getId())
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
