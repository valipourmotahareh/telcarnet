package com.thimat.sockettelkarnet.activites;

import static com.google.android.gms.maps.model.JointType.ROUND;
import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.gson.Gson;
import com.thimat.sockettelkarnet.classes.InfoWindowAdapteonline;
import com.thimat.sockettelkarnet.classes.InfoWindowAdapteonlinecar;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.geofencing.ActMainGeo;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.CarModel;
import com.thimat.sockettelkarnet.models.carOnlineModel;
import com.thimat.sockettelkarnet.models.online;
import com.thimat.sockettelkarnet.myUtils.CTypeFace;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;
import com.thimat.sockettelkarnet.socket.Socket_Connect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Valipour.Motahareh on 5/23/2018.
 */

public class ActShowOnLineLocation extends AppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.operation)
    TextView operation;
    @BindView(R.id.showcars)
    TextView showcars;
    @BindView(R.id.warnings)
    TextView warnings;
    @BindView(R.id.back_button)
    Button back_button;
    @BindView(R.id.custom_left_arrow)
    ImageView custom_left_arrow;
    @BindView(R.id.custom_right_arrow)
    ImageView custom_right_arrow;
    @BindView(R.id.icon_switch)
    ImageView icon_switch;
    @BindView(R.id.mTextField)
    TextView mTextField;
    @BindView(R.id.refresh)
    ImageView refresh;
    Context context;
    LocalDB localDB;
    private OperationService mOService;
    int id_car = 0;
    GoogleMap mMap;
    SessionManager sessionManager;
    List<CarModel> carModels = new ArrayList<CarModel>();
    int idposition = 0;
    CountDownTimer countDownTimer;
    private ProgressDialog progressDialog;
    private List<LatLng> polyLineList;
    private PolylineOptions polylineOptions;
    private Polyline greyPolyLine;
    float zoom = 0;
    //---------------------------------------
    Socket_Connect socket_connect;
    Handler uiHandler;
    Handler handler;
    boolean sendRegister = false;
    boolean pressKey = false;
    String sendMessage = "";
    String sendSms = "";
    ShowDialog showDialog;
    public boolean receiveSelectedCode = false;
    String nameCar="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_showonlinelocation);
        ButterKnife.bind(this);
        context = getApplicationContext();
        sessionManager = new SessionManager(context);
        showDialog = new ShowDialog(ActShowOnLineLocation.this);
        polyLineList = new ArrayList<>();
        GetIntent();
        initview();
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        localDB = new LocalDB(context);
        carModels = localDB.GetCars();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActShowOnLineLocation.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}
                    , 1);
            return;

        }

        displayProgress(getString(R.string.display_dialog_wail_location));


        GetDataOnline(id_car);

    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHandler = new UIHandler();
        handler = new Handler(Looper.getMainLooper());

        if(socket_connect!=null) socket_connect.socketDisconnect();

        socket_connect = new Socket_Connect(ActShowOnLineLocation.this, uiHandler);
        registerSocket();
    }

    //--------------------------------------------------------
    private void registerSocket() {

        SendMessage("*ID*" + sessionManager.getUser(), "", true);
        pressKey = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            try {
                id_car = data.getIntExtra("idcar", 0);
                String car_code = data.getStringExtra("codecar");
                GetDataOnline(id_car);
                sessionManager.setcar_code(car_code);

                SendMessage("*ID*" + sessionManager.getUser(), "", true);
                pressKey = false;


            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }

    }

    //------------------------------------------------------ Active car in map
    private void CarActiveResult(carOnlineModel onlineModels) {
        try {
            LatLng latLng = new LatLng(onlineModels.getLat(), onlineModels.getLng());
            InfoWindowAdapteonlinecar markerInfoWindowAdapter = new InfoWindowAdapteonlinecar(getApplicationContext());
            mMap.setInfoWindowAdapter(markerInfoWindowAdapter);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.carlocation);
            Marker carposition = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(onlineModels.getCar_name())
                    .icon(icon)
                    .snippet(String.valueOf(onlineModels.getCar_id())));
            carposition.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition
                            (new CameraPosition.Builder()
                                    .target(latLng)
                                    .zoom(mMap.getCameraPosition().zoom)
                                    .build()));
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(context, "برای این ماشین موقعیت جغرافیایی وجود ندارد!", Toast.LENGTH_LONG).show();
        }

    }

    //---------------------------------------------------------setUpMap
    private void setUpMapSocet(CarModel carModel_socket) {
        successProgress();
        try {
            if (mMap != null) {

                if (sessionManager.getCar_code().equals(carModel_socket.getCar_code())) {
                    receiveSelectedCode = true;
                    mMap.clear();
                    // Create a criteria object to retrieve provider
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    // Create a LatLng object for the current location

                    LatLng latLng = new LatLng(carModel_socket.getLat(), carModel_socket.getLng());
                    InfoWindowAdapteonline markerInfoWindowAdapter = new InfoWindowAdapteonline(ActShowOnLineLocation.this,
                            nameCar, carModel_socket.getId(), carModel_socket.getTstmp(),
                            carModel_socket.getLat(), carModel_socket.getLng(),
                            carModel_socket.getSpeed(), carModel_socket.getStart(),true);
                    mMap.setInfoWindowAdapter(markerInfoWindowAdapter);
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.carlocation);
                    Marker carposition = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(nameCar)
                            .icon(icon)
                            .snippet(String.valueOf(carModel_socket.getId())));
                    carposition.showInfoWindow();

                    if (Math.round(mMap.getCameraPosition().zoom) > 11) {
                        zoom = mMap.getCameraPosition().zoom;
                    } else {
                        zoom = 20;
                    }
                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                            latLng, zoom);
                    mMap.animateCamera(location);
                    //---------------------------------------------------
                    polyLineList.add(latLng);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng poly : polyLineList) {
                        builder.include(poly);
                    }
                    LatLngBounds bounds = builder.build();
                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngZoom(
                            latLng, zoom);
                    mMap.animateCamera(mCameraUpdate);
                    polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.BLUE);
                    polylineOptions.width(7);
                    polylineOptions.startCap(new SquareCap());
                    polylineOptions.endCap(new SquareCap());
                    polylineOptions.jointType(ROUND);
                    polylineOptions.addAll(polyLineList);
                    polylineOptions.clickable(true);
                    greyPolyLine = mMap.addPolyline(polylineOptions);
                    // -----------------------------------------------------
                } else {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (!receiveSelectedCode) {
                                registerSocket();
                                handler.removeCallbacksAndMessages(null);

                            }
                        }
                    }, 60000);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    //---------------------------------------------------------
    @OnClick(R.id.refresh)
    public void refresh(View view){

        GetDataOnline(id_car);

        uiHandler = new UIHandler();
        handler = new Handler(Looper.getMainLooper());


        socket_connect = new Socket_Connect(ActShowOnLineLocation.this, uiHandler);
        registerSocket();
    }


    //---------------------------------------------------------showcars
    @OnClick(R.id.showcars)
    public void showcars(View view) {
        Intent intent = new Intent(ActShowOnLineLocation.this, ActShowCars.class);
        startActivityForResult(intent, 1);
    }

    //---------------------------------------------------------warnings
    @OnClick(R.id.warnings)
    public void Warnings(View view) {
        Intent intent = new Intent(ActShowOnLineLocation.this, ActWarning.class);
        startActivityForResult(intent, 1);
    }

    //---------------------------------------------------------- initview
    private void initview() {
        FragmentManager myFM = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) myFM
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    //--------------------------------------------------- getintent
    private void GetIntent() {
        id_car = getIntent().getIntExtra("id", 0);
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

    //--------------------------------------------------- custom_left_arrow
    @OnClick(R.id.custom_left_arrow)
    public void custom_left_arrow(View view) {
        polyLineList = new ArrayList<>();
        if (carModels.size() != 0) {
            if (idposition > 0 & idposition < carModels.size() - 1) {
                idposition = idposition - 1;
                id_car = carModels.get(idposition).getId();
            } else if (idposition > 0) {
                idposition = 0;
                id_car = carModels.get(idposition).getId();
            }
            sessionManager.setcar_code(carModels.get(idposition).getCar_code());
            sessionManager.setlastid(carModels.get(idposition).getId());
            receiveSelectedCode = false;
            registerSocket();


            CarActive();
        }

    }

    //--------------------------------------------------- custom_right_arrow
    @OnClick(R.id.custom_right_arrow)
    public void custom_right_arrow(View view) {
        polyLineList = new ArrayList<>();
        if (carModels.size() != 0) {
            if (idposition >= 0 & idposition < carModels.size() - 1) {
                idposition = idposition + 1;
                id_car = carModels.get(idposition).getId();
            } else {
                idposition = 0;
                id_car = carModels.get(idposition).getId();
            }
            try {

                sessionManager.setcar_code(carModels.get(idposition).getCar_code());
                sessionManager.setlastid(carModels.get(idposition).getId());

                receiveSelectedCode = false;
                registerSocket();


                CarActive();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    //--------------------------------------------------- back_button
    @OnClick(R.id.back_button)
    public void Btn_back_button(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //---------------------------------------------------operation
    @OnClick(R.id.operation)
    public void btn_operation(View view) {
        displayPopupWindow(view);
    }

    //----------------------------------------------------displayPopupWindow
    private void displayPopupWindow(View anchorView) {
        PopupWindow popup = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popupdisplay, null);
        popup.setContentView(layout);
        LinearLayout item1 = (LinearLayout) layout.findViewById(R.id.showpath);
        LinearLayout listGeo = (LinearLayout) layout.findViewById(R.id.listgeo);
        LinearLayout addgeofencing = (LinearLayout) layout.findViewById(R.id.addgeofencing);

        //--------------------------------------------------------------------
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    localDB.Deletecar_offlineModel();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (carModels.size() > 0) {
                    Intent intent = new Intent(context, ActShowOffLinePath.class);
                    intent.putExtra("id", id_car);
                    startActivity(intent);
                } else {
                    Toast.makeText(ActShowOnLineLocation.this, "خودرویی برای نمایش وجود ندارد", Toast.LENGTH_LONG).show();
                }

            }
        });
        //-------------------------------------------------------- geofencing
        addgeofencing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActMainGeo.class);
                intent.putExtra("id", id_car);
                startActivity(intent);
            }
        });
        //------------------------------------------------------- show List Geofencing
        listGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActShowGeofencing.class);
                startActivity(intent);
            }
        });
        //-------------------------------------------------------- Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        //  popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        // Show anchored to button
        Display display = getWindowManager().getDefaultDisplay();
        popup.showAtLocation(anchorView, Gravity.BOTTOM, anchorView.getLeft() - anchorView.getWidth() + 20,
                anchorView.getBottom() + anchorView.getHeight() + 20);
        popup.showAsDropDown(anchorView);
    }

    //------------------------------------------------------ Active car in map
    private void CarActive() {
        try {
            LatLng latLng = new LatLng(carModels.get(idposition).getLat(), carModels.get(idposition).getLng());
            InfoWindowAdapteonline markerInfoWindowAdapter = new InfoWindowAdapteonline(
                    ActShowOnLineLocation.this,
                    carModels.get(idposition).getCar_name(), carModels.get(idposition).getId(), carModels.get(idposition).getTstmp(),
                    carModels.get(idposition).getLat(), carModels.get(idposition).getLng(),
                    carModels.get(idposition).getSpeed(), carModels.get(idposition).getStart(),false);
            mMap.setInfoWindowAdapter(markerInfoWindowAdapter);
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.carlocation);
            Marker carposition = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(carModels.get(idposition).getCar_name())
                    .icon(icon)
                    .snippet(String.valueOf(carModels.get(idposition).getId())));
            carposition.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition
                            (new CameraPosition.Builder()
                                    .target(latLng)
                                    .zoom(mMap.getCameraPosition().zoom)
                                    .build()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //------------------------------------------------------ onMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActShowOnLineLocation.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}
                    , 1);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        this.mMap = googleMap;
        // setUpMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //-------------------------------------------
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
    }

    //---------progress---------------
    private void displayProgress(String text) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(text);
            }
            if (!progressDialog.isShowing()) {
                try {
                    progressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------------successProgress------------
    private void successProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    //-----------------------------------------errorProgress-----------
    private void errorProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    //--------------------------------------------------- get online
    public void GetDataOnline(final int id_car) {
        try {
            displayProgress(getString(R.string.toast_msg_check_data));
            Call<online> call = mOService.GetOnline(id_car);
            call.enqueue(new Callback<online>() {
                @Override
                public void onResponse(Call<online> call, Response<online> response) {
                    if (response.isSuccessful()) {
                        successProgress();
                        if (response.body().getCar_onlineModels().size() > 0) {
                            localDB.SaveOnline(response.body().getCar_onlineModels());
                            CarActiveResult(response.body().getCar_onlineModels().get(0));
                            setUpMap(response.body().getCar_onlineModels());
                        }
                    } else {
                        errorProgress();
                    }
                }

                @Override
                public void onFailure(Call<online> call, Throwable t) {
                    t.printStackTrace();
                    errorProgress();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            errorProgress();
        }
    }

    //---------------------------------------------------------------------- adapter for fill spinner
    class spinnerListadapter extends BaseAdapter {
        private final LayoutInflater myinflater;

        private List<String> list = null;
        private final ArrayList<String> arraylist;
        public Context context;

        public spinnerListadapter(Context context) {
            this.context = context;
            myinflater = LayoutInflater.from(context);
            CTypeFace.initialized(context);
            this.arraylist = new ArrayList<String>();
            ViewGroup vg = (ViewGroup) getWindow().getDecorView();
            CTypeFace.setTypeFace(context, vg);
        }

        public void setData(List<String> list) {
            this.list = list;
            this.arraylist.clear();
            this.arraylist.addAll(list);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder viewholder;
            if (convertView == null) {
                convertView = myinflater.inflate(R.layout.spiner_list_item,
                        null);
                viewholder = new ViewHolder();
                viewholder.txtTitle = (TextView) convertView
                        .findViewById(R.id.spiner_list_item_txt_name);
                CTypeFace.processOBJ(viewholder.txtTitle);
                convertView.setTag(viewholder);
            } else
                viewholder = (ViewHolder) convertView.getTag();

            viewholder.txtTitle.setText(list.get(position));
            return convertView;
        }

        public class ViewHolder {
            public TextView txtTitle;
        }

    }

    //-------------------------------------------------------
    public class UIHandler extends Handler {

        public void handleMessage(Message msg) {
            try {
                String st = msg.obj.toString();
                if (msg.arg1 == 1) {

                    //-------------------------------------- get info car from webservice  when socket has a error
                    StringBuilder sb = new StringBuilder();
                    sb.append(st);
                    sb.append("    No: ");
                    sb.append(msg.arg2);

                    if (!sb.toString().contains("Send SMS Success!")) {
                        if (sb.toString().contains("*success*Device register successful")) {

                            if (pressKey) {
                                SendMessage(sendMessage
                                        , sendSms, false
                                );
                            } else {
                                SendMessage("*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*tell"
                                        , "", true);
                            }

                        } else {
                            ProccessMessage(sb.toString(), st);
                            sendRegister = false;
                        }
                    }

                } else if (msg.arg1 == 0 && msg.arg2 == 0) {
                    registerSocket();
                } else if (msg.arg1 == 0 && msg.arg2 == 9) {
                    showDialog.ShowDialog("فعلا ردیاب در شبکه نیست بعدا سعی کنید");
                } else if (msg.arg1 == 0 && msg.arg2 == 6) {
                    if(pressKey)    showDialog.ShowDialogsuccess("پیام با موفقیت به سوکت ارسال شد");

                }
                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //------------------------------
    public void ProccessMessage(String msg2, String st) {
        StringBuilder sb = new StringBuilder();
        sb.append(msg2);
        sb.append("\r\n");

        JSONObject json = null;
        try {
            json = new JSONObject(st);
            sendRegister = true;
        } catch (JSONException e) {
            e.printStackTrace();
            sendRegister = false;
        }
        Gson gson = new Gson();
        CarModel carModel_socket = gson.fromJson(json.toString(), CarModel.class);
        setUpMapSocet(carModel_socket);


    }


    //---------------------------------------------------------setUpMap
    private void setUpMap(List<carOnlineModel> onlineModels) {
        try {
            if (mMap != null) {
                mMap.clear();
                // Create a criteria object to retrieve provider
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                // Create a LatLng object for the current location
                for (int i = 0; i < onlineModels.size(); i++) {
                    nameCar= onlineModels.get(i).getCar_name();
                    LatLng latLng = new LatLng(onlineModels.get(i).getLat(), onlineModels.get(i).getLng());

                    InfoWindowAdapteonline markerInfoWindowAdapter = new InfoWindowAdapteonline(
                            ActShowOnLineLocation.this,nameCar
                           , onlineModels.get(i).getId(), onlineModels.get(i).getTstmp(),
                            onlineModels.get(i).getLat(), onlineModels.get(i).getLng(),
                            onlineModels.get(i).getSpeed(), onlineModels.get(i).getStart(),false);

                    mMap.setInfoWindowAdapter(markerInfoWindowAdapter);
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.carlocation);
                    Marker carposition = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(onlineModels.get(i).getCar_name())
                            .icon(icon)
                            .snippet(String.valueOf(onlineModels.get(i).getId())));
                    carposition.showInfoWindow();
                }
                LatLng latLng = new LatLng(localDB.GetCarsID(id_car).getLat(), localDB.GetCarsID(id_car).getLng());
                polyLineList.add(latLng);

                CarModel First_Car = localDB.GetCarsID(id_car);

                InfoWindowAdapteonline markerInfoWindowAdapter = new InfoWindowAdapteonline(
                        ActShowOnLineLocation.this,
                        First_Car.getCar_name(), First_Car.getId(), First_Car.getTstmp(),
                        First_Car.getLat(), First_Car.getLng(),
                        First_Car.getSpeed(), First_Car.getStart(),false);

                mMap.setInfoWindowAdapter(markerInfoWindowAdapter);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.carlocation);
                Marker carposition = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(First_Car.getCar_name())
                        .icon(icon)
                        .snippet(String.valueOf(First_Car.getId())));
                carposition.showInfoWindow();
                if (Math.round(mMap.getCameraPosition().zoom) > 11) {
                    zoom = mMap.getCameraPosition().zoom;
                } else {
                    zoom = 20;
                }
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                        latLng, zoom);
                mMap.animateCamera(location);
                //---------------------------------------------------
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng poly : polyLineList) {
                    builder.include(poly);
                }
                LatLngBounds bounds = builder.build();
                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        latLng, zoom);
                mMap.animateCamera(mCameraUpdate);
                polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(7);
                polylineOptions.startCap(new SquareCap());
                polylineOptions.endCap(new SquareCap());
                polylineOptions.jointType(ROUND);
                polylineOptions.addAll(polyLineList);
                polylineOptions.clickable(true);
                greyPolyLine = mMap.addPolyline(polylineOptions);
                // -----------------------------------------------------

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
