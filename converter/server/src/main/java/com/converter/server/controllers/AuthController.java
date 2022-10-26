package com.converter.server.controllers;

import com.converter.server.client.BaseWebClient;
import com.converter.server.constants.SpotifyAPIConstants;
import com.converter.server.constants.SpotifyApplicationConstants;
import com.converter.server.helpers.RandomString;
import com.converter.server.services.ClientIDService;
import com.converter.server.services.SpotifyTokenService;
import com.converter.server.tokens.SpotifyTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    ClientIDService clientIDService;

    @Autowired
    SpotifyTokenService spotifyTokenService;

    @GetMapping("/spotify")
    public ResponseEntity<?> authorizeSpotify(HttpServletRequest request) {

        String state = RandomString.generateRandomString(11);

        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_auth_base)
                        .path(SpotifyAPIConstants.authorize_path)
                .queryParam(SpotifyAPIConstants.client_id, SpotifyApplicationConstants.client_id)
                .queryParam(SpotifyAPIConstants.response_type, "code")
                .queryParam(SpotifyAPIConstants.scope, "playlist-read-private playlist-read-collaborative")
                .queryParam(SpotifyAPIConstants.state, state)
                .queryParam(SpotifyAPIConstants.redirect_uri, SpotifyAPIConstants.app_spotify_redirect_endpoint)
                        .build().toUri();

        HttpHeaders headers = new HttpHeaders();

        String sessionID = RandomString.generateRandomString(32);

        headers.add(HttpHeaders.SET_COOKIE, "SESSIONID="+sessionID+"; Path=/");
        headers.add(HttpHeaders.LOCATION, uri.toString());

        this.clientIDService.getSessionIDs().put(sessionID, state);

       return new ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY);

    }

    @GetMapping("/spotify/access")
    public ResponseEntity<?> accessToken(HttpServletRequest request, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {

        Optional<Cookie> sessionCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("SESSIONID")).findFirst();

        if(!sessionCookie.isEmpty()) {
            String sessionID = sessionCookie.get().getValue();

            String state1 = this.clientIDService.getSessionIDs().get(sessionID);

            if(state == null) {
                //If there is no state that is matched with the session id
                return ResponseEntity.notFound().build();
            }

            if(state.equals(request.getParameter(SpotifyAPIConstants.state))) {
                //If the state matches, check code and proceed with the authorization code to get the access and refresh tokens
                String code1 = request.getParameter(SpotifyAPIConstants.code);
                if(code != null) {

                    SpotifyTokens tokens = BaseWebClient.getSpotifyTokens(code);

                    if(tokens != null) {
                        this.spotifyTokenService.getSpotifySessionTokens().put(sessionID, tokens);
                        return new ResponseEntity<>(tokens, HttpStatus.OK);
                    }
                    else {
                        new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }
                else {
                    return new ResponseEntity<>(request.getParameter("error"), HttpStatus.BAD_REQUEST);
                }
            }
            else {
                this.clientIDService.getSessionIDs().remove(sessionID);
                //If state send for authorization code is not the same with the request state
                return ResponseEntity.badRequest().build();
            }
        }

        //If request does not have SESSIONID cookie
        return ResponseEntity.badRequest().build();
    }
}
