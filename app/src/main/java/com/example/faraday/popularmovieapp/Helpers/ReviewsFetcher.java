package com.example.faraday.popularmovieapp.Helpers;

import android.net.Uri;

import com.example.faraday.popularmovieapp.Models.Review;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by faraday on 12/14/15.
 */
public class ReviewsFetcher implements Fetcher {
    private int movieID;
    final static private String TAG = ReviewsFetcher.class.getCanonicalName();
    private HttpURLConnection connection;
    private BufferedReader reader;

    public ReviewsFetcher(int id) {
        this.movieID = id;
    }

    public String fetch() throws IOException {
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath("movie/" + this.movieID + "/reviews")
                .appendQueryParameter("api_key", API_KEY);

        try {
            final URL baseURL = new URL(builder.build().toString());

            connection = (HttpURLConnection) baseURL.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            throw e;
        } finally {
            connection.disconnect();
            try {
                reader.close();
            } catch (IOException e) {
                throw e;
            }
        }

    }
}
