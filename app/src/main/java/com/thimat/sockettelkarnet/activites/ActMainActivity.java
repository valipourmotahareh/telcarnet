package com.thimat.sockettelkarnet.activites;

import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.thimat.sockettelkarnet.adapter.RealmShowNameCarAdapter;
import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.classes.Util;
import com.thimat.sockettelkarnet.dialogActivity.DialogPower;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.CarModel;
import com.thimat.sockettelkarnet.models.ListCarModel;
import com.thimat.sockettelkarnet.models.carOnlineModel;
import com.thimat.sockettelkarnet.models.online;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.WarningService;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;
import com.thimat.sockettelkarnet.socket.Socket_Connect;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActMainActivity extends AppCompatActivity {
    @BindView(R.id.panel_tracket)
    ImageView panel_tracket;
    @BindView(R.id.linear_tracket)
    LinearLayout linear_tracket;
    @BindView(R.id.btn_telkarnet)
    Button btn_telkarnet;
    @BindView(R.id.setting)
    Button setting;
    @BindView(R.id.btn_connect)
    Button btn_connect;
    @BindView(R.id.currentgprs)
    LinearLayout currentgprs;
    @BindView(R.id.txt_speed)
    TextView txt_speed;
    @BindView(R.id.lefttelcarnet)
    LinearLayout lefttelcarnet;
    @BindView(R.id.unlock)
    LinearLayout unlock;
    @BindView(R.id.lock)
    LinearLayout lock;
    @BindView(R.id.righttelcarnet)
    LinearLayout righttelcarnet;
    @BindView(R.id.car)
    LinearLayout car;
    @BindView(R.id.sound)
    LinearLayout sound;
    @BindView(R.id.backcar)
    RelativeLayout backcar;
    @BindView(R.id.car_model)
    TextView car_model;
    @BindView(R.id.bat)
    ImageView bat;
    @BindView(R.id.txt_sharj)
    TextView txt_sharj;
    @BindView(R.id.txtbat)
    TextView txtbat;
    @BindView(R.id.sat)
    ImageView sat;
    @BindView(R.id.txtsat)
    TextView txtsat;
    @BindView(R.id.rel)
    ImageView rel;
    @BindView(R.id.txt_rele)
    TextView txt_rele;
    @BindView(R.id.archive)
    LinearLayout archive;
    @BindView(R.id.statusvoice)
    ImageView statusvoice;
    @BindView(R.id.statuslock)
    ImageView statuslock;
    @BindView(R.id.img_unlock1)
    ImageView img_unlock1;
    @BindView(R.id.img_unlock)
    ImageView img_unlock;
    @BindView(R.id.img_lock)
    ImageView img_lock;
    @BindView(R.id.img_lock1)
    ImageView img_lock1;
    @BindView(R.id.etebar)
    TextView etebar;
    @BindView(R.id.power)
    LinearLayout power;
    @BindView(R.id.ticket)
    LinearLayout ticket;
    @BindView(R.id.linear_logo)
    LinearLayout linear_logo;
    @BindView(R.id.gprs)
    ImageView gprs;
    @BindView(R.id.txt_gprs)
    TextView txt_gprs;
    @BindView(R.id.sms)
    ImageView sms;
    @BindView(R.id.txt_sms)
    TextView txt_sms;
    @BindView(R.id.blu)
    ImageView blu;
    @BindView(R.id.txtblu)
    TextView txtblu;
    @BindView(R.id.connect_type)
    Button connectType;
    private OperationService mOService;
    Context context;
    private ProgressDialog progressDialog;
    LocalDB localDB;
    SessionManager sessionManager;
    RealmResults<CarModel> carModels;
    RealmShowNameCarAdapter realmAdapterCars;
    SendSMS sendSMS;
    String getdate;
    int ID_CAR_SELECt;
    Handler handler;
    //String time;
    ShowDialog showDialog;
    MediaPlayer mp;
    int id_car = 0;
    public boolean receiveSelectedCode = false;
    public boolean Msg_ReadingError = true;
    //--------------------------------------
    public DataOutputStream out;
    Socket_Connect socket_connect;
    Handler uiHandler;
    //-----------------------------------
    CarModel carModel;
    Boolean showdialog = true;
    boolean sendRegister = false;
    CountDownTimer countDownTimer;
    boolean pressKey = false;
    String sendMessage = "";
    String sendSms = "";
    String nameCar="";

    //--------------------------------------
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCenter.start(getApplication(), "68c7c1ce-fe4b-4f41-969b-ecbc0274b89b",
                Analytics.class, Crashes.class);

        setContentView(R.layout.act_mianactivity);
        ButterKnife.bind(this);
        context = getApplicationContext();
        localDB = new LocalDB(context);
        sessionManager = new SessionManager(context);
        sendSMS = new SendSMS(ActMainActivity.this);
        showDialog = new ShowDialog(ActMainActivity.this);
        mp = MediaPlayer.create(this, R.raw.click);


        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        //------------------------------------------------------------------
        if (ActivityCompat.checkSelfPermission(ActMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActMainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}
                    , 1);
            return;

        }

        if (ActivityCompat.checkSelfPermission(ActMainActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActMainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS}, 2);
            return;

        }
        //----------------------------------------------------------------- for visible or enable some linear layout
        if (sessionManager.getTcn_v200()) {
            righttelcarnet.setVisibility(View.VISIBLE);
            lefttelcarnet.setVisibility(View.VISIBLE);
            unlock.setVisibility(View.GONE);
            lock.setVisibility(View.GONE);
            car.setVisibility(View.GONE);
            sound.setVisibility(View.GONE);
        } else if (sessionManager.getTcn_v300()) {
            righttelcarnet.setVisibility(View.GONE);
            lefttelcarnet.setVisibility(View.GONE);
            unlock.setVisibility(View.VISIBLE);
            lock.setVisibility(View.VISIBLE);
            car.setVisibility(View.VISIBLE);
            sound.setVisibility(View.VISIBLE);
        }
        //------------------------------------------------------------------- get connect type
        if (sessionManager.getConnectType() == Util.sendASMS) {
            connectType.setText(getString(R.string.popup_sms));
            sms.setBackgroundResource(R.drawable.smson);
            txt_sms.setTextColor(getResources().getColor(R.color.txt_active));
        } else if (sessionManager.getConnectType() == Util.sendAutomatic) {
            connectType.setText(getString(R.string.popup_automatic));
            sms.setBackgroundResource(R.drawable.smsoffline);
            txt_sms.setTextColor(getResources().getColor(R.color.main_icon_text));
        } else if (sessionManager.getConnectType() == Util.sendSocket) {
            connectType.setText(getString(R.string.popup_internet));
            sms.setBackgroundResource(R.drawable.smsoffline);
            txt_sms.setTextColor(getResources().getColor(R.color.main_icon_text));
        }

        //---------------------------------------------

        RealmResults<CarModel> carModelsonline = localDB.GetCars(1);
        if (carModelsonline.size() > 0) {
            if (sessionManager.GetLastId() == 0) {
                ID_CAR_SELECt = carModelsonline.get(0).getId();

            } else {
                ID_CAR_SELECt = sessionManager.GetLastId();
            }
            GetDataOnline(ID_CAR_SELECt);
        } else {
            ID_CAR_SELECt = sessionManager.GetLastId();
            if (ID_CAR_SELECt==0) CheckLogin();
        }


        carModel = localDB.GetCarCode(ID_CAR_SELECt);
        //--------------------------------------------------------- service warning run
        WarningService mSensorService = new WarningService(ActMainActivity.this);
        Intent mServiceIntent = new Intent(ActMainActivity.this, mSensorService.getClass());
        startService(mServiceIntent);
    }


    //--------------------------------------------------

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

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    //--------------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        mp.stop();
        mp.release();
    }

    //------------------------------------------------- connect_type
    @OnClick(R.id.connect_type)
    public void setConnectType(View view) {
        displayConnectType(view);
    }

    private void displayConnectType(View anchorView) {
        PopupWindow popup = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_connet_type, null);
        popup.setContentView(layout);
        LinearLayout sendSms = (LinearLayout) layout.findViewById(R.id.sendsms);
        LinearLayout sendSocket = (LinearLayout) layout.findViewById(R.id.send_socket);
        LinearLayout sendAutomatic = (LinearLayout) layout.findViewById(R.id.send_automatic);

        //--------------------------------------------------------------------
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setConnectType(Util.sendASMS);
                connectType.setText(getString(R.string.popup_sms));
                sms.setBackgroundResource(R.drawable.smson);
                txt_sms.setTextColor(getResources().getColor(R.color.txt_active));
                popup.dismiss();
            }
        });
        //------------
        sendSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setConnectType(Util.sendSocket);
                connectType.setText(getString(R.string.popup_internet));
                sms.setBackgroundResource(R.drawable.smsoffline);
                txt_sms.setTextColor(getResources().getColor(R.color.main_icon_text));
                popup.dismiss();

            }
        });
        //-----------
        sendAutomatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setConnectType(Util.sendAutomatic);
                connectType.setText(getString(R.string.popup_automatic));
                sms.setBackgroundResource(R.drawable.smsoffline);
                txt_sms.setTextColor(getResources().getColor(R.color.main_icon_text));
                popup.dismiss();

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

    //------------------------------------------------- linear_logo
    @OnClick(R.id.linear_logo)
    public void linear_logo(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        GetDataOnline(ID_CAR_SELECt);
        receiveSelectedCode = false;
        uiHandler = new UIHandler();
        handler = new Handler(Looper.getMainLooper());

        socket_connect = new Socket_Connect(ActMainActivity.this, uiHandler);
        registerSocket();

    }

    //------------------------------------------------- car
    @OnClick(R.id.car)
    public void Icon_car(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }

        SendMessage("*ID*" + sessionManager.getUser(), "*open", true);

        pressKey = true;
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*open";
        sendSms = "*open";

    }

    //------------------------------------------------sound
    @OnClick(R.id.sound)
    public void redsound(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        pressKey=true;
        SendMessage("*ID*" + sessionManager.getUser(), "*mof", true);

        sendMessage="*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*mof";
        sendSms ="*mof";
    }

    //------------------------------------------------- power
    @OnClick(R.id.power)
    public void PowerLinear(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActMainActivity.this, DialogPower.class);
        intent.putExtra("id", ID_CAR_SELECt);
        startActivity(intent);
    }

    //------------------------------------------------- ticket click
    @OnClick(R.id.ticket)
    public void Btn_ticket(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
//        Intent intent = new Intent(Act_MainActivity.this, Act_Ticket.class);
//        startActivity(intent);
        showDialog.ShowDialog("این امکان در آپدیت های بعدی اضافه خواهد شد");

    }

    //-------------------------------------------------- img_unlock1 click
    @OnClick(R.id.img_unlock1)
    public void img_unlock1(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }

        SendMessage("*ID*" + sessionManager.getUser(), "*dis", true);

        pressKey = true;
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*dis";
        sendSms = "*dis";
    }

    //--------------------------------------------------- img_unlock click
    @OnClick(R.id.img_unlock)
    public void img_unlock(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }

        SendMessage("*ID*" + sessionManager.getUser(), "*dis", true);

        pressKey = true;
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*dis";

        sendSms = "*dis";

    }

    //--------------------------------------------------- img_lock click
    @OnClick(R.id.img_lock)
    public void img_lock(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }

        SendMessage("*ID*" + sessionManager.getUser(), "*act", true);

        pressKey = true;
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*act";
        sendSms = "*act";

    }

    //--------------------------------------------------- img_lock1 click
    @OnClick(R.id.img_lock1)
    public void img_lock1(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }

        SendMessage("*ID*" + sessionManager.getUser(), "*act", true);

        pressKey = true;
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*act";
        sendSms = "*act";

    }

    //--------------------------------------------------- car_model click
    @OnClick(R.id.car_model)
    public void Clickcar_model(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        displayPopupWindow(view);
    }

    //----------------------------------------------------
    private void displayPopupWindow(View anchorView) {
        PopupWindow popup = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.show_name_car, null);
        popup.setContentView(layout);
        RecyclerView listname = (RecyclerView) layout.findViewById(R.id.list_name);
        carModels = localDB.GetCars();
        if (carModels != null) {
            realmAdapterCars = new RealmShowNameCarAdapter(carModels, true, context, new RealmShowNameCarAdapter.EventHandler() {
                @Override
                public void OnItemClick(int idcar, String car_code, String phone) {
                    gprs.setBackgroundResource(R.drawable.gprsoffline);
                    txt_gprs.setTextColor(getResources().getColor(R.color.main_icon_text));

                    GetDataOnline(idcar);
                    ID_CAR_SELECt = idcar;
                    //---------------------------------- start socket with new code car
                     sessionManager.setcar_code(car_code);
                     sessionManager.setlastid(idcar);
                    receiveSelectedCode = false;
                    SendMessage("*ID*" + sessionManager.getUser(), "", true);
                    pressKey = false;
                    popup.dismiss();

                }
            });
            listname.setLayoutManager(new LinearLayoutManager(this));
            listname.setAdapter(realmAdapterCars);
        }
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        //  popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        // Show anchored to button
        Display display = getWindowManager().getDefaultDisplay();
        popup.showAtLocation(anchorView, Gravity.BOTTOM, 0,
                display.getHeight() - anchorView.getBottom() + 200);

        popup.showAsDropDown(anchorView);
    }

    //----------------------------------------------- click archive
    @OnClick(R.id.archive)
    public void Btn_archive(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActMainActivity.this, ActInboxActivity.class);
        startActivity(intent);
    }

    //----------------------------------------------- click on currentgprs
    @OnClick(R.id.currentgprs)
    public void btn_currentgprs(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        sendSMS.sendsms("*gps?", sessionManager.getPhonenumbr());

    }

    //----------------------------------------------- click on setting
    @OnClick(R.id.setting)
    public void btn_setting(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActMainActivity.this, ActBtnSetting.class);
        intent.putExtra("id", ID_CAR_SELECt);
        startActivity(intent);
    }

    //----------------------------------------------- click on btn_connect
    @OnClick(R.id.btn_connect)
    public void btn_connect(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActMainActivity.this, ActBtnConnectDevice.class);
        startActivity(intent);
    }

    //----------------------------------------------- click on btn_telkarnet
    @OnClick(R.id.btn_telkarnet)
    public void btn_telkarnet(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActMainActivity.this, ActBtnTelkarnet.class);
        startActivity(intent);
    }

    //----------------------------------------------- click of panel_tracket
    @OnClick(R.id.panel_tracket)
    public void IMG_paneltracket(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActMainActivity.this, ActShowOnLineLocation.class);
        intent.putExtra("id", ID_CAR_SELECt);
        startActivity(intent);
    }

    //--------------------------------------------------- get online
    public void GetDataOnline(final int id_car) {
        if (showdialog) {
            displayProgress(getString(R.string.toast_msg_check_data));
        }
        Call<online> call = mOService.GetOnline(id_car);
        call.enqueue(new Callback<online>() {
            @Override
            public void onResponse(Call<online> call, Response<online> response) {
                if (response.isSuccessful()) {
                    if (showdialog) {
                        successProgress();
                    }
                    localDB.SaveOnline(response.body().getCar_onlineModels());
                    if (response.body().getCar_onlineModels().size() > 0) {
                        if (response.body().getCar_onlineModels().get(0).getCar_name().equals("")) {
                            car_model.setText("بی نام");
                        } else {
                            nameCar=response.body().getCar_onlineModels().get(0).getCar_name();
                        }
                        sessionManager.setcar_code(response.body().getCar_onlineModels().get(0).getCar_code());
                        sessionManager.setPhonenumber(response.body().getCar_onlineModels().get(0).getPhone_device());
                        ChangeStatus(response.body().getCar_onlineModels(), false);
                    } else {
                        Toast.makeText(context, "دریافت اطلاعات این ماشین با خطا مواجه شده است", Toast.LENGTH_LONG).show();
                    }

                } else {
                    if (showdialog) {
                        errorProgress();
                    }
                }
            }

            @Override
            public void onFailure(Call<online> call, Throwable t) {
                t.printStackTrace();
                if (showdialog) {
                    errorProgress();
                }
                try {
                    ChangeStatus(localDB.GetOnline(id_car), false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // ---------------------------------------------------------------- change status button
    public void ChangeStatus(List<carOnlineModel> onlineModels, boolean socketInfo) {
        // successProgress();
        if (onlineModels.size() > 0) {
            if (sessionManager.getCar_code().equals(onlineModels.get(0).getCar_code())) {
                car_model.setText(nameCar);
                if (socketInfo) receiveSelectedCode = true;
                carOnlineModel onlineModel = onlineModels.get(0);
                sessionManager.setlastid(ID_CAR_SELECt);
                if (onlineModel.getModel_device() == 2) {
                    sessionManager.setTcn_v300(true);
                    sessionManager.setTcn_v200(false);
                    righttelcarnet.setVisibility(View.GONE);
                    lefttelcarnet.setVisibility(View.GONE);
                    unlock.setVisibility(View.VISIBLE);
                    lock.setVisibility(View.VISIBLE);
                    car.setVisibility(View.VISIBLE);
                    sound.setVisibility(View.VISIBLE);
                } else if (onlineModel.getModel_device() == 1) {
                    sessionManager.setTcn_v200(true);
                    sessionManager.setTcn_v300(false);
                    righttelcarnet.setVisibility(View.VISIBLE);
                    lefttelcarnet.setVisibility(View.VISIBLE);
                    unlock.setVisibility(View.GONE);
                    lock.setVisibility(View.GONE);
                    car.setVisibility(View.GONE);
                    sound.setVisibility(View.GONE);
                }
                if (onlineModel.getRezerv() == 1) {
                    power.setBackgroundResource(R.drawable.power);
                } else if (onlineModel.getRezerv() == 0) {
                    power.setBackgroundResource(R.drawable.poweroff);
                }
                getdate = onlineModel.getTstmp();
                //--------------------------------------------------------- speed
                txt_speed.setText(String.valueOf(onlineModel.getSpeed()));
                //--------------------------------------------------------- start
                if (onlineModel.getStart() == 1 & onlineModel.getDoor() == 0 & onlineModel.getShock_sensor() == 1) {
                    backcar.setBackgroundResource(R.drawable.carclosevibra);
                } else if (onlineModel.getStart() == 1 & onlineModel.getDoor() == 0) {
                    backcar.setBackgroundResource(R.drawable.carclose);
                } else if (onlineModel.getStart() == 1 & onlineModel.getDoor() == 1 & onlineModel.getShock_sensor() == 1) {
                    backcar.setBackgroundResource(R.drawable.caractivevibra);
                } else if (onlineModel.getStart() == 1 & onlineModel.getDoor() == 1) {
                    backcar.setBackgroundResource(R.drawable.caractive);
                } else if (onlineModel.getStart() == 0 & onlineModel.getDoor() == 1 & onlineModel.getShock_sensor() == 1) {
                    backcar.setBackgroundResource(R.drawable.caroffvibra);
                } else if (onlineModel.getStart() == 0 & onlineModel.getDoor() == 1) {
                    backcar.setBackgroundResource(R.drawable.caroff);
                } else if (onlineModel.getStart() == 0 & onlineModel.getDoor() == 0 & onlineModel.getShock_sensor() == 1) {
                    backcar.setBackgroundResource(R.drawable.backcarvibra);
                } else if (onlineModel.getStart() == 0 & onlineModel.getDoor() == 0) {
                    backcar.setBackgroundResource(R.drawable.backcar);
                }

                etebar.setText(Util.SepValue(onlineModel.getSharj()) + " " + "R");
                //---------------------------------------------------- battery_connect
                if (onlineModel.getIs_battery_connected() == 0) {
                    bat.setBackgroundResource(R.drawable.batoffline);
                    txtbat.setTextColor(getResources().getColor(R.color.main_icon_text));
                    if(!onlineModel.getBattery_sharj().equals("")){
                    txt_sharj.setText(onlineModel.getBattery_sharj()+"%");
                    txt_sharj.setVisibility(View.VISIBLE);
                    }
                } else if (onlineModel.getIs_battery_connected() == 1) {
                    bat.setBackgroundResource(R.drawable.baton);
                    txtbat.setTextColor(getResources().getColor(R.color.txt_active));
                    txt_sharj.setVisibility(View.GONE);
                }
                //------------------------------------------------------- connect_gps
                if (onlineModel.getIs_conneted_gps() == 0) {
                    sat.setBackgroundResource(R.drawable.satoffline);
                    txtsat.setTextColor(getResources().getColor(R.color.main_icon_text));
                } else if (onlineModel.getIs_conneted_gps() == 1) {
                    sat.setBackgroundResource(R.drawable.saton);
                    txtsat.setTextColor(getResources().getColor(R.color.txt_active));
                }
                //---------------------------------------------------- rel
                if (onlineModel.getRele() == 1) {
                    rel.setBackgroundResource(R.drawable.reloffline);
                    txt_rele.setTextColor(getResources().getColor(R.color.main_icon_text));
                } else if (onlineModel.getRele() == 0) {
                    rel.setBackgroundResource(R.drawable.relon);
                    txt_rele.setTextColor(getResources().getColor(R.color.txt_active));
                }
                //----------------------------------------------------- statuslock
                if (onlineModel.getIs_active() == 1) {
                    statuslock.setBackgroundResource(R.drawable.lockwhite);

                } else if (onlineModel.getIs_active() == 0) {
                    statuslock.setBackgroundResource(R.drawable.whiteunlock);

                }
                //--------------------------------------------------------
                if (onlineModel.getCheck() == 1) {
                    statusvoice.setBackgroundResource(R.drawable.whitesound);

                } else if (onlineModel.getCheck() == 0) {
                    statusvoice.setBackgroundResource(R.drawable.silent);

                }

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

        } else {
            bat.setBackgroundResource(R.drawable.batoffline);
            sat.setBackgroundResource(R.drawable.satoffline);
            rel.setBackgroundResource(R.drawable.reloffline);
            txt_rele.setTextColor(getResources().getColor(R.color.main_icon_text));
            txtsat.setTextColor(getResources().getColor(R.color.main_icon_text));
            txtbat.setTextColor(getResources().getColor(R.color.main_icon_text));
        }

    }
    //-------------------------------------------------progress---------------
    private void displayProgress(String text) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(ActMainActivity.this);
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

    private void successProgress() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void errorProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    //------------------------------
    public void ProccessMessage(String msg2, String st) {
        StringBuilder sb = new StringBuilder();
        sb.append(msg2);
        sb.append("\r\n");
        if (msg2.contains("{")) {
            List<carOnlineModel> car_onlineModelList = new ArrayList<>();
            JSONObject json = null;
            try {
                json = new JSONObject(st);
                sendRegister = true;
            } catch (JSONException e) {
                sendRegister = false;
                e.printStackTrace();
            }
            Gson gson = new Gson();
            carOnlineModel car_onlineModel = gson.fromJson(json.toString(), carOnlineModel.class);
            car_onlineModelList.add(car_onlineModel);
            car_onlineModelList.add(car_onlineModel);
            gprs.setBackgroundResource(R.drawable.gprson);
            txt_gprs.setTextColor(getResources().getColor(R.color.txt_active));
            ChangeStatus(car_onlineModelList, true);

        }
    }

    //-------------------------------------------------------
    public class UIHandler extends Handler {

        public void handleMessage(Message msg) {
            try {
                String st = msg.obj.toString();
                if (msg.arg1 == 1) {
                    Msg_ReadingError=false;
                    //-------------------------------------- get info car from webservice  when socket has a error
                    StringBuilder sb = new StringBuilder();
                    sb.append(st);
                    sb.append("    No: ");
                    sb.append(msg.arg2);

                    if (!sb.toString().contains("Send SMS Success!")) {
                        if (sb.toString().contains("*success*Device register successful")) {
                            if (pressKey) {
                                SendMessage(sendMessage
                                        , sendSms, false);
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
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//                            if (Msg_ReadingError) {
//                                registerSocket();
//                                handler.removeCallbacksAndMessages(null);
//
//                            }
//                        }
//                    }, 60000);

                } else if (msg.arg1 == 0 && msg.arg2 == 9) {
                    showDialog.ShowDialog("فعلا ردیاب در شبکه نیست بعدا سعی کنید");
                } else if (msg.arg1 == 0 && msg.arg2 == 6) {
                    if (pressKey) {
                        if (!st.contains("*ID")) {
                            showDialog.ShowDialogsuccess("پیام با موفقیت به سوکت ارسال شد");
                        }
                    }

                }
                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        gprs.setBackgroundResource(R.drawable.gprsoffline);
        txt_gprs.setTextColor(getResources().getColor(R.color.main_icon_text));
        GetDataOnline(sessionManager.GetLastId());
        uiHandler = new UIHandler();
        handler = new Handler(Looper.getMainLooper());
        socket_connect = new Socket_Connect(ActMainActivity.this, uiHandler);
        registerSocket();
    }

    //--------------------------------------------------------
    private void registerSocket() {
        SendMessage("*ID*" + sessionManager.getUser(), "", true);
        pressKey = false;
    }
    //--------------------------------------------------------cheaklogin----------------------------------
    private void CheckLogin() {
        Call<ListCarModel> call = mOService.loginUser(sessionManager.getUser(),sessionManager.getPassword());
        displayProgress(getString(R.string.toast_msg_check_data));
        call.enqueue(new Callback<ListCarModel>() {
            @Override
            public void onResponse(Call<ListCarModel> call, Response<ListCarModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCarModels().size() > 0) {

                        sessionManager.setPerson_Id(response.body().getCarModels().get(0).getPerson_id());
                        sessionManager.setcar_code(response.body().getCarModels().get(0).getCar_code());
                        sessionManager.setlastid(response.body().getCarModels().get(0).getId());
                        localDB.SaveCarModel(response.body().getCarModels());
                        ID_CAR_SELECt=response.body().getCarModels().get(0).getId();
                        GetDataOnline(ID_CAR_SELECt);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListCarModel> call, Throwable t) {

            }
        });
    }


}







