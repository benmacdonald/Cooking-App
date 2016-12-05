package com.uottawa.benjaminmacdonald.cooking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.uottawa.benjaminmacdonald.cooking_app.Adapters.RecipeArrayAdapter;
import com.uottawa.benjaminmacdonald.cooking_app.Adapters.SpinnerArrayAdapter;
import com.uottawa.benjaminmacdonald.cooking_app.Utils.RealmUtils;
import com.uottawa.benjaminmacdonald.cooking_app.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import mabbas007.tagsedittext.TagsEditText;

/**
 * Created by BenjaminMacDonald on 2016-11-21.
 */

public class SearchActivity extends AppCompatActivity {
    List<String> typeArray;
    List<String> categoryArray;
    List<String> healthyArray;
    List<Recipe> recipes = new ArrayList<Recipe>();
    List<String> mIngre = new ArrayList<>();
    List<String> oIngre = new ArrayList<>();
    List<String> nIngre = new ArrayList<>();
    String typeSpinnerValue;
    String categorySpinnerValue;
    String healthySpinnerValue;

    RealmResults<RecipeType> recipeTypes;
    RealmResults<RecipeCategory> recipeCategories;

    RealmUtils realmUtils;

    RecipeArrayAdapter recipeArrayAdapter;

    //tags
    TagsEditText tags;
    TagsEditText.TagsEditListener tagsEditListener;
    Collection<String> ingredientList;

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
        recipes = realmUtils.getAllRecipesAsList();

        typeSpinnerValue = "All";
        categorySpinnerValue = "All";
        healthySpinnerValue = "All";

        //tags
        tagsEditListener = new TagsEditText.TagsEditListener() {
            @Override
            public void onTagsChanged(Collection<String> collection) {
                mIngre = new ArrayList<>();
                oIngre = new ArrayList<>();
                nIngre = new ArrayList<>();
                ingredientList = collection;
                List<String> tmp = new ArrayList<>(ingredientList);
                if(tmp.size() >= 1 && tmp.get(0) != "AND" && tmp.get(0) != "OR" && tmp.get(0) != "NOT"){
                    mIngre.add(tmp.get(0));
                }
                for(int i =0; i<tmp.size()-1; i ++){
                    if(tmp.get(i) == "AND"){
                        mIngre.add(tmp.get(i+1));
                    } else if(tmp.get(i) == "OR"){
                        oIngre.add(tmp.get(i+1));
                    } else if (tmp.get(i) == "NOT"){
                        nIngre.add(tmp.get(i+1));
                    }
                }
                updateSearchList(mIngre,oIngre,nIngre);
            }

            @Override
            public void onEditingFinished() {
            }
        };
        tags = (TagsEditText) findViewById(R.id.tagsEditText);
        tags.setTagsListener(tagsEditListener);

        //******************* SETTING UP FILTERS *******************************
        typeArray = new ArrayList<String>();
        typeArray.add("Type");
        typeArray.add("All");
        for (int i = 0; i<recipeTypes.size(); i++) {
            typeArray.add(recipeTypes.get(i).getName());
        }

        categoryArray = new ArrayList<String>();
        categoryArray.add("Category");
        categoryArray.add("All");
        for (int i = 0; i<recipeCategories.size(); i++) {
            categoryArray.add(recipeCategories.get(i).getName());
        }

        healthyArray = new ArrayList<String>();
        healthyArray.add("Is Healthy");
        healthyArray.add("All");
        healthyArray.add("Yes");
        healthyArray.add("No");

        int layout = R.layout.spinner_item_white;
        SpinnerArrayAdapter<String> filterTypeAdapter = new SpinnerArrayAdapter(this,layout,typeArray);
        filterTypeAdapter.setDropDownViewResource(R.layout.spinner_item_expanded);

        SpinnerArrayAdapter<String> filterCultureAdapter = new SpinnerArrayAdapter(this,layout,categoryArray);
        filterCultureAdapter.setDropDownViewResource(R.layout.spinner_item_expanded);

        SpinnerArrayAdapter<String> filterHealthyAdapter = new SpinnerArrayAdapter(this,layout,healthyArray);
        filterHealthyAdapter.setDropDownViewResource(R.layout.spinner_item_expanded);

        final Spinner type = (Spinner) findViewById(R.id.typeSpinner);
        type.setAdapter(filterTypeAdapter);

        final Spinner culture = (Spinner) findViewById(R.id.cultureSpinner);
        culture.setAdapter(filterCultureAdapter);

        final Spinner healthy = (Spinner) findViewById(R.id.healthySpinner);
        healthy.setAdapter(filterHealthyAdapter);

        //************************** SETTING UP SPINNER SELECTORS **********************************
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSpinnerValue = (String) type.getSelectedItem();
                if(ingredientList == null){
                    ingredientList = new ArrayList<String>();
                }
                updateSearchList(mIngre,oIngre,nIngre);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        culture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    categorySpinnerValue = (String) culture.getSelectedItem();
                    if(ingredientList == null){
                        ingredientList = new ArrayList<String>();
                    }
                updateSearchList(mIngre,oIngre,nIngre);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        healthy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    healthySpinnerValue = (String) healthy.getSelectedItem();
                    if(ingredientList == null){
                        ingredientList = new ArrayList<String>();
                    }
                    updateSearchList(mIngre,oIngre,nIngre);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //*************************Setting up recipe list view **************************************
        ListView listView = (ListView) findViewById(R.id.recipeListView);

        recipeArrayAdapter = new RecipeArrayAdapter(this,recipes);
        listView.setAdapter(recipeArrayAdapter);

        //Allowing a recipe to be clicked on and navigated to
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), RecipeActivity.class);
                intent.putExtra("RECIPE_ID", recipes.get(position).getId());
                startActivity(intent);
            }
        });
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


    // ********* METHODS *************************

    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.andButton:
                tags.setText("AND");
                break;
            case R.id.orButton:
                tags.setText("OR");
                break;
            case R.id.notButton:
                tags.setText("NOT");
        }
    }

    public void updateSearchList(List<String> mIngre, List<String> oIngre,
                                 List<String> nIngre) {
        if(mIngre != null && oIngre != null && nIngre != null){
//            List<Recipe> tmp = realmUtils.getRecipeFromIngredients(collection,typeSpinnerValue,categorySpinnerValue,healthySpinnerValue);
            List<Recipe> tmp = realmUtils.getRecipeFromIngredientsBoolean(mIngre,oIngre,nIngre,typeSpinnerValue,categorySpinnerValue,healthySpinnerValue);
            recipes.clear();
            recipes.addAll(tmp);
            recipeArrayAdapter.notifyDataSetChanged();
        }
    }

}