package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Valipour.Motahareh on 5/23/2018.
 */

public class offline {
    //-------------------
    @SerializedName("car_offline")
    @Expose
    ArrayList<carOfflineModel> car_offlineModels = new ArrayList<carOfflineModel>();

    //------------------------------------- get and set car_offlineModels
    public ArrayList<carOfflineModel> getCar_offlineModels() {
        return car_offlineModels;
    }

    public void setCar_offlineModels(ArrayList<carOfflineModel> car_offlineModels) {
        this.car_offlineModels = car_offlineModels;
    }
}
