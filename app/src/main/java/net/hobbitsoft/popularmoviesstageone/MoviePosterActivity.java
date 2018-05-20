/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmoviesstageone;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.hobbitsoft.popularmoviesstageone.constants.ImageSizes;
import net.hobbitsoft.popularmoviesstageone.constants.ListType;
import net.hobbitsoft.popularmoviesstageone.moviedbapi.MovieDBAccess;
import net.hobbitsoft.popularmoviesstageone.movies.MovieInfo;
import net.hobbitsoft.popularmoviesstageone.movies.ResultsInfo;
import net.hobbitsoft.popularmoviesstageone.utils.ParseJSONResults;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MoviePosterActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private final MovieDBAccess mMovieDBAccess = new MovieDBAccess(this);
    private GridView mPosterGrid;

    private int mSelectedMenuItem = -1;
    private int mListType = ListType.POPULAR;

    private List<MovieInfo> mMovieInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_poster);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mPosterGrid = (GridView) findViewById(R.id.poster_grid);

        if (savedInstanceState == null ||
                !savedInstanceState
                        .containsKey(this.getResources().getString(R.string.mi_instance_state_key))) {
            mMovieInfoList = new ArrayList<>();
            mListType = ListType.POPULAR;
            getMovies();
        } else {
            mMovieInfoList = savedInstanceState
                    .getParcelableArrayList(this.getResources().getString(R.string.mi_instance_state_key));
            mListType = savedInstanceState.getInt(this.getResources().getString(R.string.lt_instance_state_key));
            mSelectedMenuItem = savedInstanceState.getInt(this.getResources().getString(R.string.checked_menu_id));
            populatePosters(mMovieInfoList);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(this.getResources().getString(R.string.mi_instance_state_key),
                (ArrayList<MovieInfo>) mMovieInfoList);
        outState.putInt(this.getResources().getString(R.string.lt_instance_state_key), mListType);
        outState.putInt(this.getResources().getString(R.string.checked_menu_id), mSelectedMenuItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_type_menu, menu);
        if (mSelectedMenuItem == -1) {
            menu.findItem(R.id.lt_mn_popular).setChecked(true);
        } else {
            menu.findItem(mSelectedMenuItem).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (!item.isChecked()) {
            switch (item.getItemId()) {
                case R.id.lt_mn_popular:
                    mListType = ListType.POPULAR;
                    break;
                case R.id.lt_mn_top_rated:
                    mListType = ListType.TOP_RATED;
                    break;
            }
            item.setChecked(true);

            mSelectedMenuItem = item.getItemId();
            getMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Function to create and execute the Asynchronous Task class retrieve popular movies */
    private void getMovies() {
        new RetrievePopularMovies().execute();
    }

    /* Asynchronous Task class to retrieve popular moves*/
    class RetrievePopularMovies extends AsyncTask<Void, Void, List<MovieInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieInfo> doInBackground(Void... voids) {

            String movies = null;

            try {
                movies = mMovieDBAccess.getPopularMovieList(mListType, getResources().getString(R.string.initial_result_page));
                if (movies == null) {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            ResultsInfo results = ParseJSONResults.parseResultPage(movies);

            /*
            Not sure why, but it seems that I can only get the posters to display in the Adapter if
            I get() them here and save them as a Bitmap object in the MovieInfo class, assuming it
            has something to do with the adapter being a slightly separate object.
             */

            String moviePosterURL;
            String moviePosterDetailsURL;
            String moviePosterLargeURL;

            if (results.getResults() != null) {
                for (MovieInfo list : results.getResults()) {
                    moviePosterURL = mMovieDBAccess.getImageURL(list.getPosterPath(), ImageSizes.SMALL).toString();
                    list.setPosterImageURL(moviePosterURL);
                    moviePosterDetailsURL = mMovieDBAccess.getImageURL(list.getPosterPath(), ImageSizes.X_SMALL).toString();
                    list.setPosterDetailImageURL(moviePosterDetailsURL);
                    moviePosterLargeURL = mMovieDBAccess.getImageURL(list.getPosterPath(), ImageSizes.X_LARGE).toString();
                    list.setPosterLargeImageURL(moviePosterLargeURL);
                    try {
                        list.setPosterBitmap(Picasso.get().load(moviePosterURL).get());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return results.getResults();
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieInfo> movieInfoList) {
            if (movieInfoList == null) {
                errorToast();
            } else {
                populatePosters(movieInfoList);
            }
            mProgressBar.setVisibility((View.GONE));
        }
    }

    private void populatePosters(List<MovieInfo> movieInfoList) {

        MoviePosterAdapter mMoviePosterAdapter = new MoviePosterAdapter(this, 0, movieInfoList);
        mPosterGrid.setAdapter((ListAdapter) mMoviePosterAdapter);
        mMovieInfoList = movieInfoList;
    }

    /*
    There was an issue accessing the Movie DB and so we will show a toast to let the user know
     */
    private void errorToast() {
        String errorMsg = this.getResources().getString(R.string.er_no_access_to_movie_db);
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

}
