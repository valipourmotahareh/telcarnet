package com.thimat.sockettelkarnet.dialogActivity;

import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.localDb.LocalDB;
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

public class DialogPower extends AppCompatActivity {
    @BindView(R.id.linear_caroff)
    LinearLayout linear_caroff;
    @BindView(R.id.img_caroff)
    ImageView img_caroff;
    @BindView(R.id.linear_caron)
    LinearLayout linear_caron;
    @BindView(R.id.img_caron)
    ImageView img_caron;
    @BindView(R.id.linear_son)
    LinearLayout linear_son;
    @BindView(R.id.img_son)
    ImageView img_son;
    @BindView(R.id.linear_sof)
    LinearLayout linear_sof;
    @BindView(R.id.img_sof)
    ImageView img_sof;
    @BindView(R.id.line2)
    View line2;
    SessionManager sessionManager;
    SendSMS sendSMS;
    Context context;
    LocalDB localDB;
    carOnlineModel car_onlineModel;
    ShowDialog showDialog;
    Socket_Connect socket_connect;
    Handler uiHandler;
    boolean pressKey = false;
    String sendMessage = "";
    String textSms = "";
    String sendSms = "";
    boolean sendRegister = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_power);
        ButterKnife.bind(this);
        context = getApplicationContext();
        localDB = new LocalDB(context);
        sessionManager = new SessionManager(context);
        sendSMS = new SendSMS(DialogPower.this);
        showDialog = new ShowDialog(DialogPower.this);

        //---------------------------------------------------
        if (sessionManager.getTcn_v200()) {
            linear_sof.setVisibility(View.GONE);
            linear_son.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
        }


    }

    //---------------------------------------------------- linear_caroff
    @OnClick(R.id.linear_caroff)
    public void linear_caroff(View view) {
        img_caroff.setVisibility(View.VISIBLE);
        img_caron.setVisibility(View.INVISIBLE);
        pressKey = true;

        SendMessage("*ID*" + sessionManager.getUser(), "*caroff", true);
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*caroff";
        sendSms = "*open";


    }

    //---------------------------------------------------- linear_caron
    @OnClick(R.id.linear_caron)
    public void linear_caron(View view) {
        img_caron.setVisibility(View.VISIBLE);
        img_caroff.setVisibility(View.INVISIBLE);
        pressKey = true;

        SendMessage("*ID*" + sessionManager.getUser(), "*caron", true);
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*caron";
        sendSms = "*caron";

    }

    //--------------------------------------------------- linear_son
    @OnClick(R.id.linear_son)
    public void linear_son(View view) {
        img_son.setVisibility(View.VISIBLE);
        img_sof.setVisibility(View.INVISIBLE);

        pressKey = true;
        SendMessage("*ID*" + sessionManager.getUser(), "*son", true);
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*son";
        sendSms = "*son";

    }

    //------------------------------------------------ linear_sof
    @OnClick(R.id.linear_sof)
    public void linear_sof(View view) {
        img_sof.setVisibility(View.VISIBLE);
        img_son.setVisibility(View.INVISIBLE);
        pressKey = true;

        SendMessage("*ID*" + sessionManager.getUser(), "*sof", true);

        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*sof";
        sendSms = "*sof";
    }

    //--------------------------------------------------------
    private void registerSocket() {

        SendMessage("*ID*" + sessionManager.getUser(), "", true);
        pressKey = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        uiHandler = new UIHandler();
      //  displayProgress(getString(R.string.display_dialog_wail_location));

        if(socket_connect!=null) socket_connect.socketDisconnect();

        socket_connect = new Socket_Connect(this, uiHandler);
        registerSocket();
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
            ChangeStatus(car_onlineModelList, true);

        }
    }

    // ---------------------------------------------------------------- change status button
    public void ChangeStatus(List<carOnlineModel> onlineModels, boolean socketInfo) {
        successProgress();
        if (onlineModels.size() > 0) {
            carOnlineModel onlineModel = onlineModels.get(0);
            if (onlineModel.getStart() == 0) {
                img_son.setVisibility(View.VISIBLE);
                img_sof.setVisibility(View.INVISIBLE);
            } else {
                img_sof.setVisibility(View.VISIBLE);
                img_son.setVisibility(View.INVISIBLE);
            }
            if (onlineModel.getRele() == 1) {
                img_caroff.setVisibility(View.VISIBLE);
                img_caron.setVisibility(View.INVISIBLE);
            } else {
                img_caroff.setVisibility(View.INVISIBLE);
                img_caron.setVisibility(View.VISIBLE);
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
