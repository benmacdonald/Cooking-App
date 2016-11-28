package com.uottawa.benjaminmacdonald.cooking_app;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by BenjaminMacDonald on 2016-11-18.
 */

public class Recipe extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private Boolean isHealthy;
    private Double preparationTime;
    private Boolean isFavourite;
    private byte[] photo;
    //photo - using realm?


    //******* CONSTRUCTORS ****************
    public Recipe(String name, Boolean isHealthy, Double preparationTime, Boolean isFavourite, byte[] photo){
        this.name = name;
        this.isHealthy = isHealthy;
        this.preparationTime = preparationTime;
        this.isFavourite = isFavourite;
        this.photo = photo;
    }

    public Recipe(){
        this.name = "";
    }


    //*********** METHODS ******************

    private void createIngredient(){}






    //********* GETTERS AND SETTERS *********

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
    public void setPreparationTime(Double preparationTime){
        this.preparationTime = preparationTime;
    }
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



}
