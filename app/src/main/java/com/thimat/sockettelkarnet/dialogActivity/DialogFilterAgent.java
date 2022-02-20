package com.thimat.sockettelkarnet.dialogActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.adapter.RealmAdapterNameCity;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.AgentModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

public class DialogFilterAgent extends AppCompatActivity {
    @BindView(R.id.spinnercity)
    Spinner spinnercity;
    @BindView(R.id.search)
    Button search;
    RealmAdapterNameCity realmAdapterNameCity;
    Context context;
    LocalDB localDB;
    int code_city=1;
    RealmResults<AgentModel> agentModels;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filteragent);
        ButterKnife.bind(this);
        context=getApplicationContext();
        localDB=new LocalDB(context);
        agentModels=localDB.GetAgentcity();
        realmAdapterNameCity=new RealmAdapterNameCity(context);
        realmAdapterNameCity.setData(agentModels);
        spinnercity.setAdapter(realmAdapterNameCity);
        //---------------------------------------------------------- spinnercity
        spinnercity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int count=0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (count>0){
                        code_city=agentModels.get(position).getCod_city();
                    }else {
                        code_city=1;
                        count++;
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //-------------------------------------------- search
    @OnClick(R.id.search)
    public void btn_search(View view){
        Intent intent = getIntent();
        intent.putExtra("code_city",code_city);
        setResult(1, intent);
        finish();
    }
}
