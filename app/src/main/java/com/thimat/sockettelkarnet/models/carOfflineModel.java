package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Valipour.Motahareh on 5/23/2018.
 */

public class carOfflineModel extends RealmObject {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    int id=0;
    //----------
    @SerializedName("person_id")
    @Expose
    int person_id = 0;
    //-----------
    @SerializedName("lat")
    @Expose
    double lat = 0.0;
    //----------
    @SerializedName("lng")
    @Expose
    double lng = 0.0;
    //----------
    @SerializedName("speed")
    @Expose
    int speed = 0;
    //--------
    @SerializedName("car_id")
    @Expose
    int car_id = 0;
    //--------
    @SerializedName("car_code")
    @Expose
    String car_code = "";
    //---------
    @SerializedName("start")
    @Expose
    int start = 0;
    //---------
    @SerializedName("time")
    @Expose
    String time = "";
    //---------
    @SerializedName("date")
    @Expose
    String date = "";
    //----------
    @SerializedName("tstmp")
    @Expose
    String tstmp = "";
    //----------
    @SerializedName("deleted")
    @Expose
    int deleted = 0;

    //--------------------------------- get and set deleted
    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    //--------------------------------- get and set tstmp
    public String getTstmp() {
        return tstmp;
    }

    public void setTstmp(String tstmp) {
        this.tstmp = tstmp;
    }

    //---------------------------------- get and set date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //---------------------------------- get and set time
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //---------------------------------- get and set start
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    //----------------------------------- get and set car_code
    public String getCar_code() {
        return car_code;
    }

    public void setCar_code(String car_code) {
        this.car_code = car_code;
    }

    //------------------------------------ get and set car_id
    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    //------------------------------------ get and set speed
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    //------------------------------------ get and set lng
    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    //------------------------------------ get and set lat
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    //------------------------------------ get and set person_id
    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    //------------------------------------- get and set id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
