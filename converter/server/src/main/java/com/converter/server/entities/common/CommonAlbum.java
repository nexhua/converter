package com.converter.server.entities.common;

import java.util.ArrayList;

public class CommonAlbum {

    private String name;

    private String release_string;

    private ArrayList<CommonArtist> artists;

    private int release_year;

    public CommonAlbum() {
    }

    //region Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelease_string() {
        return release_string;
    }

    public void setRelease_string(String release_string) {
        this.release_string = release_string;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public ArrayList<CommonArtist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<CommonArtist> artists) {
        this.artists = artists;
    }

    //endregion
}
