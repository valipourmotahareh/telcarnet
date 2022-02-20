package com.thimat.sockettelkarnet.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.R;

public class InfoWindowAdapteoffline implements GoogleMap.InfoWindowAdapter {
    private final Context context;
    LocalDB localDB;

    public InfoWindowAdapteoffline(Context context) {
        this.context = context.getApplicationContext();
        localDB = new LocalDB(context);
    }
    @Override
    public View getInfoWindow(Marker arg0) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.map_marker_info_offline, null);
        TextView speed = (TextView) v.findViewById(R.id.speed);
        TextView car_name = (TextView) v.findViewById(R.id.car_name);
        TextView car_date = (TextView) v.findViewById(R.id.car_date);
        try {
            String info = arg0.getSnippet();
            String[] arr = info.split(";");
            if (arr[0].equals("")) {
                car_name.setText(arr[0]);
            } else {
                car_name.setText("زمان شروع:" + arr[0]);
            }
            car_date.setText("زمان خاتمه:" + arr[1]);
            speed.setText("توقف:" + arr[2]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        return null;
    }
}