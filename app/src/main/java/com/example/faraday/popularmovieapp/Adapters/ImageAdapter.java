package com.example.faraday.popularmovieapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.faraday.popularmovieapp.Models.MovieItem;
import com.example.faraday.popularmovieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by faraday on 12/3/15.
 */
public class ImageAdapter extends ArrayAdapter<MovieItem> {
    private Context mContext;
    private ArrayList<MovieItem> mImages;
    private int mResource;


    public ImageAdapter(Context context, int resource, ArrayList<MovieItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.mImages = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.moviesItem);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Picasso.with(mContext)
                .load(mImages.get(position).getPosterPath())
                .fit()
                .placeholder(R.raw.placeholder)
                .into(holder.image);

        return row;
    }

    static class ViewHolder {
        ImageView image;
    }
}
