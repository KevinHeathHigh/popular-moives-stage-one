/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmovies.utils;

import net.hobbitsoft.popularmovies.movies.MovieInfo;
import net.hobbitsoft.popularmovies.movies.MovieResults;
import net.hobbitsoft.popularmovies.movies.QuicktimeTrailer;
import net.hobbitsoft.popularmovies.movies.ReviewInfo;
import net.hobbitsoft.popularmovies.movies.ReviewResults;
import net.hobbitsoft.popularmovies.movies.TrailerResults;
import net.hobbitsoft.popularmovies.movies.YouTubeTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/* Class to House Useful JSON Utilities */

public class ParseJSON {

    //private static final String TAG = ParseJSON.class.getSimpleName();

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

    private static final String QUICKTIME = "quicktime";
    private static final String YOUTUBE = "youtube";

    private static final String NAME = "name";
    private static final String SIZE = "size";
    private static final String SOURCE = "source";
    private static final String TYPE = "type";

    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String URL = "url";

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
    public static MovieResults parseMovieResults(String movieResultsJSON) {

        MovieResults resultsInfo = new MovieResults();

        try {
            JSONObject results = new JSONObject(movieResultsJSON);
            resultsInfo.setPage(results.optInt(PAGE));
            resultsInfo.setTotal_pages(results.optInt(TOTAL_PAGES));
            resultsInfo.setTotal_results(results.optInt(TOTAL_RESULTS));

            JSONArray resultsArray = results.optJSONArray(RESULTS);
            ArrayList<MovieInfo> moviesInfo = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                moviesInfo.add(parseMovies(resultsArray.optString(i)));
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
    private static MovieInfo parseMovies(String moviesJSON) {
        MovieInfo movieInfo = new MovieInfo();

        //Parse the JSON String and assign to a MovieInfo object
        try {
            JSONObject movie = new JSONObject(moviesJSON);

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
            if (genre_ids != null) {
                for (int i = 0; i < genre_ids.length(); i++) {
                    genreIds.add(genre_ids.optInt(i));
                }
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

    /*
    {
        "id": 284053,
        "quicktime": [],
        "youtube": []
    }
 */
    public static TrailerResults parseTrailerResults(String trailerResultsJSON) {

        TrailerResults trailerResults = new TrailerResults();

        try {
            JSONObject result = new JSONObject(trailerResultsJSON);
            trailerResults.setId(result.optInt(ID));

            JSONArray quicktimeArray = result.optJSONArray(QUICKTIME);
            ArrayList<QuicktimeTrailer> quicktimeTrailers = new ArrayList<>();
            for (int i = 0; i < quicktimeArray.length(); i++) {
                quicktimeTrailers.add(parseQuicktimeTrailers(quicktimeArray.optString(i)));
            }
            trailerResults.setQuicktime(quicktimeTrailers);

            JSONArray youTubeArray = result.optJSONArray(YOUTUBE);
            ArrayList<YouTubeTrailer> youTubeTrailers = new ArrayList<>();
            for (int i = 0; i < youTubeArray.length(); i++) {
                youTubeTrailers.add(parseYouTubeTrailers(youTubeArray.optString(i)));
            }
            trailerResults.setYoutube(youTubeTrailers);

            return trailerResults;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unused")
    private static QuicktimeTrailer parseQuicktimeTrailers(String quicktimeJSON) {
        return new QuicktimeTrailer();
    }

    /*
    "youtube": [
        {
            "name": "\"Thor: Ragnarok\" Official Trailer",
            "size": "HD",
            "source": "ue80QwXMRHg",
            "type": "Trailer"
        }
     ]
}
 */
    private static YouTubeTrailer parseYouTubeTrailers(String youTubeJSON) {
        YouTubeTrailer youTubeTrailer = new YouTubeTrailer();

        try {
            JSONObject trailer = new JSONObject(youTubeJSON);
            youTubeTrailer.setName(trailer.optString(NAME));
            youTubeTrailer.setSize(trailer.optString(SIZE));
            youTubeTrailer.setSource(trailer.optString(SOURCE));
            youTubeTrailer.setType(trailer.optString(TYPE));

            return youTubeTrailer;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     "id": 284053,
        "page": 1,
        "results": [
    ...
        ],
        "total_pages": 1,
        "total_results": 2
     */
    public static ReviewResults parseReviewResults(String reviewResultsJSON) {
        ReviewResults resultsInfo = new ReviewResults();

        try {
            JSONObject results = new JSONObject(reviewResultsJSON);
            resultsInfo.setPage(results.optInt(PAGE));
            resultsInfo.setTotal_pages(results.optInt(TOTAL_PAGES));
            resultsInfo.setTotal_results(results.optInt(TOTAL_RESULTS));

            JSONArray resultsArray = results.optJSONArray(RESULTS);
            ArrayList<ReviewInfo> reviewInfos = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                reviewInfos.add(parseReviews(resultsArray.optString(i)));
            }
            resultsInfo.setResults(reviewInfos);
            return resultsInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
        {
            "author": "Gimly",
            "content": "_Ragnarok_ is a tough one to review completely without spoiler, so I'll keep this brief:\r\n\r\n* I was bothered by a small number of things that happened in this movie.\r\n* I was in love with a large number of things that happened in this movie.\r\n* _Thor: Ragnarok_ is not the MCU's best movie.\r\n* _Thor: Ragnarok_ is the MCU's most fun movie.\r\n* Can we **please** have a worldwide ban on putting anything that happens in the third act of your movie, in said movie's trailers?\r\nThank you for your time. 4 stars.\r\n\r\n\r\n_Final rating:★★★★ - Very strong appeal. A personal favourite._",
            "id": "59f09b979251416ac10169f7",
            "url": "https://www.themoviedb.org/review/59f09b979251416ac10169f7"
        },
 */
    private static ReviewInfo parseReviews(String reviewJSON) {
        ReviewInfo reviewInfo = new ReviewInfo();

        try {
            JSONObject review = new JSONObject(reviewJSON);
            reviewInfo.setAuthor(review.optString(AUTHOR));
            reviewInfo.setContent(review.optString(CONTENT));
            reviewInfo.setId(review.optString(ID));
            reviewInfo.setUrl(review.optString(URL));
            return reviewInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
