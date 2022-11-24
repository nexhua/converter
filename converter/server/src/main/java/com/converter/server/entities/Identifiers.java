package com.converter.server.entities;

public class Identifiers {

    private String isrc;

    private String ean;

    private String upc;

    public Identifiers() {
    }

    //region Getters and Setters

    public String getIsrc() {
        return isrc;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }


    //endregion
}
