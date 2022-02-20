package com.thimat.sockettelkarnet.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.adapter.RealmAdapterContactUs;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.contactListModel;
import com.thimat.sockettelkarnet.models.contactModel;
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

public class ActContactUs extends AppCompatActivity {
    @BindView(R.id.show_contact)
    RecyclerView show_contact;
    @BindView(R.id.btn_refresh)
    ImageView btn_refresh;
    Context context;
    LocalDB localDB;
    RealmAdapterContactUs realmAdapterContatUs;
    private OperationService mOService;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_contactus);
        ButterKnife.bind(this);
        context=getApplicationContext();
        localDB=new LocalDB(context);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        //-----------------------------------------------
        RealmResults<contactModel> contactModels=localDB.GetcontactModel();
        if (contactModels.size()==0){
            getContactUS();
        }else {
            ShowsContact();
        }

    }
    //------------------------------------------------------- btn_refresh
    @OnClick(R.id.btn_refresh)
    public void btn_refresh(View view){
        getContactUS();
    }
    //------------------------------------------------------- get news from ealm and show in list---------
    private void ShowsContact(){
        RealmResults<contactModel> contactModels=localDB.GetcontactModel();
        realmAdapterContatUs=new RealmAdapterContactUs(contactModels,true,context);
        show_contact.setLayoutManager(new LinearLayoutManager(context));
        show_contact.setAdapter(realmAdapterContatUs);
    }
    //--------------------------------------------------------getNews----------------------------------
    private void getContactUS(){
        Call<contactListModel> call = mOService.Getcontact();
        displayProgress(getString(R.string.toast_msg_check_data));
        call.enqueue(new Callback<contactListModel>() {
            @Override
            public void onResponse(Call<contactListModel> call, Response<contactListModel> response) {
                if (response.isSuccessful()) {
                    localDB.SavecontactModel(response.body().getContactModels());
                    successProgress();

                } else {
                    if (response.code() == 404) {
                        errorProgress();
                        Crouton.makeText(ActContactUs.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                    } else {
                        errorProgress();
                        Crouton.makeText(ActContactUs.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                    }
                }
                ShowsContact();
            }
            @Override
            public void onFailure(Call<contactListModel> call, Throwable t) {
                errorProgress();
                Crouton.makeText(ActContactUs.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                ShowsContact();
            }
        });
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
