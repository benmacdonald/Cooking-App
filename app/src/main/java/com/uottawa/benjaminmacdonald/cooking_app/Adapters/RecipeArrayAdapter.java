package com.uottawa.benjaminmacdonald.cooking_app.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uottawa.benjaminmacdonald.cooking_app.Cache.LruBitmapCache;
import com.uottawa.benjaminmacdonald.cooking_app.R;
import com.uottawa.benjaminmacdonald.cooking_app.Recipe;
import com.uottawa.benjaminmacdonald.cooking_app.Utils.RealmUtils;

import java.util.List;

/**
 * Created by BenjaminMacDonald on 2016-11-17.
 */

public class RecipeArrayAdapter extends ArrayAdapter<Recipe> {
    private final Context context;
    private final List<Recipe> values;
    private final RealmUtils realmUtils;
    private LruBitmapCache bitmapCache;

    public RecipeArrayAdapter(Context context, List<Recipe> values) {
        super(context, R.layout.recipe_item_layout,values);
        this.values = values;
        this.context = context;
        realmUtils = new RealmUtils(context);
        bitmapCache = LruBitmapCache.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.recipe_item_layout,parent,false);
        TextView nameView = (TextView) listView.findViewById(R.id.recipeTitle);
        nameView.setText(values.get(position).getName());
        ImageView imageView = (ImageView) listView.findViewById(R.id.recipeSmallImage);
        if(values.get(position).getPhoto() != null){
            ConvertToBitmapTask cv = new ConvertToBitmapTask(values.get(position).getPhoto(),imageView);
            cv.execute(values.get(position).getId());
        } else {
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.hamburgar_placeholder);
            imageView.setImageBitmap(icon);
        }

        return listView;
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
            if(bitmap == null){
                while(bytes == null){
                    futureRealmUtils.getRealm().waitForChange();
                    bytes = futureRealmUtils.getRecipeFromID(params[0]).getPhoto();
                }
                bitmap = futureRealmUtils.convertToBitmap(bytes);
                bitmapCache.put(params[0],bitmap);
            }
//            futureRealmUtils.getRealm().close();
            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
