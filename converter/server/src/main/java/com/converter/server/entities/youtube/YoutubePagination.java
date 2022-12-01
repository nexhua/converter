package com.converter.server.entities.youtube;

public class YoutubePagination {

    private int totalResults;

    private int resultsPerPage;

    public YoutubePagination() {
    }

    //region Getters and Setters

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }


    //endregion
}
