package com.converter.server.routers;

import com.converter.server.clients.YoutubeWebClient;
import com.converter.server.converters.YoutubeConverter;
import com.converter.server.entities.common.CommonTrack;
import com.converter.server.entities.youtube.YoutubePlaylistItemSnippet;
import com.converter.server.entities.youtube.YoutubeResult;
import com.converter.server.entities.youtube.YoutubeVideoResultBase;
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
    public Mono<ResponseEntity<List<YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet>>>>> getYoutubeSearchResults(HttpServletRequest request, @RequestBody ArrayList<CommonTrack> tracks, @RequestParam int limit) {

        Flux<YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet>>> response = youtubeWebClient.getSearchResults(tracks, limit);

        Mono<List<YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet>>>> result = response.collectList();

        return result.map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.badRequest().build());
    }

}
