/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import net.hobbitsoft.popularmovies.databinding.ActivityMovieInfoBinding;
import net.hobbitsoft.popularmovies.movies.MovieInfo;

import static net.hobbitsoft.popularmovies.utils.DateUtils.dateToYear;

public class MovieInfoActivity extends AppCompatActivity {

    private static final String TAG = MovieInfoActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE_INFO = "extra_movie_info";
    public static final String LARGE_POSTER_URL = "large_poster_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        ActivityMovieInfoBinding activityMovieInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_info);
        //Get the Intent and confirm its validity - else close activity
        final Intent intent = getIntent();
        if (intent == null) {
            Log.e(TAG, getResources().getString(R.string.er_no_intent));
            finish();
        }

        //Get the Movie ID and confirm its validity - else close activity
        Bundle movieInfoBundle = getIntent().getExtras();
        final MovieInfo movieInfo = movieInfoBundle.getParcelable(EXTRA_MOVIE_INFO);

        //Get the appropriate Movie Info
        Picasso.get().load(movieInfo.getPosterDetailImageURL()).into(activityMovieInfoBinding.moviePoster,
                new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, e.getLocalizedMessage());
                        errorToast();
                    }
                });
        activityMovieInfoBinding.movieTitle.setText(movieInfo.getTitle());
        //We just want to display the year
        activityMovieInfoBinding.movieReleaseDate.setText(dateToYear(movieInfo.getReleaseDate()));
        activityMovieInfoBinding.movieRating.setText(String.valueOf(movieInfo.getVoteAverage()
                + getResources().getString(R.string.rating_out_of)));
        activityMovieInfoBinding.movieDescription.setText(movieInfo.getOverview());

        activityMovieInfoBinding.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showPosterIntent = new Intent(MovieInfoActivity.this,
                        ShowPosterOnly.class);
                showPosterIntent.putExtra(LARGE_POSTER_URL, movieInfo.getPosterLargeImageURL());
                MovieInfoActivity.this.startActivity(showPosterIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    There was an issue accessing the Movie DB and so we will show a toast to let the user know
    */
    private void errorToast() {
        String errorMsg = this.getResources().getString(R.string.er_no_access_to_movie_db);
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }
}
