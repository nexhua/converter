package com.converter.server.entities.common;

import com.converter.server.constants.PlatformTypes;
import com.converter.server.entities.Identifiers;

import java.util.ArrayList;

public class CommonTrack {

    private String name;

    private CommonAlbum album;

    private ArrayList<CommonArtist> artist;

    private Identifiers universal_identifiers;

    private PlatformTypes originPlatform;

    public CommonTrack() {

    }
    public CommonTrack(PlatformTypes originPlatform) {
        this.originPlatform = originPlatform;
    }

    //region Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommonAlbum getAlbum() {
        return album;
    }

    public void setAlbum(CommonAlbum album) {
        this.album = album;
    }

    public ArrayList<CommonArtist> getArtist() {
        return artist;
    }

    public void setArtist(ArrayList<CommonArtist> artist) {
        this.artist = artist;
    }

    public Identifiers getUniversal_identifiers() {
        return universal_identifiers;
    }

    public void setUniversal_identifiers(Identifiers universal_identifiers) {
        this.universal_identifiers = universal_identifiers;
    }

    public PlatformTypes getOriginPlatform() {
        return originPlatform;
    }

    public void setOriginPlatform(PlatformTypes originPlatform) {
        this.originPlatform = originPlatform;
    }

    //endregion
}
