package com.converter.server.routers;

import com.converter.server.clients.SpotifyWebClient;
import com.converter.server.constants.SpotifyAPIConstants;
import com.converter.server.constants.SpotifyApplicationConstants;
import com.converter.server.helpers.RandomString;
import com.converter.server.services.ClientIDService;
import com.converter.server.services.SpotifyTokenService;
import com.converter.server.tokens.SpotifyTokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    ClientIDService clientIDService;

    @Autowired
    SpotifyTokenService spotifyTokenService;

    @Autowired
    SpotifyWebClient spotifyWebClient;

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/spotify")
    public ResponseEntity<?> authorizeSpotify(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String state = RandomString.generateRandomString(11);

        URI uri = UriComponentsBuilder.fromHttpUrl(SpotifyAPIConstants.spotify_auth_base)
                .path(SpotifyAPIConstants.authorize_path)
                .queryParam(SpotifyAPIConstants.client_id, SpotifyApplicationConstants.getClient_id())
                .queryParam(SpotifyAPIConstants.response_type, "code")
                .queryParam(SpotifyAPIConstants.scope, "playlist-read-private playlist-read-collaborative")
                .queryParam(SpotifyAPIConstants.state, state)
                .queryParam(SpotifyAPIConstants.redirect_uri, SpotifyAPIConstants.app_spotify_redirect_endpoint)
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.LOCATION, uri.toString());

        this.clientIDService.putSession(session.getId(), state);

        logger.info("Redirect - Spotify Code Access");
        return new ResponseEntity(headers, HttpStatus.FOUND);
    }

    @GetMapping("/spotify/access")
    public ResponseEntity<?> accessToken(HttpServletRequest request, @RequestParam(required = false) String code, @RequestParam(required = false) String state) {
        HttpSession session = request.getSession();

        String sessionID = session.getId();

        Optional<String> sessionStateOptional = this.clientIDService.findSessionOptional(sessionID);

        if (state == null) {
            //If there is no state that is matched with the session id
            return ResponseEntity.notFound().build();
        }

        if (sessionStateOptional.isPresent()) {
            if (sessionStateOptional.get().equals(state)) {
                //If the state matches, check code and proceed with the authorization code to get the access and refresh tokens
                if (code != null) {

                    Optional<SpotifyTokens> optionalTokens = spotifyWebClient.getSpotifyTokens(code);

                    if (optionalTokens.isPresent()) {
                        this.spotifyTokenService.add(sessionID, optionalTokens.get());
                        logger.info("Success - Received Spotify Tokens");
                        return new ResponseEntity<>(optionalTokens.get(), HttpStatus.OK);
                    } else {
                        logger.warn("Failed - No Tokens Received");
                        new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                } else {
                    logger.warn(String.format("Failed - No Code Received - %s", request.getParameter("error")));
                    return new ResponseEntity<>(request.getParameter("error"), HttpStatus.BAD_REQUEST);
                }
            } else {
                logger.warn("Failed - State Mismatch");
                this.clientIDService.removeSession(sessionID);
                //If state send for authorization code is not the same with the request state
                return ResponseEntity.badRequest().build();
            }
        }

        //If request does not have session and state pair
        logger.warn("Failed - No Session-State Pair Found");
        return ResponseEntity.internalServerError().build();
    }
}
