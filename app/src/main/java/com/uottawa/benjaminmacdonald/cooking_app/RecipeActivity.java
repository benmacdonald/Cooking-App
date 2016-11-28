package com.uottawa.benjaminmacdonald.cooking_app;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;


import com.uottawa.benjaminmacdonald.cooking_app.Adapters.IngredientArrayAdapter;
import com.uottawa.benjaminmacdonald.cooking_app.Adapters.RecipeArrayAdapter;
import com.uottawa.benjaminmacdonald.cooking_app.Adapters.SpinnerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class RecipeActivity extends AppCompatActivity {

    private boolean isFavourite = false;
    private IngredientArrayAdapter ingredientArrayAdapter;
    private List<Ingredient> ingredientList;
    boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        final ImageButton favouriteButton = (ImageButton) findViewById(R.id.favouriteButton);

        //Figure out it the data is from a cell or the add new recipe button
        String recipe_id = getIntent().getStringExtra("RECIPE_ID");
        EditText recipeField = (EditText) findViewById(R.id.recipeTitle);
        if(!recipe_id.equals("")){
            isEdit = false;
            //Set the title of the current activity to the recipe's title, only if it exists
            //TODO:: CHANGE TO RECIPE NAME and change id to an actual id
            recipeField.setText(recipe_id);

            getSupportActionBar().setTitle(recipe_id);
            //TODO:: FILL ALL THE EDIT TEXT WITH VALUES
        } else {
            isEdit = true;
            getSupportActionBar().setTitle("New Recipe");
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
                    Toast.makeText(getApplicationContext(), "Added recipe to favourites", Toast.LENGTH_SHORT).show();
                }
                else {
                    isFavourite = false;
                    favouriteButton.setImageResource(btn_star_big_off);
                    Toast.makeText(getApplicationContext(), "Removed recipe from favourites", Toast.LENGTH_SHORT).show();
                }
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

        //**** TESTING INGREDIENT LIST *****

        ingredientList = new ArrayList<Ingredient>();
        if(!isEdit){
            for(int i=0; i<3;i++){
                Ingredient ingredient = new Ingredient("Test "+i,(double) i,"mL");
                ingredientList.add(ingredient);
            }
        } else {
            Ingredient ingredient = new Ingredient();
            ingredientList.add(ingredient);
        }

        ListView listView = (ListView) findViewById(R.id.ingredientListView);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.save_button) {
            //do save
            changeState(false);
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
        ingredientList.add(new Ingredient());
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
