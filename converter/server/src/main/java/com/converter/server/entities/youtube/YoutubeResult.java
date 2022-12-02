package com.converter.server.entities.youtube;

import java.util.ArrayList;

public class YoutubeResult<T> extends YoutubeBaseWithPagination {

    private ArrayList<T> items;

    public YoutubeResult() {
    }

    //region Getters and Setters

    public ArrayList<T> getItems() {
        return items;
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }


    //endregion
}
