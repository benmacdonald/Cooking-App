package com.uottawa.benjaminmacdonald.cooking_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BenjaminMacDonald on 2016-11-21.
 */

public class SearchActivity extends AppCompatActivity {
    List<String> typeArray;
    List<String> cultureArray;
    List<String> healthyArray;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        //******************* SETTING UP FLITERS *******************************
        typeArray = new ArrayList<String>();
        typeArray.add("BREAKFAST");
        typeArray.add("LUNCH");
        typeArray.add("DINNER");
        typeArray.add("DESERT");
        typeArray.add("DRINK");

        cultureArray = new ArrayList<String>();
        cultureArray.add("CHINESE");
        cultureArray.add("INDIAN");
        cultureArray.add("ITALIAN");
        cultureArray.add("AMERICAN");

        healthyArray = new ArrayList<String>();
        healthyArray.add("YES");
        healthyArray.add("NO");

        ArrayAdapter<String> filterTypeAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,typeArray);
        filterTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        ArrayAdapter<String> filterCultureAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,cultureArray);
        filterCultureAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        ArrayAdapter<String> filterHealthyAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,healthyArray);
        filterHealthyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        Spinner type = (Spinner) findViewById(R.id.typeSpinner);
        type.setAdapter(filterTypeAdapter);

        Spinner culture = (Spinner) findViewById(R.id.cultureSpinner);
        culture.setAdapter(filterCultureAdapter);

        Spinner healthy = (Spinner) findViewById(R.id.healthySpinner);
        healthy.setAdapter(filterHealthyAdapter);
    }
}
