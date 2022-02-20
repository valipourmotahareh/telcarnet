package com.thimat.sockettelkarnet.activites;

import static com.google.android.gms.maps.model.JointType.ROUND;
import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.widgets.RouteView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.thimat.sockettelkarnet.classes.InfoWindowAdapteoffline;
import com.thimat.sockettelkarnet.classes.Jalali;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.Util;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.InfoLocation;
import com.thimat.sockettelkarnet.models.carOfflineModel;
import com.thimat.sockettelkarnet.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

/**
 * Created by Valipour.Motahareh on 5/23/2018.
 */

public class ActShowOffLinePath extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.move_path)
    ImageView move_path;
    @BindView(R.id.move_pause)
    ImageView move_pause;
    @BindView(R.id.icon_switch)
    ImageView icon_switch;
    @BindView(R.id.front_icon)
    ImageView front_icon;
    @BindView(R.id.back_icon)
    ImageView back_icon;
    @BindView(R.id.seekbarline)
    SeekBar seekbarline;
    private final EegeoMap m_eegeoMap = null;
    private final List<RouteView> m_routeViews = new ArrayList<RouteView>();
    Context context;
    LocalDB localDB;
    GoogleMap mMap;
    ArrayList<LatLng> points;
    private Marker marker;
    private Handler handler;
    private int index, next;
    private float v;
    private double lat, lng;
    private LatLng startPosition, endPosition;
    private PolylineOptions polylineOptions;
    private PolylineOptions polylineOptionsmove;
    ValueAnimator valueAnimator;
    private List<LatLng> polyLineList;
    private List<LatLng> polyLineMove;
    private List<InfoLocation> infoLocations;
    boolean run = false;
    long speedvalue = 0;
    PopupWindow popupWindow;
    SessionManager sessionManager;
    int id_car;
    float zoom = 0;
    int very_slowspeed = 3000;
    int slowspeed = 2000;
    int normalspeed = 1000;
    int fastspeed = 500;
    int very_fastspeed = 200;
    boolean ch_veryslow = false;
    boolean ch_slow = false;
    boolean ch_normal = true;
    boolean ch_fast = false;
    boolean ch_veryfast = false;
    @BindView(R.id.speed)
    TextView speed;
    @BindView(R.id.stop)
    TextView stop;
    @BindView(R.id.car_name)
    TextView car_name;
    @BindView(R.id.car_date)
    TextView car_date;
    @BindView(R.id.car_time)
    TextView car_time;
    @BindView(R.id.distance)
    TextView txt_distance;
    double totaldistance;
    MarkerOptions addMarker;
    List<Polyline> polylines = new ArrayList<Polyline>();//    Marker carposition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));
        setContentView(R.layout.act_showofflinepath);
        context = getApplicationContext();
        ButterKnife.bind(this);
        localDB = new LocalDB(context);
        sessionManager = new SessionManager(context);
        init();
        id_car = getIntent().getIntExtra("id", 0);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //-------------------------------------------- open Act_Select Date
        Intent intent = new Intent(context, ActSelectDate.class);

        intent.putExtra("id", id_car);
        startActivityForResult(intent, 1);
        //----------------------------------------------

        GetSpeed();
        polylineOptionsmove = new PolylineOptions();

        //-----------------------------------------------
        seekbarline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

               // index=i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               index=seekBar.getProgress();
                CLR_ReDraw();

            }
        });

    }
    //----------------------------------------------------------clear polyline and draw new polyline
    private void CLR_ReDraw(){
        for (Polyline line : polylines) {
            line.remove();
        }

        polylines.clear();
        polyLineMove.clear();
        polyLineMove.addAll(polyLineList.subList(0,index));
    }

    //------------------------------------------------------------
    public void GetSpeed() {
        if (sessionManager.GetSpeed_car() == 1) {
            speedvalue = very_slowspeed;
        } else if (sessionManager.GetSpeed_car() == 2) {
            speedvalue = slowspeed;
        } else if (sessionManager.GetSpeed_car() == 3) {
            speedvalue = normalspeed;
        } else if (sessionManager.GetSpeed_car() == 4) {
            speedvalue = fastspeed;
        } else if (sessionManager.GetSpeed_car() == 5) {
            speedvalue = very_fastspeed;
        }
    }

    //------------------------------------------------------------------ init
    private void init() {
        //------------Initi Toolbar---------------
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_arrow_left); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowCustomEnabled(false); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
        toolbarTitle.setText("مشاهده مسیر");
    }

    //-----------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();
        SendMessage("*ID*"+sessionManager.getUser(),"",true);

    }

    //---------------------------------------------------onPause---------------------------
    @Override
    protected void onPause() {
        super.onPause();
    }

    //--------------------------------------------------------- front_icon
    @OnClick(R.id.front_icon)
    public void front_icon(View view) {
        stop();
        if (next + 10 < polyLineList.size()) {
            for (int position = 0; position <= 10; position++) {
                index = index + 1;
                polyLineMove.add(polyLineList.get(index));
            }
        }
        start();


    }

    //--------------------------------------------------------- back_icon
    @OnClick(R.id.back_icon)
    public void back_icon(View view) {
        try {
            stop();
            if (index - 10 > 0) {
                index = index - 10;
                CLR_ReDraw();
            }
            start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //---------------------------------------------------- onDestroy----------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (m_eegeoMap != null) {
            for (RouteView routeView : m_routeViews) {
                routeView.removeFromMap();
            }
        }
    }

    //-------------------------------------------------------icon_switch------------------
    @OnClick(R.id.icon_switch)
    public void icon_switch(View view) {
        if (mMap.getMapType() == 1) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

    }

    // ------------------------------------------------------ stop button------------------
    @OnClick(R.id.move_pause)
    public void move_pause(View view) {
        stop();
        move_path.setEnabled(true);

    }

    //------------------------------------------------------- play button------------------
    @OnClick(R.id.move_path)
    public void Move_Car(View view) {
        try {
            move_path.setEnabled(false);
            if (!run) {
                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(polyLineList.get(0))
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                if (next >= polyLineList.size()) {
                    index = 0;
                    next = 1;
                }
                valueAnimator = ValueAnimator.ofFloat(0, 1);
                handler = new Handler();
                run = false;
            }
            stop();
            start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    //------------------------------------------------------onMapReady------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    //-----------------------------------------------------------------getBearing
    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    //------------------------------------------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (run) {
                stop();
                move_path.setEnabled(true);
                marker = mMap.addMarker(new MarkerOptions().position(polyLineList.get(0))
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                );

                index = 0;
                next = 1;
                run = false;
            }
            setmap();
        }
    }

    //-------------------------------------------
    public void setmap() {
        try {
            //----------------------------------------------------------
            mMap.clear();
            handler = new Handler();
            points = new ArrayList<LatLng>();
            polyLineList = new ArrayList<>();
            polyLineMove = new ArrayList<>();

            infoLocations = new ArrayList<>();
            mMap.setOnMarkerClickListener(this);
            //-------------------------------------------------------------------------------
            RealmResults<carOfflineModel> result = localDB.Getcar_offline();
            int kilometer = 0;
            Date date1 = null;
            String datestart = null;
            for (int i = 0; i < result.size(); i++) {
                LatLng location = new LatLng(result.get(i).getLat(), result.get(i).getLng());
                addMarker = new MarkerOptions().position(location).title(result.get(i).getCar_code());
                if (i == 0) {
                    polyLineList.add(location);
                    polyLineMove.add(polyLineList.get(0));

                    InfoLocation infoLocation = new InfoLocation();
                    String dtStart = result.get(i).getDate();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = format.parse(dtStart);
                    infoLocation.setDate(Jalali.getFarsiDate(date));
                    infoLocation.setSpeed(result.get(i).getSpeed());
                    infoLocation.setTime(result.get(i).getTime());
                    infoLocation.setName(localDB.GetCarsID(id_car).getCar_name());
                    infoLocation.setDistance(totaldistance);
                    infoLocations.add(infoLocation);
                    addMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.start));
                    mMap.addMarker(addMarker);
                } else {
                    if (result.get(i).getLat() != result.get(i - 1).getLat() && result.get(i).getSpeed() > 0) {
                        polyLineList.add(location);

                        Double distance=Math.floor(distance(result.get(i-1).getLat(),
                                result.get(i-1).getLng(),
                                result.get(i).getLat(),result.get(i).getLng())*100)/100;
                        totaldistance=totaldistance+distance;

                        InfoLocation infoLocation = new InfoLocation();
                        String dtStart = result.get(i).getDate();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(dtStart);
                        infoLocation.setDate(Jalali.getFarsiDate(date));
                        infoLocation.setSpeed(result.get(i).getSpeed());
                        infoLocation.setTime(result.get(i).getTime());
                        infoLocation.setName(localDB.GetCarsID(id_car).getCar_name());
                        infoLocation.setDistance(totaldistance);
                        infoLocation.setStoptime("");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        if (result.get(i).getStart() == 0 && result.get(i - 1).getStart() == 1) {
                            date1 = simpleDateFormat.parse(result.get(i).getTstmp());
                            datestart = result.get(i).getTstmp();
                        }
                        if (date1 != null) {
                            if (result.get(i).getStart() == 1 && result.get(i - 1).getStart() == 0) {
                                Date date2 = simpleDateFormat.parse(result.get(i).getTstmp());
                                String diff = Util.printDifference(date1, date2);
                                infoLocation.setStoptime(diff);
                                date1 = null;
                                //---------------------------------------------------
                                InfoWindowAdapteoffline markerInfoWindowAdapter = new InfoWindowAdapteoffline(ActShowOffLinePath.this);
                                mMap.setInfoWindowAdapter(markerInfoWindowAdapter);
                                addMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.parki));
                                addMarker.position(location);
                                addMarker.title(result.get(i).getCar_code());
                                addMarker.snippet(datestart + ";" + result.get(i).getTstmp() + ";" +
                                        diff);
                                mMap.addMarker(addMarker);
                                //------------------------------------------------------------------
                            }
                        }
                        if (i == result.size() - 1) {
                            addMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.end));
                            mMap.addMarker(addMarker);
                        }
                        infoLocations.add(infoLocation);
                    }
                }
            }
            //Adjusting bounds
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : polyLineList) {
                builder.include(latLng);
            }
            LatLngBounds bounds = builder.build();
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 10);
            mMap.animateCamera(mCameraUpdate);
            polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.BLUE);
            polylineOptions.width(7);
            polylineOptions.startCap(new SquareCap());
            polylineOptions.endCap(new SquareCap());
            polylineOptions.jointType(ROUND);
            polylineOptions.addAll(polyLineList);
            polylineOptions.clickable(true);
            mMap.addPolyline(polylineOptions);
             seekbarline.setMax(polyLineList.size());

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        if (polyLineList.size() > 0) {
            lat = polyLineList.get(0).latitude;
            lng = polyLineList.get(0).longitude;
        }

    }

    //------------------------------------------------------- menu
    //-----------------------------------------------------------------------------onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu, menu);
        return true;
    }

    //-------------------------------------------------------------------------------When Options Menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_agian:
                //-------------------------------------------- open Act_Select Date
                if (run) {
                    stop();
                    move_path.setEnabled(true);
                }
                Intent intent = new Intent(context, ActSelectDate.class);
                intent.putExtra("id", getIntent().getIntExtra("id", 0));
                startActivityForResult(intent, 1);
                //-------------------------------------------------
                return true;
            case R.id.set_speed:
                PopupWindow popupwindow_obj = popupDisplay();
                popupwindow_obj.showAtLocation(toolbar, Gravity.TOP | Gravity.LEFT, 0, toolbar.getHeight() * 2);
                return true;
            default:
                return true;
        }

    }

    //----------------------------------------------------popupDisplay
    public PopupWindow popupDisplay() {
        popupWindow = new PopupWindow(context);
        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_speed, null);
        RadioButton very_slow = (RadioButton) view.findViewById(R.id.very_slow);
        RadioButton slow = (RadioButton) view.findViewById(R.id.slow);
        RadioButton normal = (RadioButton) view.findViewById(R.id.normal);
        RadioButton fast = (RadioButton) view.findViewById(R.id.fast);
        RadioButton very_fasrt = (RadioButton) view.findViewById(R.id.very_fasrt);
        if (sessionManager.GetSpeed_car() == 1) {
            speedvalue = very_slowspeed;
            very_slow.setChecked(true);
        } else if (sessionManager.GetSpeed_car() == 2) {
            speedvalue = slowspeed;
            slow.setChecked(true);
        } else if (sessionManager.GetSpeed_car() == 3) {
            speedvalue = normalspeed;
            normal.setChecked(true);
        } else if (sessionManager.GetSpeed_car() == 4) {
            speedvalue = fastspeed;
            fast.setChecked(true);
        } else if (sessionManager.GetSpeed_car() == 5) {
            speedvalue = very_fastspeed;
            very_fasrt.setChecked(true);
        }
        //-----------------------------------------------------
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        return popupWindow;
    }

    //------------------------------------------------------------onRadioButtonClicked
    public void onRadioButtonClicked(View view) {
        stop();
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.very_slow:
                if (checked) {
                    speedvalue = very_slowspeed;
                    sessionManager.Setspeed_car(1);
                    ch_veryslow = true;
                    popupWindow.dismiss();
                    start();
                }
                break;
            case R.id.slow:
                if (checked) {
                    speedvalue = slowspeed;
                    sessionManager.Setspeed_car(2);
                    ch_slow = true;
                    popupWindow.dismiss();
                    start();
                }
                break;
            case R.id.normal:
                if (checked) {
                    speedvalue = normalspeed;
                    sessionManager.Setspeed_car(3);
                    ch_normal = true;
                    popupWindow.dismiss();
                    start();
                }
                break;
            case R.id.fast:
                if (checked) {
                    speedvalue = fastspeed;
                    sessionManager.Setspeed_car(4);
                    ch_fast = true;
                    popupWindow.dismiss();
                    start();
                }
                break;
            case R.id.very_fasrt:
                if (checked) {
                    speedvalue = very_fastspeed;
                    sessionManager.Setspeed_car(5);
                    ch_veryfast = true;
                    popupWindow.dismiss();
                    start();
                }
                break;
        }
    }

    //---------------------------------------------------------------------------------- runnable
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {

                if (Math.round(mMap.getCameraPosition().zoom) > 11) {
                    zoom = mMap.getCameraPosition().zoom;
                } else {
                    zoom = 20;
                }
                LatLng newPos = new LatLng(lat, lng);
                boolean contains = mMap.getProjection()
                        .getVisibleRegion()
                        .latLngBounds
                        .contains(newPos);
                if (!contains) {
                    // MOVE CAMERA
                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                            newPos, zoom);
                    mMap.animateCamera(location);
                }
                if (index <= polyLineList.size()) {
                    index++;
                    next = index + 1;
                    startPosition = polyLineList.get(index);
                     seekbarline.setProgress(index);
                    polyLineMove.add(startPosition);
                    if (next < polyLineList.size()) {
                        endPosition = polyLineList.get(next);
                    }

                    if (next == polyLineList.size()) {
                        stop();
                        move_path.setEnabled(true);
                        run = false;
                        return;
                    }
                } else {
                    stop();
                    move_path.setEnabled(true);
                    run = false;
                    return;
                }
                valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        v = valueAnimator.getAnimatedFraction();
                        lng = v * endPosition.longitude + (1 - v)
                                * startPosition.longitude;
                        lat = v * endPosition.latitude + (1 - v)
                                * startPosition.latitude;
                        LatLng newPos = new LatLng(lat, lng);
                        //---------------------
                        marker.setPosition(newPos);
                        marker.setAnchor(0.5f, 0.5f);
                        marker.setRotation(getBearing(startPosition, newPos));

                    }
                });

                valueAnimator.start();
                handler.postDelayed(this, speedvalue);
                //------------------------------------------------------
                try {
                    if (infoLocations.get(index).getName().equals("")) {
                        car_name.setText("بدون نام");
                    } else {
                        car_name.setText("نام خودرو:" + infoLocations.get(index).getName());
                    }
                    car_date.setText("تاریخ:" + infoLocations.get(index).getDate());
                    speed.setText("سرعت:" + infoLocations.get(index).getSpeed());
                    car_time.setText("ساعت:" + infoLocations.get(index).getTime());
                    txt_distance.setText(
                            getString(R.string.txt_distance)+
                                    new DecimalFormat("##.##").
                                            format((infoLocations.get(index).getDistance())));
                    if (!infoLocations.get(index).getStoptime().equals("")) {
                        stop.setText("توقف:" + "  " + infoLocations.get(index).getStoptime());
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //-----------------------------------------
                PolylineOptions polylineOptionsmove = new PolylineOptions();
                polylineOptionsmove.color(Color.GREEN);
                polylineOptionsmove.width(10);
                polylineOptionsmove.addAll(polyLineMove);
                polylineOptionsmove.startCap(new SquareCap());
                polylineOptionsmove.endCap(new SquareCap());
                polylineOptionsmove.jointType(ROUND);
                polylineOptionsmove.clickable(true);
                polylines.add(mMap.addPolyline(polylineOptionsmove));
                //----------------------------------------------

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    //--------------------------------------------------------------- distance
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    //---------------------------------------------------------------stop
    public void stop() {
        handler.removeCallbacks(runnable);
    }

    //---------------------------------------------------------------------start
    public void start() {
        if (!run) {
            GetSpeed();
        }
        handler.postDelayed(runnable, 0);
    }

    //-------------------------------------------------------------- onMarkerClick
    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }
}