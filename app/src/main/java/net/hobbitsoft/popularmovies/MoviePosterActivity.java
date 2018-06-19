/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.hobbitsoft.popularmovies.MoviePosterAdapter;
import net.hobbitsoft.popularmovies.constants.ImageSizes;
import net.hobbitsoft.popularmovies.constants.InstanceStateKeys;
import net.hobbitsoft.popularmovies.constants.MenuType;
import net.hobbitsoft.popularmovies.apis.MovieDBAccess;
import net.hobbitsoft.popularmovies.favoritesdb.FavoritesDB;
import net.hobbitsoft.popularmovies.movies.MovieInfo;
import net.hobbitsoft.popularmovies.movies.MovieResults;
import net.hobbitsoft.popularmovies.utils.ParseJSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MoviePosterActivity extends AppCompatActivity implements MoviePosterAdapter.ItemClickListener {

    private static final String TAG = MoviePosterActivity.class.getSimpleName();

    private final MovieDBAccess movieDBAccess = new MovieDBAccess();
    private RecyclerView posterRecyclerView;

    private int mMenuType = MenuType.POPULAR;

    private List<MovieInfo> movieInfoList;
    private ProgressBar posterProgressBar;

    private boolean hasFavorites = false;
    private boolean hasInternet = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_poster);
        posterProgressBar = (ProgressBar) findViewById(R.id.poster_progress);
        posterRecyclerView = (RecyclerView) findViewById(R.id.poster_recycler_view);
        int numberOfColumns = getResources().getInteger(R.integer.mp_num_columns);

        posterRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        if (savedInstanceState == null ||
                !savedInstanceState
                        .containsKey(InstanceStateKeys.MENU_TYPE)) {
            mMenuType = MenuType.POPULAR;
        } else {
            mMenuType = savedInstanceState.getInt(InstanceStateKeys.MENU_TYPE);
        }
        if (savedInstanceState == null ||
                !savedInstanceState
                        .containsKey(InstanceStateKeys.HAS_FAVORITES)) {
            checkForFavorites();
        } else {
            hasFavorites = savedInstanceState.getBoolean(InstanceStateKeys.HAS_FAVORITES);
        }
        if (savedInstanceState == null ||
                !savedInstanceState
                        .containsKey(InstanceStateKeys.MOVIE_INFO_LIST)) {
            movieInfoList = new ArrayList<>();
            getMovies();
        } else {
            movieInfoList = savedInstanceState
                    .getParcelableArrayList(InstanceStateKeys.MOVIE_INFO_LIST);
            populatePosters(movieInfoList);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(InstanceStateKeys.MOVIE_INFO_LIST,
                (ArrayList<MovieInfo>) movieInfoList);
        outState.putInt(InstanceStateKeys.MENU_TYPE, mMenuType);
        outState.putBoolean(InstanceStateKeys.HAS_FAVORITES, hasFavorites);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_type_menu, menu);
        //Default to displaying Popular Movies
        menu.findItem(mMenuType).setChecked(true);
        return true;
    }

    //TODO: Code to check for Internet Access
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (hasInternet) {
            menu.findItem(R.id.lt_mn_popular).setEnabled(true);
            menu.findItem(R.id.lt_mn_popular).setIcon(R.drawable.ic_popular_menu_24dp);
            menu.findItem(R.id.lt_mn_top_rated).setEnabled(true);
            menu.findItem(R.id.lt_mn_top_rated).setIcon(R.drawable.ic_top_rated_menu_24dp);
        } else {
            menu.findItem(R.id.lt_mn_popular).setEnabled(false);
            menu.findItem(R.id.lt_mn_popular).setIcon(R.drawable.ic_popular_menu_gray_24dp);
            menu.findItem(R.id.lt_mn_top_rated).setEnabled(false);
            menu.findItem(R.id.lt_mn_top_rated).setIcon(R.drawable.ic_top_rated_menu_gray_24dp);
        }
        if (hasFavorites) {
            menu.findItem(R.id.lt_mn_favorites).setEnabled(true);
            menu.findItem(R.id.lt_mn_favorites).setIcon(R.drawable.ic_favorite_menu_24dp);
        } else {
            menu.findItem(R.id.lt_mn_favorites).setEnabled(false);
            menu.findItem(R.id.lt_mn_favorites).setIcon(R.drawable.ic_favorite_menu_gray_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mMenuType != item.getItemId()) {
            item.setChecked(true);
            mMenuType = item.getItemId();
            getMovies();
        }
        return true;
    }

    @Override
    public void onItemClick(View view, MovieInfo movieInfo) {
        Intent intent = new Intent(this, MovieInfoActivity.class);

        //Serialize the MovieInfo class - per https://stackoverflow.com/questions/4249897/how-to-send-objects-through-bundle
        Bundle movieInfoBundle = new Bundle();

        movieInfoBundle.putParcelable(MovieInfoActivity.EXTRA_MOVIE_INFO, movieInfo);
        intent.putExtras(movieInfoBundle);
        this.startActivity(intent);
    }

    /*
     *  Monitor the existence of rows in the Favorites Table.
     */
    private void checkForFavorites() {
        final LiveData<Integer> mFavorites = FavoritesDB.getInstance(this).favoritesDao().hasFavorites();
        mFavorites.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == 1) {
                    hasFavorites = true;
                } else {
                    hasFavorites = false;
                }
                invalidateOptionsMenu();
            }
        });
    }

    /* Asynchronous Task class to retrieve popular moves*/
    class RetrieveMovies extends AsyncTask<Void, Void, List<MovieInfo>> {

        @Override
        protected List<MovieInfo> doInBackground(Void... voids) {

            String moviesJSON = "";
            List<MovieInfo> movieInfoList = null;
            MovieResults movieResults = null;

            try {
                moviesJSON = movieDBAccess.getMovieList(mMenuType, null);
                if (moviesJSON == null) {
                    return null;
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return null;
            }

            movieResults = ParseJSON.parseMovieResults(moviesJSON);
            movieInfoList = movieResults.getResults();

            /*
            While we are in the background thread, lets get the posters too
             */
            String moviePosterURL;
            String moviePosterDetailsURL;
            String moviePosterLargeURL;
            String movieBackdropURL;

            if (!movieInfoList.isEmpty()) {
                for (MovieInfo list : movieInfoList) {
                    moviePosterURL = movieDBAccess.getImageURL(list.getPosterPath(), ImageSizes.SMALL).toString();
                    list.setPosterImageURL(moviePosterURL);
                    moviePosterDetailsURL = movieDBAccess.getImageURL(list.getPosterPath(), ImageSizes.X_SMALL).toString();
                    list.setPosterDetailImageURL(moviePosterDetailsURL);
                    moviePosterLargeURL = movieDBAccess.getImageURL(list.getPosterPath(), ImageSizes.X_LARGE).toString();
                    list.setPosterLargeImageURL(moviePosterLargeURL);
                    try {
                        //Get the Movie Post Bitmap and save to MovieInfo Class
                        list.setPosterBitmap(Picasso.get().load(moviePosterURL).get());
                        list.setDetailBitmap(Picasso.get().load(moviePosterDetailsURL).get());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    movieBackdropURL = movieDBAccess.getImageURL(list.getBackdropPath(), ImageSizes.LARGE).toString();
                    list.setBackdropImageURL(movieBackdropURL);
                }
                return movieInfoList;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieInfo> movieInfoList) {
            if (movieInfoList == null) {
                errorToast(R.string.er_no_access_to_movie_db);
                posterProgressBar.setVisibility((View.GONE));
            } else {
                resolveListType(movieInfoList);
            }
        }
    }

    /* Function to create and execute the Asynchronous Task class retrieve popular movies */
    private void getMovies() {
        posterProgressBar.setVisibility(View.VISIBLE);
        //We only need to use the Async Task if we are actually reaching out to the MovieDB
        if (mMenuType != MenuType.FAVORITES) {
            new RetrieveMovies().execute();
        } else {
            resolveListType(null);
        }
    }

    private void resolveListType(final List<MovieInfo> movieInfoList) {
        /*
         * If we get a null, then either the MovieDB failed, or we are explicitly asking for Farvorites
         * In either case, we will display the favorites.
         */
        if (movieInfoList == null) {
            if (hasFavorites) {
                final LiveData<List<MovieInfo>> favoriteMoviesInfoList =
                        FavoritesDB.getInstance(getApplicationContext()).favoritesDao()
                                .retrieveAllFavorites();
                favoriteMoviesInfoList.observe(this, new Observer<List<MovieInfo>>() {
                    @Override
                    public void onChanged(@Nullable List<MovieInfo> movieInfos) {
                        //Basically no more favorites
                        if (movieInfos == null || movieInfos.isEmpty()) {
                            posterProgressBar.setVisibility((View.GONE));
                            errorToast(R.string.er_no_favorite_movies);
                            mMenuType = MenuType.POPULAR;
                            resolveListType(movieInfos);
                        } else {
                            //Only refresh the movies when Favorites is selected
                            if (mMenuType == MenuType.FAVORITES) {
                                populatePosters(movieInfos);
                            }
                            posterProgressBar.setVisibility((View.GONE));
                        }
                    }
                });
            }
        } else {
            populatePosters(movieInfoList);
        }
    }

    private void populatePosters(List<MovieInfo> movieInfoList) {
        MoviePosterAdapter moviePosterAdapter =
                new MoviePosterAdapter(this, 0, movieInfoList);
        moviePosterAdapter.setClickListener(this);
        posterRecyclerView.setAdapter(moviePosterAdapter);
        posterRecyclerView.setBackground(null);
        this.movieInfoList = movieInfoList;
        posterProgressBar.setVisibility((View.GONE));
    }

    /*
     *   If there was an issue accessing the Movie DB or there are now movies to show
     *   Dispaly a toast with the appropirate response.
     */
    private void errorToast(int message) {
        String errorMsg = this.getResources().getString(message);
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();

    }

}

