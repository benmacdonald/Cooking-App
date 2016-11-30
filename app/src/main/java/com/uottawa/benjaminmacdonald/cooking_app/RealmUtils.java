package com.uottawa.benjaminmacdonald.cooking_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by BenjaminMacDonald on 2016-11-28.
 */

public final class RealmUtils {

    private Realm realm;

    public RealmUtils(Context context){
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(realmConfiguration);
    }


    //Finds all the recipes that are favoured in realm
    public List<Recipe> queryFavouriteRecipes(){

        RealmResults<Recipe>  favResult = realm.where(Recipe.class)
                .equalTo("isFavourite",true)
                .findAll();

        return realm.copyFromRealm(favResult);
    }
    //Finds all the recipes in realm
    public List<Recipe> queryAllRecipes(){

        RealmResults<Recipe> queryRecipes = realm.where(Recipe.class)
                .findAll();

        return  realm.copyFromRealm(queryRecipes);
    }

    //Finds all non favourites
    public List<Recipe> queryAllNonFavourite(){

        RealmResults<Recipe> query = realm.where(Recipe.class)
                .equalTo("isFavourite",false)
                .findAll();
        return realm.copyFromRealm(query);
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
    public void saveRecipe (Recipe recipe) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(recipe);
        realm.commitTransaction();
    }

    //save ingredient to DB
    public void saveIngredient (List<Ingredient> ingredients) {
        realm.beginTransaction();
        for (int i = 0; i<ingredients.size(); i++){
            realm.copyToRealmOrUpdate(ingredients.get(i));
        }
        realm.commitTransaction();
    }

    //get indredientList from recipeID
    public List<Ingredient> getIngredientsFromRecipeID(String recipeID){

        RealmResults<Ingredient> ingredientResults = realm.where(Ingredient.class)
                .equalTo("recipeId",recipeID)
                .findAll();
        return realm.copyFromRealm(ingredientResults);
    }

    public Recipe getRecipeFromID (String recipeID){
        RealmResults<Recipe>  recipeResult = realm.where(Recipe.class)
                .equalTo("id", recipeID)
                .findAll();
        return realm.copyFromRealm(recipeResult.get(0));
    }
}
