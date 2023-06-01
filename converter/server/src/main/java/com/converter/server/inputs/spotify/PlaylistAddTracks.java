package com.converter.server.inputs.spotify;

import com.converter.server.inputs.CreatePlaylistInput;
import com.converter.server.inputs.SpotifyPlaylistCreateOptions;

import java.util.List;

public class PlaylistAddTracks {

    private List<String> uris;

    public PlaylistAddTracks(CreatePlaylistInput<SpotifyPlaylistCreateOptions> input) {
        this.setUris(
                input.getTracks().stream().map(
                        track -> String.format("spotify:track:%s", track.getAlternatives().get(track.getChosenTrack()).getPlatformIdentifier())
                ).toList());
    }

    //region Getters and Setters

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }


    //endregion
}
