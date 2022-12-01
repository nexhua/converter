package com.converter.server.entities.youtube;

public class YoutubeBase {

    private String kind;

    private String etag;

    public YoutubeBase() {
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

    //endregion
}
