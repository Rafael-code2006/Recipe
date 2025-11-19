package com.example.recipe;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Descriptions {

    @PrimaryKey(autoGenerate = true)
    private int id_description;
    private String name;
    private String type;
    private float weight;

    public Descriptions(int id_description, String name, String type, float weight) {
        this.id_description = id_description;
        this.name = name;
        this.type = type;
        this.weight = weight;
    }

    @Ignore
    public Descriptions(String name, String type, float weight) {
        this.name = name;
        this.type = type;
        this.weight = weight;
    }


    public int getId_description() {
        return id_description;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public float getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}

