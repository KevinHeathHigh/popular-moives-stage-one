package net.hobbitsoft.popularmovies.favoritesdb;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import net.hobbitsoft.popularmovies.movies.MovieInfo;
import net.hobbitsoft.popularmovies.movies.ReviewInfo;
import net.hobbitsoft.popularmovies.movies.YouTubeTrailer;

import java.util.List;

@SuppressWarnings("unused")
@Dao
public interface FavoritesDao {

    @Query("SELECT * FROM favorites")
    LiveData<List<MovieInfo>> retrieveAllFavorites();

    @Query("SELECT EXISTS(SELECT * FROM favorites)")
    LiveData<Integer> hasFavorites();

    @Query("SELECT * FROM favorites WHERE id = :movieId")
    MovieInfo retrieveSingleFavorite(int movieId);

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE id = :movieId)")
        //Returns a 1 for True
    int isAFavorite(int movieId);

    @Query("SELECT * FROM youtubetrailers WHERE movie_id = :movieId")
    List<YouTubeTrailer> retrieveYouTubeTrailers(int movieId);

    @Query("SELECT EXISTS(SELECT * FROM youtubetrailers WHERE movie_id = :movieId)")
        //Returns a 1 for True
    int hasYouTubeTrailers(int movieId);

    @Query("SELECT * FROM reviews WHERE movie_id = :movieId")
    List<ReviewInfo> retrieveReviews(int movieId);

    @Query("SELECT EXISTS(SELECT * from reviews WHERE movie_id = :movieId)")
        //Returns a 1 for True
    int hasReviews(int movieId);

    @Insert
    void addFavorite(MovieInfo movieInfo);

    @Insert
    void addYouTubeTrailer(YouTubeTrailer youTubeTrailer);

    @Insert
    void addReview(ReviewInfo reviewInfo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(MovieInfo movieInfo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateYouTubeTrailer(YouTubeTrailer youTubeTrailer);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateReview(ReviewInfo reviewInfo);

    @Delete
    void removeFavorite(MovieInfo movieInfo);

    @Delete
    void removeYouTubeTrailer(YouTubeTrailer youTubeTrailer);

    @Delete
    void removeReview(ReviewInfo reviewInfo);
}
