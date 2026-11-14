package com.example.recipe.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.recipe.database.RecipeDataBase;
import com.example.recipe.model.Ingredient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class AddIngredientViewModel extends AndroidViewModel {

    private static final String TAG_LOG = "AddIngredientVM";

    private CompositeDisposable composite = new CompositeDisposable();

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());

    private MutableLiveData<Long> ingredientId = new MutableLiveData<>();

    private LiveData<Long> getIngredientId(){
        return ingredientId;
    }



    public AddIngredientViewModel(@NonNull Application application) {
        super(application);
    }


    public void addIngredient(Ingredient ingredient){
        Disposable disposable = addIngredientRx(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.d(TAG_LOG, "Ошибка при добавлении ингредиента: {'message'=" + throwable.getMessage() + "}");
                })
                .subscribe(aLong -> {
                    ingredientId.setValue(aLong);
                    Log.d(TAG_LOG, "Добавлен ингредиент: {'id'=" + aLong  + "}");
                });
        composite.add(disposable);
    }


    @Override
    protected void onCleared() {
        composite.dispose();
        super.onCleared();
    }

    private Single<Long> addIngredientRx(Ingredient ingredient){
        return Single.fromCallable(() -> recipeDataBase.ingredientDao().add(ingredient));
    }

}
