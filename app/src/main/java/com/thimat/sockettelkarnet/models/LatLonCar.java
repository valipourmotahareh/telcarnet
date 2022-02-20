package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Valipour.Motahareh on 2/14/2018.
 */

public class LatLonCar extends RealmObject {
    @SerializedName("ID")
    @Expose
    @PrimaryKey
    int ID = 0;
    @SerializedName("EmployeeID")
    @Expose
    int EmployeeID = 0;
    @SerializedName("Lat")
    @Expose
    double Lat = 0.0;
    @SerializedName("Lng")
    @Expose
    double Lng = 0;
    @SerializedName("CreateDate")
    @Expose
    String CreateDate = "";
    @SerializedName("FarsiCreateDate")
    @Expose
    String FarsiCreateDate = "";

    //------------------------------------------------- get and set ID
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    //--------------------------------------------------- get and set EmployeeID
    public int getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(int employeeID) {
        EmployeeID = employeeID;
    }

    //------------------------------------------------- get and set Lat
    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    //------------------------------------------------ get and set Lng
    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    //------------------------------------------------ get and set CreateDate
    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    //------------------------------------------------ get and set FarsiCreateDate
    public String getFarsiCreateDate() {
        return FarsiCreateDate;
    }

    public void setFarsiCreateDate(String farsiCreateDate) {
        FarsiCreateDate = farsiCreateDate;
    }
}
