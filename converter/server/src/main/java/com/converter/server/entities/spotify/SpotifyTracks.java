package com.converter.server.entities.spotify;

import java.util.ArrayList;

public class SpotifyTracks extends SpotifyPagination  {

    private String href;

    private ArrayList<SpotifyTrackWrapper> items;

    public SpotifyTracks() {
    }

    //region

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public ArrayList<SpotifyTrackWrapper> getItems() {
        return items;
    }

    public void setItems(ArrayList<SpotifyTrackWrapper> items) {
        this.items = items;
    }


    //endregion
}
