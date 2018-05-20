/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

@SuppressWarnings("RedundantCast")
public class ShowPosterOnly extends AppCompatActivity {

    private static final String TAG = ShowPosterOnly.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_poster_only);

        ImageView posterImage = (ImageView) findViewById(R.id.show_poster_only);

        final Intent intent = getIntent();
        if (intent == null) {
            this.finish();
        }

        /* The URL for the Lager Poster Image is stored in the Intent's Extra */
        final String largeImageURL = intent.getStringExtra(MovieInfoActivity.LARGE_POSTER_URL);

        Picasso.get().load(largeImageURL).into(posterImage, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
                errorToast();
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
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        this.finish();
    }

}
