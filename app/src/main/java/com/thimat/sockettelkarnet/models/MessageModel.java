package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MessageModel extends RealmObject {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    int id = 0;
    //--------------------
    @SerializedName("type")
    @Expose
    String type = "";
    //---------------------
    @SerializedName("message")
    @Expose
    String message = "";
    //----------------------
    @SerializedName("sendernum")
    @Expose
    String sendernum = "";
    //------------------------
    @SerializedName("date")
    @Expose
    String date = "";
    //------------------------
    @SerializedName("Time")
    @Expose
    String Time = "";
    //-------------------------
    @SerializedName("read")
    @Expose
    boolean read;

    //------------------------------------ get and set Time
    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    //------------------------------------- get and set read
    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    //------------------------------------- get and set date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //------------------------------------- get and set sendernum
    public String getSendernum() {
        return sendernum;
    }

    public void setSendernum(String sendernum) {
        this.sendernum = sendernum;
    }

    //-------------------------------------- get and set message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //-------------------------------------- get and set id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //---------------------------------------get and set tupr
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
