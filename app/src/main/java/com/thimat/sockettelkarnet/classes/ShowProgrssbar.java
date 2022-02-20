package com.thimat.sockettelkarnet.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

public class ShowProgrssbar {
    private ProgressDialog progressDialog;
    Context context;
    public ShowProgrssbar(Context context){
        this.context=context;

    }
    public void displayProgress(String text) {
        try{
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(text);
            }
            if (!progressDialog.isShowing()) {
                try{
                    progressDialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //-----------------------------------------successProgress------------
     public void successProgress() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }catch (Exception ex){
           ex.printStackTrace();
        }

    }
    //-----------------------------------------errorProgress-----------
    public void errorProgress() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }
}
