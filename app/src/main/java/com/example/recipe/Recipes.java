package com.example.recipe;
// cd /d/1AndroidStudioProjects/Recipe

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Recipes {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;



    public Recipes(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Ignore
    public Recipes(String name, String discription) {
        this.id = 0;
        this.name = name;
        this.description = discription;
    }

    public int getId() {
        return this.id;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
