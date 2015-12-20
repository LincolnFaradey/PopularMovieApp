package com.example.faraday.popularmovieapp.Helpers;

import android.net.Uri;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by faraday on 12/11/15.
 */
public class MovieFetcher implements Fetcher {
    final static private String TAG = MovieFetcher.class.getCanonicalName();
    private HttpURLConnection connection;
    private BufferedReader reader;

    public String fetch(String path, HashMap<String, String> params) throws IOException {
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter("api_key", API_KEY);

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.appendQueryParameter(entry.getKey(), entry.getValue());
            }
        }


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

    public void clean() {
        if (connection != null)
            connection.disconnect();

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
