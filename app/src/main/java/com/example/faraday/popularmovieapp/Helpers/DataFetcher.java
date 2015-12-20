package com.example.faraday.popularmovieapp.Helpers;

import android.net.Uri;

import com.example.faraday.popularmovieapp.Models.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by faraday on 12/11/15.
 */
public class DataFetcher implements Fetcher {
    final static private String TAG = DataFetcher.class.getCanonicalName();
    private HttpURLConnection mConnection;
    private BufferedReader mReader;


    public String fetch(Request request) throws IOException {
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(request.getEncodedPath())
                .appendQueryParameter("api_key", API_KEY);

        if (request.getEndpoints() != null) {
            for (Map.Entry<String, String> entry : request.getEndpoints().entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        final URL baseURL = new URL(builder.build().toString());

        mConnection = (HttpURLConnection) baseURL.openConnection();
        mConnection.setRequestMethod("GET");

        mConnection.connect();

        InputStream inputStream = mConnection.getInputStream();
        if (inputStream == null) {
            return null;
        }
        mReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = mReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }


    public void close() throws IOException {
        mConnection.disconnect();
        mReader.close();
    }
}
