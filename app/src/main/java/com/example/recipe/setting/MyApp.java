package com.example.recipe.setting;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

public class MyApp extends Application {

    private static MyApp instance;
    private String baseLanguage;
    private List<String> rusUnit;
    private List<String> engUnit;
    private List<String> kazUnit;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // при запуске читаем сохранённое значение
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        baseLanguage = prefs.getString("base.language", "Рус");
        rusUnit = Arrays.asList("кг", "гр", "л", "мл", "мсл", "мчл");
        engUnit = Arrays.asList("kg", "gr", "l", "ml", "tbsp", "tsp");
        kazUnit = Arrays.asList("кг", "гр", "л", "мл", "өақ", "өшқ");
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


   public List<String> unit(){
        if(getBaseLanguage().equals("Рус")){
            return rusUnit;
        } else if(getBaseLanguage().equals("Eng")){
            return engUnit;
        } else {
            return kazUnit;
        }
   }

    public List<String> getRusUnit() {
        return rusUnit;
    }

    public List<String> getEngUnit() {
        return engUnit;
    }

    public List<String> getKazUnit() {
        return kazUnit;
    }

    public static String text(TextKey key){
        return key.getText(instance.getBaseLanguage());
    }

}