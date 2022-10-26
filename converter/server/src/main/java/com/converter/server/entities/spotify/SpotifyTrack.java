package com.converter.server.entities.spotify;

public class SpotifyTrack {

    private String href;

    private String id;

    private String name;

    public SpotifyTrack() {
    }

    //region

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //endregion
}
