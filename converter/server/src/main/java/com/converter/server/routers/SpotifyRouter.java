package com.converter.server.routers;

import com.converter.server.handlers.SpotifyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class SpotifyRouter {

    @Autowired
    private SpotifyHandler spotifyHandler;

    @Bean
    public RouterFunction<ServerResponse> spotifyRoutes() {
        return RouterFunctions.route()
                .GET("/reactive/spotify/playlists/{playlistID}/tracks",
                        RequestPredicates.param("limit", l -> true).and(RequestPredicates.param("offset", o -> true)),
                        spotifyHandler::getSpotifyPlaylistTracks)
                .GET("/reactive/spotify/playlists",
                        RequestPredicates.param("limit", limit -> true).and(RequestPredicates.param("offset", offset -> true)),
                        spotifyHandler::getCurrentUserPlaylists)
                .GET("/reactive/spotify/search", spotifyHandler::getSpotifySearch)
                .build();
    }
}
