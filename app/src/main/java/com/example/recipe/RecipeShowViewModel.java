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
    private MutableLiveData<List<Descriptions>> descriptions = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public RecipeShowViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Recipes>> getRecipes(){
        Log.d("ViewModelTest", "Количество обьектов: ");
        return recipes;
    }

    public LiveData<List<Descriptions>> getDesriptions(){
        return descriptions;
    }

    public void refreshDescriptions(){
        Disposable disposable = recipeDataBase.descriptionDao().getDescriptions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Descriptions>>() {
                    @Override
                    public void accept(List<Descriptions> descriptionsForShow) throws Throwable {
                        descriptions.setValue(descriptionsForShow);
                    }
                });
    }

    public void refreshRecipes(){
        Disposable disposableRefresh = recipeDataBase.recipesDAO().getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Recipes>>() {
                    @Override
                    public void accept(List<Recipes> recipesForDB) throws Throwable {
                        recipes.setValue(recipesForDB);
                    }
                });
        compositeDisposable.add(disposableRefresh);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
