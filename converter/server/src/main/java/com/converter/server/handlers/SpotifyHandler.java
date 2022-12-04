package com.converter.server.handlers;

import com.converter.server.clients.SpotifyWebClient;
import com.converter.server.services.SpotifyTokenService;
import com.converter.server.tokens.SpotifyTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class SpotifyHandler {

    @Autowired
    private SpotifyWebClient spotifyWebClient;

    @Autowired
    private SpotifyTokenService spotifyTokenService;

    public ServerResponse getSpotifyPlaylistTracks(ServerRequest request) {
        HttpSession session = request.session();
        Optional<SpotifyTokens> optionalSpotifyTokens = this.spotifyTokenService.findOptional(session.getId());


        if (optionalSpotifyTokens.isPresent()) {
            SpotifyTokens tokens = optionalSpotifyTokens.get();

            String limit = request.param("limit").orElse("5");
            String offset = request.param("offset").orElse("0");
            String playlistID = request.pathVariable("playlistID");

            return spotifyWebClient.getPlaylistTracks(tokens, playlistID, Integer.parseInt(limit, 10), Integer.parseInt(offset, 10)).block();
        }

        return ServerResponse.badRequest().build();
    }
}
