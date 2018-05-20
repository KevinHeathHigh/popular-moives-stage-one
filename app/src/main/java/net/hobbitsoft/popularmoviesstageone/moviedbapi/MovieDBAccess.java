/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmoviesstageone.moviedbapi;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import net.hobbitsoft.popularmoviesstageone.R;
import net.hobbitsoft.popularmoviesstageone.constants.ListType;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MovieDBAccess {

    private static Context mContext;

    private static final String TAG = MovieDBAccess.class.getSimpleName();

    /* Need the Activity Context for this class to retrieve Resources */
    public MovieDBAccess(Context context) {
        mContext = context;
    }


    /* Create the Base URI that all other functions will require to build Upon
        https://api.themoviedb.org/3/?api_key=<api_key_value>
     */
    private static Uri buildBaseAPIURL() {
        return Uri.parse(mContext.getString(R.string.md_api_base_url)).buildUpon()
                .appendQueryParameter(mContext.getString(R.string.md_api_key_param), mContext.getString(R.string.md_api_key_value))
                .build();
    }

    //https://api.themoviedb.org/3/movie/popular
    private static URL getPopularMoviesURL(String page) {
        Uri uri = buildBaseAPIURL().buildUpon()
                .appendPath(mContext.getString(R.string.md_movie_path))
                .appendPath(mContext.getString(R.string.md_popular_path))
                .appendQueryParameter(mContext.getString(R.string.md_page_key_param), page)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.d(TAG, uri.toString());
            e.printStackTrace();
        }

        return url;
    }

    //https://api.themoviedb.org/3/movie/top_rated
    private static URL getTopRatedMoviesURL(String page) {
        Uri uri = buildBaseAPIURL().buildUpon()
                .appendPath(mContext.getString(R.string.md_movie_path))
                .appendPath(mContext.getString(R.string.md_top_rated_path))
                .appendQueryParameter(mContext.getString(R.string.md_page_key_param), page)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.d(TAG, uri.toString());
            e.printStackTrace();
        }
        return url;
    }

    public static String getPopularMovieList(int listType, String page) throws IOException {

        @SuppressWarnings("RedundantStringConstructorCall") String movieList = new String();
        URL movieListURL;

        switch (listType) {
            case ListType.TOP_RATED:
                movieListURL = getTopRatedMoviesURL(page);
                break;
            case ListType.POPULAR:
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

    /*
     Base Uri for the location of Movie Images
     http://image.tmdb.org/t/p
     */
    private static Uri buildBaseImageUri() {
        return Uri.parse(mContext.getString(R.string.md_image_base_url)).buildUpon().build();
    }

    /*Full image URL that includes the Size:
            "x_small"="w92", "small"="w154", "medium"="w185",
            "large"="w342", "x_large"="w500", "xx_larger"="w780"
    and File name
    http://image.tmdb.org/t/p/w500/jjPJ4s3DWZZvI4vw8Xfi4Vqa1Q8.jpg
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
            Log.d(TAG, uri.toString());
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
