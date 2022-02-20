package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GeofenceModel extends RealmObject {
    @SerializedName("ID")
    @Expose
    @PrimaryKey
    String id = "";
    //-------------------
    @SerializedName("car_code")
    @Expose
    String car_code = "";
    //--------------------
    @SerializedName("Lat")
    @Expose
    Double Lat = 0.0;
    //---------------------
    @SerializedName("Lon")
    @Expose
    Double Lon = 0.0;
    //---------------------
    @SerializedName("Radius")
    @Expose
    Double Radius = 0.0;
    //----------------------
    @SerializedName("namegeo")
    @Expose
    String namegeo = "";
    //------------------------
    @SerializedName("flag")
    @Expose
    Boolean flag;
    //-----------------------
    @SerializedName("actdeac")
    @Expose
    Boolean actdeac = true;

    //----------------------------- get and set actdeac
    public Boolean getActdeac() {
        return actdeac;
    }

    public void setActdeac(Boolean actdeac) {
        this.actdeac = actdeac;
    }

    //----------------------------- get and set flag
    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    //------------------------------ get and set namegeo
    public String getNamegeo() {
        return namegeo;
    }

    public void setNamegeo(String namegeo) {
        this.namegeo = namegeo;
    }

    //------------------------------ get and set Radius
    public Double getRadius() {
        return Radius;
    }

    public void setRadius(Double radius) {
        Radius = radius;
    }

    //------------------------------get and set Lon
    public Double getLon() {
        return Lon;
    }

    public void setLon(Double lon) {
        Lon = lon;
    }

    //------------------------------get and set Lat
    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    //------------------------------get and set car_code
    public String getCar_code() {
        return car_code;
    }

    public void setCar_code(String car_code) {
        this.car_code = car_code;
    }

    //------------------------------get and set id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
