package com.uottawa.benjaminmacdonald.cooking_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by BenjaminMacDonald on 2016-11-28.
 */

public final class RealmUtils {

    private Realm realm;

    public RealmUtils(Context context){
        realm = Realm.getDefaultInstance();
    }


    //Finds all the recipes that are favoured in realm
    public RealmResults<Recipe> queryFavouriteRecipes(){

        RealmResults<Recipe>  favResult = realm.where(Recipe.class)
                .equalTo("isFavourite",true)
                .findAll();
        return favResult;
    }
    //Finds all the recipes in realm
    public RealmResults<Recipe> queryAllRecipes(){

        RealmResults<Recipe> queryRecipes = realm.where(Recipe.class)
                .findAllAsync();

        return  queryRecipes;
    }

    //Finds all non favourites
    public RealmResults<Recipe> queryAllNonFavourite(){

        RealmResults<Recipe> query = realm.where(Recipe.class)
                .equalTo("isFavourite",false)
                .findAll();
        return query;
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

    @Deprecated
    public void saveRecipe (Recipe recipe) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(recipe);
        realm.commitTransaction();
    }

    //creates recipe
    public Recipe createRecipe(final String name){
        final Recipe[] recipe = {new Recipe()};
        realm.executeTransactionAsync(new Realm.Transaction(){
            @Override
            public void execute(Realm bgRealm) {
                recipe[0].setName(name);
                recipe[0].setIsFavourite(false);
                recipe[0] = bgRealm.copyToRealmOrUpdate(recipe[0]);
            }
        });
        return recipe[0];
    }

    //saves recipe
    public void updateRecipe(final String id, final String name, final Boolean isHealthy, final Boolean isFavourite,
                             final Bitmap photo, final String description, final String instruction){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm){
                Recipe recipe = getRecipeFromIdAsync(bgRealm, id);
                if(recipe != null){
                    recipe.setName(name);
                    recipe.setIsHealthy(isHealthy);
                    recipe.setIsFavourite(isFavourite);
                    recipe.setPhoto(convertToByteArray(photo));
                    recipe.setDescription(description);
                    recipe.setInstructions(instruction);
                }
            }
        });
    }

    //save ingredient to DB
    public void saveIngredient (List<Ingredient> ingredients) {
        realm.beginTransaction();
        for (int i = 0; i<ingredients.size(); i++){
            realm.copyToRealmOrUpdate(ingredients.get(i));
        }
        realm.commitTransaction();
    }

//    public void saveIngredient(String name, Double amount, String unit, String recipeId){
//        realm.executeTransactionAsync(new Realm.Transaction() {
//
//        });
//
//    }

    //get indredientList from recipeID
    public List<Ingredient> getIngredientsFromRecipeID(String recipeID){

        RealmResults<Ingredient> ingredientResults = realm.where(Ingredient.class)
                .equalTo("recipeId",recipeID)
                .findAll();
        return realm.copyFromRealm(ingredientResults);
    }

    @Nullable
    public Recipe getRecipeFromID (String recipeID){
        RealmResults<Recipe>  recipeResult = realm.where(Recipe.class)
                .equalTo("id", recipeID)
                .findAll();
        if(recipeResult.size() > 0){
            return recipeResult.get(0);
        }
        return null;
    }

    @Nullable
    public Recipe getRecipeFromIdAsync(Realm bgRealm,String recipeID){
        RealmResults<Recipe>  recipeResult = bgRealm.where(Recipe.class)
                .equalTo("id", recipeID)
                .findAll();
        if(recipeResult.size() > 0){
            return recipeResult.get(0);
        }
        return null;
    }

    public void updateFavouriteForRecipe(final String recipeId, final Boolean value){
        realm.executeTransactionAsync(new Realm.Transaction() {

            @Override
            public void execute(Realm bgRealm){
                Recipe recipe = getRecipeFromIdAsync(bgRealm,recipeId);
                if(recipe != null){
                    recipe.setIsFavourite(value);
                }
            }

        });
    }
}
