package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Valipour.Motahareh on 5/23/2018.
 */

public class online {
    //-------------------
    @SerializedName("car_online")
    @Expose
    ArrayList<carOnlineModel> car_onlineModels = new ArrayList<carOnlineModel>();

    //----------------------------------------- get and set car_onlineModels
    public ArrayList<carOnlineModel> getCar_onlineModels() {
        return car_onlineModels;
    }

    public void setCar_onlineModels(ArrayList<carOnlineModel> car_onlineModels) {
        this.car_onlineModels = car_onlineModels;
    }
}
