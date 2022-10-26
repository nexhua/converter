package com.converter.server.services;

import com.converter.server.tokens.SpotifyTokens;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyTokenService {

    Map<String, SpotifyTokens> spotifySessionTokens = new HashMap<>();

    public SpotifyTokenService(Map<String, SpotifyTokens> spotifySessionTokens) {
        this.spotifySessionTokens = spotifySessionTokens;
    }

    public Map<String, SpotifyTokens> getSpotifySessionTokens() {
        return spotifySessionTokens;
    }

    public void setSpotifySessionTokens(Map<String, SpotifyTokens> spotifySessionTokens) {
        this.spotifySessionTokens = spotifySessionTokens;
    }
}
