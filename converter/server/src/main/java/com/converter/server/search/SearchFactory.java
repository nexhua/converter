package com.converter.server.search;

import com.converter.server.constants.PlatformTypes;
import com.converter.server.entities.common.CommonTrack;
import com.converter.server.exceptions.SearchBuilderException;

public class SearchFactory {

    public static AbstractPlatformSearch getPlatformSearcher(PlatformTypes platformType, CommonTrack track) {
        switch (platformType) {
            case SPOTIFY -> {
                return new SpotifySearch(track);
            }
            case YOUTUBE -> {
                return new YoutubeSearch(track);
            }
            case UNSPECIFIED -> throw new SearchBuilderException("UNSPECIFIED TARGET PLATFORM");
            default -> throw new SearchBuilderException("UNSPECIFIED TARGET PLATFORM");
        }
    }
}
