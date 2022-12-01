package com.converter.server.entities.youtube;

import com.converter.server.entities.Image;
import com.fasterxml.jackson.annotation.JsonProperty;

public class YoutubeThumbnails {

    @JsonProperty("default")
    private Image defaultImage;

    private Image medium;

    private Image high;

    public YoutubeThumbnails() {
    }

    //region Getters and Setters

    public Image getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(Image defaultImage) {
        this.defaultImage = defaultImage;
    }

    public Image getMedium() {
        return medium;
    }

    public void setMedium(Image medium) {
        this.medium = medium;
    }

    public Image getHigh() {
        return high;
    }

    public void setHigh(Image high) {
        this.high = high;
    }


    //endregion
}
