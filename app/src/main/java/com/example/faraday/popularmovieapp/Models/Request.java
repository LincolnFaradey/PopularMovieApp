package com.example.faraday.popularmovieapp.Models;

import java.util.HashMap;

/**
 * Created by faraday on 12/12/15.
 */
public class Request {
    private String encodedPath;
    private HashMap<String, String> endpoints;

    public Request(String encodedPath, HashMap<String, String> endpoints) {
        this.encodedPath = encodedPath;
        this.endpoints = endpoints;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public void setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
    }

    public HashMap<String, String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(HashMap<String, String> endpoints) {
        this.endpoints = endpoints;
    }
}
