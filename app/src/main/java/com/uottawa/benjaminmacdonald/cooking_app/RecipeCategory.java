package com.uottawa.benjaminmacdonald.cooking_app;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * This class stores the recipe category's name and ID.
 */

public class RecipeCategory extends RealmObject {
    @PrimaryKey
    String id = UUID.randomUUID().toString();
    String name;

    //*************************** CONSTRUCTOR ******************************************************
    public RecipeCategory(){} //Realm requires a constructor with no parameters.
    public RecipeCategory(String name){
        this.name = name;
    }

    //*************************** SETTERS AND GETTERS **********************************************
    public String getId() { return  id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
