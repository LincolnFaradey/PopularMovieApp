package com.example.faraday.popularmovieapp.Models;

import java.util.HashMap;

/**
 * Created by faraday on 12/12/15.
 */
public class Request {
    private String encodedPath;
    private HashMap<String, String> endpoints;

    public enum Type {
        MOVIES,
        REVIEWS,
        VIDEOS;

        public String encodedPath(int id) {
            String path = null;
            switch (this){
                case MOVIES:
                    path = "discover/movie";
                    break;
                case REVIEWS:
                    path = "movie/" + id + "/reviews";
                    break;
                case VIDEOS:
                    path = "movie/" + id + "/videos";
                    break;
            }

            return path;
        }
    }

    public Request(int id, Type type, HashMap<String, String> endpoints) {
        this.encodedPath = type.encodedPath(id);
        this.endpoints = endpoints;
    }

    public Request(Type type, HashMap<String, String> endpoints) {
        this.encodedPath = type.encodedPath(0);
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
