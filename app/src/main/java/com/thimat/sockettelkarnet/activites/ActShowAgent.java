package com.thimat.sockettelkarnet.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.adapter.RealmAdapterAgent;
import com.thimat.sockettelkarnet.dialogActivity.DialogFilterAgent;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.AgentModel;
import com.thimat.sockettelkarnet.models.ListagentModel;
import com.thimat.sockettelkarnet.R;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActShowAgent extends AppCompatActivity {
    @BindView(R.id.list_agent)
    RecyclerView list_agent;
    @BindView(R.id.btn_refresh)
    ImageView btn_refresh;
    @BindView(R.id.scrollView)
    HorizontalScrollView horizontalspecial;
    @BindView(R.id.search)
    TextView search;
    private OperationService mOService;
    private ProgressDialog progressDialog;
    LocalDB localDB;
    Context context;
    RealmAdapterAgent realmAdapterAgent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_showagent);
        ButterKnife.bind(this);
        context=getApplicationContext();
        localDB=new LocalDB(context);
        //------------------------------------------------------------- horizontalspecial
        horizontalspecial.postDelayed(() -> horizontalspecial.fullScroll(HorizontalScrollView.FOCUS_RIGHT),1L);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        //-----------------------------------------------
        RealmResults<AgentModel> agentModels=localDB.GetAgent();
        if (agentModels.size()==0)
        {
            getAgent();
        }else {
            ShowsNews();
        }

    }
    //------------------------------------------------------- search
    @OnClick(R.id.search)
    public void search(View view){
        Intent intent=new Intent(context, DialogFilterAgent.class);
        startActivityForResult(intent,1);
    }
    //---------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            int code_city=data.getIntExtra("code_city",1);
            RealmResults<AgentModel> agentModels=localDB.GetSearchAgent(code_city);
            realmAdapterAgent=new RealmAdapterAgent(agentModels,true,context);
            list_agent.setLayoutManager(new LinearLayoutManager(context));
            list_agent.setAdapter(realmAdapterAgent);
        }
    }

    //------------------------------------------------------- get news from ealm and show in list---------
    private void ShowsNews(){
        RealmResults<AgentModel> agentModels=localDB.GetAgent();
        realmAdapterAgent=new RealmAdapterAgent(agentModels,true,context);
        list_agent.setLayoutManager(new LinearLayoutManager(context));
        list_agent.setAdapter(realmAdapterAgent);
    }
    //------------------------------------------------------- btn_refresh
    @OnClick(R.id.btn_refresh)
    public void btn_refresh(View view){
        getAgent();
    }
    //--------------------------------------------------------getNews----------------------------------
    private void getAgent(){
        Call<ListagentModel> call = mOService.GetAgent();
        displayProgress(getString(R.string.toast_msg_check_data));
        call.enqueue(new Callback<ListagentModel>() {
            @Override
            public void onResponse(Call<ListagentModel> call, Response<ListagentModel> response) {
                if (response.isSuccessful()) {
                    localDB.SaveAgent(response.body().getAgentModels());
                    successProgress();
                } else {
                    if (response.code() == 404) {
                        errorProgress();
                        Toast.makeText(ActShowAgent.this, getText(R.string.error_fail_server).toString(), Toast.LENGTH_LONG).show();
                    } else {
                        errorProgress();
                        Toast.makeText(ActShowAgent.this, getText(R.string.error_fail_server).toString(), Toast.LENGTH_LONG).show();
                    }
                }
                ShowsNews();
            }
            @Override
            public void onFailure(Call<ListagentModel> call, Throwable t) {
                errorProgress();
                Toast.makeText(ActShowAgent.this, getText(R.string.error_fail_server).toString(),Toast.LENGTH_LONG).show();
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
