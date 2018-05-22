package net.hobbitsoft.popularmovies.movies;

import java.util.List;

/*
{
    "id": 284053,
    "quicktime": [],
    "youtube": []
}
 */
public class TrailerResults {
    private static String id;
    private static List<QuicktimeTrailers> quicktime;
    private static List<YouTubeTrailers> youtube;

    public TrailerResults() {
    }

    public TrailerResults(String id, List<QuicktimeTrailers> quicktime, List<YouTubeTrailers> youtube) {
        this.id = id;
        this.quicktime = quicktime;
        this.youtube = youtube;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        TrailerResults.id = id;
    }

    public static List<QuicktimeTrailers> getQuicktime() {
        return quicktime;
    }

    public static void setQuicktime(List<QuicktimeTrailers> quicktime) {
        TrailerResults.quicktime = quicktime;
    }

    public static List<YouTubeTrailers> getYoutube() {
        return youtube;
    }

    public static void setYoutube(List<YouTubeTrailers> youtube) {
        TrailerResults.youtube = youtube;
    }
}
