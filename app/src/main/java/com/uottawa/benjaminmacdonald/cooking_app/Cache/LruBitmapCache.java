package com.uottawa.benjaminmacdonald.cooking_app.Cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;


/**
 * class to cache the bitmap images so the app loads faster when transitioning between pages.
 *  adapted from https://developer.android.com/training/volley/request.html
 */
public class LruBitmapCache extends LruCache<String, Bitmap> {


    private static LruBitmapCache bitmapCache = new LruBitmapCache((int) Runtime.getRuntime().maxMemory());

    //**************************** CONSTRUCTOR *****************************************************

    private LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    private LruBitmapCache(Context ctx) {
        this(getCacheSize(ctx));
    }

    //******************************** METHODS *****************************************************

    /**
     * returns the same instance of the bitmap cache
     *
     * @return the instance of bitmapCache
     */
    public static LruBitmapCache getInstance(){
        return  bitmapCache;
    }


    /**
     * gets the size of the bitmap
     *
     * @param key - the id of the recipe
     * @param value - the bitmap images associated with the recipe
     * @return
     */
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    /**
     * get the bitmap from the recipeId
     *
     * @param id - the id of the recipe
     * @return the bitmap
     */
    public Bitmap getBitmap(String id) {
        return get(id);
    }

    /**
     * puts a bitmap into the memories hashmap
     *
     * @param id - the id of the recipe
     * @param bitmap - the bitmap image associated with the recipe
     */
    public void putBitmap(String id, Bitmap bitmap) {
        put(id, bitmap);
    }

    /**
     * Returns a cache size equal to approximately three screens worth of images.
     *
     * @param context - the context of the view
     * @return the cache size
     */
    public static int getCacheSize(Context context) {
        final DisplayMetrics displayMetrics = context.getResources().
                getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;
        // 4 bytes per pixel
        final int screenBytes = screenWidth * screenHeight * 4;

        return screenBytes * 3;
    }
}