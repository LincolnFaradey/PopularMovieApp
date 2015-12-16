package com.example.faraday.popularmovieapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by faraday on 12/7/15.
 */
public class MovieItem implements Parcelable {

    private int ID;
    private String title;
    private String posterPath;
    private String background;
    private String description;
    private String releaseDate;
    private Double rating;

    public MovieItem(JSONObject json) throws JSONException {
        this.ID = json.getInt("id");
        this.title = json.getString("original_title");
        this.posterPath = "http://image.tmdb.org/t/p/w300" + json.getString("poster_path");
        this.background = "http://image.tmdb.org/t/p/original" + json.getString("backdrop_path");
        this.description = json.getString("overview");
        this.releaseDate = json.getString("release_date");
        this.rating = json.getDouble("vote_average");
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackground() {
        return background;
    }

    public String getDescription() {
        return description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Double getRating() {
        return rating;
    }



    @Override
    public String toString() {
        return "ID:" + this.getID() + "\n"
                + "Title:" + this.getTitle() + "\n"
                + "Description:" + this.getDescription() + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.background);
        dest.writeString(this.description);
        dest.writeString(this.releaseDate);
        dest.writeValue(this.rating);
    }

    protected MovieItem(Parcel in) {
        this.ID = in.readInt();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.background = in.readString();
        this.description = in.readString();
        this.releaseDate = in.readString();
        this.rating = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}
