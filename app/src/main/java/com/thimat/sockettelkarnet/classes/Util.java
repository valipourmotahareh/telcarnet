package com.thimat.sockettelkarnet.classes;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.thimat.sockettelkarnet.activites.ActMainActivity;
import com.thimat.sockettelkarnet.myUtils.CTypeFace;
import com.thimat.sockettelkarnet.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;


public class Util {
    private static final String TAG = "***Util***";
    public static final String CatImageURL = "/order/image/Cat/";
    public static final String ProductImageURL = "/order/image/Product/";
    public static final String BrandImageURL = "/order/image/Brand/";
    public static final String seprator = ",";
    public static final String pathJavaERP_relative = "/order";
    private static final int NOTIFICATION_ID=237;
    public static final int sendAutomatic=2;
    public static final int sendASMS=1;
    public static final int sendSocket=0;

    public static String getUniqueID(Context myContext) {
        // return getWifiID(myContext); //if WIFI off return null
        return getUniquePsuedoID();
    }

    public static String generateManualCode(Context myContext) {
//        String re = getDeviceID(myContext) + System.currentTimeMillis();
        String re = String.valueOf((int) ((new Random().nextInt(10)) + System.currentTimeMillis()) / 2);

        CTypeFace.logOBJ(TAG, "ManualCode : " + re);

        return re;
    }

    private static String getDeviceID(Context myContext) { // <uses-permission
        // android:name="android.permission.READ_PHONE_STATE"
        // />
        return Secure.getString(myContext.getContentResolver(),
                Secure.ANDROID_ID);
    }

    private static String getWifiID(Context myContext) { // Requires permission
        // android.permission.ACCESS_WIFI_STATE
        // in the manifest.
        WifiManager wm = (WifiManager) myContext
                .getSystemService(Context.WIFI_SERVICE);
        return wm.getConnectionInfo().getMacAddress();
    }

    /**
     * Return pseudo unique ID
     *
     * @return ID
     */
    private static String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10)
                + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10)
                + (Build.DEVICE.length() % 10)
                + (Build.MANUFACTURER.length() % 10)
                + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their phone, there will be a
        // duplicate entry
        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null)
                    .toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode())
                    .toString();
        } catch (Exception e) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to
        // create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode())
                .toString();
    }

    private static String getThelephonyID(Context myContext) {
        final TelephonyManager tm = (TelephonyManager) myContext
                .getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + Secure.getString(
                myContext.getContentResolver(),
                Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    //
    public static void setFonts(Activity inAct, View inView) {
        ViewGroup vg = null;
        try {
            vg = (ViewGroup) inView;
            Typeface tf = Typeface.createFromAsset(inAct.getAssets(),
                    "fonts/yekan.ttf");
            // Typeface tf =
            // Typeface.createFromAsset(inAct.getAssets(),"fonts/BRoya.ttf");

            if (vg != null) {
                for (int i = 0; i < vg.getChildCount(); i++) {
                    try {
                        try {
                            if (((ViewGroup) vg.getChildAt(i)).getChildCount() != 0)
                                setFonts(inAct, ((ViewGroup) vg.getChildAt(i)));
                        } catch (Exception evg) {
                            // if (mChild instanceof ViewGroup)
                            try {
                                TextView v = (TextView) vg.getChildAt(i);
                                v.setTypeface(tf);
                            } catch (Exception ev) {
                                try {
                                    Button btn = (Button) vg.getChildAt(i);
                                    btn.setTypeface(tf);
                                } catch (Exception eb) {
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("setFonts cast TextView", e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            Log.e("find ViewGroup", e.getMessage());
        }
    }

    public static long DBVal(String inStrValue) {
        try {
            return Long.parseLong(inStrValue);
        } catch (Exception e) {
            return -1;
        }
    }

    public static int DBValInt(String inStrValue) {
        try {
            return Integer.parseInt(inStrValue);
        } catch (Exception e) {
            return -1;
        }
    }

    public static double DBDouble(String inStrValue) {
        try {
            return Double.parseDouble(inStrValue);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean DBBool(String inStrValue) {
        try {
            return Boolean.parseBoolean(inStrValue);
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean checkInternet(Context myContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) myContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        return info != null;
    }






    static public String SepValue(String S1) {
        int n, i;
        String T, S;
        String u;
        String p;
        String Seperator = seprator;
        S1 = S1.replace(Seperator, "");
        u = S1;
        i = u.indexOf(".");
        if (i >= 0) {
            u = u.substring(i + 1, u.length() - i - 1);
            p = S1;
            S = p.substring(0, i - 1);
            S = S.trim();
            T = "";
            n = S.length();
            i = n;
        } else {
            u = "";
            p = S1.trim();
            S = p;
            T = "";
            n = S.length();
            i = n;
        }
        if (S != "0") {
            while (i > 3) {
                // T = ""+Seperator.concat(S.substring(i - 3, 3)).concat(T);
                T = Seperator + S.substring(i - 3, i) + T;
                i = i - 3;
            }
            T = S.substring(0, i) + T;
            if (u != "")
                T = T + "." + u;
        } else {
            if (u != "")
                T = "0." + u;
            else
                T = "0";
        }

        return T;
    }
    //------------------------------------------------------------ compare two date
    public int getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }
        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);
        int minute=eCal.get(Calendar.MINUTE);
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return minute;
    }
    //----------------------------------------------------------------- notification
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void ShowNotification(Context context, String title, String name_car){
        NotificationManager nManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(name_car);
        builder.setSmallIcon(R.drawable.ic_launcher);
        //builder.setLargeIcon(bitmap);
        builder.setAutoCancel(true);
        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        inboxStyle.setBigContentTitle("Enter Content Text");
        inboxStyle.addLine("hi events ");
        builder.setStyle(inboxStyle);
        nManager.notify("App Name",NOTIFICATION_ID,builder.build());
    }
    //-----------------------------------------------------------------
    public static void sendNotification(Context context, String title, String name_car) {
        SessionManager sessionManager=new SessionManager(context);
        String CHANNEL_ID = "channel_telkarnet";
        if (name_car.equals("")){
            name_car="بدون نام";
        }
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(context, ActMainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(ActMainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);
        int RQS_1 = (int) System.currentTimeMillis();
        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                PendingIntent.getActivity(context,RQS_1,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getPackageName() + "/" + R.raw.beep);
        // Define the notification settings.
        builder.setSmallIcon(R.drawable.logo)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.logo))
                .setColor(Color.RED)
                .setContentTitle(title)
                .setContentText(name_car)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(notificationPendingIntent);

        if (sessionManager.GetSoundAlarm()) {
            builder.setSound(soundUri);

        }
        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);
        CharSequence name = "Telcarnet";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }
        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }
        // Issue the notification
        mNotificationManager.notify((int) (System.currentTimeMillis() % 10000), builder.build());
    }
    //-------------------------------------------------------
    //----------------------------------------------------------------
    public static String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays =Math.round(different / daysInMilli);
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        return elapsedDays+"d" +elapsedHours+"h"+elapsedMinutes+"m"+elapsedSeconds+"s";
    }

}