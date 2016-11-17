package com.uottawa.benjaminmacdonald.cooking_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.cooking_app.R;

import java.util.List;

/**
 * Created by BenjaminMacDonald on 2016-11-17.
 */

public class RecipeArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;

    public RecipeArrayAdapter(Context context, List<String> values) {
        super(context, R.layout.recipe_item_layout,values);
        this.values = values;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.recipe_item_layout,parent,false);
        TextView nameView = (TextView) listView.findViewById(R.id.recipeTitle);
        nameView.setText(values.get(position));

        return listView;
    }
}
