package com.converter.server.entities.search;

import com.converter.server.entities.common.CommonTrack;

import java.util.ArrayList;
import java.util.List;

public class PlatformSearchResultWrapper {

    List<PlatformSearchResult> alternatives;

    private int chosenTrack = -1;

    public PlatformSearchResultWrapper() {
    }

    //region Getters and Setters


    public List<PlatformSearchResult> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<PlatformSearchResult> alternatives) {
        this.alternatives = alternatives;
    }

    public int getChosenTrack() {
        return chosenTrack;
    }

    public void setChosenTrack(int chosenTrack) {
        this.chosenTrack = chosenTrack;
    }


    //endregion
}
