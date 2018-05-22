package net.hobbitsoft.popularmovies.movies;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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
public class YouTubeTrailers {

    private static final String TAG = YouTubeTrailers.class.getSimpleName();

    private static String name;
    private static String size;
    private static String source;
    private static String type;

    private static final String YOUTUBE_BASEURL = "https://www.youtube.com/";
    private static final String WATCH_PATH = "watch";
    private static final String VIDEO_PARAM = "v";

    public YouTubeTrailers() {
    }

    public YouTubeTrailers(String name, String size, String source, String type) {
        this.name = name;
        this.size = size;
        this.source = source;
        this.type = type;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        YouTubeTrailers.name = name;
    }

    public static String getSize() {
        return size;
    }

    public static void setSize(String size) {
        YouTubeTrailers.size = size;
    }

    public static String getSource() {
        return source;
    }

    public static void setSource(String source) {
        YouTubeTrailers.source = source;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        YouTubeTrailers.type = type;
    }

    public static URL getYouTubeURL() {
        Uri uri = Uri.parse(YOUTUBE_BASEURL).buildUpon()
                .appendPath(WATCH_PATH)
                .appendQueryParameter(VIDEO_PARAM, source)
                .build();

        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        }
    }
}
