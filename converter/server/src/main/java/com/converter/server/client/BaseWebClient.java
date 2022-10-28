package com.converter.server.client;

import com.converter.server.constants.SpotifyAPIConstants;
import com.converter.server.constants.SpotifyApplicationConstants;
import com.converter.server.entities.spotify.SpotifyPlaylist;
import com.converter.server.entities.spotify.SpotifyPlaylists;
import com.converter.server.services.ClientIDService;
import com.converter.server.tokens.SpotifyTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

public class BaseWebClient {

    @Autowired
    private ClientIDService clientIDService;

    private static final WebClient client = WebClient.create();



    public static SpotifyTokens getSpotifyTokens(String code) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_auth_base)
                .path(SpotifyAPIConstants.api_token_path).build().toUri();

        MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();

        bodyValues.add(SpotifyAPIConstants.code, code);
        bodyValues.add(SpotifyAPIConstants.redirect_uri, SpotifyAPIConstants.app_spotify_redirect_endpoint);
        bodyValues.add(SpotifyAPIConstants.grant_type, SpotifyAPIConstants.authorization_code);


        Mono<SpotifyTokens> tokensMono = client.post()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.AUTHORIZATION, SpotifyApplicationConstants.getEncodedAuthString())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(bodyValues))
                .retrieve()
                .bodyToMono(SpotifyTokens.class);

        return tokensMono.share().block();
    }

    public static SpotifyPlaylists getUserPlaylists(SpotifyTokens tokens) {

        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path(SpotifyAPIConstants.current_user_playlist_path).build().toUri();


        SpotifyPlaylists playlists = client.get()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                .retrieve()
                .bodyToMono(SpotifyPlaylists.class)
                .block();

        return playlists;
    }

    public static SpotifyPlaylist getUserPlaylist(SpotifyTokens tokens, String playlistID) {
        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_api_base)
                .path("/playlists")
                .path("/" + playlistID)
                .queryParam("fields", "href,id,name,images,tracks(href),tracks.items(track(href,name,id))").build().toUri();

        SpotifyPlaylist playlist = client.get()
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, tokens.toBearerTokenString())
                .retrieve()
                .bodyToMono(SpotifyPlaylist.class)
                .block();

        return playlist;
    }
}
