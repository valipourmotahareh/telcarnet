package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class subjectModel extends RealmObject {
    //---------------------------------
    @SerializedName("id")
    @Expose
    @PrimaryKey
    int id = 0;
    //-----------------------------------
    @SerializedName("reference_id")
    @Expose
    long reference_id = 0;
    //------------------------------
    @SerializedName("subject")
    @Expose
    String subject = "";
    //-------------------------------
    @SerializedName("dep_id")
    @Expose
    int dep_id = 0;
    //-------------------------------
    @SerializedName("priority")
    @Expose
    int priority = 0;
    //--------------------------------
    @SerializedName("status")
    @Expose
    int status = 0;
    //-----------------------------------
    @SerializedName("person_id")
    @Expose
    int person_id = 0;
    //------------------------------------
    @SerializedName("tstmp")
    @Expose
    String tstmp = "";
    //-------------------------------------
    @SerializedName("details")
    @Expose
    String details = "";

    //------------------------------------------ get and set details
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    //------------------------------------------ get and set id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //------------------------------------------ get and set status
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    //------------------------------------------ get and set priority
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    //------------------------------------------ get and set dep_id
    public int getDep_id() {
        return dep_id;
    }

    public void setDep_id(int dep_id) {
        this.dep_id = dep_id;
    }

    //------------------------------------------ get and set subject
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    //------------------------------------------- get and set reference_id
    public long getReference_id() {
        return reference_id;
    }

    public void setReference_id(long reference_id) {
        this.reference_id = reference_id;
    }
}
