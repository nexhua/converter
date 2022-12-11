package com.converter.server.interceptors;

import com.converter.server.clients.SpotifyWebClient;
import com.converter.server.services.SpotifyTokenService;
import com.converter.server.tokens.SpotifyTokens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SpotifyTokenInterceptor implements HandlerInterceptor {


    private static Logger logger = LoggerFactory.getLogger(SpotifyTokenInterceptor.class);
    private final SpotifyTokenService spotifyTokenService;

    private final SpotifyWebClient spotifyWebClient;

    public SpotifyTokenInterceptor(SpotifyTokenService spotifyTokenService, SpotifyWebClient spotifyWebClient) {
        this.spotifyTokenService = spotifyTokenService;
        this.spotifyWebClient = spotifyWebClient;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();


        Optional<SpotifyTokens> optionalTokens = this.spotifyTokenService.findOptional(session.getId());

        if (optionalTokens.isPresent()) {
            SpotifyTokens tokens = optionalTokens.get();

            if (tokens.shouldRefreshToken()) {
                //refresh token
                logger.info("Trying To Refresh Spotify Tokens");
                return spotifyWebClient.refreshSpotifyTokens(session.getId(), tokens);
            } else {
                return true;
            }
        }

        logger.warn("Failed - Spotify Tokens Not Found");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
