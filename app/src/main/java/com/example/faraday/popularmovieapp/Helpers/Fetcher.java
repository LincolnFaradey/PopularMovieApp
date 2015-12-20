package com.example.faraday.popularmovieapp.Helpers;

import com.example.faraday.popularmovieapp.Models.Request;

import java.io.IOException;

/**
 * Created by faraday on 12/14/15.
 */
public interface Fetcher {
    String BASE_URL = "https://api.themoviedb.org/3";
    String API_KEY = "";


    /**
     * Fetches data from a server
     * @throws IOException
     * @param request contains encoded path and hash map as params
     * @see Request class
     * @see #close() method has to by called in finally block
     * */
    String fetch(Request request) throws IOException;

    /**
     * clean up system
     * has to be called by the end of fetch() method
     * @see #fetch(Request)
     * Assumingly in finally{} block
     * */
    void close() throws IOException;
}
