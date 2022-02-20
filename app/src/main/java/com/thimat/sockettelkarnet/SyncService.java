package com.thimat.sockettelkarnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.CarModel;
import com.thimat.sockettelkarnet.models.Responce;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmResults;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncService extends BroadcastReceiver {
    SessionManager sessionManager;
    LocalDB localDB;
    OperationService mOservice;

    @Override
    public void onReceive(Context context, Intent intent) {
        sessionManager = new SessionManager(context);
        localDB = new LocalDB(context);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOservice = mSProvider.getOpService();
        RealmResults<CarModel> carModels = localDB.GetCars(0);
        if(carModels.size()>0){
            SaveCar(CreateJson());
        }
    }

    //---------------------------------------------- send car to webservice
    public void SaveCar(String jsonstring) {
        retrofit2.Call<Responce> call = mOservice.Savecar(jsonstring);
        call.enqueue(new Callback<Responce>() {
            @Override
            public void onResponse(retrofit2.Call<Responce> call, Response<Responce> response) {
                if (response.isSuccessful()) {

                }

            }

            @Override
            public void onFailure(retrofit2.Call<Responce> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    //---------------------------------------------- create json
    public String CreateJson() {
        RealmResults<CarModel> carModels = localDB.GetCars(0);
        JSONObject jsoncarmodel = new JSONObject();
        JSONArray carmodelArray = new JSONArray();
        try {
            for (int i = 0; i < carModels.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("car_code", carModels.get(i).getCar_code());
                jsonObj.put("use", sessionManager.getUser());
                jsonObj.put("pas", sessionManager.getPassword());
                jsonObj.put("model", carModels.get(i).getModel());
                jsonObj.put("name", carModels.get(i).getCar_name());
                jsonObj.put("phone_device", carModels.get(i).getPhone().trim());
                carmodelArray.put(jsonObj);
            }
            jsoncarmodel.put("CarModel", carmodelArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String json = jsoncarmodel.toString();
        return json;
    }
}
