package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AgentModel extends RealmObject {
    @SerializedName("id")
    @PrimaryKey
    @Expose
    int id = 0;
    //---------------------
    @SerializedName("cod")
    @Expose
    String cod = "";
    //--------------------
    @SerializedName("name")
    @Expose
    String name = "";
    //--------------------
    @SerializedName("tel")
    @Expose
    String tel = "";
    //-------------------
    @SerializedName("mobil")
    @Expose
    String mobil = "";
    //-------------------
    @SerializedName("city")
    @Expose
    String city = "";
    //--------------------
    @SerializedName("address")
    @Expose
    String address = "";
    //---------------------
    @SerializedName("location")
    @Expose
    String location = "";
    //------------------------
    @SerializedName("cod_city")
    @Expose
    int cod_city = 0;
    //-------------------------
    @SerializedName("priority")
    @Expose
    int priority = 0;

    //---------------------------- get and set priority
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    //----------------------------get and set cod_city
    public int getCod_city() {
        return cod_city;
    }

    public void setCod_city(int cod_city) {
        this.cod_city = cod_city;
    }

    //---------------------------- get and set location
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //----------------------------- get and set address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //----------------------------- get ands set city
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    //----------------------------- get and set mobil
    public String getMobil() {
        return mobil;
    }

    public void setMobil(String mobil) {
        this.mobil = mobil;
    }

    //------------------------------ get and set tel
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    //------------------------------ get and set
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //------------------------------ get and set cod
    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    //------------------------------ get and set id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
