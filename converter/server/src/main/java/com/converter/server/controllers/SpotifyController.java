package com.converter.server.controllers;


import com.converter.server.clients.SpotifyWebClient;
import com.converter.server.converters.SpotifyConverter;
import com.converter.server.entities.common.CommonTrack;
import com.converter.server.entities.spotify.*;
import com.converter.server.services.SpotifyTokenService;
import com.converter.server.tokens.SpotifyTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    @Autowired
    private SpotifyTokenService spotifyTokenService;

    @Autowired
    private SpotifyWebClient spotifyWebClient;

    @GetMapping("/playlists")
    public ResponseEntity<?> getCurrentUserPlaylists(HttpServletRequest request, @RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int offset) {
        HttpSession session = request.getSession();

        Optional<SpotifyTokens> tokensOptional = this.spotifyTokenService.findOptional(session.getId());

        if (tokensOptional.isPresent()) {
            SpotifyTokens tokens = tokensOptional.get();

            Optional<SpotifyPlaylists> playlists = spotifyWebClient.getUserPlaylists(tokens, limit, offset);

            if (playlists.isPresent()) {
                return ResponseEntity.ok(playlists.get().getItems());
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/playlists/{playlistID}")
    public ResponseEntity<?> getPlaylistTracks(HttpServletRequest request, @PathVariable String playlistID) {
        HttpSession session = request.getSession();

        Optional<SpotifyTokens> tokensOptional = this.spotifyTokenService.findOptional(session.getId());

        if (tokensOptional.isPresent()) {
            SpotifyTokens tokens = tokensOptional.get();

            Optional<SpotifyPlaylist> playlist = spotifyWebClient.getUserPlaylist(tokens, playlistID);

            if (playlist.isPresent()) {
                return ResponseEntity.ok(playlist.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.internalServerError().build();
    }


    @GetMapping("/playlists/{playlistID}/tracks")
    public ResponseEntity<?> getPlaylistTracksWithPagination(HttpServletRequest request, @PathVariable String playlistID, @RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "10") int offset) {
        HttpSession session = request.getSession();


        Optional<SpotifyTokens> tokensOptional = this.spotifyTokenService.findOptional(session.getId());

        if (tokensOptional.isPresent()) {
            SpotifyTokens tokens = tokensOptional.get();

            Optional<SpotifyTracks> playlist = spotifyWebClient.getPlaylistTracks(tokens, playlistID, limit, offset);

            if (playlist.isPresent()) {
                SpotifyConverter converter = new SpotifyConverter();
                return ResponseEntity.ok(converter.toCommonTracks(playlist.get().getItems().stream().map(SpotifyTrackWrapper::getTrack).collect(Collectors.toList())));
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/search")
    public Flux<ResponseEntity<SpotifyTrackSearchResultWrapper>> getSpotifySearchResults(HttpServletRequest request, @RequestBody ArrayList<CommonTrack> tracks) {
        HttpSession session = request.getSession();

        Optional<SpotifyTokens> tokensOptional = this.spotifyTokenService.findOptional(session.getId());

        if (tokensOptional.isPresent()) {
            SpotifyTokens tokens = tokensOptional.get();

            Flux<SpotifyTrackSearchResultWrapper> response = spotifyWebClient.getSpotifySearch(tokens, tracks);

            return Flux.from(response).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.badRequest().build()).log();
        }

        return Flux.just(ResponseEntity.internalServerError().build());
    }
}
