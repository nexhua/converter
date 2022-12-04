package com.converter.server.controllers;

import com.converter.server.handlers.SpotifyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.*;

@Configuration
public class SpotifyRouter {

    @Autowired
    private SpotifyHandler spotifyHandler;

    @Bean
    public RouterFunction<ServerResponse> spotifyPlaylistGetTracksRoute() {
        return RouterFunctions.route()
                .GET("/reactive/spotify/playlists/{playlistID}/tracks", RequestPredicates
                        .param("limit", t -> true)
                        .and(RequestPredicates.param("offset", o -> true)),
                        spotifyHandler::getSpotifyPlaylistTracks)
                .build();
    }
}
