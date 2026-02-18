package com.example.recipe.setting;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApp extends Application {

    private static MyApp instance;
    private String baseLanguage;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // при запуске читаем сохранённое значение
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        baseLanguage = prefs.getString("base.language", "Рус");
    }

    public static MyApp getInstance() {
        return instance;
    }

    public String getBaseLanguage() {
        return baseLanguage;
    }

    public void setBaseLanguage(String lang) {
        baseLanguage = lang;

        // сохраняем в SharedPreferences
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        prefs.edit().putString("base.language", lang).apply();
    }
}