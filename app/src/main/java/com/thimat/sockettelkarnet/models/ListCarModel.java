package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valipour.Motahareh on 5/20/2018.
 */

public class ListCarModel {
    @SerializedName("car_act")
    @Expose
    List<CarModel> carModels = new ArrayList<CarModel>();
    @SerializedName("state")
    @Expose
    String state = "";

    //------------------------------------------------------- get and set state
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    //--------------------------------------------------------- get and set carModels

    public List<CarModel> getCarModels() {
        return carModels;
    }

    public void setCarModels(List<CarModel> carModels) {
        this.carModels = carModels;
    }
}
