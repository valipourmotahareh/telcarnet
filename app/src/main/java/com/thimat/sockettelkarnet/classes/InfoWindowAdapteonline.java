package com.thimat.sockettelkarnet.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Valipour.Motahareh on 7/5/2018.
 */

public class InfoWindowAdapteonline implements GoogleMap.InfoWindowAdapter {
    private final Context context;
    private final LocalDB localDB;
    private long elapsedDays;
    private long elapsedHours;
    private long elapsedMinutes;
    private long elapsedSeconds;
    private String car_Name = "خودرو";
    private final int car_Id;
    private String car_Tstmp = "";
    private final double lat;
    private final double lng;
    private int car_Speed = 0;
    private int start = 0;
    private boolean getSocket = false;

    public InfoWindowAdapteonline(Context context, String car_name, int id, String car_Tstmp, double lat, double lng, int speed,
                                  int start, boolean getSocket) {
        this.context = context.getApplicationContext();
        localDB = new LocalDB(context);
        this.car_Name = car_name;
        this.car_Id = id;
        this.car_Tstmp = car_Tstmp;
        this.lat = lat;
        this.lng = lng;
        this.car_Speed = speed;
        this.start = start;
        this.getSocket = getSocket;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.map_marker_info_window, null);
        TextView tvLat = (TextView) v.findViewById(R.id.tvLat);
        TextView car_name = (TextView) v.findViewById(R.id.car_name);
        TextView car_date = (TextView) v.findViewById(R.id.car_date);
        TextView speed = (TextView) v.findViewById(R.id.speed);
        TextView status_car = (TextView) v.findViewById(R.id.status_car);
        TextView status_start = (TextView) v.findViewById(R.id.status_start);
        try {
            if (!car_Name.equals("")) {
                car_name.setText("نام خودرو:" + car_Name);
            } else {
                car_name.setText("نام خودرو:" + car_Id);
            }

            car_date.setText("تاریخ:" + car_Tstmp);
            tvLat.setText("موقعیت جغرافیایی:" + lat + "," + lng);
            speed.setText("سرعت:" + car_Speed);
            if (start == 0) {
                status_start.setText("استارت:خاموش");
            } else if (start == 1) {
                status_start.setText("استارت:روشن");
            }

            //--------------------------------------------------------
            try {
                if (!getSocket) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date currentTime = new Date();
                Date date1 = simpleDateFormat.parse(car_Tstmp);
                Date date2 = simpleDateFormat.parse(Jalali.getEnglishFormattedDate(currentTime));
                printDifference(date1, date2);

                    if (elapsedDays == 0 & elapsedHours == 0) {
                        if (elapsedMinutes < 5) {
                            if (car_Speed > 0) {
                                status_car.setText("وضعیت: " + "در حال حرکت(" + car_Speed + "کیلومتر)");
                            } else {
                                status_car.setText("وضعیت: " + "وصل");
                            }
                        } else {
                            status_car.setText("وضعیت: " + "آفلاین(" + elapsedMinutes + "دقیقه)");
                        }
                    } else {
                        status_car.setText("وضعیت: " + "آفلاین(" + elapsedDays + "روز" + " " + elapsedHours + "ساعت" + " " + elapsedMinutes + "دقیقه)");
                    }
                } else {
                    status_car.setText("وضعیت:آنلاین");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return v;
    }

    //----------------------------------------------------------------
    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        elapsedDays = Math.round(different / daysInMilli);
        different = different % daysInMilli;

        elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return elapsedMinutes;
    }
}