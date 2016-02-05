package com.example.faraday.popularmovieapp.Fragments;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faraday.popularmovieapp.Activities.DetailsActivity;
import com.example.faraday.popularmovieapp.Activities.ReviewsActivity;
import com.example.faraday.popularmovieapp.Helpers.DataFetcher;
import com.example.faraday.popularmovieapp.Models.MovieItem;
import com.example.faraday.popularmovieapp.Models.Request;
import com.example.faraday.popularmovieapp.MovieDBHelper;
import com.example.faraday.popularmovieapp.PopularMovieContractor;
import com.example.faraday.popularmovieapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import jp.wasabeef.picasso.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.picasso.transformations.gpu.ContrastFilterTransformation;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    final static private String TAG = DetailsActivity.class.getCanonicalName();
    private TextView mTitleTextView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;
    private TextView mDescriptionTextView;
    private ImageView mPosterImageView;
    private ImageView mBackgroundImageView;
    private Button mPlayButton;
    private Button mReviewsButton;
    private FloatingActionButton mFab;

    private TextView mMsgTextView;
    private MovieItem mMovieItem;

    private MovieDBHelper mDBHelper;


    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "onCreate: Saved");
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        mMsgTextView = (TextView) rootView.findViewById(R.id.text_view_msg);
        mMsgTextView.setVisibility(View.VISIBLE);
        mTitleTextView = (TextView) rootView.findViewById(R.id.title);
        mRatingTextView = (TextView) rootView.findViewById(R.id.rating);
        mReleaseDateTextView = (TextView) rootView.findViewById(R.id.release_date);
        mDescriptionTextView = (TextView) rootView.findViewById(R.id.description);
        mPlayButton = (Button) rootView.findViewById(R.id.play_button);
        mReviewsButton = (Button) rootView.findViewById(R.id.reviews_button);
        mPosterImageView = (ImageView) rootView.findViewById(R.id.poster_image);
        mBackgroundImageView = (ImageView) rootView.findViewById(R.id.wallpaper_img);
        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("append_to_response", "trailers");
                Request request = new Request(mMovieItem.getID(), Request.Type.VIDEOS, map);
                new AsyncFetcher().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
            }


        });

        mReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewsActivity.class);
                intent.putExtra("ID", mMovieItem.getID());
                startActivity(intent);
            }
        });

        mDBHelper = new MovieDBHelper(getActivity());

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String result = saveToFavorites();

//                Snackbar.make(view, mMovieItem.getTitle() + result, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Toast.makeText(getActivity(), mMovieItem.getTitle() + result, Toast.LENGTH_LONG).show();
            }
        });

        setVisibility(View.INVISIBLE);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            mMovieItem = bundle.getParcelable("MovieItem");
            update(mMovieItem);
        } else return rootView;


        return rootView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mMovieItem = savedInstanceState.getParcelable("MovieItem");
            Log.e(TAG, "onViewStateRestored: restored");
            if (mMovieItem != null) {
                update(mMovieItem);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState: saving bundle");
        if (mMovieItem != null)
            outState.putParcelable("MovieItem", mMovieItem);
    }

    public void update(MovieItem movieItem) {
        if (movieItem != mMovieItem)
            mMovieItem = movieItem;

        setVisibility(View.VISIBLE);
        mMsgTextView.setVisibility(View.INVISIBLE);
        mDescriptionTextView.setText(movieItem.getDescription());
        mRatingTextView.setText(String.format("%.2f", movieItem.getRating()));

        try {
            mReleaseDateTextView.setText(movieItem.getReleaseDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTitleTextView.setText(movieItem.getTitle());
        this.getActivity().setTitle(movieItem.getTitle());

        Picasso.with(getActivity()).
                load(movieItem.getPosterPath())
                .placeholder(R.raw.placeholder)
                .error(R.raw.placeholder)
                .into(mPosterImageView);

        Transformation trans1 = new ContrastFilterTransformation(getActivity(), 0.6f);
        Transformation trans2 = new BrightnessFilterTransformation(getActivity(), -0.2f);
        Picasso.with(getActivity()).
                load(movieItem.getBackground())
                .transform(trans1)
                .transform(trans2)
                .placeholder(R.raw.placeholder)
                .error(R.raw.placeholder)
                .into(mBackgroundImageView);
    }

    private void setVisibility(int v) {
        mDescriptionTextView.setVisibility(v);
        mRatingTextView.setVisibility(v);
        mReleaseDateTextView.setVisibility(v);
        mReviewsButton.setVisibility(v);
        mFab.setVisibility(v);
        mTitleTextView.setVisibility(v);
        mPlayButton.setVisibility(v);
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


    private String saveToFavorites() {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        if (db == null) {
            Log.e(TAG, "onClick: No db found");
            return "DB is Null";
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(PopularMovieContractor.MovieEntry.MOVIE_ID, mMovieItem.getID());
        contentValues.put(PopularMovieContractor.MovieEntry.TITLE, mMovieItem.getTitle());
        contentValues.put(PopularMovieContractor.MovieEntry.DESCRIPTION, mMovieItem.getDescription());
        contentValues.put(PopularMovieContractor.MovieEntry.POSTER_LINK, mMovieItem.getPosterPath());
        contentValues.put(PopularMovieContractor.MovieEntry.BACKGROUND_LINK, mMovieItem.getBackground());
        contentValues.put(PopularMovieContractor.MovieEntry.RATING, mMovieItem.getRating());
        contentValues.put(PopularMovieContractor.MovieEntry.RELEASE_DATE, mMovieItem.getReleaseDate());

        long newRowID = db.insert(PopularMovieContractor.MovieEntry.TABLE_NAME, null, contentValues);
        db.close();
        Log.d(TAG, "onClick: New Row Id " + newRowID);

        String toastMessage;
        if (newRowID >= 0) {
            toastMessage = " added to favorites";
        } else {
            toastMessage = " already saved";
        }

        return toastMessage;
    }
}
