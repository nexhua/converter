package com.converter.server.client;

import com.converter.server.constants.SpotifyAPIConstants;
import com.converter.server.constants.SpotifyApplicationConstants;
import com.converter.server.entities.spotify.SpotifyPlaylist;
import com.converter.server.entities.spotify.SpotifyPlaylists;
import com.converter.server.exceptions.SpotifyResponseException;
import com.converter.server.services.ClientIDService;
import com.converter.server.tokens.SpotifyTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;

import java.net.URI;
import java.util.Optional;

public class BaseWebClient {

    @Autowired
    private ClientIDService clientIDService;

    private static final WebClient client = WebClient.create();


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
            optionalSpotifyTokens = Optional.empty();
        }

        return optionalSpotifyTokens;
    }

    public static Optional<SpotifyPlaylists> getUserPlaylists(SpotifyTokens tokens) {

        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path(SpotifyAPIConstants.current_user_playlist_path).build().toUri();

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
            optionalSpotifyPlaylists = Optional.empty();
        }

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

    public static boolean refreshSpotifyTokens(String sessionID, SpotifyTokens tokens) {
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
                return true;
            }
            return false;
        } catch (NestedRuntimeException exception) {
            return false;
        }
    }
}
