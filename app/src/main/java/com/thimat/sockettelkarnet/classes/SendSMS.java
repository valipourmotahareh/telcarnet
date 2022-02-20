package com.thimat.sockettelkarnet.classes;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;

import androidx.core.app.ActivityCompat;

import com.thimat.sockettelkarnet.R;

public class SendSMS {
    Context context;
    SessionManager sessionManager;
    ShowDialog showDialog;


    public SendSMS(Context context) {
        this.context = context;
        sessionManager = new SessionManager(context);
        showDialog = new ShowDialog(context);



    }

    //-----------------------------------------------sendsmd
    public void sendsms(String text, String phonenumber) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.SEND_SMS}, 1);

            }
            if (!sessionManager.getPhonenumbr().equals("")) {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phonenumber, null, text, null, null);
                if (!text.equals("*rebot")) {
                    sended();
                }
            }else {
                showDialog.ShowDialog(context.getString(R.string.dialog_no_phonenumber));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //------------------------------------------------sended
    public void sended() {
        try {
            final AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle("ارسال ");
            dlg.setMessage("پیام ارسال شد!");
            dlg.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
            dlg.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            showerror();
        }

    }
    //----------------------------------------------------------- show error
    public void showerror() {
        try {
            final AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle("خطا ");
            dlg.setMessage("ارسال پیام با خطا مواجه شد!لطفا مجوز ارسال پیام را به برنامه بدهید!");
            dlg.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
            dlg.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
