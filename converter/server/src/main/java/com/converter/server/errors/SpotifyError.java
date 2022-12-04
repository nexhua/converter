package com.converter.server.errors;

public class SpotifyError {

    private SpotifyResponseError error;

    public SpotifyError() {
    }

    //region Getters and Setters

    public SpotifyResponseError getError() {
        return error;
    }

    public void setError(SpotifyResponseError error) {
        this.error = error;
    }


    //endregion
}
