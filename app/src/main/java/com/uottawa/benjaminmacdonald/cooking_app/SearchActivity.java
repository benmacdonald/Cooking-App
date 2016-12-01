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
import com.uottawa.benjaminmacdonald.cooking_app.Utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.RealmResults;

/**
 * Created by BenjaminMacDonald on 2016-11-21.
 */

public class SearchActivity extends AppCompatActivity {
    List<String> typeArray;
    List<String> categoryArray;
    List<String> healthyArray;
    List<String> recipes = new ArrayList<String>();

    RealmResults<RecipeType> recipeTypes;
    RealmResults<RecipeCategory> recipeCategories;

    RealmUtils realmUtils;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        realmUtils = new RealmUtils(this);

        recipeTypes = realmUtils.queryType();
        recipeCategories = realmUtils.queryCategory();

        //******************* SETTING UP FLITERS *******************************
        typeArray = new ArrayList<String>();
        typeArray.add("Types");
        for (int i = 0; i<recipeTypes.size(); i++) {
            typeArray.add(recipeTypes.get(i).getName());
        }

        categoryArray = new ArrayList<String>();
        categoryArray.add("Category");
        for (int i = 0; i<recipeCategories.size(); i++) {
            categoryArray.add(recipeCategories.get(i).getName());
        }

        healthyArray = new ArrayList<String>();
        healthyArray.add("Is Healthy");
        healthyArray.add("Healthy");
        healthyArray.add("Non-healthy");

        int layout = R.layout.spinner_item_white;
        SpinnerArrayAdapter<String> filterTypeAdapter = new SpinnerArrayAdapter(this,layout,typeArray);
        filterTypeAdapter.setDropDownViewResource(R.layout.spinner_item_expanded);

        SpinnerArrayAdapter<String> filterCultureAdapter = new SpinnerArrayAdapter(this,layout,categoryArray);
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

//        RecipeArrayAdapter recipeArrayAdapter = new RecipeArrayAdapter(this,recipes);
//        listView.setAdapter(recipeArrayAdapter);
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