package com.thimat.sockettelkarnet.activites;

import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.davidmiguel.multistateswitch.MultiStateSwitch;
import com.davidmiguel.multistateswitch.State;
import com.davidmiguel.multistateswitch.StateListener;
import com.google.gson.Gson;
import com.rey.material.widget.Button;
import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.models.carOnlineModel;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.socket.Socket_Connect;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActSettingErrors extends AppCompatActivity implements StateListener {
    @BindView(R.id.warning_open_door)
    Switch warning_open_door;
    @BindView(R.id.warning_start)
    Switch warning_start;
    @BindView(R.id.warning_Impact_Sensor)
    Switch warning_Impact_Sensor;
    @BindView(R.id.warning_move_Sensor)
    Switch warningMoveSensor;
    @BindView(R.id.warning_Stealing_battery)
    Switch warning_Stealing_battery;
    @BindView(R.id.warning_Unauthorized_speed)
    Switch warning_Unauthorized_speed;
    @BindView(R.id.warning_Towing_shift_warning)
    Switch warning_Towing_shift_warning;
    @BindView(R.id.sendwarningsetting)
    Button sendwarningsetting;
    @BindView(R.id.linear_warning_Impact_Sensor)
    LinearLayout linear_warning_Impact_Sensor;
    @BindView(R.id.linear_warning_move_Sensor)
    LinearLayout liMoveSensor;
    @BindView(R.id.customizedSwitch)
    MultiStateSwitch customizedSwitch;
    private ProgressDialog progressDialog;
    private boolean firstRun=true;
    private boolean select_click_warning=false;
    //----------------------------------------------
    String warningopendoor;
    String warningstart;
    String warningImpactSensor;
    String warningStealingbattery;
    String warningUnauthorizedspeed;
    String warningTowingshiftwarning;
    String warning_move_Sensor;
    //---------------------------------------
    SessionManager sessionManager;
    Context context;
    SendSMS sendSMS;
    ShowDialog showDialog;
    boolean pressKey = false;
    String sendMessage = "";
    String sendSms = "";
    Socket_Connect socket_connect;
    Handler uiHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settingerror);
        ButterKnife.bind(this);
        context=getApplicationContext();
        sessionManager=new SessionManager(context);
        sendSMS=new SendSMS(ActSettingErrors.this);
        showDialog = new ShowDialog(ActSettingErrors.this);
        if (sessionManager.getTcn_v200()) {
            linear_warning_Impact_Sensor.setVisibility(View.GONE);
            liMoveSensor.setVisibility(View.GONE);
        }
        //-------------------------------
        setupDefaultSwitch();

        //----------------------------------------------------- warning_open_door
        warning_open_door.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isPressed()) {
                if (isChecked) {
                    warningopendoor = "1";
                    sendSetting("01");
                } else {
                    warningopendoor = "0";
                    sendSetting("00");

                }
            }
        });
        //--------------------------------------------------------- warning_start
        warning_start.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isPressed()) {
                if (isChecked) {
                    warningstart = "1";
                    sendSetting("11");

                } else {
                    warningstart = "0";
                    sendSetting("10");

                }
            }
        });
        //---------------------------------------------------------------------- warning_Stealing_battery
        warning_Stealing_battery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isPressed()) {
                if (isChecked) {
                    warningStealingbattery = "1";
                    sendSetting("21");

                } else {
                    warningStealingbattery = "0";
                    sendSetting("20");

                }
            }
        });
        //--------------------------------------------------------------------- warning_Unauthorized_speed
        warning_Unauthorized_speed.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isPressed()) {
                if (isChecked) {
                    warningUnauthorizedspeed = "1";
                    sendSetting("31");

                } else {
                    warningUnauthorizedspeed = "0";
                    sendSetting("30");

                }
            }
        });
        //----------------------------------------------------------------warning_Towing_shift_warning
        warning_Towing_shift_warning.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isPressed()) {
                if (isChecked) {
                    warningTowingshiftwarning = "1";
                    sendSetting("41");

                } else {
                    warningTowingshiftwarning = "0";
                    sendSetting("40");

                }
            }
        });
        //-------------------------------------------------------------------- warning_Impact_Sensor
        warning_Impact_Sensor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isPressed()) {
                if (isChecked) {
                    warningImpactSensor = "1";
                    sendSetting("51");

                } else {
                    warningImpactSensor = "0";
                    sendSetting("50");

                }
            }
        });

        //----------------------------------------------------------------warningMoveSensor
        warningMoveSensor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isPressed()) {
                if (isChecked) {
                    warning_move_Sensor = "1";
                    sendSetting("61");

                } else {
                    warning_move_Sensor = "0";
                    sendSetting("60");

                }
            }
        });

    }
    private void setupDefaultSwitch() {
        List<String> list=new ArrayList<>();
        list.add("پیامک");
        list.add("تماس");
        list.add("پیامک و تماس");
        customizedSwitch.addStatesFromStrings(list);
        select_click_warning=false;
        customizedSwitch.addStateListener(this);
    }

    @Override
    public void onStateSelected(int i, @NotNull State state) {
        if (select_click_warning) {

            if (state.getText().equals("پیامک")) {
                sendSetting("71");

            } else if (state.getText().equals("تماس")) {
                sendSetting("72");


            } else if (state.getText().equals("پیامک و تماس")) {
                sendSetting("73");

            }
        }else{
            select_click_warning=true;
        }


    }
    //-------------------------------------------------------- sendwarningsetting
    @OnClick(R.id.sendwarningsetting)
    public void Btn_sendwarningsetting(View view){
        String warning=warningopendoor+warningstart+warningStealingbattery+warningUnauthorizedspeed+warningTowingshiftwarning+warningImpactSensor+warning_move_Sensor;
        SendMessage("*ID*"+sessionManager.getUser(),"*SET"+warning,true);

        pressKey = true;
        sendMessage ="*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*SET"+warning;
        sendSms ="*SET"+warning;
    }
    //------------------------------ sed message to socket
    public void sendSetting(String warning){
        SendMessage("*ID*"+sessionManager.getUser(),"*SET"+warning,true);

        pressKey = true;
        sendMessage ="*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*SET"+warning;
        sendSms ="*SET"+warning;
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHandler = new UIHandler();
        displayProgress(getString(R.string.display_dialog_wail_location));

        if(socket_connect!=null) socket_connect.socketDisconnect();

        socket_connect = new Socket_Connect(this, uiHandler);
        registerSocket();
    }

    //--------------------------------------------------------
    private void registerSocket() {
        SendMessage("*ID*" + sessionManager.getUser(), "", true);
        pressKey = false;
    }

    //-------------------------------------------------------
    public class UIHandler extends Handler {

        public void handleMessage(Message msg) {
            try {
                String st = msg.obj.toString();
                Log.e("Error","st=="+st);
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
                                        , sendSms, false);
                            } else {
                                SendMessage("*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*tell"
                                        , "", true);
                            }
                        } else {
                            ProccessMessage(sb.toString(), st);
                        }
                    }

                } else if (msg.arg1 == 0 && msg.arg2 == 0) {

                    registerSocket();

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

    //------------------------------
    public void ProccessMessage(String msg2, String st) {
        Log.e("Error","ProccessMessage=="+msg2);

        StringBuilder sb = new StringBuilder();
        sb.append(msg2);
        sb.append("\r\n");
        if (msg2.contains("{")) {
            List<carOnlineModel> car_onlineModelList = new ArrayList<>();
            JSONObject json = null;
            try {
                json = new JSONObject(st);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error","ProccessMessage=="+e.getMessage());

            }
            Gson gson = new Gson();
            carOnlineModel car_onlineModel = gson.fromJson(json.toString(), carOnlineModel.class);
            car_onlineModelList.add(car_onlineModel);
            car_onlineModelList.add(car_onlineModel);
            ChangeStatus(car_onlineModelList);

        }
    }
    //----------------------------------
    public void ChangeStatus(List<carOnlineModel> onlineModels){
        //000011
        //0011111
        // 0011111
        successProgress();
        Log.e("Error","firstRun=="+firstRun);

        if (firstRun) {
            if (onlineModels.size() > 0) {
                carOnlineModel onlineModel = onlineModels.get(0);
                firstRun=false;
                //-------------

                if (onlineModel.getSeting().getBytes()[0] == 49) {
                    warning_open_door.setChecked(true);
                    warningopendoor = "1";


                } else {
                    warning_open_door.setChecked(false);
                    warningopendoor = "0";

                }
                //-------------
                if (onlineModel.getSeting().getBytes()[1] == 49) {
                    warning_start.setChecked(true);
                    warningstart = "1";
                } else {
                    warning_start.setChecked(false);
                    warningstart = "0";
                }

                //-------------
                if (onlineModel.getSeting().getBytes()[2] == 49) {
                    warning_Stealing_battery.setChecked(true);
                    warningStealingbattery = "1";
                } else {
                    warning_Stealing_battery.setChecked(false);
                    warningStealingbattery = "0";
                }
                //-------------
                if (onlineModel.getSeting().getBytes()[3] == 49) {
                    warning_Unauthorized_speed.setChecked(true);
                    warningUnauthorizedspeed = "1";
                } else {
                    warning_Unauthorized_speed.setChecked(false);
                    warningUnauthorizedspeed = "0";
                }
                //-------------
                if (onlineModel.getSeting().getBytes()[4] == 49) {
                    warning_Towing_shift_warning.setChecked(true);
                    warningTowingshiftwarning = "1";
                } else {
                    warning_Towing_shift_warning.setChecked(false);
                    warningTowingshiftwarning = "0";
                }

                //-------------
                if (onlineModel.getSeting().getBytes()[5] == 49) {
                    warning_Impact_Sensor.setChecked(true);
                    warningImpactSensor = "1";
                } else {
                    warning_Impact_Sensor.setChecked(false);
                    warningImpactSensor = "0";
                }
                //-------------
                if (onlineModel.getSeting().getBytes()[6] == 49) {
                    warningMoveSensor.setChecked(true);
                    warning_move_Sensor = "1";
                } else {
                    warningMoveSensor.setChecked(false);
                    warning_move_Sensor = "0";
                }
                //----------------------
                if (onlineModel.getSeting().getBytes()[7] == 49) {
                    customizedSwitch.selectState(0);
                } else if (onlineModel.getSeting().getBytes()[7] == 50) {
                    customizedSwitch.selectState(1);
                } else if (onlineModel.getSeting().getBytes()[7] == 51) {
                    customizedSwitch.selectState(2);
                }
            }
        }


    }
    //-------------------------------------------------progress---------------
    private void displayProgress(String text) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
                progressDialog.setCancelable(true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
