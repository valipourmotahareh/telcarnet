package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListguideModel {
    @SerializedName("guide")
    @Expose
    ArrayList<GuideModel> guideModels = new ArrayList<GuideModel>();

    //------------------------------------------ get and set guideModels-------------------
    public ArrayList<GuideModel> getGuideModels() {
        return guideModels;
    }

    public void setGuideModels(ArrayList<GuideModel> guideModels) {
        this.guideModels = guideModels;
    }
}
