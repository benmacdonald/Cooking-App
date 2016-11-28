package com.uottawa.benjaminmacdonald.cooking_app.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.cooking_app.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by BenjaminMacDonald on 2016-11-27.
 */

public class SpinnerArrayAdapter<String> extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;

    public SpinnerArrayAdapter(Context context, int layout, List<String> values) {
        super(context,layout,values);
        this.values = values;
        this.context = context;
    }
    @Override
    public boolean isEnabled(int position) {
        if (position == 0) {
            return false;
        }
        return true;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View spinnerView = super.getDropDownView(position,convertView,parent);
        TextView spinnerText = (TextView) spinnerView.findViewById(R.id.spinnerItemDrop);
        if (position == 0) {
            spinnerText.setTextColor(Color.GRAY);
        } else {
            spinnerText.setTextColor(Color.BLACK);
        }
        return spinnerView;
    }
}
