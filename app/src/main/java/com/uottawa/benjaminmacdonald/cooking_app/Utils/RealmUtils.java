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
    //Finds all the recipes in realm async
    public RealmResults<Recipe> queryAllRecipesAsync(){

        RealmResults<Recipe> queryRecipes = realm.where(Recipe.class)
                .findAllAsync();

        return  queryRecipes;
    }

    public RealmResults<Recipe> queryAllRecipes(){
        RealmResults<Recipe> queryRecipes = realm.where(Recipe.class)
                .findAll();

        return  queryRecipes;
    }



    //Fins all the recipes and returns them as a List
    public List<Recipe> getAllRecipesAsList(){
        List<Recipe> recipeList = new ArrayList<Recipe>();
        RealmResults<Recipe> queryRecipes = realm.where(Recipe.class)
                .findAll();
        recipeList = realm.copyFromRealm(queryRecipes);
        return recipeList;
    }


    //Finds all non favourites
    public RealmResults<Recipe> queryAllNonFavourite(){

        RealmResults<Recipe> query = realm.where(Recipe.class)
                .equalTo("isFavourite",false)
                .findAllAsync();
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

    public RealmResults<Recipe> getRecipeFromIngredients(Collection<String> ingredientCollection,
                                String type, String category, String healthy){

        //Sets use for query duplicate
        ArrayList<String> checkArray = new ArrayList<String>();
        ArrayList<String> finalRecipe = new ArrayList<String>();

        RealmResults<Recipe> recipes = null;
        RealmResults<Ingredient> ingredients = null;
        RealmQuery<Ingredient> ingredientQuery = realm.where(Ingredient.class);

        ArrayList<String> tmp = new ArrayList<String>(ingredientCollection);

        if(tmp.size() <= 0){
            recipes = realm.where(Recipe.class).findAll();
        } else {

            for (int i = 0; i < tmp.size(); i++) {
                if (i == 0) {
                    ingredientQuery.equalTo("name", tmp.get(i), Case.INSENSITIVE);
                } else {
                    ingredientQuery.or().equalTo("name", tmp.get(i), Case.INSENSITIVE);
                }
            }

            ingredients = ingredientQuery.findAll();
            for (Ingredient ingredient : ingredients) {
                checkArray.add(ingredient.getRecipeId());
            }

            for (String id : checkArray) {
                int occurrences = Collections.frequency(checkArray, id);
                if (occurrences == tmp.size()) {
                    finalRecipe.add(id);
                }
            }
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

        if(category != "Category" && category != "All"){
            List<String> categoryTmp = new ArrayList<String>();
            RealmResults<RecipeCategory> recipeCategory = realm.where(RecipeCategory.class).equalTo("name",category,Case.INSENSITIVE).findAll();
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
        return recipes;
    }

    public RealmResults<Recipe> getRecipeFromIngredientsBoolean(List<String> mIngre, List<String> oIngre,
                                                                List<String> nIngre, String type, String cat, String healthy) {
        //Sets use for query duplicate
        ArrayList<String> checkArray = new ArrayList<String>();
        ArrayList<String> necessaryRecipe = new ArrayList<String>();
        ArrayList<String> notRecipe = new ArrayList<>();
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
//            recipes = getRecipeFromListId(finalRecipe); dont think i need it

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

    public void deleteRecipe(final String recipeId){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Recipe recipe = getRecipeFromID(recipeId);
                //delete the type if its the only recipe that uses it
                if(realm.where(Recipe.class).equalTo("recipeType",recipe.getRecipeType()).findAll().size() <= 1){
                    realm.where(RecipeType.class).equalTo("id",recipe.getRecipeType()).findAll().deleteAllFromRealm();
                }
                //delete the category if its the only recipe that uses it
                if(realm.where(Recipe.class).equalTo("recipeCategory",recipe.getRecipeCategory()).findAll().size() <=1){
                    realm.where(RecipeCategory.class).equalTo("id",recipe.getRecipeCategory()).findAll().deleteAllFromRealm();
                }
                //delete all the ingredients
                realm.where(Ingredient.class).equalTo("recipeId",recipeId).findAll().deleteAllFromRealm();
                recipe.deleteFromRealm();
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
        if(query.size() >= 1){
            return query.get(0);
        }
        return null;
    }

    public void createType(final String name){
        realm.beginTransaction();
        RecipeType recipeType = realm.createObject(RecipeType.class, UUID.randomUUID().toString());
        recipeType.setName(name);
        realm.commitTransaction();
    }

    public RealmResults<RecipeType> queryType(){
        RealmResults<RecipeType> queryType = realm.where(RecipeType.class)
                .findAll();
        return queryType;
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
        if(query.size() >= 1){
            return query.get(0);
        }
        return null;
    }

    public void createCategory(final String name){
        realm.beginTransaction();
        RecipeCategory recipeCategory = realm.createObject(RecipeCategory.class, UUID.randomUUID().toString());
        recipeCategory.setName(name);
        realm.commitTransaction();
    }

    public RealmResults<RecipeCategory> queryCategory(){
        RealmResults<RecipeCategory> queryCatergory = realm.where(RecipeCategory.class)
                .findAll();
        return queryCatergory;
    }

    // *************** SETTERSAND GETTERS **********************************************************

    public Realm getRealm(){
        return realm;
    }
}
