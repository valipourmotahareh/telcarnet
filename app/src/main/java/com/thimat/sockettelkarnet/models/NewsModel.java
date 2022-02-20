package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NewsModel extends RealmObject {
    @SerializedName("id")
    @PrimaryKey
    @Expose
    int id = 0;
    //----------------------
    @SerializedName("title")
    @Expose
    String title = "";
    //----------------------
    @SerializedName("text")
    @Expose
    String text = "";
    //----------------------
    @SerializedName("priority")
    @Expose
    int priority = 0;
    //-----------------------
    @SerializedName("date")
    @Expose
    String date = "";

    //----------------------------------------------- get and set date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //------------------------------------------------ get and set priority
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    //------------------------------------------------- get and set text
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    //------------------------------------------------- get and set title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //-------------------------------------------------- get and set id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
