package com.converter.server.errors;

public class ApplicationError extends Throwable {

    String message;

    public ApplicationError(String message) {
        this.message = message;
    }

    //region Getters and Setters

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    //endregion
}
