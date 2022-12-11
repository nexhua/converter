package com.converter.server.clients;

import com.converter.server.constants.YoutubeAPIConstants;
import com.converter.server.constants.YoutubeApplicationConstants;
import com.converter.server.entities.common.CommonTrack;
import com.converter.server.entities.youtube.YoutubePlaylistItemSnippet;
import com.converter.server.entities.youtube.YoutubeResult;
import com.converter.server.entities.youtube.YoutubeTrackSearchWrapper;
import com.converter.server.entities.youtube.YoutubeVideoResultBase;
import com.converter.server.search.YoutubeSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class YoutubeWebClient {

    static Logger logger = LoggerFactory.getLogger(YoutubeWebClient.class);

    private final WebClient client = WebClient.create();

    public Optional<YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet>>> getYoutubePlaylistItems(String playlistID) {
        URI uri = UriComponentsBuilder.fromHttpUrl(YoutubeAPIConstants.youtube_api_base)
                .path(YoutubeAPIConstants.youtube_api_version_path)
                .path(YoutubeAPIConstants.playlist_items_path)
                .queryParam(YoutubeAPIConstants.part, YoutubeAPIConstants.snippet)
                .queryParam(YoutubeAPIConstants.playlistId, playlistID)
                .queryParam(YoutubeAPIConstants.key, YoutubeApplicationConstants.getApplicationApiKey())
                .build().toUri();

        Optional<YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet>>> result = Optional.empty();
        try {
            var typeRef = new ParameterizedTypeReference<YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet>>>() {
            };
            YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet>> playlistItems = client.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(typeRef)
                    .block();

            if (playlistItems != null) {
                result = Optional.of(playlistItems);
                logger.info("Success - Youtube Playlist Get");
            }
        } catch (Exception e) {
            logger.warn(String.format("Failed - Youtube Playlist Get - %s", e.getMessage()));
        }
        return result;
    }

    public Flux<YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet>>> getSearchResults(ArrayList<CommonTrack> tracks, int limit) {
        if (limit <= 0 || limit > 50) {
            limit = 10;
        }

        int finalLimit = limit;
        return Flux.fromIterable(tracks.stream().limit(limit).collect(Collectors.toList()))
                .flatMapSequential(track -> getSearchResult(track, finalLimit));
    }

    public Mono<YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet>>> getSearchResult(CommonTrack track, int limit) {
        YoutubeSearch youtubeSearch = new YoutubeSearch(track);
        youtubeSearch.setLimit(limit);
        logger.info("Search - Youtube Track Search - " + track.getUnstructuredFullName());


        var typeRef = new ParameterizedTypeReference<YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet>>>() {};

        return client
                .get()
                .uri(youtubeSearch.getSearchString())
                .retrieve()
                .bodyToMono(typeRef)
                .retryWhen(Retry.max(0))
                .log();
    }
}
