package com.thimat.sockettelkarnet.models;

import com.google.android.gms.maps.model.LatLng;

public class InfoLocation {
    LatLng location;
    //----------------
    String name;
    //----------------
    String date;
    //----------------
    String time;
    //----------------
    int speed;
    //----------------
    String stoptime;
    //----------------
    double distance;
    //-------------------------------------- get and set distance

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    //-------------------------------------- get and set stoptime
    public String getStoptime() {
        return stoptime;
    }

    public void setStoptime(String stoptime) {
        this.stoptime = stoptime;
    }

    //-------------------------------------- get and set time
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //-------------------------------------- get and set speed
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    //-------------------------------------- get and set date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //-------------------------------------- get and set name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //-------------------------------------- get location
    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
