package com.example.recipe.model;
// cd /d/1AndroidStudioProjects/Recipe

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "recipes")
public class Recipes implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("insctruction")
    private String insctruction;
    @Ignore
    @SerializedName("ingredient_count")
    private int ingredient_count;
   @SerializedName("image")
    private String image;



    public Recipes(long id, String name, String insctruction, String image) {
        this.id = id;
        this.name = name;
        this.insctruction = insctruction;
        this.image = image;
    }

    public Recipes() {
    }

    @Ignore
    public Recipes(String name, String insctruction, String image) {
        this.name = name;
        this.insctruction = insctruction;
        this.image = image;
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

    public String getInsctruction() {
        return insctruction;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInsctruction(String insctruction) {
        this.insctruction = insctruction;
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

    @Override
    public String toString() {
        return "Recipes{" +
                "\nid=" + id +
                "\n, name='" + name + '\'' +
                "\n, insctruction='" + insctruction + '\'' +
                "\n, ingredient_count=" + ingredient_count +
                '}';
    }
}
