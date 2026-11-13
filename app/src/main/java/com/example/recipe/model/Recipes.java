package com.example.recipe.model;
// cd /d/1AndroidStudioProjects/Recipe

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "recipes")
public class Recipes implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("instructions")
    private String instructions;
    @Ignore
    @SerializedName("ingredient_count")
    private int ingredient_count;
   @SerializedName("image")
    private String image;



    public Recipes(long id, String name, String instructions, String image) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.image = image;
    }

    public Recipes() {
    }

    @Ignore
    public Recipes(String name, String instructions, String image) {
        this.name = name;
        this.instructions = instructions;
        this.image = image;
    }

    @Override
    public String toString() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("name", name);
        json.addProperty("instructions", instructions);
        json.addProperty("ingredient_count", ingredient_count);
        json.addProperty("image", image);

        return json.toString();
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Ignore
    public int getIngredient_count() {
        return ingredient_count;
    }

    @Ignore
    public void setIngredient_count(int ingredient_count) {
        this.ingredient_count = ingredient_count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
       this.image = image;
    }
}
