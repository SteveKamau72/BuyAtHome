package com.softtech.stevekamau.buyathome.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

/**
 * Created by steve on 7/18/16.
 */
public class Typefaces {

    private static final String TAG = Typefaces.class.getSimpleName();
    private static final Hashtable<String, Typeface> cache = new Hashtable<>();

    private Typefaces() {
        // no instances
    }

    static Typeface get(Context context, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(context.getAssets(), assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath + "' Error: " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
    static Typeface getRobotoBlack(Context context) {
        return get(context, "fonts/Roboto-Black.ttf");
    }

    public static Typeface getGudeaMedium(Context context) {
        return get(context, "fonts/Gudea-Bold.ttf");
    }

}