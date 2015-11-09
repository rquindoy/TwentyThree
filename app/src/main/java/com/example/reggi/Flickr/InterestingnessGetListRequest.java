package com.example.reggi.Flickr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InterestingnessGetListRequest extends FlickrRequest {

    public enum Extras {description, license, date_upload, date_taken, owner_name, icon_server,
        original_format, last_update, geo, tags, machine_tags, o_dims, views, media, path_alias,
        url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o};

    private final String METHOD = "flickr.interestingness.getList";
    private Flickr.Photos mResult;
    private ArrayList<String> mRaw;
    public InterestingnessGetListRequest() {
        addParam("method", METHOD);
        mRaw = new ArrayList<>();
    }

    public void setDate(Date date) {
        if (date == null) {
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        addParam("date", format.format(date));
    }

    public void setExtras(ArrayList<Extras> extras) {
        if (extras == null || extras.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < extras.size(); i++) {
            sb.append(extras.get(i));
            sb.append(i < extras.size() - 1 ? "," : "");
        }
        addParam("extras", sb.toString());
    }

    public void setPerPage(int perPage) {
        addParam("per_page", String.valueOf(perPage));
    }

    public void setPage(int page) {
        addParam("page", String.valueOf(page));
    }

    public Flickr.Photos getResult() {
        return mResult;
    }

    @Override
    protected void parse(InputStream inputStream) {
        JSONObject data = streamToJson(inputStream, "UTF-8");
        try {
            final String stat = data.getString("stat");
            if (stat.equals("ok")) {
                JSONObject photosData = data.getJSONObject("photos");

                mResult = new Flickr.Photos();
                mResult.mPage = photosData.getInt("page");
                mResult.mPages = photosData.getInt("pages");
                mResult.mPerPage = photosData.getInt("perpage");
                mResult.mTotal = photosData.getInt("total");

                JSONArray photoList = photosData.getJSONArray("photo");
                for (int i = 0; i < photoList.length(); i++) {
                    mRaw.add(photoList.getJSONObject(i).toString());
                }
            }
        } catch (JSONException e) {
        }
    }

    private JSONObject streamToJson(InputStream inputStream, String encoding) {
        try {
            final BufferedReader sr = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            final StringBuilder sb = new StringBuilder();
            String inputStr;
            while ((inputStr = sr.readLine()) != null)
                sb.append(inputStr);
            return new JSONObject(sb.toString());
        } catch (IOException e) {
        } catch (JSONException e) {
        }
        return null;
    }

    public List<String> getRaw() {
        return mRaw;
    }
}
