/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.hobbitsoft.popularmovies.adapters.MovieOverviewAdapter;
import net.hobbitsoft.popularmovies.adapters.MovieReviewAdapter;
import net.hobbitsoft.popularmovies.adapters.MovieTrailerAdapter;
import net.hobbitsoft.popularmovies.apis.MovieDBAccess;
import net.hobbitsoft.popularmovies.constants.InstanceStateKeys;
import net.hobbitsoft.popularmovies.constants.MenuType;
import net.hobbitsoft.popularmovies.databinding.ActivityMovieInfoBinding;
import net.hobbitsoft.popularmovies.favoritesdb.FavoritesDB;
import net.hobbitsoft.popularmovies.movies.MovieInfo;
import net.hobbitsoft.popularmovies.movies.ReviewInfo;
import net.hobbitsoft.popularmovies.movies.ReviewResults;
import net.hobbitsoft.popularmovies.movies.TrailerResults;
import net.hobbitsoft.popularmovies.movies.YouTubeTrailer;
import net.hobbitsoft.popularmovies.utils.AppExecutors;
import net.hobbitsoft.popularmovies.utils.DateUtils;
import net.hobbitsoft.popularmovies.utils.ParseJSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MovieInfoActivity extends AppCompatActivity implements MovieTrailerAdapter.ItemClickListener {

    private static final String TAG = MovieInfoActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE_INFO = "extra_movie_info";
    public static final String LARGE_POSTER_URL = "large_poster_url";

    private MovieInfo movieInfo;
    private List<YouTubeTrailer> youTubeTrailerList;
    private List<ReviewInfo> reviewInfoList;

    private RecyclerView infoRecyclerView;

    //Always default to showing the Overview
    private int mMenuType = MenuType.OVERVIEW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        final ActivityMovieInfoBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_movie_info);

        infoRecyclerView = findViewById(R.id.info_recycler_view);
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Get the Intent and confirm its validity - else close activity
        Intent intent = getIntent();
        if (intent == null) {
            Log.e(TAG, getResources().getString(R.string.er_no_intent));
            finish();
        } else {
            //Get the Movie ID and confirm its validity - else close activity
            Bundle movieInfoBundle = getIntent().getExtras();
            assert movieInfoBundle != null;
            movieInfo = movieInfoBundle.getParcelable(EXTRA_MOVIE_INFO);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(InstanceStateKeys.MOVIE_INFO)) {
                movieInfo = savedInstanceState.getParcelable(InstanceStateKeys.MOVIE_INFO);
            }
            if (savedInstanceState.containsKey(InstanceStateKeys.MENU_TYPE)) {
                mMenuType = savedInstanceState.getInt(InstanceStateKeys.MENU_TYPE);
            }
            if (savedInstanceState.containsKey(InstanceStateKeys.MOVIE_TRAILERS_LIST)) {
                youTubeTrailerList = savedInstanceState
                        .getParcelableArrayList(InstanceStateKeys.MOVIE_TRAILERS_LIST);
            }
            if (savedInstanceState.containsKey(InstanceStateKeys.REVIEW_INFO_LIST)) {
                reviewInfoList = savedInstanceState.getParcelableArrayList(InstanceStateKeys.REVIEW_INFO_LIST);
            }
        } else {
            mMenuType = MenuType.OVERVIEW;
            youTubeTrailerList = new ArrayList<>();
            getTrailers();
            reviewInfoList = new ArrayList<>();
            getReviews();
        }

        /*
         * Get the appropriate Movie Info
         * Since we can't pass the bitmaps between activities add them to MovieInfo in case this
         * movie gets added to the database.  Hopefully these bitmaps are cached
         * Need to define the Targets prior to passing them to Picasso
         * https://github.com/square/picasso/issues/39
         * This still didn't complete work, so also calling Picasso directly into the ImageView
         */
        Picasso.get().load(movieInfo.getPosterDetailImageURL()).into(binding.moviePoster);
        Target detailTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                movieInfo.setDetailBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load(movieInfo.getPosterDetailImageURL()).into(detailTarget);

        Target posterTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                movieInfo.setPosterBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load(movieInfo.getPosterImageURL()).into(posterTarget);

        binding.movieTitle.setText(movieInfo != null ? movieInfo.getTitle() : null);
        //We just want to display the year
        binding.fragmentMovieInfo.movieReleaseDate
                .setText(DateUtils.dateToYear(movieInfo.getReleaseDate()));

        implementRatings(binding, movieInfo);
        implementFavorites(binding);

        populateRecyclerView();

        //Allows the user to click on the poster and get a larger image.
        binding.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showPosterIntent = new Intent(MovieInfoActivity.this,
                        ShowPosterActivity.class);
                showPosterIntent.putExtra(LARGE_POSTER_URL, movieInfo.getPosterLargeImageURL());
                MovieInfoActivity.this.startActivity(showPosterIntent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(InstanceStateKeys.MOVIE_INFO,
                movieInfo);
        outState.putParcelableArrayList(InstanceStateKeys.MOVIE_TRAILERS_LIST,
                (ArrayList<YouTubeTrailer>) youTubeTrailerList);
        outState.putParcelableArrayList(InstanceStateKeys.REVIEW_INFO_LIST,
                (ArrayList<ReviewInfo>) reviewInfoList);
        outState.putInt(InstanceStateKeys.MENU_TYPE, mMenuType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_info_menu, menu);
        menu.findItem(mMenuType).setChecked(true);
        return true;
    }

    /*
     *   We only want to enable menus that actually have content to display
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (movieInfo.getOverview() == null || movieInfo.getOverview().isEmpty()) {
            menu.findItem(R.id.mi_menu_overview).setEnabled(false);
            menu.findItem(R.id.mi_menu_overview).setIcon(R.drawable.ic_overview_menu_gray_24dp);
        } else {
            menu.findItem(R.id.mi_menu_overview).setEnabled(true);
            menu.findItem(R.id.mi_menu_overview).setIcon(R.drawable.ic_overview_menu_24dp);
        }
        if (movieInfo.getYouTubeTrailerList() == null || movieInfo.getYouTubeTrailerList().isEmpty()) {
            menu.findItem(R.id.mi_menu_trailers).setEnabled(false);
            menu.findItem(R.id.mi_menu_trailers).setIcon(R.drawable.ic_trailers_menu_gray_24dp);
        } else {
            menu.findItem(R.id.mi_menu_trailers).setEnabled(true);
            menu.findItem(R.id.mi_menu_trailers).setIcon(R.drawable.ic_trailers_menu_24dp);
        }
        if (movieInfo.getReviewInfoList() == null || movieInfo.getReviewInfoList().isEmpty()) {
            menu.findItem(R.id.mi_menu_reviews).setEnabled(false);
            menu.findItem(R.id.mi_menu_reviews).setIcon(R.drawable.ic_reviews_menu_gray_24dp);
        } else {
            menu.findItem(R.id.mi_menu_reviews).setEnabled(true);
            menu.findItem(R.id.mi_menu_reviews).setIcon(R.drawable.ic_reviews_menu_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                mMenuType = item.getItemId();
                break;
        }
        populateRecyclerView();
        return super.onOptionsItemSelected(item);
    }

    //Populate Recycler View
    private void populateRecyclerView() {
        switch (mMenuType) {
            case MenuType.OVERVIEW:
                MovieOverviewAdapter movieOverviewAdapter = new MovieOverviewAdapter(this,
                        movieInfo.getOverview());
                infoRecyclerView.setAdapter(movieOverviewAdapter);
                break;
            case MenuType.TRAILERS:
                MovieTrailerAdapter movieTrailerAdapter = new MovieTrailerAdapter(this, 0,
                        movieInfo.getYouTubeTrailerList());
                movieTrailerAdapter.setClickListener(this);
                infoRecyclerView.setAdapter(movieTrailerAdapter);
                break;
            case MenuType.REVIEWS:
                MovieReviewAdapter movieReviewAdapter = new MovieReviewAdapter(this,
                        movieInfo.getReviewInfoList());
                infoRecyclerView.setAdapter(movieReviewAdapter);
                break;
        }
    }

    //We want to pre-get the Trailers
    @SuppressLint("StaticFieldLeak")
    class RetrieveTrailers extends AsyncTask<MovieInfo, Void, List<YouTubeTrailer>> {

        @Override
        protected List<YouTubeTrailer> doInBackground(MovieInfo... movieInfo) {
            String trailersJSON;
            List<YouTubeTrailer> youTubeTrailerList = null;
            TrailerResults trailerResults;
            FavoritesDB favoritesDB = FavoritesDB.getInstance(getApplicationContext());
            int movieID = movieInfo[0].getId();

            //We are on a back ground thread, either got to database or goto MovieDB API
            if (favoritesDB.favoritesDao().isAFavorite(movieID) == 1) {
                if (favoritesDB.favoritesDao().hasYouTubeTrailers(movieID) == 1) {
                    youTubeTrailerList = favoritesDB.favoritesDao().retrieveYouTubeTrailers(movieID);
                }
            } else {
                try {
                    trailersJSON = MovieDBAccess.getTrailersList(String.valueOf(movieInfo[0].getId()));
                    if (trailersJSON != null) {
                        trailerResults = ParseJSON.parseTrailerResults(trailersJSON);
                        youTubeTrailerList = trailerResults != null ? trailerResults.getYouTubeResults() : null;
                        for (YouTubeTrailer youTubeTrailer : Objects.requireNonNull(youTubeTrailerList)) {
                            youTubeTrailer.setMovieId(movieID);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            return youTubeTrailerList;
        }

        @Override
        protected void onPostExecute(List<YouTubeTrailer> youTubeTrailerList) {
            saveTrailers(youTubeTrailerList);
        }
    }

    //We want to pre-get the Reviews
    @SuppressLint("StaticFieldLeak")
    class RetrieveReviews extends AsyncTask<MovieInfo, Void, List<ReviewInfo>> {

        @Override
        protected List<ReviewInfo> doInBackground(MovieInfo... movieInfo) {
            String reviewsJSON;
            List<ReviewInfo> reviewInfoList = null;
            ReviewResults reviewResults;
            FavoritesDB favoritesDB = FavoritesDB.getInstance(getApplicationContext());
            int movieId = movieInfo[0].getId();

            //We are on a back ground thread, either goto database or goto MovieDB API
            if (favoritesDB.favoritesDao().isAFavorite(movieId) == 1) {
                if (favoritesDB.favoritesDao().hasReviews(movieId) == 1) {
                    reviewInfoList = favoritesDB.favoritesDao().retrieveReviews(movieId);
                }
            } else {
                try {
                    reviewsJSON = MovieDBAccess.getReviewsList(String.valueOf(movieId));
                    if (reviewsJSON != null) {
                        reviewResults = ParseJSON.parseReviewResults(reviewsJSON);
                        reviewInfoList = reviewResults != null ? reviewResults.getResults() : null;
                        for (ReviewInfo reviewInfo : Objects.requireNonNull(reviewInfoList)) {
                            reviewInfo.setMovieId(movieId);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            return reviewInfoList;
        }

        @Override
        protected void onPostExecute(List<ReviewInfo> reviewInfoList) {
            saveReviews(reviewInfoList);
        }

    }

    private void getTrailers() {
        new RetrieveTrailers().execute(movieInfo);
    }

    private void saveTrailers(List<YouTubeTrailer> youTubeTrailerList) {
        invalidateOptionsMenu();
        this.youTubeTrailerList = youTubeTrailerList;
        movieInfo.setYouTubeTrailerList((ArrayList<YouTubeTrailer>) youTubeTrailerList);
    }

    private void getReviews() {
        new RetrieveReviews().execute(movieInfo);
    }

    private void saveReviews(List<ReviewInfo> reviewInfos) {
        invalidateOptionsMenu();
        this.reviewInfoList = reviewInfos;
        movieInfo.setReviewInfoList((ArrayList<ReviewInfo>) reviewInfos);
    }

    //Logic that fills the stars in relation ot how popular the movie is
    private void implementRatings(final ActivityMovieInfoBinding binding, final MovieInfo movieInfo) {
        String contentDescription = String.valueOf(movieInfo.getVoteAverage()
                + getResources().getString(R.string.rating_out_of));
        double rating = movieInfo.getVoteAverage() / 2;
        if (rating > 0 && rating < 1) {
            binding.fragmentMovieInfo.onestar
                    .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_half_24dp));
            binding.fragmentMovieInfo.onestar.setContentDescription(contentDescription);
        } else {
            for (int i = 1; i < rating; i++) {
                switch (i) {
                    case 1:
                        binding.fragmentMovieInfo.onestar
                                .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_full_24dp));
                        binding.fragmentMovieInfo.onestar.setContentDescription(contentDescription);
                        if (rating > 1 && rating < 2) {
                            binding.fragmentMovieInfo.twostar
                                    .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_half_24dp));
                            binding.fragmentMovieInfo.twostar.setContentDescription(contentDescription);
                        }
                        break;
                    case 2:
                        binding.fragmentMovieInfo.twostar
                                .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_full_24dp));
                        binding.fragmentMovieInfo.twostar.setContentDescription(contentDescription);
                        if (rating > 2 && rating < 3) {
                            binding.fragmentMovieInfo.threestar
                                    .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_half_24dp));
                            binding.fragmentMovieInfo.threestar.setContentDescription(contentDescription);
                        }
                        break;
                    case 3:
                        binding.fragmentMovieInfo.threestar
                                .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_full_24dp));
                        binding.fragmentMovieInfo.threestar.setContentDescription(contentDescription);
                        if (rating > 3 && rating < 4) {
                            binding.fragmentMovieInfo.fourstar
                                    .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_half_24dp));
                            binding.fragmentMovieInfo.fourstar.setContentDescription(contentDescription);
                        }
                        break;
                    case 4:
                        binding.fragmentMovieInfo.fourstar
                                .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_full_24dp));
                        binding.fragmentMovieInfo.fourstar.setContentDescription(contentDescription);
                        if (rating > 4 && rating < 5) {
                            binding.fragmentMovieInfo.fivestar
                                    .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_half_24dp));
                            binding.fragmentMovieInfo.fivestar.setContentDescription(contentDescription);
                        }
                        break;
                    case 5:
                        binding.fragmentMovieInfo.fivestar
                                .setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_full_24dp));
                        binding.fragmentMovieInfo.fivestar.setContentDescription(contentDescription);
                        break;
                }
            }
        }
    }

    private void implementFavorites(final ActivityMovieInfoBinding binding) {
        //Get an instance of the FavoritesDB
        final FavoritesDB favoritesDB = FavoritesDB.getInstance(getApplicationContext());

        AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final boolean isFavorite = (FavoritesDB.getInstance(getApplicationContext())
                        .favoritesDao().isAFavorite(movieInfo.getId()) == 1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Set the initial Favorite Icon
                        if (isFavorite) {
                            binding.fragmentMovieInfo.favorite
                                    .setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_favorite_on_24dp));
                            movieInfo.setFavorite(true);
                        } else {
                            binding.fragmentMovieInfo.favorite
                                    .setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_favorite_off_24dp));
                            movieInfo.setFavorite(false);
                        }
                    }
                });

                binding.fragmentMovieInfo.favorite.setOnClickListener(new View.OnClickListener() {

                    //Control what happens when the user taps the favorite ICON
                    @Override
                    public void onClick(View v) {
                        if (movieInfo.isFavorite()) {
                            //Remove the movie from the database
                            AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    favoritesDB.favoritesDao().removeFavorite(movieInfo);
                                    if (movieInfo.getYouTubeTrailerList() != null) {
                                        for (YouTubeTrailer trailer : movieInfo.getYouTubeTrailerList()) {
                                            favoritesDB.favoritesDao().removeYouTubeTrailer(trailer);
                                        }
                                    }
                                    if (movieInfo.getReviewInfoList() != null) {
                                        for (ReviewInfo review : movieInfo.getReviewInfoList()) {
                                            favoritesDB.favoritesDao().removeReview(review);
                                        }
                                    }
                                }
                            });
                            binding.fragmentMovieInfo.favorite
                                    .setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_favorite_off_24dp));
                            movieInfo.setFavorite(false);
                        } else {
                            //Add the movie to the database
                            AppExecutors.getsInstance().getDiskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    favoritesDB.favoritesDao().addFavorite(movieInfo);
                                    if (movieInfo.getYouTubeTrailerList() != null) {
                                        for (YouTubeTrailer trailer : movieInfo.getYouTubeTrailerList()) {
                                            favoritesDB.favoritesDao().addYouTubeTrailer(trailer);
                                        }
                                    }
                                    if (movieInfo.getReviewInfoList() != null) {
                                        for (ReviewInfo review : movieInfo.getReviewInfoList()) {
                                            favoritesDB.favoritesDao().addReview(review);
                                        }
                                    }
                                }
                            });
                            binding.fragmentMovieInfo.favorite
                                    .setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_favorite_on_24dp));
                            movieInfo.setFavorite(true);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(View view, YouTubeTrailer youTubeTrailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW, youTubeTrailer.getYouTubeUri());
        startActivity(intent);
    }
}
