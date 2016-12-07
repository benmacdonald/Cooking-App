package com.uottawa.benjaminmacdonald.cooking_app;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * This class stores an ingredient's attributes: name of ingredient,
 * amount of ingredient, the unit of measurement, and the recipe ID that it's associated to.
 */

public class Ingredient extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private Double amount;
    private String unitOfMeasurement;
    private String recipeId;

    //*************************** CONSTRUCTOR ******************************************************
    public Ingredient(String name, Double amount, String unitOfMeasurement){
        this.name = name;
        this.amount = amount;
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public Ingredient(String recipeId){
        this.recipeId = recipeId;
        this.name = "";
        this.amount = 0.0;
        this.unitOfMeasurement = "";
    }

    public Ingredient(){
        this.name = "";
        this.amount = 0.0;
        this.unitOfMeasurement = "";
    }

    //*************************** SETTERS AND GETTERS **********************************************
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) { this.unitOfMeasurement = unitOfMeasurement; }

    public String getRecipeId() { return recipeId;}

    public void setRecipeId() { this.recipeId = recipeId; }
}
