package com.converter.server.routers;

import com.converter.server.handlers.YoutubeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class YoutubeRouter {

    @Autowired
    private YoutubeHandler youtubeHandler;

    @Bean
    public RouterFunction<ServerResponse> youtubeRoutes() {
        return RouterFunctions.route()
                .GET("/youtube/playlists/{playlistID}/tracks", youtubeHandler::getPlaylistTracks)
                .GET("/youtube/search", youtubeHandler::getSearchResults)
                .build();
    }
}
