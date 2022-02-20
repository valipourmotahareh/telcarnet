package com.thimat.sockettelkarnet.activites;

import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.socket.Socket_Connect;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActBtnConnectDevice extends AppCompatActivity {
    @BindView(R.id.report_device)
    LinearLayout report_device;
    @BindView(R.id.charge_sim_card)
    LinearLayout charge_sim_card;
    @BindView(R.id.connect_network)
    LinearLayout connect_network;
    @BindView(R.id.user_password)
    LinearLayout user_password;
    @BindView(R.id.blutooth)
    LinearLayout blutooth;
    @BindView(R.id.ring)
    LinearLayout ring;

    Context context;
    public int all;
    SessionManager sessionManager;
    SendSMS sendSMS;
    ShowDialog showDialog;
    MediaPlayer mp;
    boolean pressKey = false;
    String sendMessage = "";
    String sendSms = "";
    Socket_Connect socket_connect;
    Handler uiHandler;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btn_connect_device);
        context = getApplicationContext();
        ButterKnife.bind(this);
        sessionManager = new SessionManager(context);
        sendSMS = new SendSMS(ActBtnConnectDevice.this);
        showDialog = new ShowDialog(ActBtnConnectDevice.this);
        mp = MediaPlayer.create(this, R.raw.click);
    }

    //------------------------------------------------------ report_device
    @OnClick(R.id.report_device)
    public void report_device(View view) {
        mp.start();

        SendMessage("*ID*" + sessionManager.getUser(), "*rep?", true);

        pressKey = true;
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*rep?";
        sendSms = "*rep?";
    }

    //------------------------------------------------------ charge_sim_card
    @OnClick(R.id.charge_sim_card)
    public void charge_sim_card(View view) {
        mp.start();
        Intent intent = new Intent(context, Sharj.class);
        startActivity(intent);
    }

    //------------------------------------------------------ connect_network
    @OnClick(R.id.connect_network)
    public void connect_network(View view) {
        mp.start();
        Intent intent = new Intent(context, ActActiveGPRS.class);
        startActivity(intent);
    }

    //------------------------------------------------------- user_password
    @OnClick(R.id.user_password)
    public void user_password(View view) {
        mp.start();

        SendMessage("*ID*" + sessionManager.getUser(), "*pas?", true);

        pressKey = true;
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*pas?";
        sendSms = "*pas?";
    }
    //---------------------------------------------------------- blutooth
    @OnClick(R.id.blutooth)
    public void blutooth(View view){
        mp.start();
        Intent intent = new Intent(context, ActBlutoothAction.class);
        startActivity(intent);
    }
    //----------------------------------------------- click on ring
    @OnClick(R.id.ring)
    public void btn_ring(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        confirm();
    }

    //--------------------------------------------------- confirm
    public void confirm() {
        final AlertDialog.Builder confirm = new AlertDialog.Builder(ActBtnConnectDevice.this);
        confirm.setTitle("ارسال پیام");
        confirm.setMessage("آیا پیام ارسال شود؟");
        confirm.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SendMessage("*ID*" + sessionManager.getUser(), "*ring", true);

                pressKey = true;
                sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*ring";
                sendSms = "*ring";


                dialogInterface.dismiss();

            }
        });
        confirm.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        confirm.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHandler = new UIHandler();
        handler = new Handler(Looper.getMainLooper());

        if(socket_connect!=null) socket_connect.socketDisconnect();

        socket_connect = new Socket_Connect(ActBtnConnectDevice.this, uiHandler);
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
                Log.e("Act_MainActivity", msg.toString());

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
                                        , sendSms, false);
                            } else {
                                SendMessage("*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*tell"
                                        , "", true);
                            }
                        }
                    }

                } else if (msg.arg1 == 0 && msg.arg2 == 0) {

                    registerSocket();

                } else if (msg.arg1 == 0 && msg.arg2 == 9) {
                    showDialog.ShowDialog("فعلا ردیاب در شبکه نیست بعدا سعی کنید");
                } else if (msg.arg1 == 0 && msg.arg2 == 6) {
                    if(pressKey) {
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
    protected void onDestroy() {
        super.onDestroy();
    }
}
