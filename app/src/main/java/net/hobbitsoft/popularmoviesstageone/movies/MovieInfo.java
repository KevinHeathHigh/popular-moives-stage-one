/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmoviesstageone.movies;
/* Sample of Movie Info JSON
{
    "vote_count": 1626,
    "id": 337167,
    "video": false,
    "vote_average": 6,
    "title": "Fifty Shades Freed",
    "popularity": 567.058534,
    "poster_path": "/jjPJ4s3DWZZvI4vw8Xfi4Vqa1Q8.jpg",
    "original_language": "en",
    "original_title": "Fifty Shades Freed",
    "genre_ids": [
        18,
        10749
    ],
    "backdrop_path": "/9ywA15OAiwjSTvg3cBs9B7kOCBF.jpg",
    "adult": false,
    "overview": "Believing they have left behind shadowy figures from their past, newlyweds Christian and Ana fully embrace an inextricable connection and shared life of luxury. But just as she steps into her role as Mrs. Grey and he relaxes into an unfamiliar stability, new threats could jeopardize their happy ending before it even begins.",
    "release_date": "2018-02-07"
}
 */

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class MovieInfo implements Parcelable {

    private int vote_count;
    private int id;
    private String title;
    private boolean video;
    private double vote_average;
    private String backdrop_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private String original_language;
    private String original_title;
    private ArrayList<Integer> genre_ids;
    private double popularity;
    private String poster_path;
    private String posterImageURL;
    private String posterDetailImageURL;
    private String posterLargeImageURL;
    private Bitmap posterBitmap;

    public MovieInfo() {
    }

    public MovieInfo(int vote_count, int id, String title, boolean video, double vote_average, String backdrop_path, boolean adult, String overview, String release_date, String original_language, String original_title, ArrayList<Integer> genre_ids, double popularity, String poster_path) {
        this.vote_count = vote_count;
        this.id = id;
        this.title = title;
        this.video = video;
        this.vote_average = vote_average;
        this.backdrop_path = backdrop_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
        this.original_language = original_language;
        this.original_title = original_title;
        this.genre_ids = genre_ids;
        this.popularity = popularity;
        this.poster_path = poster_path;
    }

    @SuppressLint("ParcelClassLoader")
    private MovieInfo(Parcel parcel) {
        vote_count = parcel.readInt();
        id = parcel.readInt();
        title = parcel.readString();
        video = (Boolean) parcel.readValue(null);
        vote_average = parcel.readDouble();
        backdrop_path = parcel.readString();
        adult = (Boolean) parcel.readValue(null);
        overview = parcel.readString();
        release_date = parcel.readString();
        original_language = parcel.readString();
        original_title = parcel.readString();
        genre_ids = (ArrayList<Integer>) parcel.readSerializable();
        popularity = parcel.readDouble();
        poster_path = parcel.readString();
        posterImageURL = parcel.readString();
        posterDetailImageURL = parcel.readString();
        posterLargeImageURL = parcel.readString();
    }

    public int getVoteCount() {
        return this.vote_count;
    }

    public void setVoteCount(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getVideo() {
        return this.video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return this.vote_average;
    }

    public void setVoteAverage(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return this.popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return this.poster_path;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginalLanguage() {
        return this.original_language;
    }

    public void setOriginalLanguage(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginalTitle() {
        return this.original_title;
    }

    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }

    public ArrayList<Integer> getGenreIds() {
        return this.genre_ids;
    }

    public void setGenreIds(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getBackdropPath() {
        return this.backdrop_path;
    }

    public void setBackdropPath(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean getAdult() {
        return this.adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return this.release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public String getPosterImageURL() {
        return posterImageURL;
    }

    public void setPosterImageURL(String posterImageURL) {
        this.posterImageURL = posterImageURL;
    }

    public String getPosterDetailImageURL() {
        return posterDetailImageURL;
    }

    public void setPosterDetailImageURL(String posterDetailImageURL) {
        this.posterDetailImageURL = posterDetailImageURL;
    }

    public String getPosterLargeImageURL() {
        return posterLargeImageURL;
    }

    public void setPosterLargeImageURL(String posterLargeImageURL) {
        this.posterLargeImageURL = posterLargeImageURL;
    }

    public Bitmap getPosterBitmap() {
        return posterBitmap;
    }

    public void setPosterBitmap(Bitmap posterBitmap) {
        this.posterBitmap = posterBitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(vote_count);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeValue(video);
        dest.writeDouble(vote_average);
        dest.writeString(backdrop_path);
        dest.writeValue(adult);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeSerializable(genre_ids);
        dest.writeDouble(popularity);
        dest.writeString(poster_path);
        dest.writeString(posterImageURL);
        dest.writeString(posterDetailImageURL);
        dest.writeString(posterLargeImageURL);
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {

        @Override
        public MovieInfo createFromParcel(Parcel source) {
            return new MovieInfo(source);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}

