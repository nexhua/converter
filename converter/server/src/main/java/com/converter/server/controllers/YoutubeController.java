package com.converter.server.controllers;

import com.converter.server.clients.YoutubeWebClient;
import com.converter.server.converters.YoutubeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

}
