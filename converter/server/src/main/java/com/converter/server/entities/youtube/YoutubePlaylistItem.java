package com.converter.server.entities.youtube;

public class YoutubePlaylistItem extends YoutubeBase {

    private String id;

    private YoutubePlaylistItemSnippet snippet;

    public YoutubePlaylistItem() {
    }

    //region Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public YoutubePlaylistItemSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(YoutubePlaylistItemSnippet snippet) {
        this.snippet = snippet;
    }

    //endregion
}
