package com.converter.server.errors;

import org.springframework.web.reactive.function.client.ClientResponse;

public class SpotifyError extends Throwable {

    private SpotifyResponseError error;

    public SpotifyError() {
    }


    public SpotifyError(ClientResponse response) {

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
