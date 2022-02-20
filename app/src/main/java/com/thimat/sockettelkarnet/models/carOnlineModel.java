package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Valipour.Motahareh on 5/23/2018.
 */

public class carOnlineModel extends RealmObject {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    int id = 0;
    //-----------
    @SerializedName("car_name")
    @Expose
    @Nullable
    String car_name = "خودرو";
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
    //----------
    @SerializedName("temp")
    @Expose
    String temp = "";
    //----------
    @SerializedName("battery_sharj")
    @Expose
    String battery_sharj = "";
    //----------
    @SerializedName("is_battery_connected")
    @Expose
    int is_battery_connected = 0;
    //-----------
    @SerializedName("rele")
    @Expose
    int rele = 0;
    //-----------
    @SerializedName("is_conneted_gps")
    @Expose
    int is_conneted_gps = 0;
    //------------
    @SerializedName("is_active")
    @Expose
    int is_active = 0;
    //-------------
    @SerializedName("status")
    @Expose
    String status = "";
    //-------------
    @SerializedName("door")
    @Expose
    int door = 0;
    //---------------
    @SerializedName("sharj")
    @Expose
    String sharj = "";
    //----------------
    @SerializedName("up_icon")
    @Expose
    String upicon = "";
    //-----------------
    @SerializedName("shock_sensor")
    @Expose
    int shock_sensor = 0;
    //--------------------
    @SerializedName("model_device")
    @Expose
    @Nullable
    int model_device = 0;
    //---------------------
    @SerializedName("phone_device")
    @Expose
    String phone_device = "";
    //----------------------
    @SerializedName("alarm")
    @Expose
    int alarm = 0;
    //----------------------
    @SerializedName("main_phone")
    @Expose
    String main_phone = "";
    //-----------------------
    @SerializedName("phone1")
    @Expose
    String phone1 = "";
    //-----------------------
    @SerializedName("phone2")
    @Expose
    String phone2 = "";
    //------------------------
    @SerializedName("phone3")
    @Expose
    String phone3 = "";
    //------------------------
    @SerializedName("phone4")
    @Expose
    String phone4 = "";
    //------------------------------
    @SerializedName("rezerv")
    @Expose
    int rezerv = 0;

    //----------
    @SerializedName("Pass")
    @Expose
    String Pass = "";

    //------------
    @SerializedName("check")
    @Expose
    int check = 0;
    //------------
    @SerializedName("CheckRebot")
    @Expose
    String CheckRebot = "";
    //---------------------------
    @SerializedName("Seting")
    @Expose
    String Seting = "";
    //---------------------------
    @SerializedName("Speedmax")
    @Expose
    int speedMax = 0;

    public int getSpeedMax() {
        return speedMax;
    }

    public void setSpeedMax(int speedMax) {
        this.speedMax = speedMax;
    }

    public String getSeting() {
        return Seting;
    }

    public void setSeting(String seting) {
        Seting = seting;
    }

    public String getCheckRebot() {
        return CheckRebot;
    }

    public void setCheckRebot(String checkRebot) {
        CheckRebot = checkRebot;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    //---------------------------- get and set rezerv
    public int getRezerv() {
        return rezerv;
    }

    public void setRezerv(int rezerv) {
        this.rezerv = rezerv;
    }

    //---------------------------- get and set phone4
    public String getPhone4() {
        return phone4;
    }

    public void setPhone4(String phone4) {
        this.phone4 = phone4;
    }

    //----------------------------- get and set phone3
    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    //----------------------------- get and set phone2
    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    //----------------------------- get and set phone1
    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    //----------------------------- get and set main_phone
    public String getMain_phone() {
        return main_phone;
    }

    public void setMain_phone(String main_phone) {
        this.main_phone = main_phone;
    }

    //----------------------------- get and set alarm
    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    //----------------------------- get and set phone_device
    public String getPhone_device() {
        return phone_device;
    }

    public void setPhone_device(String phone_device) {
        this.phone_device = phone_device;
    }

    //----------------------------- get and set model_device
    public int getModel_device() {
        return model_device;
    }

    public void setModel_device(int model_device) {
        this.model_device = model_device;
    }

    //----------------------------- get and set shock_sensor
    public int getShock_sensor() {
        return shock_sensor;
    }

    public void setShock_sensor(int shock_sensor) {
        this.shock_sensor = shock_sensor;
    }

    //----------------------------- get and set upicon
    public String getUpicon() {
        return upicon;
    }

    public void setUpicon(String upicon) {
        this.upicon = upicon;
    }

    //----------------------------- get and set sharj
    public String getSharj() {
        return sharj;
    }

    public void setSharj(String sharj) {
        this.sharj = sharj;
    }

    //------------------------------ get and set door
    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    //------------------------------ get and set status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //------------------------------ get and set is_active
    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    //------------------------------ get and set is_conneted_gps
    public int getIs_conneted_gps() {
        return is_conneted_gps;
    }

    public void setIs_conneted_gps(int is_conneted_gps) {
        this.is_conneted_gps = is_conneted_gps;
    }

    //------------------------------- get and set rele
    public int getRele() {
        return rele;
    }

    public void setRele(int rele) {
        this.rele = rele;
    }

    //------------------------------- get and set is_battery_connected
    public int getIs_battery_connected() {
        return is_battery_connected;
    }

    public void setIs_battery_connected(int is_battery_connected) {
        this.is_battery_connected = is_battery_connected;
    }

    //-------------------------------- get and set battery_sharj
    public String getBattery_sharj() {
        return battery_sharj;
    }

    public void setBattery_sharj(String battery_sharj) {
        this.battery_sharj = battery_sharj;
    }

    //-------------------------------- get and set temp
    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    //-------------------------------- get and set car_name
    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

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

    //------------------------------------ get and set pass
    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }
}
