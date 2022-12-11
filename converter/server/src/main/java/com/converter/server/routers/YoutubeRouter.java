package com.converter.server.routers;

import com.converter.server.handlers.YoutubeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
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
                .GET("/youtube/playlists/{playlistID}/tracks", youtubeHandler::)
                .GET("/youtube/search", youtubeHandler::)
                .build();
    }

    @Bean
    public org.springframework.web.reactive.function.server.RouterFunction<org.springframework.web.reactive.function.server.ServerResponse> reactiveYoutubeRoutes() {
        return org.springframework.web.reactive.function.server.RouterFunctions.route()
                .GET("/reactive/youtube/playlists/{playlistID}/tracks", RequestPredicates.queryParam("limit", l -> true), youtubeHandler::)
                .build();
    }
}
