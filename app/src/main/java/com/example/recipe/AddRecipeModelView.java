package com.example.recipe;
// cd /d/1AndroidStudioProjects/Recipe
import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddRecipeModelView extends AndroidViewModel {

    private static final String TAG = "AddRecipeViewModel1";

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());

    private MutableLiveData<Long> IdRecipes = new MutableLiveData<>();

    private MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();

    private MutableLiveData<List<Descriptions>> descriptions = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public AddRecipeModelView(@NonNull Application application) {
        super(application);
        shouldCloseScreen.setValue(false);
    }

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




    private Single<Long> addRecipeRX(Recipes recipe){
        return Single.fromCallable(() -> recipeDataBase.recipesDAO().add(recipe));
    }



    @SuppressLint("CheckResult")
    public void loadDescription(int recipe_id){
        Disposable disposable = recipeDataBase.descriptionDao().getDescriptionWithRecipe(recipe_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Descriptions>>() {
                    @Override
                    public void accept(List<Descriptions> description) throws Throwable {
                        descriptions.setValue(description);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.getMessage().toString());
                    }
                });
        compositeDisposable.add(disposable);
    }


    @SuppressLint("CheckResult")
    public void addDescription(Descriptions description) {
        Disposable disposable = recipeDataBase.descriptionDao().insertDescription(description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "Ингредиент добавлен: " + description.getName());
                }, throwable -> {
                    Log.e(TAG, "Ошибка добавления ингредиента", throwable);
                });
        compositeDisposable.add(disposable);
    }


    public LiveData<Long> getIdRecipes() {
        return IdRecipes;
    }

    public LiveData<List<Descriptions>> getDescriptions() {
        return descriptions;
    }

    public LiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
