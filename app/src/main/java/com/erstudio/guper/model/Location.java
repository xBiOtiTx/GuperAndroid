package com.erstudio.guper.model;

/**
 * Created by Евгений on 20.09.2015.
 */
public class Location {
    private Float lat;
    private Float lon;

    public Location() {
    }

    public Location(Float lat, Float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }
}
