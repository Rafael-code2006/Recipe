package com.example.recipe.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Setter;

@Setter
@Entity(tableName = "ingredients")
public class Ingredient implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public long id;
    @SerializedName("recipe_id")
    public long recipeId;



    /**
     * Основная инфомация
     */
    @SerializedName("name")
    private String name; // Название

    @SerializedName("group")
    private String group; // Группа



    /**
     * Количество
     */

    @SerializedName("weight")
    private float weight; // Вес

    @SerializedName("unit")
    private String unit; // Тип веса


    /**
     * Пищевая ценность
     */

    @SerializedName("calories")
    private float calories;

    @SerializedName("proteins")
    private float proteins;

    @SerializedName("fats")
    private float fats;

    @SerializedName("carbohydrates")
    private float carbohydrates;


    /**
     * Дополнительно
     */

    @SerializedName("brand")
    private String brand;

    @SerializedName("notes")
    private String notes;


    public Ingredient(long id, long recipeId, String name, String group, float quantity, String unit, float calories, float proteins, float fats, float carbohydrates, String brand, String notes) {
        this.id = id;
        this.recipeId = recipeId;
        this.name = name;
        this.group = group;
        this.weight = quantity;
        this.unit = unit;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.brand = brand;
        this.notes = notes;
    }

    public Ingredient() {
    }

    @Ignore
    public Ingredient(long recipe_id, String name, float weight, String unit) {
        this.recipeId = recipe_id;
        this.name = name;
        this.weight = weight;
        this.unit = unit;
    }


    @Override
    public String toString() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("recipeId", recipeId);
        json.addProperty("name", name);
        json.addProperty("group", group);
        json.addProperty("weight", weight);
        json.addProperty("unit", unit);
        json.addProperty("calories", calories);
        json.addProperty("proteins", proteins);
        json.addProperty("fats", fats);
        json.addProperty("carbohydrates", carbohydrates);
        json.addProperty("brand", brand);
        json.addProperty("notes", notes);

        return json.toString();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }





    public long getId() {
        return id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public float getWeight() {
        return weight;
    }

    public String getUnit() {
        return unit;
    }

    public float getCalories() {
        return calories;
    }

    public float getProteins() {
        return proteins;
    }

    public float getFats() {
        return fats;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public String getBrand() {
        return brand;
    }

    public String getNotes() {
        return notes;
    }
}

