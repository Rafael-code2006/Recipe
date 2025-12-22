package com.example.recipe;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.EventLogTags;
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

    private static final String TAG = "MainViewModel1";

    private MutableLiveData<Integer> count_ingredients = new MutableLiveData<>();

    public LiveData<Integer> getCount_ingredients() {
        return count_ingredients;
    }

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());

    private MutableLiveData<List<Recipes>> recipes = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public MainViewModel(@NonNull Application application) {
        super(application);
        refreshList();
    }

    public LiveData<List<Recipes>> getRecipes(){
        Log.d("ViewModelTest", "Количество обьектов: ");
        return recipes;
    }

    @SuppressLint("CheckResult")
    public void getCountDescripton(Recipes recipe){
        recipeDataBase.descriptionDao().getDescriptionWithRecipe(recipe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Descriptions>>() {
                    @Override
                    public void accept(List<Descriptions> descriptions) throws Throwable {
                        int count = 0;
                        for(Descriptions x : descriptions){
                            count +=1;
                        }
                        Log.d(TAG, "Колво: " + count);
                        count_ingredients.setValue(count);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.getMessage().toString());
                    }
                });
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
