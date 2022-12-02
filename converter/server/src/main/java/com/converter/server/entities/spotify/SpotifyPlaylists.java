package com.converter.server.entities.spotify;

import java.util.ArrayList;

public class SpotifyPlaylists extends SpotifyPagination {

    private String href;

    private ArrayList<SpotifyPlaylist> items;

    public SpotifyPlaylists() {
    }

    //region

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public ArrayList<SpotifyPlaylist> getItems() {
        return items;
    }

    public void setItems(ArrayList<SpotifyPlaylist> items) {
        this.items = items;
    }

    //endregion
}
