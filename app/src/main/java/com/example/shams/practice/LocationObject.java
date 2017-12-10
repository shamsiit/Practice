package com.example.shams.practice;

/**
 * Created by shams on 11/11/17.
 */

public class LocationObject {

    private double lat;
    private double lon;

    public LocationObject(double lat,double lon){
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
