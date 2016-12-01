package com.uottawa.benjaminmacdonald.cooking_app;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by BenjaminMacDonald on 2016-11-30.
 */

public class RecipeType extends RealmObject {
    @PrimaryKey
    String id = UUID.randomUUID().toString();
    String name;

    //****************************** CONSTRUCTORS **************************************************

    public  RecipeType(){

    }

    public RecipeType(String name){
        this.name = name;
    }

    //******************************* SETTERS AND GETTERS ******************************************

    public String getId (){ return id; }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
