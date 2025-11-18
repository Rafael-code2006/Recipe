package com.example.recipe;

public class Descriptions {

    private int id_description;
    private String name_ingredient;
    private String type_weight_ingredient;
    private float weight;


    public Descriptions(String name_ingredient, String type_weight_ingredient, float weight) {
        this.name_ingredient = name_ingredient;
        this.type_weight_ingredient = type_weight_ingredient;
        this.weight = weight;
    }

    public String getName_ingredient() {
        return name_ingredient;
    }

    public String getType_weight_ingredient() {
        return type_weight_ingredient;
    }

    public float getWeight() {
        return weight;
    }


    public void setName_ingredient(String name_ingredient) {
        this.name_ingredient = name_ingredient;
    }

    public void setType_weight_ingredient(String type_weight_ingredient) {
        this.type_weight_ingredient = type_weight_ingredient;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }


}
