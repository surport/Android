package com.ruyicai.util;

import java.util.HashMap;

import android.graphics.Bitmap;

/**
 * Õº∆¨ª∫¥Êπ‹¿Ì∆˜
 * @author haojie
 *
 */
public class ImageCacheManager {
    
    private static final String TAG = "ImageCacheManager";

    private HashMap<String, HashMap<String, Bitmap>> mCategoryCache;
    
    private static Object mObj = new Object();
    
    private static ImageCacheManager gImageCacheManager;
    
    public static ImageCacheManager getInstance() {
        if (gImageCacheManager == null) {
            synchronized (mObj) {
                if (gImageCacheManager == null) {
                    gImageCacheManager = new ImageCacheManager();
                }
            }
        }
        
        return gImageCacheManager;
    }

    
    public Bitmap getBitmapByCategoryAndKey(String category, String key) {
        Bitmap ret = null;
        synchronized (mObj) {
            if (mCategoryCache.containsKey(category)) {
                ret = mCategoryCache.get(category).get(key);
            }
        }
        return ret;
    }
    
    
    
    
    public void cacheBitmapByCategoryAndKey(String category, String key, Bitmap bt) {
        synchronized (mObj) {
            HashMap<String, Bitmap> keyMap = mCategoryCache.get(category);
            if (keyMap == null) {
                keyMap = new HashMap<String, Bitmap>();
                mCategoryCache.put(category, keyMap);
            }
            keyMap.put(key, bt);
        }
    }

    public void releaseByCategory(String category, boolean ifRecyle) {
        HashMap<String, Bitmap> keyMap = null;
        synchronized (mObj) {
            keyMap = mCategoryCache.remove(category);
        }
        if (keyMap != null) {
            if (ifRecyle) {
                for (Bitmap bt : keyMap.values()) {
                    if (!bt.isRecycled()) {
                        bt.recycle();
                    }
                }
            }
            keyMap.clear();
        }
    }
    
    public void releaseByCategory(String category) {
        releaseByCategory(category, true);
    }
    
    private ImageCacheManager() {
        mCategoryCache = new HashMap<String, HashMap<String, Bitmap>>();
    }
}

