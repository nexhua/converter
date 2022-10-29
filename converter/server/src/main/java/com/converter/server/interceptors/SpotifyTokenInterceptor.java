package com.converter.server.interceptors;

import com.converter.server.client.BaseWebClient;
import com.converter.server.services.SpotifyTokenService;
import com.converter.server.tokens.SpotifyTokens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

public class SpotifyTokenInterceptor implements HandlerInterceptor {


    private SpotifyTokenService spotifyTokenService;

    public SpotifyTokenInterceptor(SpotifyTokenService spotifyTokenService) {
        this.spotifyTokenService = spotifyTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<Cookie> sessionCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("SESSIONID")).findFirst();

        if(sessionCookie.isPresent()) {
            Optional<SpotifyTokens> optionalTokens = this.spotifyTokenService.findOptional(sessionCookie.get().getValue());

            if(optionalTokens.isPresent()) {
                SpotifyTokens tokens = optionalTokens.get();

                if(tokens.shouldRefreshToken()) {
                    //refresh token
                    BaseWebClient.refreshSpotifyTokens(sessionCookie.get().getValue(), tokens);
                }
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
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
