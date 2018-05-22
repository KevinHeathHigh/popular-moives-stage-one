package net.hobbitsoft.popularmovies.movies;

/*
        {
            "author": "Gimly",
            "content": "_Ragnarok_ is a tough one to review completely without spoiler, so I'll keep this brief:\r\n\r\n* I was bothered by a small number of things that happened in this movie.\r\n* I was in love with a large number of things that happened in this movie.\r\n* _Thor: Ragnarok_ is not the MCU's best movie.\r\n* _Thor: Ragnarok_ is the MCU's most fun movie.\r\n* Can we **please** have a worldwide ban on putting anything that happens in the third act of your movie, in said movie's trailers?\r\nThank you for your time. 4 stars.\r\n\r\n\r\n_Final rating:★★★★ - Very strong appeal. A personal favourite._",
            "id": "59f09b979251416ac10169f7",
            "url": "https://www.themoviedb.org/review/59f09b979251416ac10169f7"
        },
 */
public class ReviewInfo {

    private static String author;
    private static String content;
    private static String id;
    private static String url;

    public ReviewInfo() {
    }

    public ReviewInfo(String author, String content, String id, String url) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }


    public static String getAuthor() {
        return author;
    }

    public static void setAuthor(String author) {
        ReviewInfo.author = author;
    }

    public static String getContent() {
        return content;
    }

    public static void setContent(String content) {
        ReviewInfo.content = content;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        ReviewInfo.id = id;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        ReviewInfo.url = url;
    }
}
