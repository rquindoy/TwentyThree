package com.example.reggi.twentythree;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reggi.Flickr.Flickr;
import com.example.reggi.Flickr.Utils;
import com.squareup.picasso.Picasso;

public class PhotoAdapter extends ArrayAdapter<Flickr.Photo> {
    private final int SIZE = 300;
    private Drawable mHeartEnabled;
    private Drawable mHeartDisabled;
    private LayoutInflater mInflater;

    public PhotoAdapter(Context context, int resource) {
        super(context, resource);
        mHeartEnabled = context.getResources().getDrawable( R.drawable.heart_enabled);
        mHeartDisabled = context.getResources().getDrawable( R.drawable.heart_disabled);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item, null);
        }

        Flickr.Photo photo = getItem(position);
        TextView url = (TextView)view.findViewById(R.id.title);
        url.setText(photo.mTitle);

        Picasso.with(getContext())
                .load(Flickr.getPhotoUrl(photo, SIZE))
                .resize(SIZE, SIZE)
                .centerCrop()
                .into((ImageView) view.findViewById(R.id.thumbnail));

        ImageView heart = (ImageView)view.findViewById(R.id.heart);
        heart.setTag(photo.mId);
        updateHeartState(heart);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long id = (long)v.getTag();
                if (Utils.isFavorite(id)) {
                    Utils.removeFavorite(id);
                } else {
                    Utils.addFavorite(id);
                }
                updateHeartState((ImageView) v);
            }
        });
        return view;
    }

    private void updateHeartState(ImageView heart) {
        final long id = (long)heart.getTag();
        heart.setImageDrawable(Utils.isFavorite(id) ? mHeartEnabled : mHeartDisabled);
    }
}
