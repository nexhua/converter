package com.converter.server.entities.search;

import com.converter.server.entities.common.CommonTrack;

public class PlatformSearchResult {

    private CommonTrack track;

    private String platformIdentifier;

    public PlatformSearchResult() {
    }

    //region Getters and Setters

    public CommonTrack getTrack() {
        return track;
    }

    public void setTrack(CommonTrack track) {
        this.track = track;
    }

    public String getPlatformIdentifier() {
        return platformIdentifier;
    }

    public void setPlatformIdentifier(String platformIdentifier) {
        this.platformIdentifier = platformIdentifier;
    }


    //endregion
}
