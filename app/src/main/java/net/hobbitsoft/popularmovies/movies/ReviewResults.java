package net.hobbitsoft.popularmovies.movies;

import java.util.ArrayList;

/*
 "id": 284053,
    "page": 1,
    "results": [
...
    ],
    "total_pages": 1,
    "total_results": 2
 */
public class ReviewResults {

    private int id;
    private int page;
    private ArrayList<ReviewInfo> results;
    private int total_pages;
    private int total_results;

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(int id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<ReviewInfo> getResults() {
        return results;
    }

    public void setResults(ArrayList<ReviewInfo> results) {
        this.results = results;
    }

    @SuppressWarnings("unused")
    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }


    @SuppressWarnings("unused")
    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}