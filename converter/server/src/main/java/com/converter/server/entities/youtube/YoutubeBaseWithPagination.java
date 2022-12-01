package com.converter.server.entities.youtube;

public class YoutubeBaseWithPagination {

    private String kind;

    private String etag;

    private String nextPageToken;

    private YoutubePagination pageInfo;

    public YoutubeBaseWithPagination() {
    }

    //region Getters and Setters

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public YoutubePagination getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(YoutubePagination pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    //endregion
}
