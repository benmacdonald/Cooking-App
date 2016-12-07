package com.uottawa.benjaminmacdonald.cooking_app.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.ArrayAdapter;

import com.uottawa.benjaminmacdonald.cooking_app.Ingredient;
import com.uottawa.benjaminmacdonald.cooking_app.Recipe;
import com.uottawa.benjaminmacdonald.cooking_app.RecipeCategory;
import com.uottawa.benjaminmacdonald.cooking_app.RecipeType;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;


/**
 * Utility class used to connect to the realm database.
 *
 */

public final class RealmUtils {

    private Realm realm;

    public RealmUtils(Context context){
        realm = Realm.getDefaultInstance();
    }

    //******************************** RECIPE CLASS ************************************************

    /**
     * This method queries realm for all the favourite recipes and returns them.
     *
     * @return favResult - a  RealmResult (list) of class type Recipe that are favourited.
     */
    public RealmResults<Recipe> queryFavouriteRecipes(){

        RealmResults<Recipe>  favResult = realm.where(Recipe.class)
                .equalTo("isFavourite",true)
                .findAll();
        return favResult;
    }

    /**
     * finds all the recipes in the realm database asynchronously
     *
     * @return queryRecipes - all the recipes saved in realm.
     */
    public RealmResults<Recipe> queryAllRecipesAsync(){

        RealmResults<Recipe> queryRecipes = realm.where(Recipe.class)
                .findAllAsync();

        return  queryRecipes;
    }

    /**
     * finds all the recipes in the realm database
     *
     * @return queryRecipes - all the recipes saved in realm.
     */
    public RealmResults<Recipe> queryAllRecipes(){
        RealmResults<Recipe> queryRecipes = realm.where(Recipe.class)
                .findAll();

        return  queryRecipes;
    }



    /**
    * gets all the recipes in the realm database as a list of recipes
    *
    * @return recipeList  - a all of the recipes in realm as a list
    */
    public List<Recipe> getAllRecipesAsList(){
        List<Recipe> recipeList = new ArrayList<Recipe>();
        RealmResults<Recipe> queryRecipes = realm.where(Recipe.class)
                .findAll();
        recipeList = realm.copyFromRealm(queryRecipes);
        return recipeList;
    }


    /**
    * find all the recipes in realm that are not favourited
    *
    * @return nonFavourite - RealmResults of Recipes that are noted favourited.
    */
    public RealmResults<Recipe> queryAllNonFavourite(){

        RealmResults<Recipe> nonFavourite = realm.where(Recipe.class)
                .equalTo("isFavourite",false)
                .findAllAsync();
        return nonFavourite;
    }


