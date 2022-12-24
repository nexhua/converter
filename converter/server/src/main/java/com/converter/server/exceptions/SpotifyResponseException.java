package com.converter.server.exceptions;

import com.converter.server.errors.ApplicationError;

public class SpotifyResponseException extends RuntimeException {

    private ApplicationError error;

    public SpotifyResponseException(String msg) {
        super(msg);
    }

    public SpotifyResponseException(String msg, ApplicationError error) {
        super(msg);
        this.error = error;
    }

    public ApplicationError getError() {
        return error;
    }

    public void setError(ApplicationError error) {
        this.error = error;
    }
}
