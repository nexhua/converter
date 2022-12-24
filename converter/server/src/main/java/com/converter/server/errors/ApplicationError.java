package com.converter.server.errors;

public class ApplicationError {

    private SpotifyResponseError error;

    public ApplicationError() {
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
