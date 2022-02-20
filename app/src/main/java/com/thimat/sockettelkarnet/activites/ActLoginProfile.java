package com.thimat.sockettelkarnet.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.rey.material.widget.Button;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.Util;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.ListCarModel;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Valipour.Motahareh on 2/10/2018.
 */

public class ActLoginProfile extends AppCompatActivity {
    @BindView(R.id.activity_login_etxt_pass)
    EditText etxtPass;
    @BindView(R.id.activity_login_etxt_username)
    EditText etxtUsername;
    @BindView(R.id.activity_login_rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.activity_login_chk_remember)
    CheckBox chk_remember;
    @BindView(R.id.line2)
    AppCompatTextView line2;
    @BindView(R.id.line3)
    AppCompatTextView line3;
    @BindView(R.id.line4)
    AppCompatTextView line4;
    @BindView(R.id.activity_restart_again)
    Button activity_restart_again;
    private OperationService mOService;
    Context context;
    String jsonString;
    private ProgressDialog progressDialog;
    LocalDB localDB;
    SessionManager sessionManager;
    //--------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_loginprofile);
        context = getApplicationContext();
        ButterKnife.bind(this);
        localDB = new LocalDB(context);
        sessionManager = new SessionManager(context);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        //-------------------------------------
        etxtUsername.setText(sessionManager.getUser());
        line2.setText(" 1- از"+sessionManager.getUser()+"کلمه *pas را به سیم کارت ردیاب پیامک کنید تا پسورد برای شما ارسال شود.");
        line3.setText("2- کلید سخت افزاری reset دستگاه را ده ثانیه در حال فشرده نگه دارید و دکمه راه اندازی مجدد را کلیک کنید.");
        line4.setText("3- درب خودرو را باز نگه دارید و سوئیچ را 8 بار سریع خاموش و روشن کنید تا دستگاه به تنظیمات اولیه برگردد و دکمه راه اندازی مجدد را کلیک کنید.");

        if (sessionManager.getLoginState()) {
            chk_remember.setChecked(true);
        }

    }

    //--------------------------------------------------------
    @OnClick(R.id.activity_login_btn_login)
    public void loginClick(View view) {
        if (Util.checkInternet(this)) {
            createJsonOrder();
            CheakLogin();
        } else {
            Crouton.makeText(ActLoginProfile.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
        }
    }

    //------------------------------------------------
    @OnClick(R.id.activity_restart_again)
    public void restartAgain(){
        finish();
        Intent intent=new Intent(context, ActEntt.class);
        startActivity(intent);

    }

    //--------------------------------------------------------
    //---------------------------------------------------------createjson--------------------------------
    private void createJsonOrder() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("ApiKey", ClientConfigs.REST_API_KEY);
            jsonObj.put("UserName", etxtUsername.getText().toString().trim());
            jsonObj.put("Password", etxtPass.getText().toString().trim());
            jsonString = jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------cheaklogin----------------------------------
    private void CheakLogin() {
        Call<ListCarModel> call = mOService.loginUser(etxtUsername.getText().toString().trim(), etxtPass.getText().toString().trim());
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

                        successProgress();
                        sessionManager.setLoginState(chk_remember.isChecked());
                        sessionManager.setUser(etxtUsername.getText().toString().trim());
                        sessionManager.setPhonenumber(etxtUsername.getText().toString().trim());
                        sessionManager.setpassword(etxtPass.getText().toString().trim());
                        try {
                            Intent intent = new Intent(context, ActMainActivity.class);
                            startActivity(intent);
                            sessionManager.setfirstrun(true);
                            finish();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        finish();
                    } else {
                        errorProgress();
                        Crouton.makeText(ActLoginProfile.this, getText(R.string.error_login_state).toString(), Style.ALERT).show();
                    }


                } else {
                    if (response.code() == 404) {
                        errorProgress();
                        Crouton.makeText(ActLoginProfile.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                    } else {
                        errorProgress();
                        Crouton.makeText(ActLoginProfile.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ListCarModel> call, Throwable t) {

                errorProgress();
                Crouton.makeText(ActLoginProfile.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
            }
        });
    }

    //-------------------------------------------------progress---------------
    private void displayProgress(String text) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void errorProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


}


