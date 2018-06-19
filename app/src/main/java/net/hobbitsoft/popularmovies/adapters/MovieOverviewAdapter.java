package net.hobbitsoft.popularmovies.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hobbitsoft.popularmovies.R;

public class MovieOverviewAdapter extends RecyclerView.Adapter<MovieOverviewAdapter.ViewHolder> {


    //private static final String TAG = MovieReviewAdapter.class.getSimpleName();

    final private String overview;
    final private LayoutInflater layoutInflater;

    public MovieOverviewAdapter(@NonNull Context context, String overview) {
        this.layoutInflater = LayoutInflater.from(context);
        this.overview = overview;
    }

    @NonNull
    @Override
    public MovieOverviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_movie_overview, parent, false);

        return new MovieOverviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieOverviewAdapter.ViewHolder holder, int position) {

        holder.overviewTextView.setText(overview);
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView overviewTextView;

        ViewHolder(View view) {
            super(view);

            overviewTextView = view.findViewById(R.id.movie_description);
        }
    }
}
