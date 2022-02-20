package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class contactListModel {
    @SerializedName("contact")
    @Expose
    List<contactModel> contactModels = new ArrayList<contactModel>();

    //----------------------------------------------------------------- get and set contact
    public List<contactModel> getContactModels() {
        return contactModels;
    }

    public void setContactModels(List<contactModel> contactModels) {
        this.contactModels = contactModels;
    }
}
