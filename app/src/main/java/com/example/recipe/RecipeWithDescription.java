package com.example.recipe;

public class RecipeWithDescription {
    private String name;
    private String name_description;
    private float weight;



    public RecipeWithDescription(String name, String name_description, float weight) {
        this.name = name;
        this.name_description = name_description;
        this.weight = weight;
    }


    public String getName() {
        return name;
    }

    public String getName_description() {
        return name_description;
    }


    public float getWeight() {
        return weight;
    }
}
