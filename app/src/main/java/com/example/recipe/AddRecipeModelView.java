package com.example.recipe;
// cd /d/1AndroidStudioProjects/Recipe
import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddRecipeModelView extends AndroidViewModel {

    private RecipeDataBase recipeDataBase = RecipeDataBase.getInstance(getApplication());

    private android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());

    private MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public AddRecipeModelView(@NonNull Application application) {
        super(application);
        shouldCloseScreen.setValue(false);
    }

    @SuppressLint("CheckResult")
    public void addRecipe(Recipes recipe){
   Disposable disposable = recipeDataBase.recipesDAO().add(recipe)
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action() {
                @Override
                public void run() throws Throwable {
                    shouldCloseScreen.setValue(true);
                }
            });
   compositeDisposable.add(disposable);
    }

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void addDescriptionAsync(Descriptions description, Consumer<Long> callback) {
        executor.execute(() -> {
            long id = recipeDataBase.descriptionDao().insertDescription(description);
            // вернём результат в UI-поток
            handler.post(() -> callback.accept(id));
        });
    }


    public long addDescription(Descriptions description) {
        // Вставка должна быть синхронной, чтобы вернуть ID
        return recipeDataBase.descriptionDao().insertDescription(description);
    }

    public LiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
