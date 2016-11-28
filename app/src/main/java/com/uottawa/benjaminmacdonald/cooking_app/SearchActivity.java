package com.uottawa.benjaminmacdonald.cooking_app;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Spinner;

import com.uottawa.benjaminmacdonald.cooking_app.Adapters.RecipeArrayAdapter;
import com.uottawa.benjaminmacdonald.cooking_app.Adapters.SpinnerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BenjaminMacDonald on 2016-11-21.
 */

public class SearchActivity extends AppCompatActivity {
    List<String> typeArray;
    List<String> cultureArray;
    List<String> healthyArray;
    List<String> recipes = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //******************* SETTING UP FLITERS *******************************
        typeArray = new ArrayList<String>();
        typeArray.add("Type");
        typeArray.add("Breakfast");
        typeArray.add("Lunch");
        typeArray.add("Dinner");
        typeArray.add("Desert");
        typeArray.add("Drink");

        cultureArray = new ArrayList<String>();
        cultureArray.add("Category");
        cultureArray.add("Chinese");
        cultureArray.add("Indian");
        cultureArray.add("Italian");
        cultureArray.add("American");

        healthyArray = new ArrayList<String>();
        healthyArray.add("Is Healthy");
        healthyArray.add("Healthy");
        healthyArray.add("Non-healthy");

        int layout = R.layout.spinner_item_white;
        SpinnerArrayAdapter<String> filterTypeAdapter = new SpinnerArrayAdapter(this,layout,typeArray);
        filterTypeAdapter.setDropDownViewResource(R.layout.spinner_item_expanded);

        SpinnerArrayAdapter<String> filterCultureAdapter = new SpinnerArrayAdapter(this,layout,cultureArray);
        filterCultureAdapter.setDropDownViewResource(R.layout.spinner_item_expanded);

        SpinnerArrayAdapter<String> filterHealthyAdapter = new SpinnerArrayAdapter(this,layout,healthyArray);
        filterHealthyAdapter.setDropDownViewResource(R.layout.spinner_item_expanded);

        Spinner type = (Spinner) findViewById(R.id.typeSpinner);
        type.setAdapter(filterTypeAdapter);

        Spinner culture = (Spinner) findViewById(R.id.cultureSpinner);
        culture.setAdapter(filterCultureAdapter);

        Spinner healthy = (Spinner) findViewById(R.id.healthySpinner);
        healthy.setAdapter(filterHealthyAdapter);

        //*************************Setting up recipe list view ***************************************

        //for testing purposes
        for (int i = 0; i<10; i++){
            recipes.add("Test "+i);
        }

        ListView listView = (ListView) findViewById(R.id.recipeListView);

        RecipeArrayAdapter recipeArrayAdapter = new RecipeArrayAdapter(this,recipes);
        listView.setAdapter(recipeArrayAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            overridePendingTransition(R.transition.slide_from_left,R.transition.slide_to_right);
        }

        return  super.onOptionsItemSelected(item);
    }


    /// ********* METHODS *************************

    public void searchBarOnClick() {

    }

}
