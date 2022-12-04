package com.converter.server.controllers;

import com.converter.server.clients.YoutubeWebClient;
import com.converter.server.converters.YoutubeConverter;
import com.converter.server.entities.common.CommonTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/youtube")
public class YoutubeController {

    @Autowired
    private YoutubeWebClient youtubeWebClient;

    @GetMapping("/playlists/{playlistID}/tracks")
    public ResponseEntity<?> getYoutubePlaylistTracks(HttpServletRequest request, @PathVariable String playlistID) {

        HttpSession session = request.getSession();

        var optionalSongs = youtubeWebClient.getYoutubePlaylistItems(playlistID);

        if (optionalSongs.isPresent()) {
            YoutubeConverter converter = new YoutubeConverter();
            return ResponseEntity.ok().body(converter.toCommonTracks(optionalSongs.get().getItems()));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<List<String>>> getYoutubeSearchResults(HttpServletRequest request, @RequestBody ArrayList<CommonTrack> tracks, @RequestParam int limit) {

        Flux<String> response = youtubeWebClient.getSearchResults(tracks, limit);

        Mono<List<String>> result = response.collectList();

        return result.map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.badRequest().build());
    }

}
