package com.uottawa.benjaminmacdonald.cooking_app.HelpSubActivities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uottawa.benjaminmacdonald.cooking_app.R;

public class SubactivityAddOwnRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_add_own_recipe);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
    }
}
