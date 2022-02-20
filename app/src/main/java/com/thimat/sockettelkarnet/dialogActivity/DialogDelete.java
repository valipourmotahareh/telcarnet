package com.thimat.sockettelkarnet.dialogActivity;

import static com.thimat.sockettelkarnet.socket.Socket_Connect.SendMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.Responce;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Valipour.Motahareh on 7/25/2018.
 */

public class DialogDelete extends AppCompatActivity {
    @BindView(R.id.dialog_delete_chx_path)
    CheckBox chx_path;
    @BindView(R.id.dialog_set_gps_btn_no)
    Button btn_no;
    @BindView(R.id.dialog_delete_chx_all)
    CheckBox chx_all;
    @BindView(R.id.delete_path)
    LinearLayout delete_path;
    @BindView(R.id.delete_car)
    LinearLayout delete_car;
    @BindView(R.id.dialog_set_gps_btn_yes)
    Button  btn_yes;
    private OperationService mOService;
    private ProgressDialog progressDialog;
    LocalDB localDB;
    Context context;
    int codecar;
    int parameter;
    SendSMS sendSMS;
    SessionManager sessionManager;
    ShowDialog showDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete);
        ButterKnife.bind(this);
        context=getApplicationContext();
        sendSMS = new SendSMS(DialogDelete.this);
        sessionManager=new SessionManager(context);
        showDialog = new ShowDialog(DialogDelete.this);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        localDB=new LocalDB(context);
        getintent();
        //-------------------------------------- delete_path
        delete_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chx_path.setChecked(true);
                chx_all.setChecked(false);
                parameter=0;

            }
        });
        //--------------------------------- delete_car
        delete_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chx_path.setChecked(false);
                chx_all.setChecked(true);
                parameter=1;
            }
        });
        //-------------------------------------------chx_path
        chx_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chx_path.setChecked(true);
                chx_all.setChecked(false);
                parameter=0;
            }
        });
        //-----------------------------------------chx_all
        chx_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chx_path.setChecked(false);
                chx_all.setChecked(true);
                parameter=1;
            }
        });

    }
    //------------------------------------------------- get intent--------
    private void getintent(){
        codecar=getIntent().getExtras().getInt("car_id",0);

    }
    //------------------------------------
    @OnClick(R.id.dialog_set_gps_btn_no)
    public void byn_no(View view){
        finish();
    }
    //---------------------------------------------- send to webservice
    @OnClick(R.id.dialog_set_gps_btn_yes)
     public void SendDelete(View view){
        if (!chx_path.isChecked() && !chx_all.isChecked()){
            Toast.makeText(getApplicationContext(), "یکی از گزینه ها را انتخاب کنید", Toast.LENGTH_LONG).show();
        }else {
            displayProgress(getString(R.string.toast_msg_check_data));
            Call<Responce> call=mOService.delete(codecar,parameter);
            call.enqueue(new Callback<Responce>() {
                @Override
                public void onResponse(Call<Responce> call, Response<Responce> response) {
                    if (response.isSuccessful()){
                        if (parameter==1){
                            localDB.DeleteCar(codecar);


                            SendMessage("*ID*"+sessionManager.getUser(),"rebot",true);

                            SendMessage("*ans*"+sessionManager.getUser()+"*"+sessionManager.getCar_code()+"*"+sessionManager.getPassword()+"*rebot"
                                    ,"*rebot",false);


                        }

                        Toast.makeText(getApplicationContext(), "خودرو با موفقیت حذف شد", Toast.LENGTH_LONG).show();
                        successProgress();
                        Intent intent = getIntent();
                        setResult(1, intent);
                        finish();
                    }

                }

                @Override
                public void onFailure(Call<Responce> call, Throwable t) {
                    errorProgress();
                    Toast.makeText(getApplicationContext(), "خطا در حذف خودرو", Toast.LENGTH_LONG).show();

                }
            });
        }

     }
    //-------------------------------------------------progress---------------
    private void displayProgress(String text) {
        try{
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this);
                progressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(text);
            }
            if (!progressDialog.isShowing()) {
                try{
                    progressDialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
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
