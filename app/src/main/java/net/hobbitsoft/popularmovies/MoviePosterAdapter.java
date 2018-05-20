/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import net.hobbitsoft.popularmovies.movies.MovieInfo;

import java.util.List;

class MoviePosterAdapter extends ArrayAdapter<MovieInfo> {

    private final Context mContext;

    public MoviePosterAdapter(@NonNull Context context, int resource, @NonNull List<MovieInfo> movieInfoList) {
        super(context, resource, movieInfoList);
        this.mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final MovieInfo movieInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.poster_image, parent, false);
        }

        ImageView posterImage = convertView.findViewById(R.id.posterImage);

        posterImage.setImageBitmap(movieInfo.getPosterBitmap());

        posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MovieInfoActivity.class);

                //Serialize the MovieInfo class - per https://stackoverflow.com/questions/4249897/how-to-send-objects-through-bundle
                Bundle movieInfoBundle = new Bundle();

                movieInfoBundle.putParcelable(MovieInfoActivity.EXTRA_MOVIE_INFO, movieInfo);
                intent.putExtras(movieInfoBundle);
                mContext.startActivity(intent);

            }
        });

        return convertView;
    }
}

