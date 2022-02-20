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
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.models.carOnlineModel;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.socket.Socket_Connect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by motahreh on 8/30/2015.
 */
public class SpeedActivity extends AppCompatActivity {
    @BindView(R.id.btn_delete)
    Button btn_delete;
    @BindView(R.id.buttonspeed)
    Button send;
    @BindView(R.id.editTextspeed)
     EditText speed;
    public  String PHONENUMBER;
    public  int thisspeech;
    SendSMS sendSMS;
    Context context;
    SessionManager sessionManager;
    ShowDialog showDialog;
    boolean pressKey = false;
    String sendMessage = "";
    String sendSms = "";
    Socket_Connect socket_connect;
    Handler uiHandler;
    boolean sendRegister=false;
    private ProgressDialog progressDialog;
    private boolean firstRun=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speed);
        ButterKnife.bind(this);
        context=getApplicationContext();
        sendSMS=new SendSMS(SpeedActivity.this);
        showDialog = new ShowDialog(SpeedActivity.this);
        sessionManager=new SessionManager(context);
        PHONENUMBER =sessionManager.getPhonenumbr();
        thisspeech=sessionManager.getspeed();
        speed.setText(String.valueOf(thisspeech));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sp= Integer.parseInt(speed.getText().toString());
                if (sp<100)
                {
                    SendMessage("*ID*" + sessionManager.getUser(), "*speed*0"+sp+"#", true);

                    pressKey = true;
                    sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*speed*0"+sp+"#";
                    sendSms ="*speed*0"+sp+"#";

                }else {
                    SendMessage("*ID*" + sessionManager.getUser(), "*speed*" + sp +"#" + "*", true);

                    pressKey = true;
                    sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*speed*" + sp +"#" + "*";
                    sendSms ="*speed*" + sp +"#" + "*";
                }
                sessionManager.setSpeed(sp);
            }
        });


    }
    //--------------------------------------------------------- btn_delete
    @OnClick(R.id.btn_delete)
    public void btn_delete(View view){

        speed.setText("0");

        SendMessage("*ID*" + sessionManager.getUser(), "*speed*0"+"#", true);

        pressKey = true;
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*speed*0"+"#";
        sendSms ="*speed*0"+"#";

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
                            sendRegister = false;
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
            ChangeStatus(car_onlineModelList);

        }
    }
    //----------------------------------
    public void ChangeStatus(List<carOnlineModel> onlineModels){
        successProgress();
        Log.e("speed","firstRun=="+firstRun);
        if (firstRun) {
            if (onlineModels.size() > 0) {
                firstRun=false;
                carOnlineModel onlineModel = onlineModels.get(0);
                Log.e("speed","speed=="+onlineModel.getSpeedMax());

                speed.setText(onlineModel.getSpeedMax()+"");

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

