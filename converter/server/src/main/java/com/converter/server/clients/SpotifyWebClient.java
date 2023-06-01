package com.converter.server.clients;

import com.converter.server.constants.SpotifyAPIConstants;
import com.converter.server.constants.SpotifyApplicationConstants;
import com.converter.server.converters.SpotifyConverter;
import com.converter.server.entities.common.CommonTrack;
import com.converter.server.entities.search.PlatformSearchResult;
import com.converter.server.entities.search.PlatformSearchResultWrapper;
import com.converter.server.entities.spotify.*;
import com.converter.server.errors.SpotifyError;
import com.converter.server.exceptions.SpotifyResponseException;
import com.converter.server.inputs.CreatePlaylistInput;
import com.converter.server.inputs.SpotifyPlaylistCreateOptions;
import com.converter.server.inputs.spotify.PlaylistAddTracks;
import com.converter.server.search.SpotifySearch;
import com.converter.server.services.ClientIDService;
import com.converter.server.tokens.SpotifyTokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SpotifyWebClient {

    static Logger logger = LoggerFactory.getLogger(SpotifyWebClient.class);

    @Autowired
    private ClientIDService service;

    private final WebClient client = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(10485760))
                    .build())
            .build();

    public Optional<SpotifyTokens> getSpotifyTokens(String code) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_auth_base)
                .path(SpotifyAPIConstants.api_token_path).build().toUri();

        MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();

        bodyValues.add(SpotifyAPIConstants.code, code);
        bodyValues.add(SpotifyAPIConstants.redirect_uri, SpotifyAPIConstants.app_spotify_redirect_endpoint);
        bodyValues.add(SpotifyAPIConstants.grant_type, SpotifyAPIConstants.authorization_code);

        Optional<SpotifyTokens> optionalSpotifyTokens;

        try {
            SpotifyTokens spotifyTokens = client.post()
                    .uri(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, SpotifyApplicationConstants.getEncodedAuthString())
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromFormData(bodyValues))
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class).map(SpotifyResponseException::new))
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class).map(SpotifyResponseException::new))
                    .bodyToMono(SpotifyTokens.class)
                    .block();

            if (spotifyTokens != null) {
                optionalSpotifyTokens = Optional.of(spotifyTokens);
            } else {
                optionalSpotifyTokens = Optional.empty();
            }
        } catch (NestedRuntimeException exception) {
            logger.warn(String.format("Failed - Get Spotify Tokens - %s", exception.getMessage()));
            optionalSpotifyTokens = Optional.empty();
        }
        logger.info("Success - Get Spotify Tokens");
        return optionalSpotifyTokens;
    }

    public Mono<SpotifyUser> getCurrentUserProfile(SpotifyTokens tokens) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path(SpotifyAPIConstants.current_user_path)
                .build().toUri();

        logger.info("Started - Get Spotify Current User Profile");
        return client.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(SpotifyUser.class);
    }

    public Mono<ServerResponse> getUserPlaylists(SpotifyTokens tokens, int limit, int offset) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path(SpotifyAPIConstants.current_user_playlist_path)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .build().toUri();

        logger.info("Started - Get Spotify User Playlists");
        return client.get()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handleErrors)
                .onStatus(HttpStatus::is5xxServerError, this::handleErrors)
                .bodyToMono(SpotifyPlaylists.class)
                .map(playlists -> ServerResponse.ok().body(playlists.getItems()))
                .onErrorReturn(ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> getPlaylistTracks(SpotifyTokens tokens, String playlistID, int limit, int offset) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path("/playlists")
                .path("/" + playlistID)
                .path("/tracks")
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .build().toUri();

        SpotifyConverter converter = new SpotifyConverter();

        logger.info(String.format("Started - Spotify Playlist Get Tracks Request - playlistId: %s", playlistID));
        return client.get()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handleErrors)
                .onStatus(HttpStatus::is5xxServerError, this::handleErrors)
                .bodyToMono(SpotifyTracks.class)
                .map(spotifyTracks -> ServerResponse.ok().body(converter.toCommonTracks(spotifyTracks.getItems().stream().map(SpotifyTrackWrapper::getTrack).collect(Collectors.toList()))))
                .onErrorResume(SpotifyResponseException.class, error -> error.getError() == null ?
                        Mono.just(ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).body(error.getMessage())) :
                        Mono.just(ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).body(error.getError().getError().getMessage())))
                .onErrorReturn(ServerResponse.notFound().build());
    }

    public boolean refreshSpotifyTokens(String sessionID, SpotifyTokens tokens) {
        logger.info("Started - Spotify Refresh Tokens");
        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_auth_base)
                .path(SpotifyAPIConstants.api_token_path).build().toUri();

        MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();

        bodyValues.add(SpotifyAPIConstants.refresh_token, tokens.getRefresh_token());
        bodyValues.add(SpotifyAPIConstants.grant_type, SpotifyAPIConstants.refresh_token);


        try {
            SpotifyTokens refreshedToken = client.post()
                    .uri(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, SpotifyApplicationConstants.getEncodedAuthString())
                    .body(BodyInserters.fromFormData(bodyValues))
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, this::handleErrors)
                    .onStatus(HttpStatus::is5xxServerError, this::handleErrors)
                    .bodyToMono(SpotifyTokens.class)
                    .block();

            if (refreshedToken != null) {
                tokens.updateToken(refreshedToken);
                logger.info("Success - Spotify Refresh Tokens");
                return true;
            }
            logger.warn("Failed - Spotify Refresh Tokens - Failed To Get");
            return false;
        } catch (NestedRuntimeException exception) {
            logger.warn(String.format("Failed - Spotify Refresh Tokens - %s", exception.getMessage()));
            return false;
        }
    }

    public Flux<PlatformSearchResultWrapper> getSpotifySearch(SpotifyTokens tokens, ArrayList<CommonTrack> tracksToSearch, int limit) {

        return Flux.fromIterable(tracksToSearch)
                .flatMapSequential(track -> getSearchResult(track, tokens, limit));
    }

    private Mono<PlatformSearchResultWrapper> getSearchResult(CommonTrack track, SpotifyTokens tokens, int limit) {
        SpotifySearch spotifySearch = new SpotifySearch(track);
        spotifySearch.setLimit(limit);

        SpotifyConverter converter = new SpotifyConverter();

        logger.info(String.format("Spotify - Search Track - %s", track.getUnstructuredFullName()));
        return client
                .get()
                .uri(spotifySearch.getSearchString())
                .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(SpotifyTrackSearchResultWrapper.class)
                .map(this::createSearchResult)
                .log();
    }


    public Mono<SpotifyPlaylist> createPlaylist(SpotifyTokens tokens, CreatePlaylistInput<SpotifyPlaylistCreateOptions> input, SpotifyUser user) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path(SpotifyAPIConstants.current_user_create_playlist_path)
                .buildAndExpand(user.getId()).toUri();

        logger.info(String.format("Spotify - Create Playlist User - %s Playlist - %s", user.getId(), input.getOptions().getName()));

        return client.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(input.getOptions()))
                .retrieve()
                .bodyToMono(SpotifyPlaylist.class)
                .doOnError(err -> System.out.println(err.getMessage()))
                .log();
    }

    public Mono<ServerResponse> addTracksToPlaylist(SpotifyTokens tokens, SpotifyPlaylist playlist, CreatePlaylistInput<SpotifyPlaylistCreateOptions> input) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path(SpotifyAPIConstants.playlist_add_tracks)
                .buildAndExpand(playlist.getId()).toUri();

        logger.info(String.format("Spotify - Create Playlist Playlist - %s - Track Count - %d", input.getOptions().getName(), input.getTracks().size()));

        PlaylistAddTracks body = new PlaylistAddTracks(input);

        return client.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ServerResponse.ok().body(response))
                .log();
    }

    private Mono<Throwable> handleErrors(ClientResponse response) {

        return response
                .bodyToMono(SpotifyError.class)
                .flatMap(Mono::error);
    }

    private PlatformSearchResultWrapper createSearchResult(SpotifyTrackSearchResultWrapper result) {
        SpotifyConverter converter = new SpotifyConverter();
        PlatformSearchResultWrapper searchResultWrapper = new PlatformSearchResultWrapper();
        List<PlatformSearchResult> alternatives = result.getTracks().getItems().stream()
                .map(track -> {
                    PlatformSearchResult platformSearchResult = new PlatformSearchResult();
                    platformSearchResult.setTrack(converter.toCommonTrack(track));
                    platformSearchResult.setPlatformIdentifier(track.getId());
                    return platformSearchResult;
                }).toList();

        searchResultWrapper.setAlternatives(alternatives);
        searchResultWrapper.setChosenTrack(0);
        return searchResultWrapper;
    }
}
