package com.converter.server.search;

import com.converter.server.constants.PlatformTypes;
import com.converter.server.constants.YoutubeAPIConstants;
import com.converter.server.constants.YoutubeApplicationConstants;
import com.converter.server.entities.common.CommonTrack;
import org.springframework.web.util.UriComponentsBuilder;

public class YoutubeSearch extends AbstractPlatformSearch {

    private int limit = 5;

    @Override
    public PlatformTypes getTargetPlatform() {
        return PlatformTypes.YOUTUBE;
    }

    @Override
    String build() {
        return UriComponentsBuilder
                .fromHttpUrl(YoutubeAPIConstants.youtube_api_base)
                .path(YoutubeAPIConstants.youtube_api_version_path)
                .path(YoutubeAPIConstants.search_path)
                .queryParam(YoutubeAPIConstants.part, YoutubeAPIConstants.snippet)
                .queryParam(YoutubeAPIConstants.maxResults, this.getLimit())
                .queryParam("q", getParams())
                .queryParam(YoutubeAPIConstants.type, "video")
                .queryParam(YoutubeAPIConstants.key, YoutubeApplicationConstants.getApplicationApiKey())
                .build()
                .toUriString();
    }

    String getParams() {
        if (this.track.IsStructured()) {
            return this.track.getArtist().get(0).getName() + " " + this.track.getName();
        } else {
            return this.track.getUnstructuredFullName();
        }
    }

    public YoutubeSearch(CommonTrack track) {
        super(track);
    }

    //region Getters and Setters

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


    //endregion
}
