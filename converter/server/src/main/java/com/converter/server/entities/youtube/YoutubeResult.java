package com.converter.server.entities.youtube;

public class YoutubeResult<T> extends YoutubeBaseWithPagination {

    private T items;

    public YoutubeResult() {
    }

    //region Getters and Setters

    public T getItems() {
        return items;
    }

    public void setItems(T items) {
        this.items = items;
    }

    //endregion
}
