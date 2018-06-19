package net.hobbitsoft.popularmovies.movies;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/*
        {
            "author": "Gimly",
            "content": "_Ragnarok_ is a tough one to review completely without spoiler, so I'll keep this brief:\r\n\r\n* I was bothered by a small number of things that happened in this movie.\r\n* I was in love with a large number of things that happened in this movie.\r\n* _Thor: Ragnarok_ is not the MCU's best movie.\r\n* _Thor: Ragnarok_ is the MCU's most fun movie.\r\n* Can we **please** have a worldwide ban on putting anything that happens in the third act of your movie, in said movie's trailers?\r\nThank you for your time. 4 stars.\r\n\r\n\r\n_Final rating:★★★★ - Very strong appeal. A personal favourite._",
            "id": "59f09b979251416ac10169f7",
            "url": "https://www.themoviedb.org/review/59f09b979251416ac10169f7"
        },
 */
@Entity(tableName = "reviews",
        indices = {@Index("movie_id")})
public class ReviewInfo implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;
    @ColumnInfo(name = "movie_id")
    private int movieId;
    private String author;
    private String content;
    private String id;
    private String url;

    @Ignore
    public ReviewInfo() {
    }

    public ReviewInfo(int rowId, int movieId, String author, String content, String id, String url) {
        this.rowId = rowId;
        this.movieId = movieId;
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    private ReviewInfo(Parcel parcel) {
        movieId = parcel.readInt();
        author = parcel.readString();
        content = parcel.readString();
        id = parcel.readString();
        url = parcel.readString();
    }

    public int getRowId() {
        return rowId;
    }

    @SuppressWarnings("unused")
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(id);
        dest.writeString(url);
    }

    public static final Parcelable.Creator<ReviewInfo> CREATOR = new Parcelable.Creator<ReviewInfo>() {

        @Override
        public ReviewInfo createFromParcel(Parcel parcel) {
            return new ReviewInfo(parcel);
        }

        @Override
        public ReviewInfo[] newArray(int size) {
            return new ReviewInfo[size];
        }
    };
}
