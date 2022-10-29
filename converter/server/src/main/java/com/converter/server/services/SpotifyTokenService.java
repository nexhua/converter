package com.converter.server.services;

import com.converter.server.tokens.SpotifyTokens;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SpotifyTokenService {

    private Map<String, SpotifyTokens> spotifySessionTokens = new HashMap<>();


    public SpotifyTokenService() {
    }

    public SpotifyTokenService(Map<String, SpotifyTokens> spotifySessionTokens) {
        this.spotifySessionTokens = spotifySessionTokens;
    }

    //region

    public Map<String, SpotifyTokens> getSpotifySessionTokens() {
        return spotifySessionTokens;
    }

    public void setSpotifySessionTokens(Map<String, SpotifyTokens> spotifySessionTokens) {
        this.spotifySessionTokens = spotifySessionTokens;
    }


    //endregion

    public void add(String sessionID, SpotifyTokens token) {
        this.spotifySessionTokens.put(sessionID, token);
    }

    public SpotifyTokens find(String sessionID) {
        return this.spotifySessionTokens.get(sessionID);
    }

    public Optional<SpotifyTokens> findOptional(String sessionID) {
        SpotifyTokens tokens = this.spotifySessionTokens.get(sessionID);

        if(tokens != null) {
            return Optional.of(tokens);
        }
        else {
            return Optional.empty();
        }
    }
}
