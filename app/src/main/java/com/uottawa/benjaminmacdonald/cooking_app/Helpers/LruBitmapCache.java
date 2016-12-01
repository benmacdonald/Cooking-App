package com.uottawa.benjaminmacdonald.cooking_app.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;

//adapted from https://developer.android.com/training/volley/request.html
public class LruBitmapCache extends LruCache<String, Bitmap> {

    private static LruBitmapCache bitmapCache = new LruBitmapCache((int) Runtime.getRuntime().maxMemory());

    private LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    private LruBitmapCache(Context ctx) {
        this(getCacheSize(ctx));
    }

    public static LruBitmapCache getInstance(){
        return  bitmapCache;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    public Bitmap getBitmap(String id) {
        return get(id);
    }

    public void putBitmap(String id, Bitmap bitmap) {
        put(id, bitmap);
    }

    // Returns a cache size equal to approximately three screens worth of images.
    public static int getCacheSize(Context ctx) {
        final DisplayMetrics displayMetrics = ctx.getResources().
                getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;
        // 4 bytes per pixel
        final int screenBytes = screenWidth * screenHeight * 4;

        return screenBytes * 3;
    }
}