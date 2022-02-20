package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReadTicketModel {
    @SerializedName("subject")
    @Expose
    List<subjectModel> subjectModels = new ArrayList<subjectModel>();
    //-----------------------------
    @SerializedName("message")
    @Expose
    List<MessageTicketModel> readTickets = new ArrayList<MessageTicketModel>();

    //----------------------------------------------- get and set subjectModel
    public List<subjectModel> getSubjectModels() {
        return subjectModels;
    }

    public void setSubjectModels(List<subjectModel> subjectModels) {
        this.subjectModels = subjectModels;
    }

    //------------------------------------------- get and set readTickets
    public List<MessageTicketModel> getReadTickets() {
        return readTickets;
    }

    public void setReadTickets(List<MessageTicketModel> readTickets) {
        this.readTickets = readTickets;
    }
}
