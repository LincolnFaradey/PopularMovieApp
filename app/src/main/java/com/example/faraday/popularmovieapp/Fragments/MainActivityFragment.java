package com.example.faraday.popularmovieapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.faraday.popularmovieapp.Activities.DetailsActivity;
import com.example.faraday.popularmovieapp.Adapters.ImageAdapter;
import com.example.faraday.popularmovieapp.Helpers.DataFetcher;
import com.example.faraday.popularmovieapp.Models.MovieItem;
import com.example.faraday.popularmovieapp.Models.Request;
import com.example.faraday.popularmovieapp.MovieDBHelper;
import com.example.faraday.popularmovieapp.PopularMovieContractor;
import com.example.faraday.popularmovieapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    final static private String TAG = MainActivityFragment.class.getCanonicalName();
    private GridView mGridView;
    private ImageAdapter mAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mAdapter = new ImageAdapter(getActivity(), R.layout.movie_list_item, new ArrayList<MovieItem>());

        mGridView = (GridView) rootView.findViewById(R.id.moviesList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailsActivityFragment df = (DetailsActivityFragment)getFragmentManager().findFragmentByTag("DTAG");
                if (df == null) {
                    MovieItem item = mAdapter.getItem(position);
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);

                    Log.e(TAG, "onItemClick: put item");
                    intent.putExtra("MovieItem", item);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "onItemClick: already exist");
                    df.update(mAdapter.getItem(position));
                }

            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        HashMap<String, String> map = new HashMap<>();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String settingsProperty = SP.getString("sort_by", "0");
        int result = Integer.parseInt(settingsProperty);
        Log.d(TAG, "onStart: Result = " + result);
        switch (result) {
            case 0:
                map.put("sort_by", "popularity.desc");
                break;
            case 1:
                map.put("sort_by", "vote_average.desc");
                break;
            case 2:
                new FetchMoviesFromDB().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getCursor());
                return;
        }

        Request request = new Request(Request.Type.MOVIES, map);
        new FetchMovies().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
    }


    private Cursor getCursor() {
        MovieDBHelper helper = new MovieDBHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                PopularMovieContractor.MovieEntry._ID,
                PopularMovieContractor.MovieEntry.MOVIE_ID,
                PopularMovieContractor.MovieEntry.TITLE,
                PopularMovieContractor.MovieEntry.DESCRIPTION,
                PopularMovieContractor.MovieEntry.POSTER_LINK,
                PopularMovieContractor.MovieEntry.BACKGROUND_LINK,
                PopularMovieContractor.MovieEntry.RELEASE_DATE,
                PopularMovieContractor.MovieEntry.RATING
        };

        String order = PopularMovieContractor.MovieEntry.RATING + " DESC";

        return db.query(PopularMovieContractor.MovieEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                order);
    }


    private class FetchMovies extends AsyncTask<Request, Integer, ArrayList<MovieItem>> {
        private final String TAG = this.getClass().getCanonicalName();

        @Override
        protected ArrayList<MovieItem> doInBackground(Request... params) {
            Request request = params[0];

            DataFetcher fetcher = new DataFetcher();
            try {
                final String resp = fetcher.fetch(request);

                return parseJSONString(resp);
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    fetcher.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> movieItems) {
            super.onPostExecute(movieItems);
            if (movieItems == null) {
                Log.e(TAG, "onPostExecute: NULL");
                return;
            }

            mAdapter.clear();
            mAdapter.addAll(movieItems);
        }

        private ArrayList<MovieItem> parseJSONString(String jsonString) throws JSONException {
            JSONObject json = new JSONObject(jsonString);
            JSONArray jsonArray = json.getJSONArray("results");

            ArrayList<MovieItem> arrayList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieJSON = jsonArray.getJSONObject(i);
                MovieItem movieItem;
                try {
                    movieItem = new MovieItem(movieJSON);
                } catch (ParseException e) {
                    continue;
                }
                arrayList.add(movieItem);
            }

            return arrayList;
        }
    }


    private class FetchMoviesFromDB extends AsyncTask<Cursor, Integer, ArrayList<MovieItem>> {

        @Override
        protected ArrayList<MovieItem> doInBackground(Cursor... params) {
            Cursor cursor = params[0];

            cursor.moveToFirst();
            ArrayList<MovieItem> items = new ArrayList<>(cursor.getCount());
            do {
                items.add(new MovieItem(cursor));
            } while(cursor.moveToNext());

            cursor.close();
            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> movieItems) {
            super.onPostExecute(movieItems);
            if (movieItems == null) {
                Log.e(TAG, "onPostExecute: NULL");
                return;
            }

            mAdapter.clear();
            mAdapter.addAll(movieItems);
        }

    }
}
