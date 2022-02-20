package com.thimat.sockettelkarnet;

import android.content.Context;

import com.thimat.sockettelkarnet.classes.SessionManager;

/**
 * Created by motahreh on 9/30/2015.
 */
public class returntype {
    Context context;
    SessionManager sessionManager;

    public String returntype(String message, Context context) {
        this.context = context;
        sessionManager = new SessionManager(context);
        if (message.contains("اعتبار") && message.contains("دستگاه")) {
            return "گزارش گیری";
        } else if (message.contains("اعتبار")) {
            return "شماره سیم کارت";
        } else if (message.contains("تلفن کاربرمجاز")) {
            return "کاربرمجاز";
        } else if (message.contains("رله قطع شد")) {
            return "قطع رله";
        } else if (message.contains("رله وصل شد")) {
            return "وصل رله";
        } else if (message.contains("آژیر دزدگیر به صدا درآمد")) {
            return "آژیر";
        } else if (message.contains("speed max") && message.contains("device:") == false) {
            return "اخطار سرعت";
        } else if (message.contains("خودرو روشن شد")) {
            return "استارت روشن";
        } else if (message.contains("you are master number") && !message.toLowerCase().contains("use")) {
            return "پیام خوش آمدگویی";
        } else if (message.toLowerCase().contains("https://maps.google.com/maps?q=")
                && message.contains("last position") == false
                && message.toLowerCase().contains("use")) {
            String[] arr = message.split("\n");
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].contains("use")) {
                    String[] user = arr[i].split(":");
                    sessionManager.setUser(user[1].trim());
                } else if (arr[i].contains("Pas")) {
                    String[] pass = arr[i].split(":");
                    sessionManager.setpassword(pass[1].trim());
                }
            }
            return context.getString(R.string.Current_location);
        } else if (message.toLowerCase().contains("last position") && message.toLowerCase().contains("use")) {
            String[] arr = message.split("\n");
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].contains("use")) {
                    String[] user = arr[i].split(":");
                    sessionManager.setUser(user[1].trim());
                } else if (arr[i].contains("Pas")) {
                    String[] pass = arr[i].split(":");
                    sessionManager.setpassword(pass[1].trim());
                }
            }
            return context.getString(R.string.Last_Location);

        } else if (message.contains("https://maps.google.com/maps?q=")
                && message.contains("موقعیت")) {
            return context.getString(R.string.Current_location);
        } else if (message.contains("https://maps.google.com/maps?q=")
                && message.contains("ماهواره قطع")) {
            return "آخرین موقعیت";
        } else if (message.contains("خودرو خاموش شد")) {
            return "استارت خاموش";
        } else if (message.contains("اینترنت خاموش شد")) {
            return "اینترنت خاموش";
        } else if (message.contains("پرمصرف فعال شد")) {
            return "اینترنت فعال";
        } else if (message.contains("use")) {
            String[] arr = message.split("\n");
            String sn = "";
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].contains("use")) {
                    String[] user = arr[i].split(":");
                    sessionManager.setUser(user[1].trim());
                } else if (arr[i].contains("Pas")) {
                    String[] pass = arr[i].split(":");
                    sessionManager.setpassword(pass[1].trim());
                } else if (arr[i].contains("SN")) {
                    String[] snstring = arr[i].split(":");
                    sn = snstring[1].trim();
                }
            }

            return context.getString(R.string.txt_user);
        } else if (message.contains("مصرف بهینه فعال شد")) {
            return "اینترنت بهینه";
        } else if (message.contains("درب خودرو باز شد")) {
            return "درب";
        } else if (message.contains("باتری خودرو وصل شد")) {
            return "وصل باتری";
        } else if (message.contains("دستگاه به تنظیمات کارخانه برگشت")) {
            return "تنظیم کارخانه";
        } else if (message.contains("دستگاه فعال شد")) {
            return "فعال";
        } else if (message.contains("دستگاه غیر فعال شد")) {
            return "غیرفعال";
        } else if (message.contains("باتری خودرو قطع شد")) {
            return "قطع باتری";
        } else if (message.contains("به خودرو ضربه وارد شد")) {
            return "ضربه";
        } else if (message.contains("هشدار سرعت غیر مجاز")) {
            return "اخطار سرعت";
        } else if (message.contains("هشدار جابجایی با یدک کش")) {
            return "حمل با چرثقیل";
        } else if (message.contains("پسورد")) {
            return "پسورد";
        }
        return "سایر";

    }
}
