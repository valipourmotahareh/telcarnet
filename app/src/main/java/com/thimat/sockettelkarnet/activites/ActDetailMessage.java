package com.thimat.sockettelkarnet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActDetailMessage extends Activity {
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.name_car)
    TextView name_car;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.from)
    TextView from;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.offline)
    TextView offline;
    @BindView(R.id.online)
    TextView online;
    double lat;
    double lon;
    Context context;
    LocalDB localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_message);
        ButterKnife.bind(this);
        context = getApplicationContext();
        localDB = new LocalDB(context);
        offline.setVisibility(View.INVISIBLE);
        online.setVisibility(View.INVISIBLE);
        type.setText(getIntent().getExtras().getString("type"));
        message.setText(getIntent().getExtras().getString("message"));
        String sms = getIntent().getExtras().getString("message");
        from.setText(getIntent().getExtras().getString("from"));
        String phonenumber = getIntent().getExtras().getString("from");
        date.setText(getIntent().getExtras().getString("date"));
        time.setText(getIntent().getStringExtra("time"));
        name_car.setText(localDB.CarName(phonenumber));
        Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,12}\\,\\d{1,3}\\.\\d{1,12}");
        Matcher match = pattern.matcher(sms);
        String latlon = "";
        if (match.find()) {
            latlon = match.group();
            offline.setVisibility(View.VISIBLE);
            online.setVisibility(View.VISIBLE);
            Log.d("test", latlon);
            String[] result = latlon.split(",");
            lat = Double.parseDouble(result[0]);
            lon = Double.parseDouble(result[1]);
        }
        //////////////////////////////////offlinebutton
        offline.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT > 27) {
                Intent intentmapactivity = new Intent(context, MapViewActivity.class);
                intentmapactivity.putExtra("lat", lat);
                intentmapactivity.putExtra("lon", lon);
                intentmapactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentmapactivity);
            }else{
                Intent intentmapactivity = new Intent(context, ActMapActivity.class);
                intentmapactivity.putExtra("lat", lat);
                intentmapactivity.putExtra("lon", lon);
                intentmapactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentmapactivity);
            }
        });
        ////////////////////////////////////onlinebutton
        online.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://maps.google.com/maps?q=" + lat + "," + lon));
            startActivity(i);
        });
    }

}
