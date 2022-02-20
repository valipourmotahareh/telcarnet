package com.thimat.sockettelkarnet.activites;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.adapter.RealmAdapterGeofencing;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.GeofenceModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class ActShowGeofencing extends AppCompatActivity {
    @BindView(R.id.list_geo)
    RecyclerView list_geo;
    Context context;
    LocalDB localDB;
    RealmAdapterGeofencing realmAdapterGeofencing;
    RealmResults<GeofenceModel> geofenceModels;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_showgeofencing);
        context=getApplicationContext();
        ButterKnife.bind(this);
        localDB=new LocalDB(context);
        GetGeofencing();

    }
    //-------------------------------------------------- getgeofencing
    public void GetGeofencing(){
        geofenceModels=localDB.GetGeofencing();
        realmAdapterGeofencing=new RealmAdapterGeofencing(geofenceModels,true, ActShowGeofencing.this);
        list_geo.setLayoutManager(new LinearLayoutManager(this));
        list_geo.setAdapter(realmAdapterGeofencing);


    }
}
