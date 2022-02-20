package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class contactModel extends RealmObject {
    @SerializedName("modir")
    @Expose
    String modir = "";
    //----------------------
    @SerializedName("poshtibani")
    @Expose
    String poshtibani = "";
    //-----------------------
    @SerializedName("telgram")
    @Expose
    String telegram = "";
    //-----------------------
    @SerializedName("instagram")
    @Expose
    String instagram = "";
    //------------------------
    @SerializedName("whatsapp")
    @Expose
    String whatsapp = "";
    //--------------------------
    @SerializedName("sait")
    @Expose
    String sait = "";
    //----------------------------
    @SerializedName("address")
    @Expose
    String address = "";
    //-----------------------------
    @SerializedName("phone")
    @Expose
    String phone = "";

    //---------------------------------------- get and set phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //---------------------------------------- get and set address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //---------------------------------------- get and set sait
    public String getSait() {
        return sait;
    }

    public void setSait(String sait) {
        this.sait = sait;
    }

    //---------------------------------------- get and set whatsapp
    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    //---------------------------------------- get and set instagram
    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    //----------------------------------------- get and set telegram

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    //----------------------------------------- get and set poshtibani
    public String getPoshtibani() {
        return poshtibani;
    }

    public void setPoshtibani(String poshtibani) {
        this.poshtibani = poshtibani;
    }

    //------------------------------------------ get and set modir
    public String getModir() {
        return modir;
    }

    public void setModir(String modir) {
        this.modir = modir;
    }
}
