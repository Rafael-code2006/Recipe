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

public class MainViewModel extends AndroidViewModel {

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());

    private MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Recipes>> getRecipes(){
        Log.d("ViewModelTest", "Количество обьектов: ");
        return recipes;
    }

    public void refreshList(){
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

    public void remove(Recipes recipe){
      Disposable disposableRemove = recipeDataBase.recipesDAO().remove(recipe.getId())
                .subscribeOn(Schedulers.io())
                .subscribe();
      compositeDisposable.add(disposableRemove);
        refreshList();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
