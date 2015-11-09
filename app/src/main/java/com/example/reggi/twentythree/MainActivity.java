package com.example.reggi.twentythree;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.reggi.Flickr.Flickr;
import com.example.reggi.Flickr.FlickrLoader;
import com.example.reggi.Flickr.Utils;

import java.util.List;

/*
Our goal is for this to be like any normal day of coding, so looking at Stackoverflow and other resources is fine and encouraged. Open source libraries are fine as well, please make a note of the ones you choose to use in the Technical Breakdown.
We aren't expecting anything flashy for design, standard controls will work fine.
Focus on functionality over any visual design, but do pay attention to clean code, app usability, and failure cases.

---
Create a Flickr app for iOS or Android using their developer api: https://www.flickr.com/services/developer/api/

The app should:
•	Allow the user to navigate a stream of interesting photos.
•	Hint: https://www.flickr.com/services/api/flickr.interestingness.getList.html
•	or, http://code.flickr.net/2009/03/03/panda-tuesday-the-history-of-the-panda-new-apis-explore-and-you/
•	Allow a user to favorite/unfavorite a photo.
•	Display that favorite status in some manner.
•	A label, separate screen, etc.
Deliver to us:
•	A technical breakdown of the app and its functionality.
•	Please list any assumptions, known limitations, and if any, next steps.
•	Please discuss your overall architecture, and note how you would go about scaling it up with additional entity types and new screens.
•	Please discuss any non-SDK libraries you use here.
•	The functional app project, either a link to a git/svn repo or as a zip file.
•	For Android, please use Android Studio.
•	For iOS, Objective-C or Swift is fine.


You can reply directly to this message or click the following link:
https://app.jobvite.com/em?i74fadd5b5090fdc27a5828452d02ca42

You can change your email preferences at:
https://app.jobvite.com/l?ksEqVJfws

Flickr url https://www.flickr.com/services/api/misc.urls.html


https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
<photo
title="Shorebirds at Pillar Point"
id="3313428913"
secret="2cd3cb44cb"
server="3609"
farm="4"
owner="72442527@N00"
ownername="Pat Ulrich" />

https://farm4.staticflickr.com/3609/3313428913_2cd3cb44cb.jpg

 */


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Flickr.Photo>> {

    private FlickrLoader mLoader;
    private PhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Reggie", "onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final ListView list = (ListView) findViewById(R.id.list);
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean shown = true;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0 && !shown) {
                    fab.show();
                    shown = true;
                }
                if (firstVisibleItem == 0 && shown) {
                    fab.hide();
                    shown = false;
                }
            }
        });
        Utils.loadFavorites(this);
        Utils.loadPhotos(this);
        mPhotoAdapter = new PhotoAdapter(this, R.layout.list_item);
        list.setAdapter(mPhotoAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.smoothScrollToPositionFromTop(0, 0);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        mLoader = (FlickrLoader)getLoaderManager().initLoader(0, null, this);
        mLoader.forceLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            mPhotoAdapter.clear();
            mLoader.reset();
            mLoader.loadNextPage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Flickr.Photo>> onCreateLoader(int id, Bundle args) {
        return new FlickrLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Flickr.Photo>> loader, List<Flickr.Photo> data) {
        if (data != null) {
            mPhotoAdapter.clear();
            mPhotoAdapter.addAll(data);
        }
        if (((FlickrLoader) loader).hasMorePages()) {
            ((FlickrLoader) loader).loadNextPage();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Flickr.Photo>> loader) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Reggie", "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("Reggie", "onPause");
        Utils.saveFavorites(this);
        Utils.savePhotos(this);
    }
}
