package com.uottawa.benjaminmacdonald.cooking_app;

import android.graphics.Bitmap;

/**
 * Created by BenjaminMacDonald on 2016-11-18.
 */

public class Recipe {
    private final String id;
    private String name;
    private Boolean isHealthy;
    private Double preperationTime;
    private Boolean isFavourite;
    private Bitmap photo;
    //photo - using realm?


    //******* CONSTRUCTORS ****************
    public Recipe(){
        id = ""; //CREATE RANDOM ID
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
    public void setPreperationTime(Double preperationTime){
        this.preperationTime = preperationTime;
    }
    public Double getPreperationTime(){
        return preperationTime;
    }
    public void setIsFavourite(Boolean isFavourite){
        this.isFavourite = isFavourite;
    }
    public Boolean getIsFavourite(){
        return isFavourite;
    }
    public void setPhoto(Bitmap photo){
        this.photo = photo;
    }
    public Bitmap getPhoto(){
        return photo;
    }



}
