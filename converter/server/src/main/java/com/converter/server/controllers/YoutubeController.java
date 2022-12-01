package com.converter.server.controllers;

import com.converter.server.client.BaseWebClient;
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


    @GetMapping("/playlists/{playlistID}/tracks")
    public ResponseEntity<?> getYoutubePlaylistTracks(HttpServletRequest request, @PathVariable String playlistID) {

        HttpSession session = request.getSession();

        return ResponseEntity.ok(BaseWebClient.getYoutubePlaylistItems(playlistID));
    }

}
