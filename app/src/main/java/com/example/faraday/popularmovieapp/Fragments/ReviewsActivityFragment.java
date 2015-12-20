package com.example.faraday.popularmovieapp.Fragments;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.faraday.popularmovieapp.Adapters.ReviewsAdapter;
import com.example.faraday.popularmovieapp.Helpers.MovieFetcher;
import com.example.faraday.popularmovieapp.Helpers.ReviewsFetcher;
import com.example.faraday.popularmovieapp.Models.Review;
import com.example.faraday.popularmovieapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewsActivityFragment extends Fragment {
    final static private String TAG = ReviewsActivityFragment.class.getCanonicalName();
    ReviewsAdapter mAdapter;
    ListView mListView;
    ArrayList<Review> mReviews;
    int mID;

    public ReviewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);

        mID = getActivity().getIntent().getExtras().getInt("ID");
        mReviews = new ArrayList<>();
        mAdapter = new ReviewsAdapter(getActivity(), R.layout.review_list_view, mReviews);

        mListView = (ListView) rootView.findViewById(R.id.reviews_list);
        mListView.setAdapter(mAdapter);

//        new ReviewsFetcher(mID).
        new Fetcher().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mID);
        return rootView;
    }

    private class Fetcher extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            String resp = null;
            ReviewsFetcher fetcher = new ReviewsFetcher(params[0]);
            try {
                resp = fetcher.fetch();
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

                ArrayList<Review> reviews = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Review r = new Review(obj);

                    reviews.add(r);
                }

                mAdapter.clear();
                mAdapter.addAll(reviews);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
