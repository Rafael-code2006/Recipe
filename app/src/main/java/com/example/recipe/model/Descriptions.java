package com.example.recipe.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "descriptions")
public class Descriptions {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_description")
    public long id_description;
    @SerializedName("recipe_id")
    public long recipe_id;

    @SerializedName("name")
    private String name;
    @SerializedName("weight")
    private float weight;

    @SerializedName("unit")
    private String unit;


    public Descriptions(long id_description, long recipe_id, String name, float weight, String unit) {
        this.id_description = id_description;
        this.recipe_id = recipe_id;
        this.name = name;
        this.weight = weight;
        this.unit = unit;
    }

    @Ignore
    public Descriptions(long recipe_id, String name, float weight, String unit) {
        this.recipe_id = recipe_id;
        this.name = name;
        this.weight = weight;
        this.unit = unit;
    }

    public Descriptions() {
    }

    public long getId_description() {
        return id_description;
    }

    public long getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(long recipe_id) {
        this.recipe_id = recipe_id;
    }

    public void setId_description(long id_description) {
        this.id_description = id_description;
    }

    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }

    public String getUnit() {
        return unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Descriptions{" +
                "id_description=" + id_description +
                ", recipe_id=" + recipe_id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", unit='" + unit + '\'' +
                '}';
    }
}

