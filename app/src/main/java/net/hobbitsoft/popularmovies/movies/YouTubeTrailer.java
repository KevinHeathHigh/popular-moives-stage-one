package net.hobbitsoft.popularmovies.movies;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import net.hobbitsoft.popularmovies.apis.YouTubeAccess;


/*
    "youtube": [
        {
            "name": "\"Thor: Ragnarok\" Official Trailer",
            "size": "HD",
            "source": "ue80QwXMRHg",
            "type": "Trailer"
        }
     ]
}
 */
@SuppressWarnings("unused")
@Entity(tableName = "youtubetrailers",
        indices = {@Index("movie_id")})
public class YouTubeTrailer implements Parcelable {

    //private static final String TAG = YouTubeTrailer.class.getSimpleName();
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    private String name;
    private String size;
    private String source;
    private String type;

    @Ignore
    public YouTubeTrailer() {
    }

    public YouTubeTrailer(int rowId, int movieId, String name, String size, String source, String type) {
        this.rowId = rowId;
        this.movieId = movieId;
        this.name = name;
        this.size = size;
        this.source = source;
        this.type = type;
    }

    private YouTubeTrailer(Parcel parcel) {
        movieId = parcel.readInt();
        name = parcel.readString();
        size = parcel.readString();
        source = parcel.readString();
        type = parcel.readString();
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Uri getYouTubeUri() {
        return YouTubeAccess.getYouTubeUri(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(source);
        dest.writeString(type);
    }

    public static final Parcelable.Creator<YouTubeTrailer> CREATOR = new Parcelable.Creator<YouTubeTrailer>() {

        @Override
        public YouTubeTrailer createFromParcel(Parcel parcel) {
            return new YouTubeTrailer(parcel);
        }

        @Override
        public YouTubeTrailer[] newArray(int size) {
            return new YouTubeTrailer[size];
        }
    };
}
