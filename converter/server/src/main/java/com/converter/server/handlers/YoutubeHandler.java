package com.converter.server.handlers;

import com.converter.server.clients.YoutubeWebClient;
import com.converter.server.entities.common.CommonTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class YoutubeHandler {

    @Autowired
    private YoutubeWebClient client;

    Logger logger = LoggerFactory.getLogger(YoutubeHandler.class);


    public ServerResponse getPlaylistTracks(ServerRequest request) {
        String playlistID = request.pathVariable("playlistID");

        String limit = request.param("limit").orElse("3");

        if (!playlistID.isBlank()) {
            return client.getYoutubePlaylistItems(playlistID, Integer.parseInt(limit)).block();
        } else {
            return ServerResponse.badRequest().build();
        }
    }

    public ServerResponse getSearchResults(ServerRequest request) {
        Optional<String> limit = request.param("limit");


        ArrayList<CommonTrack> tracks = new ArrayList<>();
        int parsedLimit = 1;
        if (limit.isPresent()) {
            try {
                tracks = request.body(new ParameterizedTypeReference<ArrayList<CommonTrack>>() {
                });
                parsedLimit = Integer.parseInt(limit.get(), 10);
            } catch (ServletException | IOException | NumberFormatException e) {
                return ServerResponse.badRequest().build();
            }
        }

        return client.getSearchResults(tracks, parsedLimit)
                .collect(Collectors.toList())
                .map(result -> ServerResponse.ok().body(result))
                .defaultIfEmpty(ServerResponse.notFound().build()).block();
    }
}
