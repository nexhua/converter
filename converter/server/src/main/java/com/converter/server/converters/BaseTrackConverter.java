package com.converter.server.converters;

import com.converter.server.interfaces.IConvertible;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaseTrackConverter<T extends IConvertible, R> {

    private final Function<T, R> baseToTarget;
    private final Function<R, T> targetToBase;

    public BaseTrackConverter(final Function<T, R> baseToTarget, final Function<R, T> targetToBase) {
        this.baseToTarget = baseToTarget;
        this.targetToBase = targetToBase;
    }

    public final R toCommonTrack(final T track) {
        return baseToTarget.apply(track);
    }

    public final T toBaseTrack(final R track) {
        return targetToBase.apply(track);
    }

    public final List<R> toCommonTracks(final Collection<T> tracks) {
        return tracks.stream().map(this::toCommonTrack).collect(Collectors.toList());
    }

    public final List<T> toTargetTracks(final Collection<R> tracks) {
        return tracks.stream().map(this::toBaseTrack).collect(Collectors.toList());
    }
}
