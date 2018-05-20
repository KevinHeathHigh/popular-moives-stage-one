/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmoviesstageone.utils;

import net.hobbitsoft.popularmoviesstageone.movies.MovieInfo;
import net.hobbitsoft.popularmoviesstageone.movies.ResultsInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/* Class to House Useful JSON Utilities */

public class ParseJSONResults {

    //Constants to represent the respective JSON keys
    private static final String RESULTS = "results";
    private static final String PAGE = "page";
    private static final String TOTAL_PAGES = "total_pages";
    private static final String TOTAL_RESULTS = "total_results";
    private static final String VOTE_COUNT = "vote_count";
    private static final String ID = "id";
    private static final String VIDEO = "video";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String TITLE = "title";
    private static final String POPULARITY = "popularity";
    private static final String POSTER_PATH = "poster_path";
    private static final String ORIGINAL_LANGUAGE = "original_language";
    private static final String ORIGINAL_TITLE = "original_title";

    private static final String GENRE_IDS = "genre_ids";

    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String ADULT = "adult";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";

    /*
    The movies are in a results Array under the root JSON Hash, need to parse out
    this Array before we can retrieve the movies.
    {
        "page": 2,
            "total_results": 359398,
            "total_pages": 17970,
            "results": [
        {
           ...
        }
     */
    public static ResultsInfo parseResultPage(String movieList) {

        ResultsInfo resultsInfo = new ResultsInfo();

        try {
            JSONObject results = new JSONObject(movieList);
            resultsInfo.setPage(results.optInt(PAGE));
            resultsInfo.setTotal_pages(results.optInt(TOTAL_PAGES));
            resultsInfo.setTotal_results(results.optInt(TOTAL_RESULTS));

            JSONArray jsonArray = results.optJSONArray(RESULTS);
            ArrayList<MovieInfo> moviesInfo = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                moviesInfo.add(parseMovieJSON(jsonArray.optString(i)));
            }
            resultsInfo.setResults(moviesInfo);
            return resultsInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

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
    private static MovieInfo parseMovieJSON(String movieJSON) {
        MovieInfo movieInfo = new MovieInfo();

        //Parse the JSON String and assign to a MovieInfo object
        try {
            JSONObject movie = new JSONObject(movieJSON);

            movieInfo.setVoteCount(movie.optInt(VOTE_COUNT));
            movieInfo.setId(movie.optInt(ID));
            movieInfo.setVideo(movie.optBoolean(VIDEO));
            movieInfo.setVoteAverage(movie.optDouble(VOTE_AVERAGE));
            movieInfo.setTitle(movie.optString(TITLE));
            movieInfo.setPopularity(movie.optDouble(POPULARITY));
            movieInfo.setPosterPath(movie.optString(POSTER_PATH));
            movieInfo.setOriginalLanguage(movie.optString(ORIGINAL_LANGUAGE));
            movieInfo.setOriginalTitle(movie.optString(ORIGINAL_TITLE));

            //Need to convert a JSONArray to an Array List
            JSONArray genre_ids = movie.optJSONArray(GENRE_IDS);
            ArrayList<Integer> genreIds = new ArrayList<>();
            for (int i = 0; i < genre_ids.length(); i++) {
                genreIds.add(genre_ids.optInt(i));
            }
            movieInfo.setGenreIds(genreIds);

            movieInfo.setBackdropPath(movie.optString(BACKDROP_PATH));
            movieInfo.setAdult(movie.optBoolean(ADULT));
            movieInfo.setOverview(movie.optString(OVERVIEW));
            movieInfo.setReleaseDate(movie.optString(RELEASE_DATE));
            return movieInfo;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
