/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 * This POJO defines the All the objects associated with a single MovieDB Record
 * It also defines the columns for the Favorites table using Room Annotation.
 * https://developer.android.com/topic/libraries/architecture/room
 */

package net.hobbitsoft.popularmovies.movies;
/* Sample of Movie Info JSON
{
    "voteCount": 1626,
    "id": 337167,
    "video": false,
    "voteAverage": 6,
    "title": "Fifty Shades Freed",
    "popularity": 567.058534,
    "posterPath": "/jjPJ4s3DWZZvI4vw8Xfi4Vqa1Q8.jpg",
    "originalLanguage": "en",
    "originalTitle": "Fifty Shades Freed",
    "genreIds": [
        18,
        10749
    ],
    "backdropPath": "/9ywA15OAiwjSTvg3cBs9B7kOCBF.jpg",
    "adult": false,
    "overview": "Believing they have left behind shadowy figures from their past, newlyweds Christian and Ana fully embrace an inextricable connection and shared life of luxury. But just as she steps into her role as Mrs. Grey and he relaxes into an unfamiliar stability, new threats could jeopardize their happy ending before it even begins.",
    "releaseDate": "2018-02-07"
}
 */

import android.annotation.SuppressLint;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import net.hobbitsoft.popularmovies.utils.ArrayConverter;
import net.hobbitsoft.popularmovies.utils.BitmapConverter;

import java.util.ArrayList;

@Entity(tableName = "favorites")
public class MovieInfo implements Parcelable {

    //issue with Room that requires these to be public because it can't find their Getters
    @ColumnInfo(name = "vote_count")
    private int voteCount;
    @PrimaryKey
    private int id;
    private String title;
    private boolean video;
    @ColumnInfo(name = "vote_average")
    private double voteAverage;
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;
    private boolean adult;
    private String overview;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name = "original_language")
    private String originalLanguage;
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    @ColumnInfo(name = "genre_ids")
    @TypeConverters(ArrayConverter.class)
    private ArrayList<Integer> genreIds;
    private double popularity;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @ColumnInfo(name = "poster_image_url")
    private String posterImageURL;
    @ColumnInfo(name = "poster_detail_image_url")
    private String posterDetailImageURL;
    @ColumnInfo(name = "poster_large_image_url")
    private String posterLargeImageURL;
    @ColumnInfo(name = "backdrop_image_url")
    private String backdropImageURL;
    @ColumnInfo(name = "poster_bitmap")
    @TypeConverters(BitmapConverter.class)
    private Bitmap posterBitmap;
    @ColumnInfo(name = "detail_bitmap")
    @TypeConverters(BitmapConverter.class)
    private Bitmap detailBitmap;
    @Ignore
    private ArrayList<YouTubeTrailer> youTubeTrailers;
    @Ignore
    private ArrayList<ReviewInfo> reviewInfoList;
    @Ignore
    private boolean isFavorite;

    @Ignore
    public MovieInfo() {
    }

    public MovieInfo(int voteCount, int id, String title, boolean video, double voteAverage, String backdropPath, boolean adult, String overview, String releaseDate, String originalLanguage, String originalTitle, ArrayList<Integer> genreIds, double popularity, String posterPath, Bitmap posterBitmap, Bitmap detailBitmap) {
        this.voteCount = voteCount;
        this.id = id;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.posterBitmap = posterBitmap;
        this.detailBitmap = detailBitmap;
    }

    @SuppressLint("ParcelClassLoader")
    private MovieInfo(Parcel parcel) {
        voteCount = parcel.readInt();
        id = parcel.readInt();
        title = parcel.readString();
        video = (Boolean) parcel.readValue(null);
        voteAverage = parcel.readDouble();
        backdropPath = parcel.readString();
        adult = (Boolean) parcel.readValue(null);
        overview = parcel.readString();
        releaseDate = parcel.readString();
        originalLanguage = parcel.readString();
        originalTitle = parcel.readString();
        genreIds = ArrayConverter.stringToArray(parcel.readString());
        popularity = parcel.readDouble();
        posterPath = parcel.readString();
        posterImageURL = parcel.readString();
        posterDetailImageURL = parcel.readString();
        posterLargeImageURL = parcel.readString();
    }

    @SuppressWarnings("unused")
    public int getVoteCount() {
        return this.voteCount;
    }

    public void setVoteCount(int vote_count) {
        this.voteCount = vote_count;
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
        return this.voteAverage;
    }

    public void setVoteAverage(double vote_average) {
        this.voteAverage = vote_average;
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
        return this.posterPath;
    }

    public void setPosterPath(String poster_path) {
        this.posterPath = poster_path;
    }

    public String getOriginalLanguage() {
        return this.originalLanguage;
    }

    public void setOriginalLanguage(String original_language) {
        this.originalLanguage = original_language;
    }

    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public void setOriginalTitle(String original_title) {
        this.originalTitle = original_title;
    }

    public ArrayList<Integer> getGenreIds() {
        return this.genreIds;
    }

    public void setGenreIds(ArrayList<Integer> genre_ids) {
        this.genreIds = genre_ids;
    }

    public String getBackdropPath() {
        return this.backdropPath;
    }

    public void setBackdropPath(String backdrop_path) {
        this.backdropPath = backdrop_path;
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
        return this.releaseDate;
    }

    public void setReleaseDate(String release_date) {
        this.releaseDate = release_date;
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

    public String getBackdropImageURL() {
        return backdropImageURL;
    }

    public void setBackdropImageURL(String backdropImageURL) {
        this.backdropImageURL = backdropImageURL;
    }

    public Bitmap getPosterBitmap() {
        return posterBitmap;
    }

    public void setPosterBitmap(Bitmap posterBitmap) {
        this.posterBitmap = posterBitmap;
    }

    public Bitmap getDetailBitmap() {
        return detailBitmap;
    }

    public void setDetailBitmap(Bitmap detailBitmap) {
        this.detailBitmap = detailBitmap;
    }

    public ArrayList<YouTubeTrailer> getYouTubeTrailerList() {
        return youTubeTrailers;
    }

    public void setYouTubeTrailerList(ArrayList<YouTubeTrailer> youTubeTrailerList) {
        this.youTubeTrailers = youTubeTrailerList;
    }

    public ArrayList<ReviewInfo> getReviewInfoList() {
        return reviewInfoList;
    }

    public void setReviewInfoList(ArrayList<ReviewInfo> reviewInfoList) {
        this.reviewInfoList = reviewInfoList;
    }

    //Just looks prettier than using 'get'
    public boolean isFavorite() {
        return isFavorite;
    }

    @SuppressWarnings("unused")
    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(voteCount);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeValue(video);
        dest.writeDouble(voteAverage);
        dest.writeString(backdropPath);
        dest.writeValue(adult);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(ArrayConverter.arrayToString(genreIds));
        dest.writeDouble(popularity);
        dest.writeString(posterPath);
        dest.writeString(posterImageURL);
        dest.writeString(posterDetailImageURL);
        dest.writeString(posterLargeImageURL);
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {

        @Override
        public MovieInfo createFromParcel(Parcel parcel) {
            return new MovieInfo(parcel);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}

