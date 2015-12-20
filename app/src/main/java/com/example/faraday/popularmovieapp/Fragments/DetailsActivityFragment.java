package com.example.faraday.popularmovieapp.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.faraday.popularmovieapp.Activities.DetailsActivity;
import com.example.faraday.popularmovieapp.Activities.ReviewsActivity;
import com.example.faraday.popularmovieapp.Helpers.DataFetcher;
import com.example.faraday.popularmovieapp.Models.MovieItem;
import com.example.faraday.popularmovieapp.Models.Request;
import com.example.faraday.popularmovieapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.ContrastFilterTransformation;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    final static private String TAG = DetailsActivity.class.getCanonicalName();
    private ImageView mPosterImageView;
    private ImageView mBackgroundImageView;
    private Button mPlayButton;
    private Button mReviewsButton;
    private MovieItem mMovieItem;


    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null)
            mMovieItem = bundle.getParcelable("MovieItem");
        else
            return rootView;

        mPlayButton = (Button) rootView.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("append_to_response", "trailers");
                Request request = new Request(mMovieItem.getID(), Request.Type.VIDEOS, map);
                new AsyncFetcher().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
            }


        });

        mReviewsButton = (Button) rootView.findViewById(R.id.reviews_button);
        mReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewsActivity.class);
                intent.putExtra("ID", mMovieItem.getID());
                startActivity(intent);
            }
        });

        mPosterImageView = (ImageView) rootView.findViewById(R.id.poster_image);
        mBackgroundImageView = (ImageView) rootView.findViewById(R.id.wallpaper_img);

        TextView textView = (TextView) rootView.findViewById(R.id.description);
        textView.setText(mMovieItem.getDescription());

        TextView ratingTextView = (TextView) rootView.findViewById(R.id.rating);
        ratingTextView.setText(String.format("%.2f", mMovieItem.getRating()));

        TextView releaseDateTextView = (TextView) rootView.findViewById(R.id.release_date);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final Calendar calendar = Calendar.getInstance();

        try {
            Date date = dateFormat.parse(mMovieItem.getReleaseDate());
            calendar.setTime(date);
            releaseDateTextView.setText(String.format("%d", calendar.get(Calendar.YEAR)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView titleTextView = (TextView) rootView.findViewById(R.id.title);
        titleTextView.setText(mMovieItem.getTitle());
        this.getActivity().setTitle(mMovieItem.getTitle());

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mMovieItem == null) {
            return;
        }

        Picasso.with(getActivity()).
                load(mMovieItem.getPosterPath())
                .placeholder(R.raw.placeholder)
                .error(R.raw.placeholder)
                .into(mPosterImageView);

        Transformation trans1 = new ContrastFilterTransformation(getActivity(), 0.6f);
        Transformation trans2 = new BrightnessFilterTransformation(getActivity(), -0.2f);
        Picasso.with(getActivity()).
                load(mMovieItem.getBackground())
                .transform(trans1)
                .transform(trans2)
                .placeholder(R.raw.placeholder)
                .error(R.raw.placeholder)
                .into(mBackgroundImageView);
    }



    private class AsyncFetcher extends AsyncTask<Request, Integer, String> {

        @Override
        protected String doInBackground(Request... params) {
            String resp = null;
            DataFetcher fetcher = new DataFetcher();
            try {
                resp = fetcher.fetch(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onClick: " + resp);
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) return;

            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("results");
                if (array.length() < 1) return;

                JSONObject obj = array.getJSONObject(0);
                String key = obj.getString("key");
                Log.d(TAG, "onPostExecute: " + key);

                Uri content = Uri.parse("vnd.youtube:" + key);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, content);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.d(TAG, "onPostExecute: Open at youtube app: " + content.toString());
                } catch (ActivityNotFoundException e) {
                    content = Uri.parse("http://youtu.be/" + key);
                    Intent intent = new Intent(Intent.ACTION_VIEW, content);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    Log.d(TAG, "onPostExecute: Open at browser app: " + content.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
