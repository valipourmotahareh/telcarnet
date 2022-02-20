package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class GuideModel extends RealmObject {
    @SerializedName("id")
    @Expose
    int id = 0;
    //---------------
    @SerializedName("model")
    @Expose
    String model = "";
    //-------------------
    @SerializedName("title")
    @Expose
    String title = "";

    //------------------------------------- get and set title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //------------------------------------- get and set model
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    //------------------------------------- get and set id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
