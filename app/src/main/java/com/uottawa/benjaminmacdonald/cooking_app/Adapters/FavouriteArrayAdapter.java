package com.uottawa.benjaminmacdonald.cooking_app.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.cooking_app.Helpers.LruBitmapCache;
import com.uottawa.benjaminmacdonald.cooking_app.R;
import com.uottawa.benjaminmacdonald.cooking_app.RealmUtils;
import com.uottawa.benjaminmacdonald.cooking_app.Recipe;

import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by BenjaminMacDonald on 2016-11-11.
 */

public class FavouriteArrayAdapter extends ArrayAdapter<Recipe>  { //CHANGE TO RECIPE
    private final Context context;
    private RealmResults<Recipe> values;
    private RealmUtils realmUtils;
    private LruBitmapCache bitmapCache;

    public FavouriteArrayAdapter(Context context, RealmResults<Recipe> values) {
        super(context, R.layout.recipe_favourite_item_layout,values);
        this.values = values;
        this.context = context;
        bitmapCache = LruBitmapCache.getInstance();
        realmUtils = new RealmUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardView = inflater.inflate(R.layout.recipe_favourite_item_layout,parent,false);
        TextView nameView = (TextView) cardView.findViewById(R.id.favTextView);
        nameView.setText(values.get(position).getName());
        ImageView imageView = (ImageView) cardView.findViewById(R.id.favouriteImageView);
        ConvertToBitmapTask cv = new ConvertToBitmapTask(values.get(position).getPhoto(),imageView);
        cv.execute(values.get(position).getId());
        System.out.println(values.size());
        System.out.println(values.get(position).getName());
        return cardView;
    }

    // *************************** SUBCLASS ASYNC CLASS ******************************************

    protected class ConvertToBitmapTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        private Recipe recipe;
        private byte[] bytes;

        public  ConvertToBitmapTask(byte[] bytes, ImageView imageView){
            this.imageView = imageView;
            this.bytes = bytes;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            RealmUtils futureRealmUtils = new RealmUtils(context);
            Bitmap bitmap = bitmapCache.get(params[0]);
            if(bitmap != null){
                return bitmapCache.get(params[0]);
            } else {
                bitmap = futureRealmUtils.convertToBitmap(bytes);
                bitmapCache.put(params[0],bitmap);
                return bitmap;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}
