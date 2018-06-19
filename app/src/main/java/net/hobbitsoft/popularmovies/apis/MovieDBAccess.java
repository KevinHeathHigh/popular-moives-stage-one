/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmovies.apis;

import android.net.Uri;
import android.util.Log;

import net.hobbitsoft.popularmovies.BuildConfig;
import net.hobbitsoft.popularmovies.constants.MenuType;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MovieDBAccess {

    //Movie Database API URL Component Constants
    private static final String MD_API_BASE_URL = "https://api.themoviedb.org/3";
    private static final String MD_MOVIE_PATH = "movie";
    private static final String MD_POPULAR_PATH = "popular";
    private static final String MD_TOP_RATED_PATH = "top_rated";
    private static final String MD_REVIEWS_PATH = "reviews";
    private static final String MD_TRAILERS_PATH = "trailers";
    private static final String MD_PAGE_KEY_PARAM = "page";
    private static final String MD_API_KEY_PARAM = "api_key";
    private static final String INITIAL_RESULTS_PAGE = "1";
    private static final String MD_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String TAG = MovieDBAccess.class.getSimpleName();

    public MovieDBAccess() {
    }

    /* Create the Base URI that all other functions will require to build Upon
        https://api.themoviedb.org/3/?api_key=<api_key_value>
     */
    private static Uri buildBaseAPIURL() {
        return Uri.parse(MD_API_BASE_URL).buildUpon()
                .appendQueryParameter(MD_API_KEY_PARAM,
                        BuildConfig.themoviedb_api_key)
                .build();
    }

    //https://api.themoviedb.org/3/movie/popular
    private static URL getPopularMoviesURL(String page) {
        Uri uri = buildBaseAPIURL().buildUpon()
                .appendPath(MD_MOVIE_PATH)
                .appendPath(MD_POPULAR_PATH)
                .appendQueryParameter(MD_PAGE_KEY_PARAM, page)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }

        return url;
    }

    //https://api.themoviedb.org/3/movie/top_rated
    private static URL getTopRatedMoviesURL(String page) {
        Uri uri = buildBaseAPIURL().buildUpon()
                .appendPath(MD_MOVIE_PATH)
                .appendPath(MD_TOP_RATED_PATH)
                .appendQueryParameter(MD_PAGE_KEY_PARAM, page)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }
        return url;
    }

    //https://api.themoviedb.org/3/movie/{id}/trailers
    private static URL getMovieTrailers(String movieId) {
        Uri uri = buildBaseAPIURL().buildUpon()
                .appendPath(MD_MOVIE_PATH)
                .appendPath(movieId)
                .appendPath(MD_TRAILERS_PATH)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage());
        }
        return url;
    }

    //https://api.themoviedb.org/3/movie/{id}/reviews
    private static URL getMovieReviews(String movieId) {
        Uri uri = buildBaseAPIURL().buildUpon()
                .appendPath(MD_MOVIE_PATH)
                .appendPath(movieId)
                .appendPath(MD_REVIEWS_PATH)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getMovieList(int listType, String page) throws IOException {

        String movieList = "";
        URL movieListURL;
        if (page == null) {
            page = INITIAL_RESULTS_PAGE;
        }
        switch (listType) {
            case MenuType.TOP_RATED:
                movieListURL = getTopRatedMoviesURL(page);
                break;
            case MenuType.POPULAR:
                movieListURL = getPopularMoviesURL(page);
                break;
            default:
                movieListURL = getPopularMoviesURL(page);
        }

        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) movieListURL.openConnection();
        InputStream inputStream;
        try {

            inputStream = httpsURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);

            if (scanner.hasNextLine()) {
                movieList += scanner.nextLine();
            }

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;

        } finally {
            httpsURLConnection.disconnect();
        }

        return movieList;
    }

    public static String getTrailersList(String movieId) throws IOException {

        String trailerList = "";
        URL trailerListURL = getMovieTrailers(movieId);

        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) trailerListURL.openConnection();
        InputStream inputStream;

        try {
            inputStream = httpsURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);

            if (scanner.hasNextLine()) {
                trailerList += scanner.nextLine();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } finally {
            httpsURLConnection.disconnect();
        }

        return trailerList;
    }

    public static String getReviewsList(String movieId) throws IOException {

        String reviewsList = "";

        URL reviewsListURL = getMovieReviews(movieId);

        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) reviewsListURL.openConnection();
        InputStream inputStream;

        try {
            inputStream = httpsURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);

            if (scanner.hasNextLine()) {
                reviewsList += scanner.nextLine();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } finally {
            httpsURLConnection.disconnect();
        }

        return reviewsList;
    }

    /*
     * Base Uri for the location of Movie Images
     * http://image.tmdb.org/t/p
     */
    private static Uri buildBaseImageUri() {
        return Uri.parse(MD_IMAGE_BASE_URL).buildUpon().build();
    }

    /*Full image URL that includes the Size:
     *       "x_small"="w92", "small"="w154", "medium"="w185",
     *       "large"="w342", "x_large"="w500", "xx_larger"="w780"
     * and File name
     * http://image.tmdb.org/t/p/w500/jjPJ4s3DWZZvI4vw8Xfi4Vqa1Q8.jpg
    */
    public static URL getImageURL(String fileName, String size) {
        Uri uri = buildBaseImageUri().buildUpon()
                .appendPath(size)
                //Need to remove the initial back slash '/'
                .appendPath(stripForwardSlash(fileName))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /*
    The Image path includes a '/' forward slash, this needs to be removed inorder for the URL to
    return appropriate values.
     */
    private static String stripForwardSlash(String s) {
        return s.substring(1, s.length());
    }

}
