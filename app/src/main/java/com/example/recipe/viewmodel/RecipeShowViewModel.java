package com.example.recipe.viewmodel;

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

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecipeShowViewModel extends AndroidViewModel {

    private static final String TAG = "RecipeShowViewModel1";


    // DataBase
    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());


    // MutableLiveData
    private MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>();
    private MutableLiveData<Recipes> recipe = new MutableLiveData<>();
    private MutableLiveData<List<Descriptions>> descriptions = new MutableLiveData<>();


    // CompositeDisposable
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    // Getters
    public LiveData<List<Recipes>> getRecipes(){
        Log.d("ViewModelTest", "Количество обьектов: ");
        return recipes;
    }
    public LiveData<Recipes> getRecipe() {
        return recipe;
    }
    public LiveData<List<Descriptions>> getDesriptions(){
        return descriptions;
    }


    // Конструктор
    public RecipeShowViewModel(@NonNull Application application) {
        super(application);
    }



    // Обновление списка ингридиентов по id рецепта
    public void refreshDescriptions(Recipes recipes){
        Disposable disposable = recipeDataBase.descriptionDao().getDescriptionForRecipe(recipes.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Descriptions>>() {
                    @Override
                    public void accept(List<Descriptions> getDescriptions) throws Throwable {
                        descriptions.setValue(getDescriptions);
                    }
                });
    }


    // Возврат рецепта по id
    @SuppressLint("CheckResult")
    public void loadRecipe(Recipes recipeUser){
        recipeDataBase.recipesDAO().getRecipe(recipeUser.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d("RecipeShowViewModel1", throwable.getMessage());
                    }
                })
                .subscribe(new Consumer<Recipes>() {
                    @Override
                    public void accept(Recipes getRecipe) throws Throwable {
                        recipe.setValue(getRecipe);
                    }
                });
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
