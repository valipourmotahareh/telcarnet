package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WarningModel extends RealmObject {
    //--------------------------------- id warning
    @SerializedName("id")
    @PrimaryKey
    @Expose
    int id = 0;
    //--------------------
    @SerializedName("idcar")
    @Expose
    int idcar = 0;
    //---------------------
    @SerializedName("car_name")
    @Expose
    String car_name = "خودرو";
    //----------------------
    @SerializedName("date")
    @Expose
    String date = "";
    //--------------------- car_code and SN(from sms)
    @SerializedName("car_code")
    @Expose
    String car_code = "";
    //--------------------- warning
    @SerializedName("txtwarning")
    @Expose
    String txtwarning = "";
    //--------------------
    int sync = 0;

    //-------------------------------------- get and set txtwarning
    public String getTxtwarning() {
        return txtwarning;
    }

    public void setTxtwarning(String txtwarning) {
        this.txtwarning = txtwarning;
    }

    //-------------------------------------- get and set car_code
    public String getCar_code() {
        return car_code;
    }

    public void setCar_code(String car_code) {
        this.car_code = car_code;
    }

    //------------------------------------- get and set sync
    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    //------------------------------------- get and set car_name
    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    //-------------------------------------- get and set id
    public int getIdcar() {
        return idcar;
    }

    public void setIdcar(int idcar) {
        this.idcar = idcar;
    }

    //---------------------------------- get and set date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    //----------------------------------- get and set id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}