package com.uottawa.benjaminmacdonald.cooking_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.cooking_app.Ingredient;
import com.uottawa.benjaminmacdonald.cooking_app.R;
import com.uottawa.benjaminmacdonald.cooking_app.Utils.RealmUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Adapter class used to display ingredients in the ListView found in the RecipeActivity
 */

public class IngredientArrayAdapter extends ArrayAdapter<Ingredient> {
    private final Context context;
    private final List<Ingredient> values;
    private final List<String> measurementUnit;
    private final RealmUtils realmUtils;

    public IngredientArrayAdapter(Context context, List<Ingredient> values) {
        super(context, R.layout.ingredient_item_layout,values);
        this.values = values;
        this.context = context;
        measurementUnit = Arrays.asList(context.getResources().getStringArray(R.array.measurementUnits));
        realmUtils = new RealmUtils(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View listView = inflater.inflate(R.layout.ingredient_item_layout,parent,false);

        EditText amountView = (EditText) listView.findViewById(R.id.ingredientAmount);
        amountView.setText(String.valueOf(values.get(position).getAmount()));

        Spinner spinner = (Spinner) listView.findViewById(R.id.measurementSpinner);
        SpinnerArrayAdapter<String> spinnerArrayAdapter =
                new SpinnerArrayAdapter<String>(context,R.layout.spinner_item_black,measurementUnit);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_expanded);
        spinner.setAdapter(spinnerArrayAdapter);
        String unitValue = values.get(position).getUnitOfMeasurement();

        //If the unit value has been set by the user, set the selection of the spinner accordingly
        if(unitValue != "Units" && unitValue != ""){
            spinner.setSelection(measurementUnit.indexOf(values.get(position).getUnitOfMeasurement()));
        }
        spinner.requestLayout(); //used to fix layout issues when manually selecting spinner element

        //Set the name of the ingredient
        EditText nameView = (EditText) listView.findViewById(R.id.ingredientTitle);
        nameView.setText(values.get(position).getName());

        //Set an onClickListener for the delete button
        ImageButton deleteIngredientBtn = (ImageButton) listView.findViewById(R.id.deleteIngredient);
        deleteIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmUtils.deleteIngredient(values.get(position).getId());
                delete(position);
            }
        });


        return listView;
    }


    /**
     * Deletes an item in the ListView and ArrayAdapter at the given position
     * @param position The position of the item to delete
     */
    public void delete(int position){
        values.remove(position);
        notifyDataSetChanged();
    }
}
