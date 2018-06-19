/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.hobbitsoft.popularmovies.movies.MovieInfo;

import java.util.List;

/*
Adapted from:
https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
 */
class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.ViewHolder> {

    private final List<MovieInfo> movieInfoList;
    private final LayoutInflater layoutInflater;
    private ItemClickListener clickListener;


    public MoviePosterAdapter(@NonNull Context context, @SuppressWarnings("unused") int resource, @NonNull List<MovieInfo> movieInfoList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.movieInfoList = movieInfoList;
    }

    @NonNull
    @Override
    public MoviePosterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_poster_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePosterAdapter.ViewHolder holder, int position) {
        MovieInfo mMovieInfo = movieInfoList.get(position);
        holder.posterImage.setImageBitmap(mMovieInfo.getPosterBitmap());
    }

    @Override
    public int getItemCount() {
        if (movieInfoList == null) {
            return 0;
        } else {
            return movieInfoList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView posterImage;

        ViewHolder(View view) {
            super(view);
            posterImage = view.findViewById(R.id.poster_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onItemClick(view, movieInfoList.get(getAdapterPosition()));
        }
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ItemClickListener {
        void onItemClick(@SuppressWarnings("unused") View view, MovieInfo movieInfo);
    }
}