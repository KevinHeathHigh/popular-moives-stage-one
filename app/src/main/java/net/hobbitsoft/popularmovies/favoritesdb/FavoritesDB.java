package net.hobbitsoft.popularmovies.favoritesdb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import net.hobbitsoft.popularmovies.movies.MovieInfo;
import net.hobbitsoft.popularmovies.movies.ReviewInfo;
import net.hobbitsoft.popularmovies.movies.YouTubeTrailer;

@Database(entities = {MovieInfo.class, YouTubeTrailer.class, ReviewInfo.class}, version = 1, exportSchema = false)
public abstract class FavoritesDB extends RoomDatabase {

    private static final String TAG = FavoritesDB.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popcornmovies";
    private static FavoritesDB sInstance;

    public static FavoritesDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database: " + DATABASE_NAME);
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoritesDB.class, FavoritesDB.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Retrieving database instance");
        return sInstance;
    }

    public abstract FavoritesDao favoritesDao();
}
