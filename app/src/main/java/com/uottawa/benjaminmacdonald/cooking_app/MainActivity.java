package com.uottawa.benjaminmacdonald.cooking_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.uottawa.benjaminmacdonald.cooking_app.Adapters.FavouriteArrayAdapter;
import com.uottawa.benjaminmacdonald.cooking_app.Adapters.RecipeArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

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

        //******************** WRITING TO REALM (FIRST STARTUP ONLY) *******************************
//        List<Integer> drawableList = new ArrayList<Integer>();
//        String[] favName = {"Cookies","Hamburger","Burrito"};
//        drawableList.add(R.drawable.cookies);
//        drawableList.add(R.drawable.hamburger);
//        drawableList.add(R.drawable.burrito);
//
//        for (int i = 0; i<3; i++) {
//            if(i < 3){
//                if(i<2){
//                    Recipe recipe = new Recipe();
//                            realmUtils.createRecipe("Test "+i);
//                }
//            }
//        }



        //********************* Query favourite recipes and all recipes ****************************

        favourites = realmUtils.queryFavouriteRecipes();
        recipes = realmUtils.queryAllNonFavourite();

        //************************ Setting up image cache ******************************************



        //*************************Setting up favourite view ***************************************

        GridView gridView = (GridView) findViewById(R.id.gridView);


        final FavouriteArrayAdapter favArrayAdapter = new FavouriteArrayAdapter(this,favourites);
        gridView.setAdapter(favArrayAdapter);
        gridView.setNumColumns(favourites.size());
        if(favourites.size() > 0){
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

        //*************************Setting up recipe list view *************************************
        ListView listView = (ListView) findViewById(R.id.recipeListView);

        final RecipeArrayAdapter recipeArrayAdapter = new RecipeArrayAdapter(this,recipes);
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
        if(id== R.id.search_button){
            navToSearchAct();
        }

        return super.onOptionsItemSelected(item);
    }

    //***************************** OUR METHODS **************************************

    public Bitmap convertToBitMap(int drawable){
        return BitmapFactory.decodeResource(getResources(),
                drawable);
    }

    public void navToSearchAct(){
        startActivity(new Intent(this,SearchActivity.class));
        overridePendingTransition(R.transition.slide_from_right,R.transition.slide_to_left);
    }
    public void navToHelpAct() {
        startActivity(new Intent(this,HelpActivity.class));
    }


    //********************* STACK OVERFLOW METHODS *************************************************

    //FROM STACKOVERFLOW http://stackoverflow.com/questions/5725745/horizontal-scrolling-grid-view

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
        totalWidth = totalWidth*items;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.width = totalWidth;
        gridView.setLayoutParams(params);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.slide_from_left,R.transition.slide_to_right);
    }


}