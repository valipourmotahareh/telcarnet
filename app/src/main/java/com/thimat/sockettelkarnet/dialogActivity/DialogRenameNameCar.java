package com.thimat.sockettelkarnet.dialogActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.
        ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rey.material.widget.Button;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.CarModel;
import com.thimat.sockettelkarnet.models.Responce;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Valipour.Motahareh on 7/26/2018.
 */

public class DialogRenameNameCar extends AppCompatActivity {
    @BindView(R.id.dialog_set_gps_btn_no)
    Button btn_no;
    @BindView(R.id.dialog_set_gps_btn_yes)
    Button  btn_yes;
    @BindView(R.id.car_name)
    EditText car_name;
    @BindView(R.id.code_car)
    EditText code_car;
    @BindView(R.id.spinnermodel)
    Spinner spinnermodel;
    @BindView(R.id.edt_phonenumber)
    EditText edt_phonenumber;
    private OperationService mOService;
    private ProgressDialog progressDialog;
    LocalDB localDB;
    Context context;
    String codecar;
    String namecar;
    int model;
    String phone_device;
    int car_id;
    boolean getTcn_v200=true;
    boolean getTcn_v300=false;
    int modelcar=1;
    int count = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_renamecar);
        ButterKnife.bind(this);
        context=getApplicationContext();
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        localDB=new LocalDB(context);
        getintent();
        //----------------------------------------
        car_name.setText(namecar);
        code_car.setText(codecar+"");
        edt_phonenumber.setText(phone_device);
        //-----------------------------------------
        String[] arraySpinner = new String[]{
                "ردیاب", "دزدگیرـ ردیاب"
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spinnermodel.setAdapter(adapter);
        if (model==1){
            spinnermodel.setPrompt(arraySpinner[0]);
            spinnermodel.setSelection(0);
        }else if (model==2){
            spinnermodel.setPrompt(arraySpinner[1]);
            spinnermodel.setSelection(1);
        }
        spinnermodel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int count=0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (count>0){
                    if (position==0){
                        getTcn_v200=true;
                        getTcn_v300=false;
                        modelcar=1;
                    }else if (position==1){
                        getTcn_v200=false;
                        getTcn_v300=true;
                        modelcar=2;
                    }
                }else {
                    modelcar=model;
                }
                count++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
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
    //------------------------------------------------- get intent--------
    private void getintent(){
        car_id=getIntent().getIntExtra("car_id",0);
        codecar=getIntent().getExtras().getString("car_code");
        namecar=getIntent().getExtras().getString("car_name");
        model=getIntent().getExtras().getInt("car_model");
        phone_device=getIntent().getExtras().getString("phone_device");
    }
    //------------------------------------
    @OnClick(R.id.dialog_set_gps_btn_no)
    public void byn_no(View view){
        finish();
    }
    //---------------------------------------------- send to webservice
    @OnClick(R.id.dialog_set_gps_btn_yes)
    public void SendDelete(View view){
            displayProgress(getString(R.string.toast_msg_check_data));
            String json=CreateJson();
            Call<Responce> call=mOService.rename(json);
            call.enqueue(new Callback<Responce>() {
                @Override
                public void onResponse(Call<Responce> call, Response<Responce> response) {
                    if (response.isSuccessful()){
                        try {
                           localDB.RenameCar(car_id,car_name.getText().toString());
                            Toast.makeText(getApplicationContext(), "تغییر نام خودرو با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                            successProgress();
                            Intent intent = getIntent();
                            setResult(1, intent);
                            finish();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Responce> call, Throwable t) {
                    errorProgress();
                    Toast.makeText(getApplicationContext(), "تغییر نام با خطا مواجه شد", Toast.LENGTH_LONG).show();

                }
            });
    }
    //--------------------------------------------------- send ticket json
    public String CreateJson() {
        RealmResults<CarModel> carModels=localDB.GetCars();
        JSONObject jsonObj = new JSONObject();
        if (carModels.size()>0){
            try {
                jsonObj.put("car_id",car_id);
                jsonObj.put("name",car_name.getText().toString());
                jsonObj.put("phone_device", edt_phonenumber.getText().toString().replace(" ",""));
                jsonObj.put("model_device",modelcar);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObj.toString();
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
                    cursor = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                    if (cursor != null && cursor.moveToNext()) {
                        String phone = cursor.getString(0);
                        edt_phonenumber.setText(phone);
                        count = 0;
                        // Do something with phone
                    }

            }
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
