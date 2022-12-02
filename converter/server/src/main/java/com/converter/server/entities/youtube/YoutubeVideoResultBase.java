package com.converter.server.entities.youtube;

public class YoutubeVideoResultBase<T> extends YoutubeBase {

    private String id;

    private T snippet;

    //region Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getSnippet() {
        return snippet;
    }

    public void setSnippet(T snippet) {
        this.snippet = snippet;
    }


    //endregion
}
