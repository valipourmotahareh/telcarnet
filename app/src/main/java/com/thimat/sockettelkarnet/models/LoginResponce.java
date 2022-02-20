package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Valipour.Motahareh on 2/10/2018.
 */

public class LoginResponce {
    @SerializedName("ErrorStatus")
    @Expose
    private int errorStatus = 0;

    @SerializedName("Message")
    @Expose
    private String message = "";
    @SerializedName("UserID")
    @Expose
    int userid = 0;
    @SerializedName("EmployeeID")
    @Expose
    int employeeid = 0;

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

    //------------------------------------- get and set userid
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    //------------------------------------- get and set employeeid
    public int getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(int employeeid) {
        this.employeeid = employeeid;
    }
}
