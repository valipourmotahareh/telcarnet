package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Valipour.Motahareh on 07/09/2016.
 */
public class Responce {
    @SerializedName("ErrorStatus")
    @Expose
    private int errorStatus = 0;

    @SerializedName("Message")
    @Expose
    private String message = "";

    public int getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(int errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}