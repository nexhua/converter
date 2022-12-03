package com.converter.server.entities.search;

import com.converter.server.entities.spotify.SpotifyTrack;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;

public class SpotifySearchResult implements Callable<Mono<SpotifyTrack>> {

    @Override
    public Mono<SpotifyTrack> call() throws Exception {
        return null;
    }
}
