package com.example.recipe;
// cd /d/1AndroidStudioProjects/Recipe

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipes {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private long id_description;



    public Recipes(int id, String name, long id_description) {
        this.id = id;
        this.name = name;
        this.id_description = id_description;
    }

    @Ignore
    public Recipes(String name, long id_description) {
        this.name = name;
        this.id_description = id_description;
    }

    public int getId() {
        return this.id;
    }


    public String getName() {
        return name;
    }

    public long getId_description() {
        return id_description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId_description(long id_description) {
        this.id_description = id_description;
    }
}
