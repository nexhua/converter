package com.converter.server.entities.spotify;

public class SpotifyTrackSearchResultWrapper {

    private SpotifyTrackSearchResult tracks;

    public SpotifyTrackSearchResultWrapper() {
    }

    //region Getters and Setters

    public SpotifyTrackSearchResult getTracks() {
        return tracks;
    }

    public void setTracks(SpotifyTrackSearchResult tracks) {
        this.tracks = tracks;
    }


    //endregion
}
