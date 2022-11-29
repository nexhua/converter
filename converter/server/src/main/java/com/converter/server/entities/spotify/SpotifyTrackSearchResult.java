package com.converter.server.entities.spotify;

import java.util.ArrayList;

public class SpotifyTrackSearchResult extends SpotifyPagination {

    private String href;

    private ArrayList<SpotifyTrack> items;

    public SpotifyTrackSearchResult() {
    }

    //region Getter and Setters

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public ArrayList<SpotifyTrack> getItems() {
        return items;
    }

    public void setItems(ArrayList<SpotifyTrack> items) {
        this.items = items;
    }

    //endregion
}
