/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hobbitsoft.popularmovies.R;
import net.hobbitsoft.popularmovies.movies.YouTubeTrailer;


import java.util.List;

/*
Adapted from:
https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
 */
public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    //private static final String TAG = MovieTrailerAdapter.class.getSimpleName();

    private final List<YouTubeTrailer> youTubeTrailerList;
    private final LayoutInflater layoutInflater;
    private ItemClickListener clickListener;

    public MovieTrailerAdapter(@NonNull Context context, @SuppressWarnings("unused") int resource, @NonNull List<YouTubeTrailer> youTubeTrailerList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.youTubeTrailerList = youTubeTrailerList;
    }

    @NonNull
    @Override
    public MovieTrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_movie_trailers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YouTubeTrailer mYouTubeTrailer = youTubeTrailerList.get(position);

        holder.typeTextView.setText(mYouTubeTrailer.getType());
        holder.sizeTextView.setText(mYouTubeTrailer.getSize());
        holder.nameTextView.setText(mYouTubeTrailer.getName());

    }

    @Override
    public int getItemCount() {
        return youTubeTrailerList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView typeTextView;
        final TextView sizeTextView;
        final TextView nameTextView;

        ViewHolder(View view) {
            super(view);
            typeTextView = view.findViewById(R.id.type);
            sizeTextView = view.findViewById(R.id.size);
            nameTextView = view.findViewById(R.id.name);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onItemClick(view, youTubeTrailerList.get(getAdapterPosition()));
        }
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ItemClickListener {
        void onItemClick(@SuppressWarnings("unused") View view, YouTubeTrailer youTubeTrailer);
    }
}
