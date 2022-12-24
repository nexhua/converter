package com.converter.server.converters;

import com.converter.server.entities.common.CommonTrack;
import com.converter.server.entities.youtube.YoutubePlaylistItemSnippet;
import com.converter.server.entities.youtube.YoutubeVideoResultBase;

public class YoutubeConverter extends BaseTrackConverter<YoutubeVideoResultBase<YoutubePlaylistItemSnippet, String>, CommonTrack> {

    public YoutubeConverter() {
        super(YoutubeConverter::youtubeTrackToCommon, YoutubeConverter::commonToYoutubeTrack);
    }

    private static CommonTrack youtubeTrackToCommon(YoutubeVideoResultBase<YoutubePlaylistItemSnippet, String> track) {
        return track.convertTrack();
    }

    private static YoutubeVideoResultBase<YoutubePlaylistItemSnippet, String> commonToYoutubeTrack(CommonTrack track) {
        return new YoutubeVideoResultBase<YoutubePlaylistItemSnippet, String>();
    }
}
