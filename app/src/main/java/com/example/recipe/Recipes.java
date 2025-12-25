package com.example.recipe;
// cd /d/1AndroidStudioProjects/Recipe

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipes {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String insctruction;
    @Ignore
    private int ingredient_count;



    public Recipes(long id, String name, String insctruction) {
        this.id = id;
        this.name = name;
        this.insctruction = insctruction;
    }

    @Ignore
    public Recipes(String name, String insctruction) {
        this.name = name;
        this.insctruction = insctruction;
    }

    public long getId() {
        return this.id;
    }


    public String getName() {
        return name;
    }

    public String getInsctruction() {
        return insctruction;
    }

    @Ignore
    public int getIngredient_count() {
        return ingredient_count;
    }

    @Ignore
    public void setIngredient_count(int ingredient_count) {
        this.ingredient_count = ingredient_count;
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
