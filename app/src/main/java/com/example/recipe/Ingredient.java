package com.example.recipe;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "descriptions")
public class Descriptions {

    @PrimaryKey(autoGenerate = true)
    int id_description;
    private String name_description;
    private float weight;

    public Descriptions(int id_description, String name, float weight) {
        this.id_description = id_description;
        this.name_description = name;
        this.weight = weight;
    }

    public Descriptions(){};

    @Ignore
    public Descriptions(String name, float weight) {
        this.name_description = name;
        this.weight = weight;
    }


    public int getId_description() {
        return id_description;
    }

    public String getName_description() {
        return name_description;
    }


    public float getWeight() {
        return weight;
    }

    public void setName_description(String name_description) {
        this.name_description = name_description;
    }


    public void setWeight(float weight) {
        this.weight = weight;
    }
}

