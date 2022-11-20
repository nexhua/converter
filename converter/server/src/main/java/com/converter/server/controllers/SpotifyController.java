package com.converter.server.controllers;


import com.converter.server.client.BaseWebClient;
import com.converter.server.entities.spotify.SpotifyPlaylist;
import com.converter.server.entities.spotify.SpotifyPlaylists;
import com.converter.server.entities.spotify.SpotifyTracks;
import com.converter.server.services.SpotifyTokenService;
import com.converter.server.tokens.SpotifyTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    @Autowired
    private SpotifyTokenService spotifyTokenService;

    @GetMapping("/playlists")
    public ResponseEntity<?> getCurrentUserPlaylists(HttpServletRequest request, @RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int offset) {
        Optional<Cookie> sessionCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("SESSIONID")).findFirst();

        if (sessionCookie.isPresent()) {
            Optional<SpotifyTokens> tokensOptional = this.spotifyTokenService.findOptional(sessionCookie.get().getValue());

            if (tokensOptional.isPresent()) {
                SpotifyTokens tokens = tokensOptional.get();

                Optional<SpotifyPlaylists> playlists = BaseWebClient.getUserPlaylists(tokens, limit, offset);

                if(playlists.isPresent()) {
                    return ResponseEntity.ok(playlists.get().getItems());
                }
                else{
                    return ResponseEntity.badRequest().build();
                }
            }
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/playlists/{playlistID}")
    public ResponseEntity<?> getPlaylistTracks(HttpServletRequest request, @PathVariable String playlistID) {
        Optional<Cookie> sessionCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("SESSIONID")).findFirst();

        if (sessionCookie.isPresent()) {
            Optional<SpotifyTokens> tokensOptional = this.spotifyTokenService.findOptional(sessionCookie.get().getValue());

            if (tokensOptional.isPresent()) {
                SpotifyTokens tokens = tokensOptional.get();

                Optional<SpotifyPlaylist> playlist = BaseWebClient.getUserPlaylist(tokens, playlistID);

                if(playlist.isPresent()){
                    return ResponseEntity.ok(playlist.get());
                }
                else {
                    return ResponseEntity.badRequest().build();
                }
            }
        }
        return ResponseEntity.internalServerError().build();
    }


    @GetMapping("/playlists/{playlistID}/tracks")
    public ResponseEntity<?> getPlaylistTracksWithPagination(HttpServletRequest request, @PathVariable String playlistID, @RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "10") int offset) {
        Optional<Cookie> sessionCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("SESSIONID")).findFirst();

        if (sessionCookie.isPresent()) {
            Optional<SpotifyTokens> tokensOptional = this.spotifyTokenService.findOptional(sessionCookie.get().getValue());

            if (tokensOptional.isPresent()) {
                SpotifyTokens tokens = tokensOptional.get();

                Optional<SpotifyTracks> playlist = BaseWebClient.getPlaylistTracks(tokens, playlistID, limit, offset);

                if(playlist.isPresent()){
                    return ResponseEntity.ok(playlist.get());
                }
                else {
                    return ResponseEntity.badRequest().build();
                }
            }
        }
        return ResponseEntity.internalServerError().build();
    }
}
