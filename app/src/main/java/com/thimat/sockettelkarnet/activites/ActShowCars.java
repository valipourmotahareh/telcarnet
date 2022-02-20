package com.thimat.sockettelkarnet.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.adapter.RealmAdapterCars;
import com.thimat.sockettelkarnet.dialogActivity.DialogDelete;
import com.thimat.sockettelkarnet.dialogActivity.DialogRenameNameCar;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.CarModel;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class ActShowCars extends AppCompatActivity {
    @BindView(R.id.list_car)
    RecyclerView list_car;
    LocalDB localDB;
    Context context;
    RealmResults<CarModel> carModels;
    RealmAdapterCars realmAdapterCars;
    private OperationService mOService;
    private ProgressDialog progressDialog;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_showcars);
        context = getApplicationContext();
        ButterKnife.bind(this);
        localDB = new LocalDB(context);
        carModels = localDB.GetCars();
       //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        //------------------------------------------------------------ get cars
        GetCars();
    }
    //----------------------------------------------------------- fill list_name
    public void GetCars() {
        carModels = localDB.GetCars();
        realmAdapterCars = new RealmAdapterCars(carModels, false, context, (idcar, car_code) -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("idcar", idcar);
            returnIntent.putExtra("codecar",car_code);
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }, (car_code, car_id, car_name, model, phone) -> {
            Intent intent = new Intent(context, DialogRenameNameCar.class);
            intent.putExtra("car_code", car_code);
            intent.putExtra("car_id", car_id);
            intent.putExtra("car_name", car_name);
            intent.putExtra("car_model",model);
            intent.putExtra("phone_device",phone);
            startActivityForResult(intent, REQUEST_CODE);
        }, (car_id, sync) -> {
            if (sync == 1) {
                Intent intent = new Intent(context, DialogDelete.class);
                intent.putExtra("car_id", car_id);
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                localDB.DeleteCar(car_id);
            }
        });
        list_car.setLayoutManager(new LinearLayoutManager(this));
        list_car.setAdapter(realmAdapterCars);
    }

}
