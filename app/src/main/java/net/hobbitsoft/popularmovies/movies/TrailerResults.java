package net.hobbitsoft.popularmovies.movies;

import java.util.List;

/*
{
    "id": 284053,
    "quicktime": [],
    "youtube": []
}
 */
@SuppressWarnings("ALL")
public class TrailerResults {
    private int id;
    private List<QuicktimeTrailer> quicktime;
    private List<YouTubeTrailer> youtube;

    public TrailerResults() {
    }

    public TrailerResults(int id, List<QuicktimeTrailer> quicktime, List<YouTubeTrailer> youtube) {
        this.id = id;
        this.quicktime = quicktime;
        this.youtube = youtube;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<QuicktimeTrailer> getQuicktime() {
        return quicktime;
    }

    public void setQuicktime(List<QuicktimeTrailer> quicktime) {
        this.quicktime = quicktime;
    }

    public List<YouTubeTrailer> getYouTubeResults() {
        return youtube;
    }

    public void setYoutube(List<YouTubeTrailer> youtube) {
        this.youtube = youtube;
    }
}
