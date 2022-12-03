package com.converter.server.interfaces;

import com.converter.server.constants.PlatformTypes;
import com.converter.server.entities.common.CommonAlbum;
import com.converter.server.entities.common.CommonArtist;
import com.converter.server.entities.common.CommonTrack;

import java.util.ArrayList;

public interface IConvertible {

    PlatformTypes getPlatformType();

    CommonAlbum convertAlbum();

    ArrayList<CommonArtist> convertArtists();

    CommonTrack convertTrack();
}
