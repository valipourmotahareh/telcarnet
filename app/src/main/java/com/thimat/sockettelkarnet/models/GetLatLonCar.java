package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Valipour.Motahareh on 2/14/2018.
 */

public class GetLatLonCar {
    @SerializedName("PathPromotersLog")
    @Expose
    ArrayList<LatLonCar> getLatLonCars = new ArrayList<LatLonCar>();

    //-------------------------------------------------------------- get and set
    public ArrayList<LatLonCar> getGetLatLonCars() {
        return getLatLonCars;
    }

    public void setGetLatLonCars(ArrayList<LatLonCar> getLatLonCars) {
        this.getLatLonCars = getLatLonCars;
    }
}
