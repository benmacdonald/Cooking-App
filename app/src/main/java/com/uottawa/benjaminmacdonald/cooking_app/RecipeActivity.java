package com.uottawa.benjaminmacdonald.cooking_app;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class RecipeActivity extends AppCompatActivity {

    private boolean isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        final ImageButton favouriteButton = (ImageButton) findViewById(R.id.favouriteButton);

        //Set the title of the current activity to the recipe's title
        EditText recipeField = (EditText) findViewById(R.id.recipeTitle);
        if (!recipeField.getText().equals("")) {
            getSupportActionBar().setTitle(recipeField.getText());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Add an onClickListener for the favourite button
        //Handles cases for adding and removing the recipe from favourites
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavourite) {
                    isFavourite = true;
                    favouriteButton.setImageResource(btn_star_big_on);
                    Toast.makeText(getApplicationContext(), "Added recipe to favourites", Toast.LENGTH_SHORT).show();
                }
                else {
                    isFavourite = false;
                    favouriteButton.setImageResource(btn_star_big_off);
                    Toast.makeText(getApplicationContext(), "Removed recipe from favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Allow the EditText to be focused out of
        //Via http://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext/19828165#19828165
        recipeField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

    }

    //Method used to hide the keyboard. Called whenever the focus is changed from the EditText to another view
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
