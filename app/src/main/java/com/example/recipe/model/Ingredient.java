package com.example.recipe.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import lombok.Setter;

@Setter
@Entity(tableName = "ingredients")
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public long id;
    @SerializedName("recipe_id")
    public long recipeId;



    /**
     * Основная инфомация
     */
    @SerializedName("name")
    private String name;

    @SerializedName("group")
    private String group;



    /**
     * Количество
     */

    @SerializedName("quantity")
    private float quantity;

    @SerializedName("unit")
    private String unit;

    @SerializedName("weight")
    private boolean weight;

    @SerializedName("volume")
    private boolean volume;


    /**
     * Пищевая ценность
     */

    @SerializedName("calories")
    private long calories;

    @SerializedName("proteins")
    private int proteins;

    @SerializedName("fats")
    private int fats;

    @SerializedName("carbohydrates")
    private int carbohydrates;


    /**
     * Дополнительно
     */

    @SerializedName("brand")
    private String brand;

    @SerializedName("notes")
    private String notes;


    public Ingredient(long id, long recipeId, String name, String group, float quantity, String unit, boolean weight, boolean volume, long calories, int proteins, int fats, int carbohydrates, String brand, String notes) {
        this.id = id;
        this.recipeId = recipeId;
        this.name = name;
        this.group = group;
        this.quantity = quantity;
        this.unit = unit;
        this.weight = weight;
        this.volume = volume;
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
        this.quantity = weight;
        this.unit = unit;
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

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setWeight(boolean weight) {
        this.weight = weight;
    }

    public void setVolume(boolean volume) {
        this.volume = volume;
    }

    public void setCalories(long calories) {
        this.calories = calories;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public void setCarbohydrates(int carbohydrates) {
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

    public float getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isWeight() {
        return weight;
    }

    public boolean isVolume() {
        return volume;
    }

    public long getCalories() {
        return calories;
    }

    public int getProteins() {
        return proteins;
    }

    public int getFats() {
        return fats;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public String getBrand() {
        return brand;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id_description=" + id +
                ", recipe_id=" + recipeId +
                ", name='" + name + '\'' +
                ", weight=" + quantity +
                ", unit='" + unit + '\'' +
                '}';
    }
}

