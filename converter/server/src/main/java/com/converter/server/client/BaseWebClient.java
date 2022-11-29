package com.converter.server.client;

import com.converter.server.constants.SpotifyAPIConstants;
import com.converter.server.constants.SpotifyApplicationConstants;
import com.converter.server.entities.common.CommonTrack;
import com.converter.server.entities.spotify.*;
import com.converter.server.exceptions.SpotifyResponseException;
import com.converter.server.search.SpotifySearch;
import com.converter.server.services.ClientIDService;
import com.converter.server.tokens.SpotifyTokens;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

public class BaseWebClient {

    static Logger logger = LoggerFactory.getLogger(BaseWebClient.class);

    @Autowired
    private ClientIDService clientIDService;

    private static final WebClient client = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(10485760))
                    .build())
            .build();


    public static Optional<SpotifyTokens> getSpotifyTokens(String code) {
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

    public static Optional<SpotifyPlaylists> getUserPlaylists(SpotifyTokens tokens, int limit, int offset) {
        if (limit <= 0 || limit > 50) {
            limit = 10;
        }

        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path(SpotifyAPIConstants.current_user_playlist_path)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .build().toUri();

        Optional<SpotifyPlaylists> optionalSpotifyPlaylists;

        try {
            SpotifyPlaylists playlists = client.get()
                    .uri(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class).map(SpotifyResponseException::new))
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class).map(SpotifyResponseException::new))
                    .bodyToMono(SpotifyPlaylists.class)
                    .block();

            if (playlists != null) {
                optionalSpotifyPlaylists = Optional.of(playlists);
            } else {
                optionalSpotifyPlaylists = Optional.empty();
            }
        } catch (NestedRuntimeException exception) {
            logger.warn(String.format("Failed - Get Spotify User Playlists - %s", exception.getMessage()));
            optionalSpotifyPlaylists = Optional.empty();
        }
        logger.info("Success - Get Spotify User Playlists");
        return optionalSpotifyPlaylists;
    }

    public static Optional<SpotifyPlaylist> getUserPlaylist(SpotifyTokens tokens, String playlistID) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path("/playlists")
                .path("/" + playlistID)
                .queryParam("fields", "href,id,name,images,tracks(href),tracks.items(track(href,name,id))").build().toUri();


        Optional<SpotifyPlaylist> optionalSpotifyPlaylist;
        try {

            SpotifyPlaylist playlist = client.get()
                    .uri(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class).map(SpotifyResponseException::new))
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class).map(SpotifyResponseException::new))
                    .bodyToMono(SpotifyPlaylist.class)
                    .block();

            if (playlist != null) {
                optionalSpotifyPlaylist = Optional.of(playlist);
            } else {
                optionalSpotifyPlaylist = Optional.empty();
            }
        } catch (NestedRuntimeException exception) {
            optionalSpotifyPlaylist = Optional.empty();
        }

        return optionalSpotifyPlaylist;
    }

    public static Optional<SpotifyTracks> getPlaylistTracks(SpotifyTokens tokens, String playlistID, int limit, int offset) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path("/playlists")
                .path("/" + playlistID)
                .path("/tracks")
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .build().toUri();


        Optional<SpotifyTracks> optionalSpotifyPlaylist;
        try {

            SpotifyTracks playlist = client.get()
                    .uri(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class).map(SpotifyResponseException::new))
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class).map(SpotifyResponseException::new))
                    .bodyToMono(SpotifyTracks.class)
                    .block();

            if (playlist != null) {
                optionalSpotifyPlaylist = Optional.of(playlist);
            } else {
                optionalSpotifyPlaylist = Optional.empty();
            }
        } catch (NestedRuntimeException exception) {
            logger.warn(String.format("Failed - Get Spotify User Playlist Tracks - %s", exception.getMessage()));
            optionalSpotifyPlaylist = Optional.empty();
        }
        logger.info("Success - Get Spotify User Playlist Tracks");
        return optionalSpotifyPlaylist;
    }

    public static boolean refreshSpotifyTokens(String sessionID, SpotifyTokens tokens) {
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
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> clientResponse.bodyToMono(String.class).map(SpotifyResponseException::new))
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class).map(SpotifyResponseException::new))
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

    public static Mono<SpotifyTrackSearchResultWrapper> getSpotifySearch(SpotifyTokens tokens, ArrayList<CommonTrack> tracksToSearch) {
        SpotifySearch spotifySearch = new SpotifySearch(tracksToSearch.get(0));

        Optional<SpotifyTrackSearchResultWrapper> response = Optional.empty();
        try {
            Mono<SpotifyTrackSearchResultWrapper> spotifyTrackSearchResultWrapperMono = client.get()
                    .uri(spotifySearch.getSearchString())
                    .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(SpotifyTrackSearchResultWrapper.class)
                    .log();

            return spotifyTrackSearchResultWrapperMono;
        } catch (
                NestedRuntimeException exception) {
            logger.warn(String.format("Failed - Spotify Search - %s", exception.getMessage()));
        }

        return Mono.empty();
    }
}
