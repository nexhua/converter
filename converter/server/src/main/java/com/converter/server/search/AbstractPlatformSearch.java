package com.converter.server.search;

import com.converter.server.constants.PlatformTypes;
import com.converter.server.entities.common.CommonTrack;
import com.converter.server.interfaces.ISearchable;

public abstract class AbstractPlatformSearch implements ISearchable {

    protected CommonTrack track;

    @Override
    public abstract PlatformTypes getTargetPlatform();

    @Override
    public String getSearchString() {
        return this.build();
    }

    abstract String build();

    public AbstractPlatformSearch(CommonTrack track) {
        this.setTrack(track);
    }


    //region Getters and Setters

    public CommonTrack getTrack() {
        return track;
    }

    public void setTrack(CommonTrack track) {
        this.track = track;
    }


    //endregion
}
