package com.uottawa.benjaminmacdonald.cooking_app;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.uottawa.benjaminmacdonald.cooking_app.Adapters.RecipeArrayAdapter;

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

        //******************* SETTING UP FLITERS *******************************
        typeArray = new ArrayList<String>();
        typeArray.add("Breakfast");
        typeArray.add("Lunch");
        typeArray.add("Dinner");
        typeArray.add("Desert");
        typeArray.add("Drink");

        cultureArray = new ArrayList<String>();
        cultureArray.add("Chinese");
        cultureArray.add("Indian");
        cultureArray.add("Italian");
        cultureArray.add("American");

        healthyArray = new ArrayList<String>();
        healthyArray.add("Healthy");
        healthyArray.add("Non-healthy");

        ArrayAdapter<String> filterTypeAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,typeArray);
        filterTypeAdapter.setDropDownViewResource(R.layout.spinner_item_expanded);

        ArrayAdapter<String> filterCultureAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,cultureArray);
        filterCultureAdapter.setDropDownViewResource(R.layout.spinner_item_expanded);

        ArrayAdapter<String> filterHealthyAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,healthyArray);
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
}
