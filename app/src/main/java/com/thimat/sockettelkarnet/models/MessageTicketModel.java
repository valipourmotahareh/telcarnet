package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MessageTicketModel extends RealmObject {
    //---------------------------------
    @SerializedName("id")
    @Expose
    @PrimaryKey
    int id = 0;
    //----------------------------------
    @SerializedName("ticket_id")
    @Expose
    int ticket_id = 0;
    //-----------------------------------
    @SerializedName("reference_id")
    @Expose
    long reference_id = 0;
    //-------------------------------
    @SerializedName("dep_id")
    @Expose
    int dep_id = 0;
    //---------------------------------
    @SerializedName("message")
    @Expose
    String message = "";
    //---------------------------------
    @SerializedName("modir_id")
    @Expose
    int modir_id = 0;
    //-----------------------------------
    @SerializedName("person_id")
    @Expose
    int person_id = 0;
    //------------------------------------
    @SerializedName("tstmp")
    @Expose
    String tstmp = "";

    //------------------------------------------ get and set ticket_id
    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    //------------------------------------------ get and set tstmp
    public String getTstmp() {
        return tstmp;
    }

    public void setTstmp(String tstmp) {
        this.tstmp = tstmp;
    }

    //------------------------------------------ get and set person_id
    public int getPerson_id() {
        return person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    //------------------------------------------ get and set modir_id
    public int getModir_id() {
        return modir_id;
    }

    public void setModir_id(int modir_id) {
        this.modir_id = modir_id;
    }

    //------------------------------------------ get and set message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //------------------------------------------ get and set id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //------------------------------------------ get and set dep_id
    public int getDep_id() {
        return dep_id;
    }

    public void setDep_id(int dep_id) {
        this.dep_id = dep_id;
    }

    //------------------------------------------- get and set reference_id
    public long getReference_id() {
        return reference_id;
    }

    public void setReference_id(long reference_id) {
        this.reference_id = reference_id;
    }
}
