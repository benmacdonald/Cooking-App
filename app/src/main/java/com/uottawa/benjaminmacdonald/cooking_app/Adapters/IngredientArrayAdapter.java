package com.uottawa.benjaminmacdonald.cooking_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.cooking_app.Ingredient;
import com.uottawa.benjaminmacdonald.cooking_app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BenjaminMacDonald on 2016-11-27.
 */

public class IngredientArrayAdapter extends ArrayAdapter<Ingredient> {
    private final Context context;
    private final List<Ingredient> values;
    private final List<String> measurementUnit;

    public IngredientArrayAdapter(Context context, List<Ingredient> values) {
        super(context, R.layout.ingredient_item_layout,values);
        this.values = values;
        this.context = context;
        measurementUnit = Arrays.asList(context.getResources().getStringArray(R.array.measurementUnits));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.ingredient_item_layout,parent,false);

        EditText amountView = (EditText) listView.findViewById(R.id.ingredientAmount);
        amountView.setText(String.valueOf(values.get(position).getAmount()));

        Spinner spinner = (Spinner) listView.findViewById(R.id.measurementSpinner);
        SpinnerArrayAdapter<String> spinnerArrayAdapter =
                new SpinnerArrayAdapter<String>(context,R.layout.spinner_item_black,measurementUnit);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_expanded);
        spinner.setAdapter(spinnerArrayAdapter);



        EditText nameView = (EditText) listView.findViewById(R.id.ingredientTitle);
        nameView.setText(values.get(position).getName());

        return listView;
    }
}
