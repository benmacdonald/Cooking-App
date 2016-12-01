package com.uottawa.benjaminmacdonald.cooking_app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;


import com.uottawa.benjaminmacdonald.cooking_app.Adapters.IngredientArrayAdapter;
import com.uottawa.benjaminmacdonald.cooking_app.Utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class RecipeActivity extends AppCompatActivity {

    private boolean isFavourite = false;
    private IngredientArrayAdapter ingredientArrayAdapter;
    private List<Ingredient> ingredientList;
    boolean isEdit = false;
    private RealmUtils realmUtils;
    private Recipe recipe;
    private String recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //realm implementation
        realmUtils = new RealmUtils(this);

        final ImageButton favouriteButton = (ImageButton) findViewById(R.id.favouriteButton);

        //Figure out it the data is from a cell or the add new recipe button
        recipeId = getIntent().getStringExtra("RECIPE_ID");
        //Initialize all the fields
        ImageView imageView = (ImageView) findViewById(R.id.recipeImage);
        EditText recipeField = (EditText) findViewById(R.id.recipeTitle);
        EditText recipeDescription = (EditText) findViewById(R.id.textDescription);
        EditText recipeInstruction = (EditText) findViewById(R.id.textInstruction);
        EditText recipeType = (EditText) findViewById(R.id.typeText);
        EditText recipeCat = (EditText) findViewById(R.id.categoryText);
        if(!recipeId.equals("")){
            isEdit = false;
            recipe = realmUtils.getRecipeFromID(recipeId);
            ingredientList = realmUtils.getIngredientsFromRecipeID(recipeId);
            //Set the title of the current activity to the recipe's title, only if it exists
            RecipeType type = realmUtils.getTypeFromId(recipe.getRecipeType());
            RecipeCategory category = realmUtils.getCategoryFromId(recipe.getRecipeCategory());
            getSupportActionBar().setTitle(recipe.getName());
            imageView.setImageBitmap(realmUtils.convertToBitmap(recipe.getPhoto()));
            recipeField.setText(recipe.getName());
            recipeDescription.setText(recipe.getDescription());
            recipeInstruction.setText(recipe.getInstructions());
            recipeType.setText(type.getName());
            recipeCat.setText(category.getName());
            if(recipe.getIsFavourite() == true){
                isFavourite = true;
                favouriteButton.setImageResource(btn_star_big_on);
            }
        } else {
            isEdit = true;
            getSupportActionBar().setTitle("New Recipe");
            recipe = realmUtils.createRecipe("tmp-bmat");
            recipeId = recipe.getId();
            ingredientList = new ArrayList<Ingredient>();
            ingredientList.add(new Ingredient(recipeId));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Add an onClickListener for the favourite button
        //Handles cases for adding and removing the recipe from favourites
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavourite) {
                    isFavourite = true;
                    favouriteButton.setImageResource(btn_star_big_on);
                }
                else {
                    isFavourite = false;
                    favouriteButton.setImageResource(btn_star_big_off);
                }
                realmUtils.updateFavouriteForRecipe(recipeId,isFavourite);
            }
        });


        //Allow the EditText to be focused out of
        //Via http://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext/19828165#19828165
        recipeField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        //**** Setting up ingredient list **********************************************************

        ListView listView = (ListView) findViewById(R.id.ingredientListView);

        if(ingredientList.size() <= 0){
            ingredientList.add(new Ingredient(recipeId));
        }

        ingredientArrayAdapter = new IngredientArrayAdapter(this,ingredientList);
        listView.setAdapter(ingredientArrayAdapter);

        View footerView =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ingredient_list_footer, null, false);
        listView.addFooterView(footerView);

        setListViewHeightBasedOnChildren(listView);


        final Button addIngredientBtn = (Button) findViewById(R.id.addIngredient);
        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                addIngredientRow();
            }
        });

        changeState(isEdit);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        MenuItem save = menu.findItem(R.id.save_button);
        MenuItem edit = menu.findItem(R.id.edit_button);
        if(isEdit == false){
            save.setVisible(false);
            edit.setVisible(true);
        } else {
            save.setVisible(true);
            edit.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            //check if recipe is tmp
            //TODO:: check if default then delete
        }
        if (id == R.id.save_button) {
            //do save
            changeState(false);
            updateValues();
        }
        if(id== R.id.edit_button){
            changeState(true);
        }

        return super.onOptionsItemSelected(item);
    }

    // ************** OUR METHODS *******************************

    //Method used to hide the keyboard. Called whenever the focus is changed from the EditText to another view
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void addIngredientRow(){
        ListView listView = (ListView) findViewById(R.id.ingredientListView);
        for(int i=0; i<listView.getLastVisiblePosition() - listView.getFirstVisiblePosition();i++){
            View rowView = listView.getChildAt(i);
            if(rowView != null){
                EditText ingredientTitle = (EditText) rowView.findViewById(R.id.ingredientTitle);
                EditText ingredientAmount = (EditText) rowView.findViewById(R.id.ingredientAmount);
                Spinner spinner = (Spinner) rowView.findViewById(R.id.measurementSpinner);
                if(ingredientTitle.getText().toString() != ""){
                    ingredientList.get(i).setName(ingredientTitle.getText().toString());
                    ingredientList.get(i).setAmount(Double.parseDouble(ingredientAmount.getText().toString()));
                    ingredientList.get(i).setUnitOfMeasurement(spinner.getSelectedItem().toString());
                }
            }
        }
        ingredientList.add(new Ingredient(recipeId));
        ingredientArrayAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(listView);
    }


    public void changeState(Boolean state){
        ListView listView = (ListView) findViewById(R.id.ingredientListView);
        EditText recipeTitle = (EditText) findViewById(R.id.recipeTitle);
        recipeTitle.setEnabled(state);
        EditText description = (EditText) findViewById(R.id.textDescription);
        description.setEnabled(state);
        EditText instruction = (EditText) findViewById(R.id.textInstruction);
        instruction.setEnabled(state);

        Button addIngredientButton = (Button) findViewById(R.id.addIngredient);
        if(!state){
            addIngredientButton.setVisibility(View.GONE);
        } else {
            addIngredientButton.setVisibility(View.VISIBLE);
        }

        for(int i=0; i<listView.getLastVisiblePosition() - listView.getFirstVisiblePosition();i++){
            View rowView = listView.getChildAt(i);
            if(rowView != null){
                EditText ingredientTitle = (EditText) rowView.findViewById(R.id.ingredientTitle);
                ingredientTitle.setEnabled(state);
                EditText ingredientAmount = (EditText) rowView.findViewById(R.id.ingredientAmount);
                ingredientAmount.setEnabled(state);

                Spinner spinner = (Spinner) rowView.findViewById(R.id.measurementSpinner);
                spinner.setEnabled(state);
            }
        }
        isEdit = state;

        invalidateOptionsMenu();
    }

    public void updateValues (){
        //********************** IMAGE **************************
        ImageView imageView = (ImageView) findViewById(R.id.recipeImage);
        Bitmap photo = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        //********************** NAME ***************************
        EditText recipeTitle = (EditText) findViewById(R.id.recipeTitle);
        String name = recipeTitle.getText().toString();

        //*********************** DESCRIPTION ************************
        EditText descriptionText = (EditText) findViewById(R.id.textDescription);
        String description = descriptionText.getText().toString();

        //*********************** INSTRUCTION **************************
        EditText instructionText = (EditText) findViewById(R.id.textInstruction);
        String instruction = instructionText.getText().toString();

        //************************ RECIPETYPE ***************************
        //TODO:: Query realm for recipetype name, if it exists get recipetype ID and assign to recipe
        EditText typeText = (EditText) findViewById(R.id.typeText);
        String type = typeText.getText().toString().trim();
        String typeId = realmUtils.getTypeIDFromName(type);
        if(typeId == null){
            realmUtils.createType(type);
            typeId = realmUtils.getTypeIDFromName(type);
        }

        //************************* RECIPECATEGORY ***********************
        //TODO:: Query realm for recipecat name, if it exists get recipecat ID and assign to recipe
        EditText categoryText = (EditText) findViewById(R.id.categoryText);
        String category = categoryText.getText().toString().trim();
        String catId = realmUtils.getCategoryIDFromName(category);
        if(catId == null){
            realmUtils.createCategory(category);
            catId = realmUtils.getCategoryIDFromName(type);
        }

        //************************* IS HEALTHY ****************************
        //TODO:: assign boolean


        //************************ INGREDIENTS ***************************
        ListView listView = (ListView) findViewById(R.id.ingredientListView);

        for(int i=0; i<listView.getLastVisiblePosition() - listView.getFirstVisiblePosition();i++){
            View rowView = listView.getChildAt(listView.getLastVisiblePosition()-1);
            if(rowView != null){
                EditText ingredientTitle = (EditText) rowView.findViewById(R.id.ingredientTitle);
                EditText ingredientAmount = (EditText) rowView.findViewById(R.id.ingredientAmount);
                Spinner spinner = (Spinner) rowView.findViewById(R.id.measurementSpinner);
                if(ingredientTitle.getText().toString() != ""){
                    ingredientList.get(ingredientList.size()-1).setName(ingredientTitle.getText().toString());
                    ingredientList.get(ingredientList.size()-1).setAmount(Double.parseDouble(ingredientAmount.getText().toString()));
                    ingredientList.get(ingredientList.size()-1).setUnitOfMeasurement(spinner.getSelectedItem().toString());
                }
            }
        }

        //************************ ACCESS REALM AND UPDATE ********************
        realmUtils.updateRecipe(recipeId,name,true,isFavourite,photo,description,instruction,typeId,catId);
        realmUtils.saveIngredient(ingredientList);
    }

    public void deleteRecipe (){
        /**
         * TODO:: first query database and check if other recipes use the typeID or catID
         * TODO:: if not delete the type and cat, then delete the ingredients and finally the recipe.
         *
         */
    }


    // ************** STACKOVERFLOW METHODS FOR LAYOUT *******************

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    //http://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
