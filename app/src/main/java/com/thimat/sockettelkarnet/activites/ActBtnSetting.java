package com.thimat.sockettelkarnet.activites;

import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.classes.ShowProgrssbar;
import com.thimat.sockettelkarnet.classes.UpdateApp;
import com.thimat.sockettelkarnet.dialogActivity.ActDefineLoginPass;
import com.thimat.sockettelkarnet.dialogActivity.DialogSettingSound;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;
import com.thimat.sockettelkarnet.socket.Socket_Connect;

import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ActBtnSetting extends AppCompatActivity {
    @BindView(R.id.add_delete_user)
    LinearLayout add_delete_user;
    @BindView(R.id.define_new_car)
    LinearLayout define_new_car;
    @BindView(R.id.set_max_speed)
    LinearLayout set_max_speed;
    @BindView(R.id.password_login)
    LinearLayout password_login;
    @BindView(R.id.setting_error)
    LinearLayout setting_error;
    @BindView(R.id.change_password_pannel)
    LinearLayout change_password_pannel;
    @BindView(R.id.resetting)
    LinearLayout resetting;
    @BindView(R.id.loginform)
    LinearLayout loginform;
    @BindView(R.id.update)
    LinearLayout update;
    @BindView(R.id.settingsound)
    LinearLayout settingsound;
    @BindView(R.id.active_deactive_notif)
    Switch activeDeactiveNotif;
    Context context;
    SessionManager sessionManager;
    SendSMS sendSMS;
    LocalDB localDB;
    int car_id;
    private OperationService mOService;
    private PackageInfo info;
    ShowDialog showDialog;
    MediaPlayer mp;
    boolean pressKey = false;
    String sendMessage = "";
    String sendSms = "";
    Socket_Connect socket_connect;
    Handler uiHandler;
    boolean sendRegister=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btn_setting);
        context = getApplicationContext();
        ButterKnife.bind(this);
        sessionManager = new SessionManager(context);
        sendSMS = new SendSMS(ActBtnSetting.this);
        showDialog = new ShowDialog(ActBtnSetting.this);
        localDB = new LocalDB(context);
        car_id = getIntent().getIntExtra("id", 0);
        mp = MediaPlayer.create(this, R.raw.click);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        try {
            PackageManager manager = getPackageManager();
            info = manager.getPackageInfo(getPackageName(), 0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        activeDeactiveNotif.setChecked(sessionManager.getActDeactNotif());
        //-----------------------------------------------------------
        activeDeactiveNotif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sessionManager.setActDeactNotif(isChecked);
            }
        });

    }

    //-------------------------------------------------------------- settingsound
    @OnClick(R.id.settingsound)
    public void linear_settingsound(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }

        Intent intent = new Intent(ActBtnSetting.this, DialogSettingSound.class);
        startActivity(intent);
    }

    //-------------------------------------------------------------- update
    @OnClick(R.id.update)
    public void linear_update(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        initDownload();
    }

    //--------------------------------------------------------------- resetting
    @OnClick(R.id.resetting)
    public void btn_resetting(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        confirm();
    }

    //----------------------------------------------------confirm
    public void confirm() {
        final android.app.AlertDialog.Builder confirm = new android.app.AlertDialog.Builder(ActBtnSetting.this);
        confirm.setTitle("ارسال پیام");
        confirm.setMessage("آیا پیام ارسال شود؟");
        confirm.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!sessionManager.getPhonenumbr().equals("")) {

                    SendMessage("*ID*"+sessionManager.getUser(),"*rebot",true);

                    pressKey = true;
                    sendMessage ="*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*rebot";
                    sendSms ="*rebot";
                    localDB.DeleteAll();
                    Intent intentback = new Intent(getApplicationContext(), ActEntt.class);
                    startActivity(intentback);
                    sessionManager.setfirstrun(false);
                    sessionManager.setTcn_v200(false);
                    sessionManager.setTcn_v300(false);
                    sessionManager.setUser("");
                    finish();
                } else {
                    showDialog.ShowDialog(getString(R.string.dialog_no_phonenumber));
                }
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

    //--------------------------------------------------------------- loginform
    @OnClick(R.id.loginform)
    public void loginform(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActBtnSetting.this, ActLoginProfile.class);
        startActivity(intent);
        finish();
    }

    //---------------------------------------------------------------- add_delete_user
    @OnClick(R.id.add_delete_user)
    public void add_delete_user(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActBtnSetting.this, ActAddDeleteUser.class);
        intent.putExtra("id", car_id);
        startActivity(intent);
    }

    //---------------------------------------------------------------- define_new_car
    @OnClick(R.id.define_new_car)
    public void define_new_car(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActBtnSetting.this, ActShowAndNewCar.class);
        startActivity(intent);
    }

    //---------------------------------------------------------------- set_max_speed
    @OnClick(R.id.set_max_speed)
    public void set_max_speed(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(context, SpeedActivity.class);
        startActivity(intent);

    }

    //---------------------------------------------------------------- password_login
    @OnClick(R.id.password_login)
    public void password_login(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActBtnSetting.this, ActDefineLoginPass.class);
        startActivity(intent);
    }

    //---------------------------------------------------------------- setting_error
    @OnClick(R.id.setting_error)
    public void setting_error(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActBtnSetting.this, ActSettingErrors.class);
        startActivity(intent);
    }

    //---------------------------------------------------------------- change_password_pannel
    @OnClick(R.id.change_password_pannel)
    public void change_password_pannel(View view) {
        if (sessionManager.GetSoundButton()) {
            mp.start();
        }
        Intent intent = new Intent(ActBtnSetting.this, ActChangePasswordPanel.class);
        startActivity(intent);

    }

    //--------------------------------------------------------------------initDownload
    private void initDownload() {
        final ShowProgrssbar showDialog = new ShowProgrssbar(ActBtnSetting.this);
        showDialog.displayProgress(getString(R.string.toast_msg_get_data));
        Call<ResponseBody> call = mOService.downloadFile();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Properties props = new Properties();
                    String version = "1";
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        props.load(response.body().byteStream());
                        version = props.getProperty("version", "1");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (Integer.parseInt(version) > info.versionCode) {
                        UpdateApp upGrade = new UpdateApp();
                        upGrade.setContext(ActBtnSetting.this);
                        upGrade.execute(ClientConfigs.updateURL);
                        showDialog.successProgress();
                    } else {
                        Crouton.makeText(ActBtnSetting.this, R.string.use_last_version, Style.CONFIRM).show();
                        showDialog.successProgress();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Crouton.makeText(ActBtnSetting.this, getText(R.string.error_update_software)
                        .toString(), Style.ALERT).show();
                showDialog.errorProgress();
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
