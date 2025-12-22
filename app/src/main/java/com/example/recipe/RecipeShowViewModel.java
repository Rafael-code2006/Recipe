package com.example.recipe;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecipeShowViewModel extends AndroidViewModel {

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());

    private MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>();

    private MutableLiveData<Recipes> recipe = new MutableLiveData<>();
    private MutableLiveData<List<Descriptions>> descriptions = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public RecipeShowViewModel(@NonNull Application application) {
        super(application);
    }

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

    public void refreshDescriptions(long recipe_id){
        Disposable disposable = recipeDataBase.descriptionDao().getDescriptionWithRecipe(recipe_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Descriptions>>() {
                    @Override
                    public void accept(List<Descriptions> getDescriptions) throws Throwable {
                        descriptions.setValue(getDescriptions);
                    }
                });
    }
    public void refreshRecipes(Recipes recipes){
        Disposable disposableRefresh =
                recipeDataBase
                   .descriptionDao()
                        .getDescriptionWithRecipe(recipes.getId())
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                Log.d("RecipeShowViewModel1", throwable.getMessage().toString());
                            }
                        })
                                .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<List<Descriptions>>() {
                                                    @Override
                                                    public void accept(List<Descriptions> descriptions) throws Throwable {

                                                    }
                                                });
        compositeDisposable.add(disposableRefresh);
    }

    public void loadRecipe(long recipe_id){
        recipeDataBase.recipesDAO().getRecipe(recipe_id)
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
