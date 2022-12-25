package com.converter.server.handlers;

import com.converter.server.clients.SpotifyWebClient;
import com.converter.server.converters.SpotifyConverter;
import com.converter.server.entities.common.CommonTrack;
import com.converter.server.services.SpotifyTokenService;
import com.converter.server.tokens.SpotifyTokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpotifyHandler {

    @Autowired
    private SpotifyWebClient spotifyWebClient;

    @Autowired
    private SpotifyTokenService spotifyTokenService;

    Logger logger = LoggerFactory.getLogger(SpotifyHandler.class);

    public ServerResponse getCurrentUserPlaylists(ServerRequest request) {
        HttpSession session = request.session();
        Optional<SpotifyTokens> optionalSpotifyTokens = this.spotifyTokenService.findOptional(session.getId());

        if (optionalSpotifyTokens.isPresent()) {
            SpotifyTokens tokens = optionalSpotifyTokens.get();
            String limit = request.param("limit").orElse("5");
            String offset = request.param("offset").orElse("0");

            return spotifyWebClient.getUserPlaylists(tokens, Integer.parseInt(limit), Integer.parseInt(offset)).block();
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

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

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ServerResponse getSpotifySearch(ServerRequest request) throws ServletException, IOException {
        HttpSession session = request.session();
        Optional<SpotifyTokens> optionalSpotifyTokens = this.spotifyTokenService.findOptional(session.getId());

        if (optionalSpotifyTokens.isPresent()) {
            SpotifyTokens tokens = optionalSpotifyTokens.get();
            var typeRef = new ParameterizedTypeReference<ArrayList<CommonTrack>>() {
            };

            try {
                ArrayList<CommonTrack> tracks = request.body(typeRef);

                String limit = request.param("limit").orElse("3");

                SpotifyConverter converter = new SpotifyConverter();

                return spotifyWebClient.getSpotifySearch(tokens, tracks, Integer.parseInt(limit, 10))
                        .collect(Collectors.toList())
                        .map(result -> ServerResponse.ok().body(result))
                        .defaultIfEmpty(ServerResponse.notFound().build())
                        .block();
            } catch (ServletException | IOException e) {
                logger.warn("Failed - Spotify Search - Parse Error - " + e.getMessage());
            }

        }

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
