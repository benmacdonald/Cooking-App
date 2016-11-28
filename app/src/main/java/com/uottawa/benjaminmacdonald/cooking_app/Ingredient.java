package com.uottawa.benjaminmacdonald.cooking_app;

/**
 * Created by BenjaminMacDonald on 2016-11-18.
 */

public class Ingredient {
    private String id;
    private String name;
    private Double amount;
    private String unitOfMeasurement;


    //********** CONSTRUCTOR ************

    public Ingredient(String name,Double amount, String unitOfMeasurement){
        this.name = name;
        this.amount = amount;
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public Ingredient(){
        this.name = "";
        this.amount = 0.0;
        this.unitOfMeasurement = "";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }
}
