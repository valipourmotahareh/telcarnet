package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Valipour.Motahareh on 5/20/2018.
 */

public class CarModel extends RealmObject {
    @SerializedName("id")
    @PrimaryKey
    @Expose
    int id = 0;
    //--------------------- car_code and SN(from sms)
    @SerializedName("car_code")
    @Expose
    String car_code = "";
    //---------------------
    @SerializedName("car_name")
    @Expose
    String car_name = "خودرو";
    //---------------------
    @SerializedName("person_id")
    @Expose
    int person_id = 0;
    //---------------------
    @SerializedName("sharj")
    @Expose
    String sharj = "";
    //--------------------
    @SerializedName("lat")
    @Expose
    @Nullable
    double lat = 0.0;
    //-------------------
    @SerializedName("lng")
    @Expose
    @Nullable
    double lng = 0.0;
    //-------------------
    @SerializedName("tstmp")
    @Expose
    String tstmp = "";
    //---------------------
    @SerializedName("phone_device")
    @Expose
    String phone = "";
    //----------------------
    @SerializedName("model_device")
    @Expose
    int model = 0;
    //----------
    @SerializedName("speed")
    @Expose
    int speed = 0;
    //---------
    @SerializedName("time")
    @Expose
    String time = "";
    //---------
    @SerializedName("date")
    @Expose
    String date = "";
    //---------------------
    @SerializedName("ultra_sensor")
    @Expose
    int ultra_sensor = 0;
    //---------------------
    @SerializedName("shock_sensor")
    @Expose
    int shock_sensor = 0;
    //---------------------
    @SerializedName("is_battery_connected")
    @Expose
    int is_battery_connected = 0;
    //----------------------
    @SerializedName("door")
    @Expose
    int door = 0;
    //----------------------
    @SerializedName("cod_ticket")
    @Expose
    int cod_ticket = 0;
    //---------
    @SerializedName("start")
    @Expose
    int start = 0;
    //--------------------
    @SerializedName("act_status")
    @Expose
    int act_status = 0;
    //---------------------
    @SerializedName("status")
    @Expose
    String status = "";
    //--------------------
    //-------------------------
    @SerializedName("notification")
    @Expose
    int notification = 0;
    int sync = 0;

    //------------------------------------- get and set notification
    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    //------------------------------------- get and set status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //------------------------------------- get and set act_status
    public int getAct_status() {
        return act_status;
    }

    public void setAct_status(int act_status) {
        this.act_status = act_status;
    }

    //------------------------------------- get and set door
    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    //------------------------------------- get and set is_battery_connected
    public int getIs_battery_connected() {
        return is_battery_connected;
    }

    public void setIs_battery_connected(int is_battery_connected) {
        this.is_battery_connected = is_battery_connected;
    }

    //------------------------------------- get and set shock_sensor
    public int getShock_sensor() {
        return shock_sensor;
    }

    public void setShock_sensor(int shock_sensor) {
        this.shock_sensor = shock_sensor;
    }

    //------------------------------------- get and set ultra_sensor
    public int getUltra_sensor() {
        return ultra_sensor;
    }

    public void setUltra_sensor(int ultra_sensor) {
        this.ultra_sensor = ultra_sensor;
    }

    //------------------------------------- get and set cod_ticket
    public int getCod_ticket() {
        return cod_ticket;
    }

    public void setCod_ticket(int cod_ticket) {
        this.cod_ticket = cod_ticket;
    }

    //------------------------------------- get and set sync
    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    //------------------------------------- get and set model
    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    //------------------------------------- get and set phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //------------------------------------- get and set tstmp
    public String getTstmp() {
        return tstmp;
    }

    public void setTstmp(String tstmp) {
        this.tstmp = tstmp;
    }

    //------------------------------------- get and set lng
    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    //------------------------------------- get and set lat
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    //------------------------------------- get and set sharj
    public String getSharj() {
        return sharj;
    }

    public void setSharj(String sharj) {
        this.sharj = sharj;
    }

    //------------------------------------- get and set person_id
    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    //------------------------------------- get and set car_name
    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    //-------------------------------------- get and set car_code
    public String getCar_code() {
        return car_code;
    }

    public void setCar_code(String car_code) {
        this.car_code = car_code;
    }

    //-------------------------------------- get and set id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //------------------------------------ get and set speed
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    //---------------------------------- get and set time
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //---------------------------------- get and set date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //---------------------------------- get and set start
    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

}
