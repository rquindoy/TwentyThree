package com.example.reggi.Flickr;

import android.util.Pair;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public abstract class FlickrRequest {

    private static final String API_URL = "https://api.flickr.com/services/rest/";
    private static final String API_KEY = "0ddeba63d4b8be2d2372dbdae8b027d2";
    private static final String FORMAT = "json";
    private static final String NO_JSON_CALLBACK = "1";

    private ArrayList<Pair<String, String>> mParams;

    public FlickrRequest() {
        mParams = new ArrayList<>();
        mParams.add(new Pair<String, String>("api_key", API_KEY));
        mParams.add(new Pair<String, String>("format", FORMAT));
        mParams.add(new Pair<String, String>("nojsoncallback", NO_JSON_CALLBACK));
    }

    public void addParam(String key, String value) {
        mParams.add(new Pair(key, value));
    }

    public void execute() {
        // GET
        StringBuilder sb = new StringBuilder();

        try {
            for(int i = 0; i < mParams.size(); i++) {
                sb.append(URLEncoder.encode(mParams.get(i).first, "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(mParams.get(i).second, "UTF-8"));
                sb.append(i < mParams.size() - 1 ? "&" : "");
            }
        } catch (UnsupportedEncodingException e) {
        }

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(API_URL + "?" + sb.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            parse(urlConnection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void parse(InputStream is);
}
