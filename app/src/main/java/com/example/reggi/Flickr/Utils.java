package com.example.reggi.Flickr;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Utils {
    private static final String PREF_ID = "PrefFlickrData";
    private static final String PREF_FAVORITES = "PrefFavorites";
    private static final String PREF_PHOTOS = "PrefPhotos";
    private static final String PREF_PAGE = "PrefPage";
    private static final String PREF_PAGES = "PrefPages";

    private static HashSet<Long> mFavorites;
    private static HashSet<String> mPhotos = new HashSet<>();
    private static int mPage;
    private static int mPages;

    public static void loadFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        HashSet<String> items = (HashSet<String>) prefs.getStringSet(PREF_FAVORITES, new HashSet<String>());
        mFavorites = new HashSet<>();
        for(String item : items) {
            mFavorites.add(Long.parseLong(item));
        }
        Log.d("Reggie", "loadFavorites:"+mFavorites.size());
    }

    public static void saveFavorites(Context context) {
        Log.d("Reggie", "saveFavorites:"+mFavorites.size());
        HashSet<String> items = new HashSet<>();
        for (Long item : mFavorites) {
            items.add(String.valueOf(item));
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE).edit();
        editor.putStringSet(PREF_FAVORITES, items);
        editor.commit();
    }

    public static boolean isFavorite(long id) {
        return mFavorites.contains(id);
    }

    public static void addFavorite(long id) {
        mFavorites.add(id);
    }

    public static void removeFavorite(long id) {
        mFavorites.remove(id);
    }

    public static void loadPhotos(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        mPhotos = (HashSet<String>) prefs.getStringSet(PREF_PHOTOS, new HashSet<String>());
        mPage = prefs.getInt(PREF_PAGE, 0);
        mPages = prefs.getInt(PREF_PAGES, 0);
        Log.d("Reggie", "loadPhotos:"+mPhotos.size()+":"+mPage+":"+mPages);
    }

    public static void savePhotos(Context context) {
        Log.d("Reggie", "savePhotos:"+mPhotos.size()+":"+mPage+":"+mPages);
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE).edit();
        editor.putStringSet(PREF_PHOTOS, mPhotos);
        editor.putInt(PREF_PAGE, mPage);
        editor.putInt(PREF_PAGES, mPages);
        editor.commit();
    }

    public static void addPhotos(List<String> photos) {
        mPhotos.addAll(photos);
    }

    public static List<Flickr.Photo> getPhotoList() {
        ArrayList<Flickr.Photo> result = new ArrayList<>();
        try {
            for (String data : mPhotos) {
                JSONObject photoData = new JSONObject(data);
                Flickr.Photo photo = new Flickr.Photo();
                photo.mId = photoData.getLong("id");
                photo.mOwner = photoData.getString("owner");
                photo.mSecret = photoData.getString("secret");
                photo.mServer = photoData.getInt("server");
                photo.mFarm = photoData.getInt("farm");
                photo.mTitle = photoData.getString("title");
                photo.mIsPublic = photoData.getInt("ispublic") == 1;
                photo.mIsFriend = photoData.getInt("isfriend") == 1;
                photo.mIsFamily = photoData.getInt("isfamily") == 1;
                result.add(photo);
            }
        } catch (JSONException e) {
        }
        return result;
    }

    public static int getPhotoCount() {
        return mPhotos.size();
    }

    public static void clearPhotos() {
        mPhotos.clear();
        mPage = 0;
        mPages = 0;
    }

    public static void upDatePagePosition(int page, int pages) {
        mPage = page;
        mPages = pages;
    }

    public static boolean hasMorePages() {
        return 0 < mPage  && mPage < mPages;
    }

    public static boolean isPhotosEmpty() {
        return getPhotoCount() == 0;
    }

    public static int getCurrentPage() {
        return mPage;
    }
}
