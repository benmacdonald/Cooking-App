package com.uottawa.benjaminmacdonald.cooking_app;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {
    List<String> favourites = new ArrayList<String>();
    List<String> recipes = new ArrayList<String>();

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //*************************Setting up favourite view ***************************************

        GridView gridView = (GridView) findViewById(R.id.gridView);
        favourites = new ArrayList<String>();

        for (int i = 0; i<10; i++){
            favourites.add("Test"+i);
            recipes.add("Test "+i);
        }

        FavouriteArrayAdapter favArrayAdapter = new FavouriteArrayAdapter(this,favourites);
        gridView.setAdapter(favArrayAdapter);
        gridView.setNumColumns(favourites.size());
        setDynamicWidth(gridView);
        gridView.setDrawSelectorOnTop(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast = Toast.makeText(getApplication().getBaseContext(),
                        String.valueOf(position), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        //*************************Setting up recipe list view ***************************************
        ListView listView = (ListView) findViewById(R.id.recipeListView);

        RecipeArrayAdapter recipeArrayAdapter = new RecipeArrayAdapter(this,recipes);
        listView.setAdapter(recipeArrayAdapter);
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
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            navToHelpAct();
        }
        if(id== R.id.search_button){
            navToSearchAct();
        }

        return super.onOptionsItemSelected(item);
    }

    public void navToSearchAct(){
        startActivity(new Intent(this,SearchActivity.class));
    }
//
    public void navToHelpAct() {
        startActivity(new Intent(this,HelpActivity.class));
    }

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

}
