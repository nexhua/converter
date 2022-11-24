package com.converter.server.entities.spotify;

import java.util.ArrayList;

public class SpotifyAlbum {

    private String href;

    private String id;

    private String name;

    private String release_date;

    private String release_date_precision;

    private ArrayList<SpotifyArtist> artists;

    public SpotifyAlbum() {
    }

    //region Getters and Setters

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

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRelease_date_precision() {
        return release_date_precision;
    }

    public void setRelease_date_precision(String release_date_precision) {
        this.release_date_precision = release_date_precision;
    }

    public ArrayList<SpotifyArtist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<SpotifyArtist> artists) {
        this.artists = artists;
    }

    //endregion
}
