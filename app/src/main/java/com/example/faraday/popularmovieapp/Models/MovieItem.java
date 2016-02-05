package com.example.faraday.popularmovieapp.Models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.faraday.popularmovieapp.PopularMovieContractor;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public MovieItem(Cursor cursor) {
        this.ID = cursor.getInt(cursor.getColumnIndexOrThrow(PopularMovieContractor.MovieEntry.MOVIE_ID));
        this.title = cursor.getString(cursor.getColumnIndexOrThrow(PopularMovieContractor.MovieEntry.TITLE));
        this.description = cursor.getString(cursor.getColumnIndexOrThrow(PopularMovieContractor.MovieEntry.DESCRIPTION));
        this.posterPath = cursor.getString(cursor.getColumnIndexOrThrow(PopularMovieContractor.MovieEntry.POSTER_LINK));
        this.background = cursor.getString(cursor.getColumnIndexOrThrow(PopularMovieContractor.MovieEntry.BACKGROUND_LINK));
        this.releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(PopularMovieContractor.MovieEntry.RELEASE_DATE));
        this.rating = cursor.getDouble(cursor.getColumnIndexOrThrow(PopularMovieContractor.MovieEntry.RATING));
    }

    public MovieItem(JSONObject json) throws JSONException, ParseException {
        this.ID = json.getInt("id");
        this.title = json.getString("original_title");
        this.posterPath = "http://image.tmdb.org/t/p/w300" + json.getString("poster_path");
        this.background = "http://image.tmdb.org/t/p/w500" + json.getString("backdrop_path");
        this.description = json.getString("overview");

        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final Calendar calendar = Calendar.getInstance();

        Date date = dateFormat.parse(json.getString("release_date"));
        calendar.setTime(date);

        this.releaseDate = String.format("%d", calendar.get(Calendar.YEAR));
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


    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ID:" + this.getID() + "\n"
                + "Title:" + this.getTitle() + "\n"
                + "Description:" + this.getDescription() + "\n";
    }

    @Override
    public boolean equals(Object o) {
        MovieItem mi = (MovieItem)o;
        return mi.ID == this.ID;
    }

    @Override
    public int hashCode() {
        return ID;
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
