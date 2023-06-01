package com.converter.server.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpotifyPlaylistCreateOptions {

    private String name;

    private String description;

    @JsonProperty("public")
    private boolean isPublic;

    private boolean collaborative;

    public SpotifyPlaylistCreateOptions() {
    }

    //region Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(boolean collaborative) {
        this.collaborative = collaborative;
    }

    //endregion
}
