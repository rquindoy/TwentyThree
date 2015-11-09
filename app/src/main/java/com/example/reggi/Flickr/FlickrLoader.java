package com.example.reggi.Flickr;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class FlickrLoader extends AsyncTaskLoader<List<Flickr.Photo>> {

    private int mPage;

    public FlickrLoader(Context context) {
        super(context);
    }

    public boolean hasMorePages() {
        return Utils.isPhotosEmpty() || Utils.hasMorePages();
    }

    public void loadNextPage() {
        if (Utils.isPhotosEmpty()) {
            loadPage(1);
        } else if (Utils.hasMorePages()) {
            loadPage(Utils.getCurrentPage() + 1);
        }
    }

    public void loadPage(int page) {
        Log.d("Reggie", "loadPage:" + page);
        if (page == 1) {
            Utils.clearPhotos();
        }
        mPage = page;
        forceLoad();
    }

    @Override
    public List<Flickr.Photo> loadInBackground() {
        if (Utils.getCurrentPage() == 0 || Utils.hasMorePages()) {
            InterestingnessGetListRequest req = new InterestingnessGetListRequest();
            req.setPage(mPage);
            req.execute();
            Utils.addPhotos(req.getRaw());
            Utils.upDatePagePosition(req.getResult().mPage, req.getResult().mPages);
        }
        return Utils.getPhotoList();
    }

    @Override
    public void reset() {
        super.reset();
        Utils.clearPhotos();
    }
}
