package com.converter.server.errors;

import java.util.List;

public class YoutubeResponseError {

    private int code;

    private String message;

    private List<YoutubeError> errors;

    public YoutubeResponseError() {
    }

    //region Getters and Setters

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<YoutubeError> getErrors() {
        return errors;
    }

    public void setErrors(List<YoutubeError> errors) {
        this.errors = errors;
    }


    //endregion
}