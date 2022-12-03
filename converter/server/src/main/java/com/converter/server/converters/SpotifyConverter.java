package com.converter.server.converters;

import com.converter.server.entities.common.CommonTrack;
import com.converter.server.entities.spotify.SpotifyTrack;

public class SpotifyConverter extends BaseTrackConverter<SpotifyTrack, CommonTrack> {

    public SpotifyConverter() {
        super(SpotifyConverter::spotifyTrackToCommon, SpotifyConverter::commonTrackToSpotify);
    }

    private static CommonTrack spotifyTrackToCommon(SpotifyTrack track) {
        return track.convertTrack();
    }

    private static SpotifyTrack commonTrackToSpotify(CommonTrack track) {
        return new SpotifyTrack();
    }
}
