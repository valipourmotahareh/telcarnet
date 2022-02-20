package com.thimat.sockettelkarnet.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thimat.sockettelkarnet.adapter.RealmAdapterCars;
import com.thimat.sockettelkarnet.classes.SendSMS;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.ShowDialog;
import com.thimat.sockettelkarnet.classes.ShowProgrssbar;
import com.thimat.sockettelkarnet.classes.Util;
import com.thimat.sockettelkarnet.dialogActivity.DialogAddCar;
import com.thimat.sockettelkarnet.dialogActivity.DialogRenameNameCar;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.CarModel;
import com.thimat.sockettelkarnet.models.ListCarModel;
import com.thimat.sockettelkarnet.models.Responce;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActShowAndNewCar extends AppCompatActivity {
    @BindView(R.id.list_car)
    RecyclerView list_car;
    @BindView(R.id.fabicon)
    FloatingActionButton fabicon;
    LocalDB localDB;
    Context context;
    RealmResults<CarModel> carModels;
    RealmAdapterCars realmAdapterCars;
    private OperationService mOService;
    private ProgressDialog progressDialog;
    SendSMS sendSMS;
    SessionManager sessionManager;
    ShowDialog showDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_showandnewcar);
        context = getApplicationContext();
        ButterKnife.bind(this);
        localDB = new LocalDB(context);
        carModels = localDB.GetCars();
        sendSMS = new SendSMS(ActShowAndNewCar.this);
        showDialog = new ShowDialog(ActShowAndNewCar.this);
        sessionManager = new SessionManager(context);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
    }

    //----------------------------------------------------------- fab icon
    @OnClick(R.id.fabicon)
    public void Fab_Icon(View view) {
        Intent intent = new Intent(ActShowAndNewCar.this, DialogAddCar.class);
        startActivityForResult(intent, 1);
    }

    //------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            GetCars();
        }
    }

    //--------------------------------------------------------cheaklogin----------------------------------
    private void CheakLogin() {
        if (!sessionManager.getUser().equals("")) {
            Call<ListCarModel> call = mOService.loginUser(sessionManager.getUser(), sessionManager.getPassword());
            ShowProgrssbar showProgrssbar = new ShowProgrssbar(ActShowAndNewCar.this);
            showProgrssbar.displayProgress(getString(R.string.toast_msg_check_data));
            call.enqueue(new Callback<ListCarModel>() {
                @Override
                public void onResponse(Call<ListCarModel> call, Response<ListCarModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCarModels().size() > 0) {
                            localDB.SaveCarModel(response.body().getCarModels());
                            sessionManager.setPerson_Id(response.body().getCarModels().get(0).getPerson_id());
                            GetCars();
                        }
                        showProgrssbar.successProgress();
                    } else {
                        showProgrssbar.errorProgress();
                        if (response.code() == 404) {
                            Crouton.makeText(ActShowAndNewCar.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                        } else {
                            Crouton.makeText(ActShowAndNewCar.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListCarModel> call, Throwable t) {
                    Crouton.makeText(ActShowAndNewCar.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                    showProgrssbar.errorProgress();
                    GetCars();
                }
            });
        } else {
            GetCars();
        }

    }

    //----------------------------------------------------------- fill list_name
    public void GetCars() {
        carModels = localDB.GetCars();
        realmAdapterCars = new RealmAdapterCars(carModels, false, context, (idcar, car_code) -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("idcar", idcar);
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }, (car_code, car_id, car_name, model, phone) -> {
            Intent intent = new Intent(context, DialogRenameNameCar.class);
            intent.putExtra("car_code", car_code);
            intent.putExtra("car_id", car_id);
            intent.putExtra("car_name", car_name);
            intent.putExtra("car_model", model);
            intent.putExtra("phone_device", phone);
            startActivityForResult(intent, 1);
        }, (car_id, sync) -> showLocationDialog(car_id, 1, sync));
        list_car.setLayoutManager(new LinearLayoutManager(this));
        list_car.setAdapter(realmAdapterCars);
    }

    //-------------------------------------------------------------- dialog for Exit Button
    private void showLocationDialog(int car_id, int parameter, int sync) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActShowAndNewCar.this, R.style.MyDialogTheme);
        builder.setTitle(context.getString(R.string.dialog_title));
        builder.setMessage(context.getString(R.string.dialog_message));

        String positiveText = "بله";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sync == 1) {
                            DeleteCar(car_id, parameter);
                        } else {
                            localDB.DeleteCar(car_id);
                            GetCars();
                        }

                    }
                });
        String negativeText = "خیر";
        builder.setNegativeButton(negativeText,
                (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //-------------------------------------------------------------- delete all information car
    public void DeleteCar(int codecar, int parameter) {
        displayProgress(getString(R.string.toast_msg_check_data));
        Call<Responce> call = mOService.delete(codecar, parameter);
        call.enqueue(new Callback<Responce>() {
            @Override
            public void onResponse(Call<Responce> call, Response<Responce> response) {
                if (response.isSuccessful()) {
                    successProgress();
                    localDB.DeleteCar(codecar);
                    Toast.makeText(getApplicationContext(), "خودرو با موفقیت حذف شد", Toast.LENGTH_LONG).show();
                    if (localDB.GetCars().size() <= 0) {
                        Intent intentback = new Intent(getApplicationContext(), ActEntt.class);
                        startActivity(intentback);
                        sessionManager.setfirstrun(false);
                        sessionManager.setTcn_v200(false);
                        sessionManager.setTcn_v300(false);
                        finish();
                    } else {
                        GetCars();
                    }

                }

            }

            @Override
            public void onFailure(Call<Responce> call, Throwable t) {
                errorProgress();
                Toast.makeText(getApplicationContext(), "خطا در حذف خودرو", Toast.LENGTH_LONG).show();

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
    protected void onPostResume() {
        super.onPostResume();
        //------------------------------------------------------------ get cars
        if (Util.checkInternet(context)) {
            CheakLogin();
        } else {
            GetCars();
        }
    }
}
