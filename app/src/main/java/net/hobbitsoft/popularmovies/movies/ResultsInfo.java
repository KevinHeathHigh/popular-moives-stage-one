/*
 * Copyright (c) 2018.  HobbitSoft - Kevin Heath High
 */

package net.hobbitsoft.popularmovies.movies;

import java.util.ArrayList;

/*
Sample of Page level results
{
    "page": 2,
        "total_results": 359398,
        "total_pages": 17970,
        "results": [
    {
       ...
    }
 */
public class ResultsInfo {

    private int page;
    private int total_results;
    private int total_pages;
    private ArrayList<MovieInfo> results;

    public ResultsInfo() {
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public void setResults(ArrayList<MovieInfo> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public ArrayList<MovieInfo> getResults() {
        return results;
    }

}

