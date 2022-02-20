package com.thimat.sockettelkarnet.activites;

import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.socket.Socket_Connect;

/**
 * Created by motahreh on 10/15/2015.
 */
public class Sharj extends AppCompatActivity {
    public  String PHONENUMBER;
    public  EditText edit1;
    public EditText edit2;
    SendSMS sendSMS;
    Context context;
    SessionManager sessionManager;
    ShowDialog showDialog;
    Socket_Connect socket_connect;
    Handler uiHandler;
    boolean pressKey = false;
    String sendMessage = "";
    String textSms = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharj);
        context=getApplicationContext();
        sessionManager=new SessionManager(context);
        sendSMS=new SendSMS(Sharj.this);
        showDialog = new ShowDialog(Sharj.this);
        SharedPreferences shared =getSharedPreferences("Prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        PHONENUMBER = shared.getString("phonenumber", "");
        TextView txt1=(TextView)findViewById(R.id.txt1);
        TextView  txt2=(TextView)findViewById(R.id.txt2);
        TextView txt3=(TextView)findViewById(R.id.txt3);
        edit1=(EditText)findViewById(R.id.edit1);
        edit1.setText("141");
        edit2=(EditText)findViewById(R.id.edit2);
        Button send=(Button)findViewById(R.id.ok);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressKey = true;
                SendMessage("*ID*" + sessionManager.getUser(), "*cal*"+edit1.getText().toString()+"*"+edit2.getText().toString()+"#", true);
                sendMessage = "*ans*" + sessionManager.getUser() + "*" + sessionManager.getCar_code() + "*" + sessionManager.getPassword() + "*cal*"+edit1.getText().toString()+"*"+edit2.getText().toString()+"#";
                textSms = "*cal*"+edit1.getText().toString()+"*"+edit2.getText().toString()+"#";
            }
        });

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
