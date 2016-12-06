package com.uottawa.benjaminmacdonald.cooking_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.uottawa.benjaminmacdonald.cooking_app.Adapters.FavouriteArrayAdapter;
import com.uottawa.benjaminmacdonald.cooking_app.Adapters.RecipeArrayAdapter;
import com.uottawa.benjaminmacdonald.cooking_app.Utils.RealmUtils;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * This class is used to handle populating the main activity. This includes
 * the favourites, the rest of recipes in a list, the toolbar, and the add button.
 * This is also the home screen for the app.
 */

public class MainActivity extends AppCompatActivity {

    RealmResults<Recipe> favourites;
    RealmResults<Recipe> recipes;
    private RealmUtils realmUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RecipeActivity.class);
                intent.putExtra("RECIPE_ID", "");
                startActivity(intent);
            }
        });

        //********************* SET UP REALM OBJECT ************************************************
        Realm.init(this);
        realmUtils = new RealmUtils(this);

        //********************* QUERY FAVOURITE RECIPES AND ALL RECIPES ****************************
        favourites = realmUtils.queryFavouriteRecipes();
        recipes = realmUtils.queryAllNonFavourite();

        //*************************Setting up favourite view ***************************************
        GridView gridView = (GridView) findViewById(R.id.gridView);

        final FavouriteArrayAdapter favArrayAdapter = new FavouriteArrayAdapter(this, favourites);
        gridView.setAdapter(favArrayAdapter);
        gridView.setNumColumns(favourites.size());
        if (favourites.size() > 0) {
            setDynamicWidth(gridView);
        }
        gridView.setDrawSelectorOnTop(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO:: CHANGE TO RECIPE ID
                Intent intent = new Intent(getBaseContext(), RecipeActivity.class);
                intent.putExtra("RECIPE_ID", favourites.get(position).getId());
                startActivity(intent);

            }
        });

        //************************* SETTING UP THE RECIPE LIST VIEW ********************************
        ListView listView = (ListView) findViewById(R.id.recipeListView);

        final RecipeArrayAdapter recipeArrayAdapter = new RecipeArrayAdapter(this, recipes);
        listView.setAdapter(recipeArrayAdapter);

        //Allowing a recipe to be clicked on and navigated to
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO:: CHANGE TO RECIPE ID
                Intent intent = new Intent(getBaseContext(), RecipeActivity.class);
                intent.putExtra("RECIPE_ID", recipes.get(position).getId());
                startActivity(intent);
            }
        });

        //************************ SETTING UP VALUE CHANGE LISTENERS *******************************

        //adding for favourite array
        RealmChangeListener<RealmResults<Recipe>> favChange = new RealmChangeListener<RealmResults<Recipe>>() {
            @Override
            public void onChange(RealmResults<Recipe> element) {
                favArrayAdapter.notifyDataSetChanged();

            }
        };
        favourites.addChangeListener(favChange);

        //adding for recipe array
        RealmChangeListener<RealmResults<Recipe>> recipeChange = new RealmChangeListener<RealmResults<Recipe>>() {
            @Override
            public void onChange(RealmResults<Recipe> element) {
                recipeArrayAdapter.notifyDataSetChanged();
            }
        };
        recipes.addChangeListener(recipeChange);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            navToHelpAct();
        }
        if (id == R.id.search_button) {
            navToSearchAct();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.slide_from_left, R.transition.slide_to_right);
    }

    //***************************** OUR METHODS **************************************

    /**
     * Switches to the search activity.
     */
    public void navToSearchAct() {
        startActivity(new Intent(this, SearchActivity.class));
        overridePendingTransition(R.transition.slide_from_right, R.transition.slide_to_left);
    }

    /**
     * Switches to the help activity.
     */
    public void navToHelpAct() {
        startActivity(new Intent(this, HelpActivity.class));
    }

    //********************* STACK OVERFLOW METHODS *************************************************

    /**
     * Sets the amount of rows depending on the amount of elements.
     * Used for horizontal scrolling.
     * Method taken from stackoverflow.
     * source: http://stackoverflow.com/questions/5725745/horizontal-scrolling-grid-view
     * @param gridView is the layout for the horizontal scrolling.
     */
    private void setDynamicWidth(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            return;
        }
        int totalWidth;
        int items = gridViewAdapter.getCount();
        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalWidth = listItem.getMeasuredWidth();
        totalWidth = totalWidth * items;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.width = totalWidth;
        gridView.setLayoutParams(params);
    }
}