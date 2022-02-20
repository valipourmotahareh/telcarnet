package com.thimat.sockettelkarnet.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShowDialog {
    Context context;
    private ProgressDialog progressDialog;
    SweetAlertDialog pDialog;
    public ShowDialog(Context context) {
        this.context=context;

    }
    //---------progress---------------
    public void displayProgress(String text) {
        try{
           pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(text);
            pDialog.setCancelable(false);
            pDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void successProgress(String text) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(text)
                    .show();
        }
    }

    public void errorProgress(String text) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(text)
                    .show();
        }

    }
    //---------------------------------------------------close dialog without error or success
    public void clossprogress(){
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
    //------------------------------------------------------ show dialog for error
    public void ShowDialog(String text)
    {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(text)
                .show();
    }
    //------------------------------------------------------ show dialog for success
    public void ShowDialogsuccess(String text)
    {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(text)
                .show();
    }
}
