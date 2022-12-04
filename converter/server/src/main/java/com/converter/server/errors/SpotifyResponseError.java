package com.converter.server.errors;

public class SpotifyResponseError {

    private int status;

    private String message;

    public SpotifyResponseError() {
    }

    //region Getters and Setters

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    //endregion
}
