package com.uottawa.benjaminmacdonald.cooking_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by BenjaminMacDonald on 2016-11-28.
 */

public final class RealmUtils {

    public RealmUtils(){}


    //Finds all the recipes that are favoured in realm
    public RealmResults<Recipe> queryFavouriteRecipes(Realm realm){
        RealmResults<Recipe>  favResult = realm.where(Recipe.class)
                .equalTo("isFavourite",true)
                .findAll();

        return favResult;
    }
    //Finds all the recipes in realm
    public RealmResults<Recipe> queryAllRecipes(Realm realm){
        RealmResults<Recipe> queryRecipes = realm.where(Recipe.class)
                .findAll();
        return  queryRecipes;
    }

    //converts byte array to bitmap
    public Bitmap convertToBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    //converts bitmap to byte array
    public byte[] convertToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }

    //save recipe to DB
    public void saveRecipe (Realm realm, Recipe recipe) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(recipe);
        realm.commitTransaction();
    }

    //save ingredient to DB
    public void saveIngredient (Realm realm, ArrayList<Ingredient> ingredients) {
        realm.beginTransaction();
        for (int i = 0; i<ingredients.size(); i++){
            realm.copyToRealmOrUpdate(ingredients.get(i));
        }
        realm.commitTransaction();
    }

    public Recipe getRecipeFromID (Realm realm, String recipeID){
        RealmResults<Recipe>  recipeResult = realm.where(Recipe.class)
                .equalTo("id", recipeID)
                .findAll();
        return realm.copyFromRealm(recipeResult.get(0));
    }
}
