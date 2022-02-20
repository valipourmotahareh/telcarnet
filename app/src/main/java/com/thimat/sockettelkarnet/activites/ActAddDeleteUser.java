package com.thimat.sockettelkarnet.activites;

import static android.provider.ContactsContract.CommonDataKinds;
import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.carOnlineModel;
import com.thimat.sockettelkarnet.models.online;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;
import com.thimat.sockettelkarnet.socket.Socket_Connect;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActAddDeleteUser extends AppCompatActivity implements View.OnTouchListener {
    @BindView(R.id.buttonadd)
    Button buttonadd;
    @BindView(R.id.buttonadduser2)
    Button buttonadduser2;
    @BindView(R.id.buttonadduser3)
    Button buttonadduser3;
    @BindView(R.id.buttonadduser4)
    Button buttonadduser4;
    @BindView(R.id.editText)
    EditText editTextuser1;
    @BindView(R.id.editTextuser2)
    EditText editTextuser2;
    @BindView(R.id.editTextuser3)
    EditText editTextuser3;
    @BindView(R.id.editTextuser4)
    EditText editTextuser4;
    @BindView(R.id.buttondel)
    Button buttondel;
    @BindView(R.id.buttondeluser2)
    Button buttondeluser2;
    @BindView(R.id.buttondeluser3)
    Button buttondeluser3;
    @BindView(R.id.buttondeluser4)
    Button buttondeluser4;
    @BindView(R.id.title)
    TextView title;
    Context context;
    LocalDB localDB;
    SessionManager sessionManager;
    SendSMS sendSMS;
    int count = 0;
    ShowDialog showDialog;
    MediaPlayer mp;
    boolean sendRegister=false;
    Socket_Connect socket_connect;
    Handler uiHandler;
    private ProgressDialog progressDialog;
    private OperationService mOService;
    boolean pressKey = false;
    String sendMessage = "";
    String sendSms = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_delete_user);
        ButterKnife.bind(this);
        context = getApplicationContext();
        localDB = new LocalDB(context);
        sessionManager = new SessionManager(context);
        showDialog = new ShowDialog(ActAddDeleteUser.this);
        sendSMS = new SendSMS(context);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();

        mp = MediaPlayer.create(this, R.raw.click);
        try {
            int car_id = getIntent().getIntExtra("id", 0);
            GetDataOnline(car_id);

            //----------------------------------------------------------- editTextuser
            editTextuser1.setOnTouchListener(this);
            editTextuser2.setOnTouchListener(this);
            editTextuser3.setOnTouchListener(this);
            editTextuser4.setOnTouchListener(this);
            //-------------------------------------------------------------

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //------------------------------------------------------------------onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1) {
                Cursor cursor = null;
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, new String[]{CommonDataKinds.Phone.NUMBER}, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    String phone = cursor.getString(0);
                    editTextuser1.setText(validPhone(phone));
                    count = 0;
                    // Do something with phone
                }
            }
            if (requestCode == 2) {
                Cursor cursor = null;
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, new String[]{CommonDataKinds.Phone.NUMBER}, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    String phone = cursor.getString(0);
                    editTextuser2.setText(validPhone(phone));
                    count = 0;
                    // Do something with phone
                }
            }
            if (requestCode == 3) {
                Cursor cursor = null;
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, new String[]{CommonDataKinds.Phone.NUMBER}, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    String phone = cursor.getString(0);
                    editTextuser3.setText(validPhone(phone));
                    count = 0;
                    // Do something with phone
                }
            }
            if (requestCode == 4) {
                Cursor cursor = null;
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, new String[]{CommonDataKinds.Phone.NUMBER}, null, null, null);
                if (cursor != null && cursor.moveToNext()) {
                    String phone = cursor.getString(0);
                    editTextuser4.setText(validPhone(phone));
                    count = 0;
                    // Do something with phone
                }
            }
    }

    //------------------------------------------------------------------- valid phonenumber
    private  String validPhone(String phone){
         phone = phone.trim().replace(" ","");
        if(phone.startsWith("+98")) phone=phone.replace("+98","0");
        if (phone.length()<0 || phone.length()>11) {
            Toast.makeText(context,"شماره تلفن باید 11 رقم باشد" ,Toast.LENGTH_LONG).show();
            return "";
        }
        return  phone;
    }

    //----------------------------------------------- buttonadd
    @OnClick(R.id.buttonadd)
    public void buttonadduser1(View view) {
        if (sessionManager.GetSoundButton()){
            mp.start();
        }

        SendMessage("*ID*"+sessionManager.getUser(),"*mob1*"+editTextuser1.getText().toString(),true);

        pressKey = true;
        sendMessage ="*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*mob1-"+editTextuser1.getText().toString();
        sendSms = "*mob1*"+editTextuser1.getText().toString();


    }

    //-----------------------------------------------buttonadduser2
    @OnClick(R.id.buttonadduser2)
    public void buttonadduser2(View view) {
        if (sessionManager.GetSoundButton()){
            mp.start();
        }

        SendMessage("*ID*"+sessionManager.getUser(),"*mob2*"+editTextuser2.getText().toString(),true);
        pressKey = true;
        sendMessage ="*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*mob2-"+editTextuser2.getText().toString();
        sendSms = "*mob2*"+editTextuser2.getText().toString();
    }

    //---------------------------------------------- buttonadduser3
    @OnClick(R.id.buttonadduser3)
    public void buttonadduser3(View view) {
        if (sessionManager.GetSoundButton()){
            mp.start();
        }
        SendMessage("*ID*"+sessionManager.getUser(),"*mob3*"+editTextuser3.getText().toString(),true);
        pressKey = true;
        sendMessage ="*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*mob3-"+editTextuser3.getText().toString();
        sendSms = "*mob3*"+editTextuser3.getText().toString();

    }

    //--------------------------------------------- buttonadduser4
    @OnClick(R.id.buttonadduser4)
    public void buttonadduser4(View view) {
        if (sessionManager.GetSoundButton()){
            mp.start();
        }
         SendMessage("*ID*"+sessionManager.getUser(),"*mob4*"+editTextuser4.getText().toString(),true);
        pressKey = true;
        sendMessage ="*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*mob4-"+editTextuser4.getText().toString();
        sendSms = "*mob4*"+editTextuser4.getText().toString();

    }

    //----------------------------------------------------------- buttondel
    @OnClick(R.id.buttondel)
    public void buttondel(View view) {
        if (sessionManager.GetSoundButton()){
            mp.start();
        }
        SendMessage("*ID*"+sessionManager.getUser(),"*mob1*del#"+editTextuser1.getText().toString(),true);
        pressKey = true;
        sendMessage ="*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*mob1-del#"+editTextuser1.getText().toString();
        sendSms ="*mob1*del#"+editTextuser1.getText().toString();
    }

    //----------------------------------------------------------- buttondeluser2
    @OnClick(R.id.buttondeluser2)
    public void buttondeluser2(View view) {
        if (sessionManager.GetSoundButton()){
            mp.start();
        }
        SendMessage("*ID*"+sessionManager.getUser(),"*mob2*del#"+editTextuser2.getText().toString(),true);
        pressKey = true;
        sendMessage ="*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*mob2-del#"+editTextuser2.getText().toString();
        sendSms ="*mob2*del#"+editTextuser2.getText().toString();
    }

    //--------------------------------------------------------- buttondeluser3
    @OnClick(R.id.buttondeluser3)
    public void buttondeluser3(View view) {
        if (sessionManager.GetSoundButton()){
            mp.start();
        }
        SendMessage("*ID*"+sessionManager.getUser(),"*mob3*del#"+editTextuser3.getText().toString(),true);
        pressKey = true;
        sendMessage ="*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*mob3-del#"+editTextuser3.getText().toString();
        sendSms ="*mob3*del#"+editTextuser3.getText().toString();
    }

    //--------------------------------------------------------- buttondeluser4
    @OnClick(R.id.buttondeluser4)
    public void buttondeluser4(View view) {
        if (sessionManager.GetSoundButton()){
            mp.start();
        }
        SendMessage("*ID*"+sessionManager.getUser(),"*mob4*del#"+editTextuser4.getText().toString(),true);
        pressKey = true;
        sendMessage ="*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*mob4-del#"+editTextuser4.getText().toString();
        sendSms ="*mob4*del#"+editTextuser4.getText().toString();

    }

    //---------------------------------------------------------------onTouch
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;
        if (v.getId()==R.id.editText){
            if (event.getRawX() >= (editTextuser1.getRight() - editTextuser1.getCompoundDrawables()
                    [DRAWABLE_RIGHT].getBounds().width())) {
                if (count == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent, 1);
                }
                count++;
                return true;
            }
        }
        if (v.getId()==R.id.editTextuser2){
            if (event.getRawX() >= (editTextuser2.getRight() - editTextuser2.getCompoundDrawables()
                    [DRAWABLE_RIGHT].getBounds().width())) {
                if (count == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent, 2);
                }
                count++;
                return true;
            }
        }
        if (v.getId()==R.id.editTextuser3){
            if (event.getRawX() >= (editTextuser3.getRight() - editTextuser3.getCompoundDrawables()
                    [DRAWABLE_RIGHT].getBounds().width())) {
                if (count == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent, 3);
                }
                count++;
                return true;
            }
        }
        if (v.getId()==R.id.editTextuser4){
            if (event.getRawX() >= (editTextuser4.getRight() - editTextuser4.getCompoundDrawables()
                    [DRAWABLE_RIGHT].getBounds().width())) {
                if (count == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent, 4);
                }
                count++;
                return true;
            }
        }

        count = 0;
        return false;
    }

    //-------------------------------------------------------
    public class UIHandler extends Handler {

        public void handleMessage(Message msg) {
            try {
                Log.e("Act_AddDeleteUser", msg.toString());

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
                            sendRegister = false;
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


    // ---------------------------------------------------------------- change status button
    public void ChangeStatus(List<carOnlineModel> onlineModels) {
        if (onlineModels.size() > 0) {
            carOnlineModel onlineModel = onlineModels.get(0);

            if(onlineModel.getPhone1()!=null) {
                if (onlineModel.getPhone1().equals("")) {
                    editTextuser1.setText("+98");
                } else {
                    editTextuser1.setText(onlineModel.getPhone1());
                }
            }
            //----------------------
            if(onlineModel.getPhone2()!=null) {
                if (onlineModel.getPhone2().equals("")) {
                    editTextuser2.setText("+98");
                } else {
                    editTextuser2.setText(onlineModel.getPhone2());
                }
            }
            //------------------------
            if(onlineModel.getPhone3()!=null) {

                if (onlineModel.getPhone3().equals("")) {
                    editTextuser3.setText("+98");
                } else {
                    editTextuser3.setText(onlineModel.getPhone3());
                }
            }
            //----------------------------
            if(onlineModel.getPhone4()!=null) {
                if (onlineModel.getPhone4().equals("")) {
                    editTextuser4.setText("+98");
                } else {
                    editTextuser4.setText(onlineModel.getPhone4());
                }
            }
        }
    }

    //--------------------------------------------------- get online
    public void GetDataOnline(final int id_car) {

            displayProgress(getString(R.string.toast_msg_check_data));
        Call<online> call = mOService.GetOnline(id_car);
        call.enqueue(new Callback<online>() {
            @Override
            public void onResponse(Call<online> call, Response<online> response) {
                if (response.isSuccessful()) {
                        successProgress();

                    localDB.SaveOnline(response.body().getCar_onlineModels());
                    if (response.body().getCar_onlineModels().size() > 0) {
                        ChangeStatus(response.body().getCar_onlineModels());
                    } else {
                        Toast.makeText(context, "دریافت اطلاعات این ماشین با خطا مواجه شده است", Toast.LENGTH_LONG).show();
                    }

                } else {

                        errorProgress();

                }
            }

            @Override
            public void onFailure(Call<online> call, Throwable t) {
                t.printStackTrace();
                    errorProgress();

                try {
                    ChangeStatus(localDB.GetOnline(id_car));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    //-------------------------------------------------progress---------------
    private void displayProgress(String text) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(ActAddDeleteUser.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        uiHandler = new UIHandler();

        if(socket_connect!=null) socket_connect.socketDisconnect();

        socket_connect = new Socket_Connect(this, uiHandler);
        registerSocket();
    }

    //--------------------------------------------------------
    private void registerSocket() {
        SendMessage("*ID*" + sessionManager.getUser(), "", true);
        pressKey = false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
