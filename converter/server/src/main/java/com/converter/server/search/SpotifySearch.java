package com.converter.server.search;

import com.converter.server.constants.PlatformTypes;
import com.converter.server.constants.SpotifyAPIConstants;
import com.converter.server.entities.common.CommonTrack;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class SpotifySearch extends AbstractPlatformSearch {

    @Override
    public PlatformTypes getTargetPlatform() {
        return PlatformTypes.SPOTIFY;
    }

    @Override
    String build() {
        String uri = UriComponentsBuilder
                .fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path("/search")
                .queryParam("type", "track")
                .queryParam("q", this.getParams())
                .build()
                .toUriString();

        return uri;
    }

    String getParams() {
        StringBuilder builder = new StringBuilder();

        builder.append("track").append(":").append(this.getTrack().getName()).append(" ");
        builder.append("album").append(":").append(this.getTrack().getAlbum().getName()).append(" ");
        builder.append("year").append(":").append(this.getTrack().getAlbum().getRelease_year()).append(" ");
        builder.append("artist").append(":").append(this.getTrack().getArtist().get(0).getName()).append(" ");

        return builder.toString();
    }

    public SpotifySearch(CommonTrack track) {
        super(track);
    }
}
