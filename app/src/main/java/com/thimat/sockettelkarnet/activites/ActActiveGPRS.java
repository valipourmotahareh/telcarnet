package com.thimat.sockettelkarnet.activites;

import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.socket.Socket_Connect;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by motahreh on 9/29/2015.
 */
public class ActActiveGPRS extends AppCompatActivity {
    @BindView(R.id.btn_FL)
    Button btn_FL;
    @BindView(R.id.img_FL)
    ImageView img_FL;
    @BindView(R.id.button_FF)
    Button button_FF;
    @BindView(R.id.img_FF)
    ImageView img_FF;
    @BindView(R.id.btn_ON)
    Button btn_ON;
    @BindView(R.id.img_ON)
    ImageView img_ON;
    public int all;
    boolean allgprs;
    Context context;
    SessionManager sessionManager;
    SendSMS sendSMS;
    ShowDialog showDialog;
    LocalDB localDB;
    boolean pressKey = false;
    String sendMessage = "";
    String sendSms = "";
    Socket_Connect socket_connect;
    Handler uiHandler;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_gprs);
        ButterKnife.bind(this);
        context = getApplicationContext();
        sessionManager = new SessionManager(context);
        localDB=new LocalDB(context);
        sendSMS = new SendSMS(ActActiveGPRS.this);
        showDialog = new ShowDialog(ActActiveGPRS.this);
        allgprs = sessionManager.getallgprs();
         try {
             if (localDB.GetCarsID(sessionManager.GetLastId()).getStatus().equals("FF")){
                 img_FF.setVisibility(View.VISIBLE);
                 img_FL.setVisibility(View.INVISIBLE);
                 img_ON.setVisibility(View.INVISIBLE);
             }else  if (localDB.GetCarsID(sessionManager.GetLastId()).getStatus().equals("FL")){
                 img_FL.setVisibility(View.VISIBLE);
                 img_ON.setVisibility(View.INVISIBLE);
                 img_FF.setVisibility(View.INVISIBLE);
             }else  if (localDB.GetCarsID(sessionManager.GetLastId()).getStatus().equals("ON")){
                 img_ON.setVisibility(View.VISIBLE);
                 img_FL.setVisibility(View.INVISIBLE);
                 img_FF.setVisibility(View.INVISIBLE);
             }
         }catch (Exception ex){
             ex.printStackTrace();
         }
        if (allgprs == true) {
            btn_ON.setBackgroundResource(R.drawable.gprsallactive);
        }
        btn_FL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all = 0;

               SendMessage("*ID*" + sessionManager.getUser(), "*gprs9h", true);

                pressKey = true;
                sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*gprs9h";
                sendSms = "*gprs9h";

            }
        });
        btn_ON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all = 1;

                SendMessage("*ID*" + sessionManager.getUser(), "gprson", true);

                pressKey = true;
                sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*gprson";
                sendSms = "gprson";
            }
        });
        button_FF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all = 0;


                SendMessage("*ID*" + sessionManager.getUser(), "disgprs", true);

                pressKey = true;
                sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*disgprs?";
                sendSms = "disgprs";
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHandler = new UIHandler();
        handler = new Handler(Looper.getMainLooper());

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
                Log.e("Act_Active_GPRS", msg.toString());

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

                } else if (msg.arg1 == 0 && msg.arg2 == 3) {

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
