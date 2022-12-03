package com.converter.server.converters;

import com.converter.server.entities.common.CommonTrack;
import com.converter.server.entities.youtube.YoutubePlaylistItemSnippet;
import com.converter.server.entities.youtube.YoutubeVideoResultBase;

public class YoutubeConverter extends BaseTrackConverter<YoutubeVideoResultBase<YoutubePlaylistItemSnippet>, CommonTrack> {

    public YoutubeConverter() {
        super(YoutubeConverter::youtubeTrackToCommon, YoutubeConverter::commonToYoutubeTrack);
    }

    private static CommonTrack youtubeTrackToCommon(YoutubeVideoResultBase<YoutubePlaylistItemSnippet> track) {
        return track.convertTrack();
    }

    private static YoutubeVideoResultBase<YoutubePlaylistItemSnippet> commonToYoutubeTrack(CommonTrack track) {
        return new YoutubeVideoResultBase<YoutubePlaylistItemSnippet>();
    }
}
