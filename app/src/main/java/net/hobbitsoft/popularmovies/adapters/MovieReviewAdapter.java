package net.hobbitsoft.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hobbitsoft.popularmovies.R;
import net.hobbitsoft.popularmovies.movies.ReviewInfo;

import java.util.List;

import ru.noties.markwon.Markwon;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    //private static final String TAG = MovieReviewAdapter.class.getSimpleName();

    private final List<ReviewInfo> reviewInfoList;
    private final LayoutInflater layoutInflater;

    public MovieReviewAdapter(@NonNull Context context, List<ReviewInfo> reviewInfoList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.reviewInfoList = reviewInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.fragment_movie_reviews, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewInfo mReviewInfo = reviewInfoList.get(position);

        Markwon.setMarkdown(holder.review, mReviewInfo.getContent());
        holder.reviewer.setText(mReviewInfo.getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviewInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView review;
        final TextView reviewer;

        ViewHolder(View view) {
            super(view);

            review = view.findViewById(R.id.review);
            reviewer = view.findViewById(R.id.reviewer);
        }
    }
}
