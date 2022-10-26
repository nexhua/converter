package com.converter.server.entities.spotify;

import com.converter.server.entities.Image;

import java.util.ArrayList;

public class SpotifyPlaylist {

    private String href;

    private String id;

    ArrayList<Image> images;

    private String name;

    private SpotifyTracks tracks;

    public SpotifyPlaylist() {
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

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpotifyTracks getTracks() {
        return tracks;
    }

    public void setTracks(SpotifyTracks tracks) {
        this.tracks = tracks;
    }

    //endregion
}
