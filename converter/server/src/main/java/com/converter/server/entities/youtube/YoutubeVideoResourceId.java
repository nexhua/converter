package com.converter.server.entities.youtube;

public class YoutubeVideoResourceId {

    private String kind;

    private String videoId;

    public YoutubeVideoResourceId() {
    }

    //region Getters and Setters

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }


    //endregion
}
