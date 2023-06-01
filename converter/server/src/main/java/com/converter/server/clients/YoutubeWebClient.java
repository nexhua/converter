package com.converter.server.clients;

import com.converter.server.constants.YoutubeAPIConstants;
import com.converter.server.constants.YoutubeApplicationConstants;
import com.converter.server.converters.YoutubeConverter;
import com.converter.server.entities.common.CommonTrack;
import com.converter.server.entities.search.PlatformSearchResult;
import com.converter.server.entities.search.PlatformSearchResultWrapper;
import com.converter.server.entities.youtube.YoutubePlaylistItemSnippet;
import com.converter.server.entities.youtube.YoutubeResult;
import com.converter.server.entities.youtube.YoutubeVideoResourceId;
import com.converter.server.entities.youtube.YoutubeVideoResultBase;
import com.converter.server.search.YoutubeSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class YoutubeWebClient {

    static Logger logger = LoggerFactory.getLogger(YoutubeWebClient.class);

    private final WebClient client = WebClient.create();

    public Mono<ServerResponse> getYoutubePlaylistItems(String playlistID, int limit) {
        logger.info(String.format("Youtube Playlist Get - Playlist: %s", playlistID));

        URI uri = UriComponentsBuilder.fromHttpUrl(YoutubeAPIConstants.youtube_api_base)
                .path(YoutubeAPIConstants.youtube_api_version_path)
                .path(YoutubeAPIConstants.playlist_items_path)
                .queryParam(YoutubeAPIConstants.part, YoutubeAPIConstants.snippet)
                .queryParam(YoutubeAPIConstants.playlistId, playlistID)
                .queryParam(YoutubeAPIConstants.key, YoutubeApplicationConstants.getApplicationApiKey())
                .queryParam(YoutubeAPIConstants.maxResults, limit)
                .build().toUri();

        var typeRef = new ParameterizedTypeReference<YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet, String>>>() {
        };

        YoutubeConverter converter = new YoutubeConverter();

        return client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(typeRef)
                .map(result -> ServerResponse.ok().body(
                        converter.toCommonTracks(result.getItems())
                ))
                .onErrorReturn(ServerResponse.notFound().build());
    }

    public Flux<PlatformSearchResultWrapper> getSearchResults(ArrayList<CommonTrack> tracks, int limit) {
        if (limit <= 0 || limit > 50) {
            limit = 10;
        }

        return Flux.fromIterable(tracks.stream().limit(limit).collect(Collectors.toList()))
                .flatMapSequential(track -> getSearchResult(track, 5));
    }

    public Mono<PlatformSearchResultWrapper> getSearchResult(CommonTrack track, int limit) {
        YoutubeSearch youtubeSearch = new YoutubeSearch(track);
        youtubeSearch.setLimit(limit);
        logger.info("Search - Youtube Track Search - " + track.getUnstructuredFullName());


        var typeRef = new ParameterizedTypeReference<YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet, YoutubeVideoResourceId>>>() {
        };

        return client
                .get()
                .uri(youtubeSearch.getSearchString())
                .retrieve()
                .bodyToMono(typeRef)
                .map(this::createSearchResult)
                .log();
    }


    private PlatformSearchResultWrapper createSearchResult(YoutubeResult<YoutubeVideoResultBase<YoutubePlaylistItemSnippet, YoutubeVideoResourceId>> searchResult) {
        YoutubeConverter converter = new YoutubeConverter();
        PlatformSearchResultWrapper searchResultWrapper = new PlatformSearchResultWrapper();

        var alternatives = searchResult.getItems().stream().map(
                track -> {
                    var newTrack = new YoutubeVideoResultBase<YoutubePlaylistItemSnippet, String>();
                    newTrack.setSnippet(track.getSnippet());
                    newTrack.setId(track.getId().getVideoId());

                    CommonTrack commonTrack = converter.toCommonTrack(newTrack);

                    PlatformSearchResult platformSearchResult = new PlatformSearchResult();
                    platformSearchResult.setPlatformIdentifier(track.getId().getVideoId());
                    platformSearchResult.setTrack(commonTrack);
                    return platformSearchResult;
                }).toList();

        searchResultWrapper.setAlternatives(alternatives);
        searchResultWrapper.setChosenTrack(0);
        return searchResultWrapper;
    }
}
