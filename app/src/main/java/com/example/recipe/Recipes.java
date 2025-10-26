package com.example.recipe;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Recipes {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public Recipes(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Ignore
    public Recipes(String name) {
        new Recipes(0, name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
