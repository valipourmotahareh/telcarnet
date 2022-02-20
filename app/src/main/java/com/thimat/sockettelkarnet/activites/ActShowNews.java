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

import com.thimat.sockettelkarnet.adapter.RealmAdapterews;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.ListNewsModel;
import com.thimat.sockettelkarnet.models.NewsModel;
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

public class ActShowNews extends AppCompatActivity {
    @BindView(R.id.list_news)
    RecyclerView list_news;
    @BindView(R.id.btn_refresh)
    ImageView btn_refresh;
    private OperationService mOService;
    private ProgressDialog progressDialog;
    LocalDB localDB;
    Context context;
    RealmAdapterews realmAdapterews;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_shownews);
        ButterKnife.bind(this);
        context=getApplicationContext();
        localDB=new LocalDB(context);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        //-----------------------------------------------\
        RealmResults<NewsModel> newsModels=localDB.GetNews();
        if (newsModels.size()==0){
            getnews();
        }else {
            ShowsNews();
        }

    }
    //------------------------------------------------------- btn_refresh
    @OnClick(R.id.btn_refresh)
    public void btn_refresh(View view){
        getnews();
    }
    //------------------------------------------------------- get news from ealm and show in list---------
    private void ShowsNews(){
        RealmResults<NewsModel> newsModels=localDB.GetNews();
        realmAdapterews=new RealmAdapterews(newsModels,false,context);
        list_news.setLayoutManager(new LinearLayoutManager(context));
        list_news.setAdapter(realmAdapterews);
    }
    //--------------------------------------------------------getNews----------------------------------
    private void getnews(){
        Call<ListNewsModel> call = mOService.GetNews();
        displayProgress(getString(R.string.toast_msg_check_data));
        call.enqueue(new Callback<ListNewsModel>() {
            @Override
            public void onResponse(Call<ListNewsModel> call, Response<ListNewsModel> response) {
                if (response.isSuccessful()) {
                    localDB.SaveNews(response.body().getNewsModelList());
                    successProgress();

                } else {
                    if (response.code() == 404) {
                        errorProgress();
                        Crouton.makeText(ActShowNews.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                    } else {
                        errorProgress();
                        Crouton.makeText(ActShowNews.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                    }
                }
                ShowsNews();
            }
            @Override
            public void onFailure(Call<ListNewsModel> call, Throwable t) {
                errorProgress();
                Crouton.makeText(ActShowNews.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
                ShowsNews();
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
