package com.uottawa.benjaminmacdonald.cooking_app;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * This class stores an recipe's attributes: name of recipe, whether it's healthy or not,
 * the preparation time, whether it's a favourite recipe or not, a photo stored in a byte array,
 * the description, the instructions to prepare it, the recipe type, the recipe category, and
 * the recipe ID.
 */

public class Recipe extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private Boolean isHealthy;
    private Double preparationTime;
    private Boolean isFavourite;
    private byte[] photo;
    private String description;
    private String instructions;
    private String recipeType;
    private String recipeCategory;

    //*************************** CONSTRUCTOR ******************************************************
    public Recipe(String name, Boolean isHealthy, Double preparationTime, Boolean isFavourite, byte[] photo){
        this.name = name;
        this.isHealthy = isHealthy;
        this.preparationTime = preparationTime;
        this.isFavourite = isFavourite;
        this.photo = photo;
    }

    public Recipe(){
        this.name = "";
        isFavourite = false;
    }

    //*************************** SETTERS AND GETTERS **********************************************
    public String getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setIsHealthy(Boolean isHealthy){
        this.isHealthy = isHealthy;
    }

    public Boolean getIsHealthy(){
        return isHealthy;
    }

    public void setPreparationTime(Double preparationTime){ this.preparationTime = preparationTime; }

    public Double getPreparationTime(){
        return preparationTime;
    }

    public void setIsFavourite(Boolean isFavourite){
        this.isFavourite = isFavourite;
    }

    public Boolean getIsFavourite(){
        return isFavourite;
    }

    public void setPhoto(byte[] photo){
        this.photo = photo;
    }

    public byte[] getPhoto(){
        return photo;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getInstructions() { return instructions; }

    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public String getRecipeCategory() {
        return recipeCategory;
    }

    public void setRecipeCategory(String recipeCategory) { this.recipeCategory = recipeCategory; }

}
