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



    public Recipes(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public Recipes(String name) {
        this.name = name;
    }

    public long getId() {
        return this.id;
    }


    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Recipes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
