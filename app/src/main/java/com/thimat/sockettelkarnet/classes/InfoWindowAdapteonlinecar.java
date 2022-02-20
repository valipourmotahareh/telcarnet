package com.thimat.sockettelkarnet.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.carOnlineModel;
import com.thimat.sockettelkarnet.R;

public class InfoWindowAdapteonlinecar implements GoogleMap.InfoWindowAdapter {
    private final Context context;
    LocalDB localDB;

    public InfoWindowAdapteonlinecar(Context context) {
        this.context = context.getApplicationContext();
        localDB=new LocalDB(context);
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.map_marker_info_window, null);
        TextView tvLat = (TextView) v.findViewById(R.id.tvLat);
        TextView car_name=(TextView) v.findViewById(R.id.car_name);
        TextView car_date=(TextView) v.findViewById(R.id.car_date);
        TextView speed = (TextView) v.findViewById(R.id.speed);
        try {
            carOnlineModel carModel=localDB.Getcar_onlineModel(Integer.parseInt(arg0.getSnippet()));
            if (!carModel.getCar_name().equals("")){
                car_name.setText("نام خودرو:"+carModel.getCar_name());
            }else {
                car_name.setText("نام خودرو:"+carModel.getId());
            }

            car_date.setText("تاریخ:"+carModel.getTstmp());
            tvLat.setText("موقعیت جغرافیایی:" +carModel.getLat()+","+carModel.getLng());
            speed.setText("سرعت:"+carModel.getSpeed());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return v;
    }

}