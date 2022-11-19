package com.converter.server.exceptions;

import org.springframework.core.NestedRuntimeException;

public class SpotifyResponseException extends NestedRuntimeException {

    public SpotifyResponseException(String msg) {
        super(msg);
    }
}
