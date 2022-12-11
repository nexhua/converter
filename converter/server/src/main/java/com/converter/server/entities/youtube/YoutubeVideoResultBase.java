package com.converter.server.entities.youtube;

import com.converter.server.constants.PlatformTypes;
import com.converter.server.constants.TrackStructure;
import com.converter.server.entities.common.CommonAlbum;
import com.converter.server.entities.common.CommonArtist;
import com.converter.server.entities.common.CommonTrack;
import com.converter.server.interfaces.IConvertible;

import java.util.ArrayList;

public class YoutubeVideoResultBase<T> extends YoutubeBase implements IConvertible {

    private YoutubeVideoResourceId id;

    private T snippet;

    public YoutubeVideoResultBase() {
    }

    //region Getters and Setters


    public YoutubeVideoResourceId getId() {
        return id;
    }

    public void setId(YoutubeVideoResourceId id) {
        this.id = id;
    }

    public T getSnippet() {
        return snippet;
    }

    public void setSnippet(T snippet) {
        this.snippet = snippet;
    }


    //endregion


    @Override
    public PlatformTypes getPlatformType() {
        return PlatformTypes.YOUTUBE;
    }

    @Override
    public CommonAlbum convertAlbum() {
        return null;
    }

    @Override
    public ArrayList<CommonArtist> convertArtists() {
        return null;
    }

    @Override
    public CommonTrack convertTrack() {
        CommonTrack track = new CommonTrack(this.getPlatformType());
        track.setTrackStructure(TrackStructure.UNSTRUCTURED);
        if (this.getSnippet() instanceof YoutubePlaylistItemSnippet) {
            track.setUnstructuredFullName(((YoutubePlaylistItemSnippet) this.getSnippet()).getTitle());
        }
        return track;
    }
}
