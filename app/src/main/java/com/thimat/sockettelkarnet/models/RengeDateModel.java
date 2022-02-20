package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class RengeDateModel extends RealmObject {
    @SerializedName("ID")
    @Expose
    private int ID=0;
    @SerializedName("Title")
    @Expose
    private String Title="";

    //-----------------------------------------------get and set ID
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    //------------------------------------------------ get and set Title

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
