package com.uottawa.benjaminmacdonald.cooking_app.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import com.uottawa.benjaminmacdonald.cooking_app.Ingredient;
import com.uottawa.benjaminmacdonald.cooking_app.Recipe;
import com.uottawa.benjaminmacdonald.cooking_app.RecipeCategory;
import com.uottawa.benjaminmacdonald.cooking_app.RecipeType;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Case;
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

    //******************************** RECIPE CLASS ************************************************


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
                             final Bitmap photo, final String description, final String instruction,
                                final String typeId, final String catId){
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
                    recipe.setRecipeType(typeId);
                    recipe.setRecipeCategory(catId);
                }
            }
        });
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


    //******************************** INGREDIENT CLASS ********************************************

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


    // **************************** TYPE CLASS *****************************************************

    @Nullable
    public String getTypeIDFromName(String name){
        RecipeType query = realm.where(RecipeType.class)
                .equalTo("name",name, Case.INSENSITIVE)
                .findFirst();
        if(query!=null){
            return query.getId();
        }
        return null;

    }

    public RecipeType getTypeFromId(String id){
        RealmResults<RecipeType> query = realm.where(RecipeType.class)
                .equalTo("id",id)
                .findAll();
        return  query.get(0);
    }

    public void createType(final String name){
        realm.beginTransaction();
        RecipeType recipeType = realm.createObject(RecipeType.class, UUID.randomUUID().toString());
        recipeType.setName(name);
        realm.commitTransaction();
    }


    //******************************** CATEGORY CLASS **********************************************

    public String getCategoryIDFromName(String name){
        RecipeCategory query = realm.where(RecipeCategory.class)
                .equalTo("name",name, Case.INSENSITIVE)
                .findFirst();
        if(query != null){
            return query.getId();
        }
        return null;
    }

    public RecipeCategory getCategoryFromId(String id){
        RealmResults<RecipeCategory> query = realm.where(RecipeCategory.class)
                .equalTo("id",id)
                .findAll();
        return query.get(0);
    }

    public void createCategory(final String name){
        realm.beginTransaction();
        RecipeCategory recipeCategory = realm.createObject(RecipeCategory.class, UUID.randomUUID().toString());
        recipeCategory.setName(name);
        realm.commitTransaction();
    }


}
