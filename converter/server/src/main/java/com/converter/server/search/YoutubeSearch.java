package com.converter.server.search;

import com.converter.server.constants.PlatformTypes;
import com.converter.server.entities.common.CommonTrack;

public class YoutubeSearch extends AbstractPlatformSearch {

    @Override
    public PlatformTypes getTargetPlatform() {
        return PlatformTypes.YOUTUBE;
    }

    @Override
    String build() {
        return null;
    }

    public YoutubeSearch(CommonTrack track) {
        super(track);
    }
}
