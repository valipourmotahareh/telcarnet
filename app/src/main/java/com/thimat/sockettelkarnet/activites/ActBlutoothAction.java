package com.thimat.sockettelkarnet.activites;

import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class ActBlutoothAction extends AppCompatActivity {
    @BindView(R.id.blutoothon)
    Button blutoothon;
    @BindView(R.id.blutoothpair)
    Button blutoothpair;
    @BindView(R.id.blutoothoff)
    Button blutoothoff;
    Context context;
    SessionManager sessionManager;
    SendSMS sendSMS;
    ShowDialog showDialog;
    Socket_Connect socket_connect;
    Handler uiHandler;
    boolean pressKey = false;
    String sendMessage = "";
    String textSms = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_blutooth_action);
        ButterKnife.bind(this);
        context = getApplicationContext();
        sessionManager = new SessionManager(context);
        sendSMS = new SendSMS(ActBlutoothAction.this);
        showDialog = new ShowDialog(ActBlutoothAction.this);
    }
    //-----------------------------------------------blutoothon
    @OnClick(R.id.blutoothon)
    public void blutoothon(View view){
        pressKey = true;

        SendMessage("*ID*" + sessionManager.getUser(), "*bon", true);
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*bon";
        textSms = "*bon";
    }
    //------------------------------------------------ blutoothpair
    @OnClick(R.id.blutoothpair)
    public void blutoothpair(View view){
        pressKey = true;

        SendMessage("*ID*" + sessionManager.getUser(), "*bcd", true);
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*bcd";
        textSms = "*bcd";
    }
    //-----------------------------------------------blutoothoff
    @OnClick(R.id.blutoothoff)
    public void blutoothoff(){
        pressKey = true;

        SendMessage("*ID*" + sessionManager.getUser(), "*bof", true);
        sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*bof";
        textSms = "*bof";
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

        if(socket_connect!=null) socket_connect.socketDisconnect();

        socket_connect = new Socket_Connect(this, uiHandler);
        registerSocket();
    }

    public class UIHandler extends Handler {

        public void handleMessage(Message msg) {
            try {
                String st = msg.obj.toString();
                Log.e("Dilaog_Silent", msg.toString());

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
                                        , textSms, false);
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
    protected void onDestroy() {
        super.onDestroy();
    }

}