    /**
    * converts a byte array into a bitmap. (this is used when getting the bitmap from realm)
    *
    * @param byteArray - the byte array to be converted
    * @return Bitmap - the bitmap to be used
    */
    public Bitmap convertToBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    /**
    * converts a bitmap into a byte array. (this is used to store the bitmap in realm)
    *
    * @param bitmap - the bitmap to be converted
    * @return byteArray - the byte array to be used
    */
    public byte[] convertToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }

    /**
    * creates a recipe and adds it to realm
    *
    * @param name - the name of the recipe to be created
    * @return recipe[0] - the recipe object that was created
    */
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

    /**
     *  updates the recipe in realm
     *
     * @param id - the id of the recipe to be updated
     * @param name - the name of the recipe to be updated
     * @param isHealthy - the value of the healthy boolean
     * @param isFavourite - the value of the favourite boolean
     * @param photo - the photo as a bitmap
     * @param description - the description of the recipe
     * @param instruction - the instruction of the recipe
     * @param typeId - the id of the RecipeType that the recipe is linked to
     * @param catId - the id of the RecipeCategory that the recipe is linked to
     */
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

    /**
     * gets a single recipe from the realm database
     *
     * @param recipeID - the id of the recipe
     * @return - the recipe if it exists in realm, null otherwise
     */
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

    /**
     * get a single recipe from the realm database, it allows the recipe to be retried from inside an async block
     *
     * @param bgRealm - the realm instance inside the aync block
     * @param recipeID - the id of the recipe
     * @return - the recipe iif it exists in realm, null otherwise
     */
    public Recipe getRecipeFromIdAsync(Realm bgRealm,String recipeID){
        RealmResults<Recipe>  recipeResult = bgRealm.where(Recipe.class)
                .equalTo("id", recipeID)
                .findAll();
        if(recipeResult.size() > 0){
            return recipeResult.get(0);
        }
        return null;
    }


    /**
     * updates the recipe and sets its favourite value
     *
     * @param recipeId - the id of the recipe to be updated
     * @param value - the boolean value of to be set, (true = favourite) (false = unfavourite)
     */
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


    /**
     * Boolean operator search that handles AND, OR, NOT and filters by TYPE, CATEGORY and ISHEALTHY
     *
     * @param mIngre - ingredients that must be included (AND)
     * @param oIngre - ingredinets that are option (OR)
     * @param nIngre - ingredients that should be excluded (NOT)
     * @param type - the type to be filtered by
     * @param cat - the category to be filtered by
     * @param healthy - the healthy type to be filtered by
     * @return a list of recipes that matches the criteria
     */
    public RealmResults<Recipe> getRecipeFromIngredientsBoolean(List<String> mIngre, List<String> oIngre,
                                                                List<String> nIngre, String type, String cat, String healthy) {
        //Sets use for query duplicate
        ArrayList<String> checkArray = new ArrayList<String>();
        ArrayList<String> necessaryRecipe = new ArrayList<String>();
        ArrayList<String> finalRecipe = new ArrayList<>();

        RealmResults<Recipe> recipes = null;
        RealmResults<Ingredient> ingredients = null;
        RealmResults<Ingredient> optionalIngredients = null;
        RealmResults<Ingredient> notIngredients = null;
        RealmQuery<Ingredient> ingredientQuery = realm.where(Ingredient.class);
        RealmQuery<Ingredient> optionalQuery = realm.where(Ingredient.class);
        RealmQuery<Ingredient> notQuery = realm.where(Ingredient.class);


        if (mIngre.size() > 0) {

            for (int i = 0; i < mIngre.size(); i++) {
                if (i == 0) {
                    ingredientQuery.equalTo("name", mIngre.get(i), Case.INSENSITIVE);
                } else {
                    ingredientQuery.or().equalTo("name", mIngre.get(i), Case.INSENSITIVE);
                }
            }

            ingredients = ingredientQuery.findAll();
            for (Ingredient ingredient : ingredients) {
                checkArray.add(ingredient.getRecipeId());
            }

            for (String id : checkArray) {
                int occurrences = Collections.frequency(checkArray, id);
                if (occurrences == mIngre.size()) {
                    necessaryRecipe.add(id);
                }
            }
        }

        if (oIngre.size() <= 0) {
            finalRecipe = necessaryRecipe;
        } else {

            for (int i = 0; i < oIngre.size(); i++) {
                if (i == 0) {
                    optionalQuery.equalTo("name", oIngre.get(i), Case.INSENSITIVE);
                } else {
                    optionalQuery.or().equalTo("name", oIngre.get(i), Case.INSENSITIVE);
                }

                optionalIngredients = optionalQuery.findAll();
            }
            if (mIngre.size() <= 0) {
                for (Ingredient ingredient : optionalIngredients) {
                    finalRecipe.add(ingredient.getRecipeId());
                }
            } else {
                for (Ingredient ingredient : optionalIngredients) {
                    if (necessaryRecipe.contains(ingredient.getRecipeId())) {
                        finalRecipe.add(ingredient.getRecipeId());
                    }
                }
            }
        }

            if (nIngre.size() >= 0) {
                if (oIngre.size() <= 0 && mIngre.size() <= 0) {
                    for( int i =0; i< nIngre.size(); i++){
                        if (i == 0) {
                            notQuery.equalTo("name", nIngre.get(i), Case.INSENSITIVE);
                        } else {
                            notQuery.or().equalTo("name", nIngre.get(i), Case.INSENSITIVE);
                        }
                        notIngredients = notQuery.findAll();
                    }
                    if(notIngredients != null){
                        List<Recipe> tmp = getAllRecipesAsList();
                        for(Recipe recipe : tmp){
                            finalRecipe.add(recipe.getId());
                        }
                        for(Ingredient ingredient : notIngredients){
                            if(finalRecipe.contains(ingredient.getRecipeId())){
                                finalRecipe.remove(ingredient.getRecipeId());
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < nIngre.size(); i++) {
                        if (i == 0) {
                            notQuery.equalTo("name", nIngre.get(i), Case.INSENSITIVE);
                        } else {
                            notQuery.or().equalTo("name", nIngre.get(i), Case.INSENSITIVE);
                        }

                        notIngredients = notQuery.findAll();
                    }
                    if (notIngredients != null) {
                        for (Ingredient ingredient : notIngredients) {
                            if (finalRecipe.contains(ingredient.getRecipeId())) {
                                finalRecipe.remove(ingredient.getRecipeId());
                            }
                        }
                    }
                }
            }

        if(nIngre.size() <=0 && oIngre.size() <= 0 && mIngre.size() <= 0){
            recipes = queryAllRecipes();
        } else {
            recipes = getRecipeFromListId(finalRecipe);
        }

        if(type != "Type" && type != "All"){
            List<String> typeTmp = new ArrayList<String>();
            RealmResults<RecipeType> recipeTypes = realm.where(RecipeType.class).equalTo("name",type,Case.INSENSITIVE).findAll();
            for(Recipe recipe : recipes ){
                if(recipe.getRecipeType() != null) {
                    if (recipe.getRecipeType().equals(recipeTypes.get(0).getId())) {
                        typeTmp.add(recipe.getId());
                    }
                }
            }
            recipes = getRecipeFromListId(typeTmp);
        }

        if(cat != "Category" && cat != "All"){
            List<String> categoryTmp = new ArrayList<String>();
            RealmResults<RecipeCategory> recipeCategory = realm.where(RecipeCategory.class).equalTo("name",cat,Case.INSENSITIVE).findAll();
            for(Recipe recipe : recipes){
                if(recipe.getRecipeCategory() != null){
                    if(recipe.getRecipeCategory().equals(recipeCategory.get(0).getId())){
                        categoryTmp.add(recipe.getId());
                    }
                }
            }
            recipes = getRecipeFromListId(categoryTmp);
        }
        if(healthy != "Healthy" && healthy != "All"){
            List<String> healthyTmp = new ArrayList<String>();
            if(healthy == "Yes"){ healthy = "true"; } else if (healthy == "No"){healthy = "false";}
            for(Recipe recipe: recipes){
                if(recipe.getIsHealthy() != null){
                    String test = recipe.getIsHealthy().toString();
                    if(recipe.getIsHealthy().toString().equals(healthy)){
                        healthyTmp.add(recipe.getId());
                    }
                }
            }
            recipes = getRecipeFromListId(healthyTmp);
        }
        return  recipes;
    }

    /**
     * gets recipe object from realm for each recipe
     *
     * @param finalRecipe
     * @return
     */
    public RealmResults<Recipe> getRecipeFromListId(List<String> finalRecipe){
        RealmQuery<Recipe> recipeQuery = realm.where(Recipe.class);

        if(finalRecipe.size() <= 0){
            recipeQuery.equalTo("id","");
        } else {
            int i = 0;
            for(String id : finalRecipe){
                if(i==0) recipeQuery.equalTo("id",id);
                else {
                    recipeQuery.or().equalTo("id",id);
                }
                i++;
            }
        }

        return recipeQuery.findAll();
    }

    /**
     * deletes a recipe from the realm database
     *
     * @param recipeId - id of the recipe to be deleted
     */
    public void deleteRecipe(final String recipeId) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Recipe recipe = getRecipeFromID(recipeId);
                //delete the type if its the only recipe that uses it
                if (realm.where(Recipe.class).equalTo("recipeType", recipe.getRecipeType()).findAll().size() <= 1) {
                    realm.where(RecipeType.class).equalTo("id", recipe.getRecipeType()).findAll().deleteAllFromRealm();
                }
                //delete the category if its the only recipe that uses it
                if (realm.where(Recipe.class).equalTo("recipeCategory", recipe.getRecipeCategory()).findAll().size() <= 1) {
                    realm.where(RecipeCategory.class).equalTo("id", recipe.getRecipeCategory()).findAll().deleteAllFromRealm();
                }
                //delete all the ingredients
                realm.where(Ingredient.class).equalTo("recipeId", recipeId).findAll().deleteAllFromRealm();
                recipe.deleteFromRealm();
            }
        });

    }

    //******************************** INGREDIENT CLASS ********************************************

    /**
     * delets an ingredient from the database
     *
     * @param ingredientId - id of the ingredient to be deleted
     */
    public void deleteIngredient(final String ingredientId){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Ingredient.class).equalTo("id", ingredientId).findAll().deleteAllFromRealm();
            }
        });

    }

    /**
     * saves a list to ingredient to the realm database
     *
     * @param ingredients - list of ingredients to be added or updated
     */
    public void saveIngredient (List<Ingredient> ingredients) {
        realm.beginTransaction();
        for (int i = 0; i<ingredients.size(); i++){
            realm.copyToRealmOrUpdate(ingredients.get(i));
        }
        realm.commitTransaction();
    }

    /**
     * returns a list of ingredients that are apart of a recipe
     *
     * @param recipeID - the id of the recipe
     * @return a list of ingredients
     */
    public List<Ingredient> getIngredientsFromRecipeID(String recipeID){

        RealmResults<Ingredient> ingredientResults = realm.where(Ingredient.class)
                .equalTo("recipeId",recipeID)
                .findAll();
        return realm.copyFromRealm(ingredientResults);
    }


    // **************************** TYPE CLASS *****************************************************

    /**
     * gets a RecipeType from it's name
     *
     * @param name - name of RecipeType
     * @return the object RecipeType if it exists in the database else null
     */
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

    /**
     * gets the RecipeType from it's id
     *
     * @param id - id of the RecipeType
     * @return the recipe type if it exists else null
     */
    public RecipeType getTypeFromId(String id){
        RealmResults<RecipeType> query = realm.where(RecipeType.class)
                .equalTo("id",id)
                .findAll();
        if(query.size() >= 1){
            return query.get(0);
        }
        return null;
    }

    /**
     * creates a RecipeType and adds it to the realm database
     *
     * @param name - name of the recipe type
     */
    public void createType(final String name){
        realm.beginTransaction();
        RecipeType recipeType = realm.createObject(RecipeType.class, UUID.randomUUID().toString());
        recipeType.setName(name);
        realm.commitTransaction();
    }

    /**
     * query all the types in the realm database
     *
     * @return returns
     */
    public RealmResults<RecipeType> queryType(){
        RealmResults<RecipeType> recipeTypes = realm.where(RecipeType.class)
                .findAll();
        return recipeTypes;
    }

    //******************************** CATEGORY CLASS **********************************************

    /**
     * gets the recipe category id from it's name
     *
     * @param name - recipe category name
     * @return the recipte category id, if the recipe category exists else null
     */
    public String getCategoryIDFromName(String name){
        RecipeCategory recipeCategory = realm.where(RecipeCategory.class)
                .equalTo("name",name, Case.INSENSITIVE)
                .findFirst();
        if(recipeCategory != null){
            return recipeCategory.getId();
        }
        return null;
    }

    /**
     * gets the recipe category from the category id
     *
     * @param id - the id of the recipe category
     * @return the recipe category else null
     */
    public RecipeCategory getCategoryFromId(String id){
        RealmResults<RecipeCategory> query = realm.where(RecipeCategory.class)
                .equalTo("id",id)
                .findAll();
        if(query.size() >= 1){
            return query.get(0);
        }
        return null;
    }

    /**
     * creates the recipe category and adds it to realm
     *
     * @param name - the name of the recipe category
     */
    public void createCategory(final String name){
        realm.beginTransaction();
        RecipeCategory recipeCategory = realm.createObject(RecipeCategory.class, UUID.randomUUID().toString());
        recipeCategory.setName(name);
        realm.commitTransaction();
    }

    /**
     * gets all the categories in the realm database
     *
     * @return - a RealmResult<RecipeCategory> of all the categories in the realm database
     */
    public RealmResults<RecipeCategory> queryCategory(){
        RealmResults<RecipeCategory> catergories = realm.where(RecipeCategory.class)
                .findAll();
        return catergories;
    }

    // *************** SETTERS AND GETTERS **********************************************************

    public Realm getRealm(){
        return realm;
    }
}
