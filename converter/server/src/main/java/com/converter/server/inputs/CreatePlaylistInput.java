package com.converter.server.inputs;

import com.converter.server.entities.search.PlatformSearchResultWrapper;

import java.util.List;

public class CreatePlaylistInput<T> {


    private List<PlatformSearchResultWrapper> tracks;

    private T options;

    public CreatePlaylistInput() {
    }

    //region Getters and Setters

    public List<PlatformSearchResultWrapper> getTracks() {
        return tracks;
    }

    public void setTracks(List<PlatformSearchResultWrapper> tracks) {
        this.tracks = tracks;
    }

    public T getOptions() {
        return options;
    }

    public void setOptions(T options) {
        this.options = options;
    }


    //endregion
}
