package com.converter.server.entities.spotify;

import com.converter.server.constants.PlatformTypes;
import com.converter.server.constants.TrackStructure;
import com.converter.server.entities.Identifiers;
import com.converter.server.entities.common.CommonAlbum;
import com.converter.server.entities.common.CommonArtist;
import com.converter.server.entities.common.CommonTrack;
import com.converter.server.interfaces.IConvertible;

import java.util.ArrayList;

public class SpotifyTrack implements IConvertible {

    private String href;

    private String id;

    private String name;

    private SpotifyAlbum album;

    private ArrayList<SpotifyArtist> artists;

    private Identifiers external_ids;

    public SpotifyTrack() {
    }

    //region

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Identifiers getExternal_ids() {
        return external_ids;
    }

    public void setExternal_ids(Identifiers external_ids) {
        this.external_ids = external_ids;
    }

    public ArrayList<SpotifyArtist> getArtists() {
        return artists;
    }

    public void setArtists(ArrayList<SpotifyArtist> artists) {
        this.artists = artists;
    }

    public SpotifyAlbum getAlbum() {
        return album;
    }

    public void setAlbum(SpotifyAlbum album) {
        this.album = album;
    }

    //endregion

    @Override
    public PlatformTypes getPlatformType() {
        return PlatformTypes.SPOTIFY;
    }

    @Override
    public CommonAlbum convertAlbum() {
        CommonAlbum album = new CommonAlbum();
        album.setName(this.getAlbum().getName());
        album.setRelease_string(this.getAlbum().getRelease_date());

        int parsedYear;
        try {
            parsedYear = Integer.parseInt(this.getAlbum().getRelease_date().substring(0, 4), 10);
        } catch (NumberFormatException e) {
            parsedYear = -1;
        }
        album.setRelease_year(parsedYear);

        ArrayList<CommonArtist> albumArtists = new ArrayList<>();
        for (int i = 0; i < this.getAlbum().getArtists().size(); i++) {
            CommonArtist artist = new CommonArtist();
            artist.setName(this.getAlbum().getArtists().get(i).getName());
            albumArtists.add(artist);
        }
        album.setArtists(albumArtists);
        return album;
    }

    @Override
    public ArrayList<CommonArtist> convertArtists() {
        ArrayList<CommonArtist> artists = new ArrayList<>();

        for (int i = 0; i < this.getArtists().size(); i++) {
            CommonArtist artist = new CommonArtist();
            artist.setName(this.getArtists().get(i).getName());
            artists.add(artist);
        }
        return artists;
    }

    @Override
    public CommonTrack convertTrack(PlatformTypes platformType) {
        CommonTrack track = new CommonTrack(platformType);
        track.setName(this.getName());
        track.setUnstructuredFullName(String.format("%s %s", this.getArtists().get(0).getName(), this.getName()));
        track.setUniversal_identifiers(this.getExternal_ids());
        track.setTrackStructure(TrackStructure.STRUCTURED);
        return track;
    }
}
