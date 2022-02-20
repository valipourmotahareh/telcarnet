package com.thimat.sockettelkarnet.activites;

import static android.provider.ContactsContract.CommonDataKinds;
import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.ListCarModel;
import com.thimat.sockettelkarnet.models.Responce;
import com.thimat.sockettelkarnet.models.carOnlineModel;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;
import com.thimat.sockettelkarnet.socket.Socket_Connect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActEntt extends AppCompatActivity {
    @BindView(R.id.img_traket)
    ImageView img_traket;
    @BindView(R.id.edt_phonenumber)
    EditText edt_phonenumber;
    @BindView(R.id.btn_contact)
    Button btn_contact;
    @BindView(R.id.tcn_200)
    LinearLayout tcn_200;
    @BindView(R.id.tcn_300)
    LinearLayout tcn_300;
    @BindView(R.id.tcn1_200)
    LinearLayout tcn1_200;
    @BindView(R.id.tcn1_300)
    LinearLayout tcn1_300;
    @BindView(R.id.edt_namecar)
    EditText edt_namecar;
    @BindView(R.id.burglaralarm)
    ImageView burglaralarm;
    @BindView(R.id.top)
    LinearLayout top;

    @BindView(R.id.edt_codecar)
    EditText carCode;
    @BindView(R.id.edt_password)
    EditText password;
    @BindView(R.id.edt_sim_card_1)
    EditText simCard1;
    @BindView(R.id.edt_sim_card_2)
    EditText simCard2;
    Context context;
    SessionManager sessionManager;
    private OperationService mOService;
    private ProgressDialog progressDialog;
    LocalDB localDB;
    private final int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
    int count = 0;
    int modelcar = 0;
    ShowDialog showDialog;
    Socket_Connect socket_connect;
    Handler uiHandler;
    MediaPlayer mp;
    boolean sendRegister = false;
    String codeCar="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entt);
        ButterKnife.bind(this);
        context = getApplicationContext();
        localDB = new LocalDB(context);
        sessionManager = new SessionManager(context);
        showDialog = new ShowDialog(ActEntt.this);
        mp = MediaPlayer.create(this, R.raw.click);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        //-------------------------------------------
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
            return;
        }
        //---------------------------------------------- edt_phonenumber click
        edt_phonenumber.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getRawX() >= (edt_phonenumber.getRight() - edt_phonenumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (count == 0) {
                        Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
                        intent.setType(CommonDataKinds.Phone.CONTENT_TYPE);
                        startActivityForResult(intent, 1);
                    }
                    count++;
                    return true;
                }
                count = 0;
                return false;
            }
        });


    }


    //-----------------------------------------------------------------------------loginform
    @OnClick(R.id.top)
    public void loginform(View view) {
        Intent intent = new Intent(ActEntt.this, ActLoginProfile.class);
        startActivity(intent);
        finish();
    }

    //-----------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            //-------------------------------------------
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_SMS_RECEIVE);
                return;
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //--------------------------------------------------------img_traket
    @OnClick(R.id.tcn_200)
    public void Traketclick(View view) {
        sessionManager.setTcn_v200(true);
        sessionManager.setTcn_v300(false);
        tcn_200.setVisibility(View.GONE);
        tcn1_200.setVisibility(View.VISIBLE);
        tcn1_300.setVisibility(View.GONE);
        tcn_300.setVisibility(View.VISIBLE);
        modelcar = 1;
    }

    //---------------------------------------------------------- tcn_300
    @OnClick(R.id.tcn_300)
    public void burglar_alarm(View view) {
        sessionManager.setTcn_v300(true);
        sessionManager.setTcn_v200(false);
        tcn1_300.setVisibility(View.VISIBLE);
        tcn_300.setVisibility(View.GONE);
        tcn_200.setVisibility(View.VISIBLE);
        tcn1_200.setVisibility(View.GONE);
        modelcar = 2;
    }

    //------------------------------------------------------------------onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                    Cursor cursor = null;
                    Uri uri = data.getData();
                    cursor = getContentResolver().query(uri, new String[]{CommonDataKinds.Phone.NUMBER}, null, null, null);
                    if (cursor != null && cursor.moveToNext()) {
                        String phone = cursor.getString(0);
                        if (phone.startsWith("+98")) {
                            phone = phone.replace("+98", "0");
                        } else if (phone.startsWith("98")) {
                            phone = phone.replace("98", "0");

                        }
                        phone = phone.replace("-", "");
                        phone = phone.replace(" ", "");
                        if (phone.trim().length() == 11) {
                            edt_phonenumber.setText(phone.trim());
                        } else {
                            Toast.makeText(context, "شماره موبایل صحیح نمی باشد", Toast.LENGTH_LONG).show();
                        }
                        count = 0;
                        // Do something with phone
                    }
            }
        }
    }

    //------------------------------------------------------ click on btn_contact
    @OnClick(R.id.btn_contact)
    public void btn_contact(View view) {
        mp.start();
        //------------------------------------------------ socket
        if (simCard1.getText().toString().equals("")) {
            Toast.makeText(context, getText(R.string.toast_empty_simCard1), Toast.LENGTH_LONG).show();


        } else if (simCard1.getText().toString().length() != 11) {
            Toast.makeText(context, getText(R.string.toast_length_simCard1), Toast.LENGTH_LONG).show();

        } else if (password.getText().toString().equals("")) {
            Toast.makeText(context, getText(R.string.toast_empty_password), Toast.LENGTH_LONG).show();


        } else if (password.getText().toString().length() != 4) {
            Toast.makeText(context, getText(R.string.toast_count_password), Toast.LENGTH_LONG).show();
        } else if (modelcar == 0) {
            Toast.makeText(context, getText(R.string.txt_entt), Toast.LENGTH_LONG).show();

        } else {
            codeCar=carCode.getText().toString().trim().toLowerCase();
            //---------------------------------------------------Socket
            displayProgress(getString(R.string.display_dialog_wail));
            uiHandler = new UIHandler();

            if(socket_connect!=null) socket_connect.socketDisconnect();

            socket_connect = new Socket_Connect(ActEntt.this, uiHandler);
            registerSocket();
        }
    }

    //--------------------------------------------------------
    private void registerSocket() {

        SendMessage("*ID*" + simCard1.getText().toString().trim(), "", true);
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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void errorProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    //-----------------------------------------------------------------------
    public int getDefaultSimmm() {

        Object tm = context.getSystemService(Context.TELEPHONY_SERVICE);
        Method method_getDefaultSim;
        int defaultSimm = -1;
        try {
            method_getDefaultSim = tm.getClass().getDeclaredMethod("getDefaultSim");
            method_getDefaultSim.setAccessible(true);
            defaultSimm = (Integer) method_getDefaultSim.invoke(tm);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Method method_getSmsDefaultSim;
        int smsDefaultSim = -1;
        try {
            method_getSmsDefaultSim = tm.getClass().getDeclaredMethod("getSmsDefaultSim");
            smsDefaultSim = (Integer) method_getSmsDefaultSim.invoke(tm);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return smsDefaultSim;
    }

    @Override
    protected void onResume() {


        super.onResume();


    }

    //-------------------------------------------------------
    public class UIHandler extends Handler {

        public void handleMessage(Message msg) {
            try {
                String st = msg.obj.toString();
                Log.e("Act_Entt", msg.toString());
                if (msg.arg1 == 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(st);
                    sb.append("    No: ");
                    sb.append(msg.arg2);
                    if (!sb.toString().contains("Send SMS Success!")) {
                        if (sb.toString().contains("*success*Device register successful")) {
                            SendMessage("*ans*" + simCard1.getText().toString().trim() + "*" +
                                    codeCar+ "*" + password.getText().toString() + "*" +
                                    "sav-" + simCard2.getText().toString().trim(), "", true);
                        } else {
                            ProccessMessage(sb.toString(), st);
                        }
                    }

                } else if (msg.arg1 == 0 && msg.arg2 == 0) {
                    registerSocket();
                } else if (msg.arg1 == 0 && msg.arg2 == 9) {
                    showDialog.ShowDialog("فعلا ردیاب در شبکه نیست بعدا سعی کنید");
                } else if (msg.arg2 == 3) {
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
                Gson gson = new Gson();
                carOnlineModel car_onlineModel = gson.fromJson(json.toString(), carOnlineModel.class);
                car_onlineModelList.add(car_onlineModel);
                car_onlineModelList.add(car_onlineModel);
                ChangeStatus(car_onlineModelList);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }

    }

    // ---------------------------------------------------------------- change status button
    public void ChangeStatus(List<carOnlineModel> onlineModels) {
        if (onlineModels.size() > 0) {
            carOnlineModel onlineModel = onlineModels.get(0);
            if (codeCar.equals(onlineModel.getCar_code())) {

                Log.e("json5", onlineModel.getMain_phone());

                if (onlineModel.getCheckRebot().equals("A")) {
                    successProgress();
                    if (onlineModel.getCar_code() != null)
                        sessionManager.setcar_code(onlineModel.getCar_code());

                    sessionManager.setpassword(password.getText().toString().trim());
                    sessionManager.setUser(simCard1.getText().toString().trim());
                    sessionManager.setPhonenumber(edt_phonenumber.getText().toString().trim());
                    sessionManager.setfirstrun(true);
                    SaveCar(CreateJson());

                } else if (onlineModel.getCheckRebot().equals("B")) {

                    successProgress();

                    ShowDialog showDialog = new ShowDialog(context);
                    showDialog.displayProgress(" دستگاه از طریق شماره سیم کارت " + onlineModel.getMain_phone() + " ثبت شده است از گزینه ورود استفاده شود.");

                    Intent intent = new Intent(ActEntt.this, ActLoginProfile.class);
                    sendRegister = true;

                    startActivity(intent);

                    if (onlineModel.getCar_code() != null)
                        sessionManager.setcar_code(onlineModel.getCar_code());

                    sessionManager.setpassword(password.getText().toString().trim());
                    sessionManager.setUser(onlineModel.getPhone4());
                    sessionManager.setPhonenumber(onlineModel.getMain_phone());
//                countDownTimer.cancel();
                    finish();

                }
            }

        }
    }

    //---------------------------------------------- send car to webservice
    public void SaveCar(String jsonstring) {
        retrofit2.Call<Responce> call = mOService.Savecar(jsonstring);
        call.enqueue(new Callback<Responce>() {
            @Override
            public void onResponse(retrofit2.Call<Responce> call, Response<Responce> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(context, ActMainActivity.class);
                    sessionManager.setfirstrun(true);
                    startActivity(intent);
                    finish();
                    sendRegister = true;
                    CheckLogin();

                }

            }

            @Override
            public void onFailure(retrofit2.Call<Responce> call, Throwable t) {
                t.getMessage();
                Toast.makeText(context, "webservice" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //---------------------------------------------- create json
    public String CreateJson() {
        JSONObject jsoncarmodel = new JSONObject();
        JSONArray carmodelArray = new JSONArray();
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("car_code",codeCar);
            jsonObj.put("use", simCard1.getText().toString().trim());
            jsonObj.put("pas", password.getText().toString().trim());
            jsonObj.put("model", modelcar);
            jsonObj.put("name", edt_namecar.getText().toString());
            jsonObj.put("phone_device", edt_phonenumber.getText().toString());
            jsonObj.put("phone2", simCard2.getText().toString().trim());
            carmodelArray.put(jsonObj);

            jsoncarmodel.put("CarModel", carmodelArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String json = jsoncarmodel.toString();
        return json;
    }

    //--------------------------------------------------------cheaklogin----------------------------------
    private void CheckLogin() {
        Call<ListCarModel> call = mOService.loginUser(simCard1.getText().toString().trim(), password.getText().toString().trim());
        displayProgress(getString(R.string.toast_msg_check_data));
        call.enqueue(new Callback<ListCarModel>() {
            @Override
            public void onResponse(Call<ListCarModel> call, Response<ListCarModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCarModels().size() > 0) {

                        sessionManager.setPerson_Id(response.body().getCarModels().get(0).getPerson_id());
                        sessionManager.setcar_code(response.body().getCarModels().get(0).getCar_code());
                        sessionManager.setlastid(response.body().getCarModels().get(0).getId());
                        localDB.SaveCarModel(response.body().getCarModels());

                    }
                }
            }

            @Override
            public void onFailure(Call<ListCarModel> call, Throwable t) {

            }
        });
    }
}

