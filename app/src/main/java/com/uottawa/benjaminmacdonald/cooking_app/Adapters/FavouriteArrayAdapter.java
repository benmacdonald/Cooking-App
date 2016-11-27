package com.uottawa.benjaminmacdonald.cooking_app.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.cooking_app.R;
import com.uottawa.benjaminmacdonald.cooking_app.Recipe;

import java.util.List;

/**
 * Created by BenjaminMacDonald on 2016-11-11.
 */

public class FavouriteArrayAdapter extends ArrayAdapter<Recipe> { //CHANGE TO RECIPE
    private final Context context;
    private final List<Recipe> values;

    public FavouriteArrayAdapter(Context context, List<Recipe> values) {
        super(context, R.layout.recipe_favourite_item_layout,values);
        this.values = values;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardView = inflater.inflate(R.layout.recipe_favourite_item_layout,parent,false);
        TextView nameView = (TextView) cardView.findViewById(R.id.favTextView);
        ImageView imageView = (ImageView) cardView.findViewById(R.id.favouriteImageView);
        nameView.setText(values.get(position).getName());
        imageView.setImageBitmap(values.get(position).getPhoto());

        return cardView;
    }
}
