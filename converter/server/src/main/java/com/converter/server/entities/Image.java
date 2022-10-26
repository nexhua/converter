package com.converter.server.entities;

public class Image {
    private String url;

    private Integer height;

    private Integer width;

    public Image() {
    }

    //region

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }


    //endregion
}
