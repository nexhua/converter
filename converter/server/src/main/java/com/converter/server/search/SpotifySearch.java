package com.converter.server.search;

import com.converter.server.constants.PlatformTypes;
import com.converter.server.constants.SpotifyAPIConstants;
import com.converter.server.entities.common.CommonTrack;
import org.springframework.web.util.UriComponentsBuilder;

public class SpotifySearch extends AbstractPlatformSearch {

    @Override
    public PlatformTypes getTargetPlatform() {
        return PlatformTypes.SPOTIFY;
    }

    @Override
    String build() {
        return UriComponentsBuilder
                .fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path("/search")
                .queryParam("type", "track")
                .queryParam("q", this.getParams())
                .build()
                .toUriString();
    }

    String getParams() {

        if (this.getTrack().IsStructured()) {

            String builder = "track" + ":" + this.getTrack().getName() + " " +
                    "album" + ":" + this.getTrack().getAlbum().getName() + " " +
                    "year" + ":" + this.getTrack().getAlbum().getRelease_year() + " " +
                    "artist" + ":" + this.getTrack().getArtist().get(0).getName() + " ";

            return builder;
        } else {
            return this.track.getUnstructuredFullName();
        }
    }

    public SpotifySearch(CommonTrack track) {
        super(track);
    }
}
