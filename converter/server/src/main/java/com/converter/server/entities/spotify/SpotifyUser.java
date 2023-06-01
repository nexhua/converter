package com.converter.server.entities.spotify;

public class SpotifyUser {

    private String display_name;

    private String id;

    public SpotifyUser() {
    }

    //region Getters and Setters

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    //endregion
}
