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

import androidx.annotation.Nullable;
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

public class ActChangePasswordPanel extends AppCompatActivity {
    @BindView(R.id.buttonsend)
    Button buttonsend;
    @BindView(R.id.buttonshowpass)
    Button buttonshowpass;
    @BindView(R.id.editTextpass)
    EditText editTextpass;
    SendSMS sendSMS;
    Context context;
    SessionManager sessionManager;
    ShowDialog showDialog;
    boolean sendRegister = false;
    Socket_Connect socket_connect;
    Handler uiHandler;
    boolean pressKey = false;
    String sendMessage = "";
    String sendSms = "";
    private ProgressDialog progressDialog;
    private boolean firstRun=true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_change_password_pannel);
        context = getApplicationContext();
        ButterKnife.bind(this);
        sessionManager = new SessionManager(context);
        sendSMS = new SendSMS(ActChangePasswordPanel.this);
        showDialog = new ShowDialog(ActChangePasswordPanel.this);
    }

    //--------------------------------------------------------- buttonsend passwordpanel
    @OnClick(R.id.buttonsend)
    public void buttonsend(View view) {

        SendMessage("*ID*" + sessionManager.getUser(), "*npas" + editTextpass.getText().toString(), true);
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*npas" + editTextpass.getText().toString();
        sendSms = "*npas" + editTextpass.getText().toString();
        pressKey = true;

    }

    //---------------------------------------------------------- buttonshowpass panel
    @OnClick(R.id.buttonshowpass)
    public void buttonshowpass(View view) {
        SendMessage("*ID*" + sessionManager.getUser(), "pas?", true);

        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*pas?";
        sendSms = "*pas?";
        pressKey = true;

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
                Log.e("Act_ChangePassword", msg.toString());

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

            if (onlineModels.size() > 0) {
                carOnlineModel onlineModel = onlineModels.get(0);
                if (firstRun) {
                    firstRun=false;
                    editTextpass.setText(onlineModel.getPass());
                }
                if (onlineModel.getPass().equals(editTextpass.getText().toString())){
                    sessionManager.setpassword(onlineModel.getPass());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
