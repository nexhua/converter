package com.converter.server.interceptors;

import com.converter.server.clients.SpotifyWebClient;
import com.converter.server.services.SpotifyTokenService;
import com.converter.server.tokens.SpotifyTokens;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SpotifyTokenInterceptor implements HandlerInterceptor {


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
                return spotifyWebClient.refreshSpotifyTokens(session.getId(), tokens);
            }
        }

        return true;
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
