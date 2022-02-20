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

import com.thimat.sockettelkarnet.adapter.RealmAdapterGuide;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.GuideModel;
import com.thimat.sockettelkarnet.models.ListguideModel;
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

public class ActShowGuide extends AppCompatActivity {
    @BindView(R.id.list_guide)
    RecyclerView list_guide;
    @BindView(R.id.btn_refresh)
    ImageView  btn_refresh;
    private OperationService mOService;
    private ProgressDialog progressDialog;
    LocalDB localDB;
    Context context;
    RealmAdapterGuide realmAdapterGuide;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_showguide);
        ButterKnife.bind(this);
        context=getApplicationContext();
        localDB=new LocalDB(context);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        //-----------------------------------------------
        RealmResults<GuideModel> guideModels=localDB.GetGuide();
        if (guideModels.size()==0){
            getGuide();
        }else {
            ShowGuide();
        }
    }
    //------------------------------------------------------- btn_refresh
    @OnClick(R.id.btn_refresh)
    public void btn_refresh(View view){
        getGuide();
    }
    //------------------------------------------------------- get news from ealm and show in list---------
    private void ShowGuide(){
        RealmResults<GuideModel> guideModels=localDB.GetGuide();
        realmAdapterGuide=new RealmAdapterGuide(guideModels,true,context);
        list_guide.setLayoutManager(new LinearLayoutManager(context));
        list_guide.setAdapter(realmAdapterGuide);
    }
    //--------------------------------------------------------getNews----------------------------------
    private void getGuide(){
        Call<ListguideModel> call = mOService.GetGuide();
        displayProgress(getString(R.string.toast_msg_check_data));
        call.enqueue(new Callback<ListguideModel>() {
            @Override
            public void onResponse(Call<ListguideModel> call, Response<ListguideModel> response) {
                if (response.isSuccessful()) {
                    localDB.SaveGuide(response.body().getGuideModels());
                    successProgress();
                } else {
                    if (response.code() == 404) {
                        errorProgress();
                        Crouton.makeText(ActShowGuide.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                    } else {
                        errorProgress();
                        Crouton.makeText(ActShowGuide.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                    }
                }
                ShowGuide();
            }
            @Override
            public void onFailure(Call<ListguideModel> call, Throwable t) {
                errorProgress();
                Crouton.makeText(ActShowGuide.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                ShowGuide();
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
