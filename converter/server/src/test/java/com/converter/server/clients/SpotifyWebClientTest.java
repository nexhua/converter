package com.converter.server.clients;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class SpotifyWebClientTest {

    @Autowired
    private SpotifyWebClient client;

    @Test
    void getSpotifyTokens() {

        client.getSpotifyTokens("wee");

    }
}