package com.example.faraday.popularmovieapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.faraday.popularmovieapp.Models.Review;
import com.example.faraday.popularmovieapp.R;

import java.util.ArrayList;

/**
 * Created by faraday on 12/14/15.
 */
public class ReviewsAdapter extends ArrayAdapter<Review> {
    private Context mContext;
    private ArrayList<Review> mReviews;
    private int mResource;


    public ReviewsAdapter(Context context, int resource, ArrayList<Review> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.mReviews = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        Review review = mReviews.get(position);
        ReviewViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mResource, parent, false);

            holder = new ReviewViewHolder();
            holder.author = (TextView) row.findViewById(R.id.author_name);
            holder.review = (TextView) row.findViewById(R.id.review);
            row.setTag(holder);
        } else {
            holder = (ReviewViewHolder) row.getTag();
        }

        holder.author.setText(review.getAuthor());
        holder.review.setText(review.getReview());
        return row;
    }

    private class ReviewViewHolder {
        TextView author;
        TextView review;
    }

}
