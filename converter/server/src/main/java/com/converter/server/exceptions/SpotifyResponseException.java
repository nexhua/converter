package com.converter.server.exceptions;

import com.converter.server.errors.SpotifyError;

public class SpotifyResponseException extends RuntimeException {

    private SpotifyError error;

    public SpotifyResponseException(String msg) {
        super(msg);
    }

    public SpotifyResponseException(String msg, SpotifyError error) {
        super(msg);
        this.error = error;
    }

    public SpotifyError getError() {
        return error;
    }

    public void setError(SpotifyError error) {
        this.error = error;
    }
}
