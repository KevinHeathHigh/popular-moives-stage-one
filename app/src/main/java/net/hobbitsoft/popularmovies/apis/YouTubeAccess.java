package net.hobbitsoft.popularmovies.apis;

import android.net.Uri;

public class YouTubeAccess {

    //private static final String TAG = YouTubeAccess.class.getSimpleName();

    private static final String YOUTUBE_BASEURL = "https://m.youtube.com/";
    private static final String WATCH_PATH = "watch";
    private static final String VIDEO_PARAM = "v";

    public static Uri getYouTubeUri(String source) {
        return Uri.parse(YOUTUBE_BASEURL).buildUpon()
                .appendPath(WATCH_PATH)
                .appendQueryParameter(VIDEO_PARAM, source)
                .build();
    }
}