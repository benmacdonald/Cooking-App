package com.uottawa.benjaminmacdonald.cooking_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.*;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void getFAQ(View view)
    {
        Intent intent = new Intent(this, SubactivityFAQ.class);
        startActivity(intent);
    }

    public void getSearchHelp(View view)
    {
        Intent intent = new Intent(this, SubactivitySearchHelp.class);
        startActivity(intent);
    }

    public void getFavorite(View view)
    {
        Intent intent = new Intent(this, SubactivityFavorite.class);
        startActivity(intent);
    }

    public void getAddOwnRecipe(View view)
    {
        Intent intent = new Intent(this, SubactivityAddOwnRecipe.class);
        startActivity(intent);
    }

    public void getContact(View view)
    {
        Intent intent = new Intent(this, SubactivityContact.class);
        startActivity(intent);
    }
}
