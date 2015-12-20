package com.example.faraday.popularmovieapp.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by faraday on 12/14/15.
 */
public class Review {
    private String author;
    private String review;

    public Review(String author, String review) {
        this.author = author;
        this.review = review;
    }

    public Review(JSONObject object) throws JSONException {
        this.author = object.getString("author");
        this.review = object.getString("content");
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
