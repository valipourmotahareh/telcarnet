package com.thimat.sockettelkarnet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thimat.sockettelkarnet.activites.ActEntt;
import com.thimat.sockettelkarnet.activites.ActLoginPasswordApp;
import com.thimat.sockettelkarnet.activites.ActMainActivity;
import com.thimat.sockettelkarnet.activites.ActWaitSmsValidation;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class ActSplash extends AppCompatActivity {
    Context context;
    private final int SPLASH_DISPLAY_LUNCHT = 5000;
    private OperationService mOService;
    private ProgressDialog progressDialog;
    LocalDB localDB;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashload);
        //-----------------------------------------------
        AppCenter.start(getApplication(), "68c7c1ce-fe4b-4f41-969b-ecbc0274b89b",
                Analytics.class, Crashes.class);

        context = getApplicationContext();
        localDB = new LocalDB(context);
        sessionManager = new SessionManager(context);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        try {
            new Handler().postDelayed(new Runnable() {
                                          public void run() {
                                              boolean isFirstRun = sessionManager.getfirstrun();
                                              if (!isFirstRun) {
                                                  // Code to run once
                                                  Intent intent = new Intent(context, ActEntt.class);
                                                  startActivity(intent);
                                                  ActSplash.this.finish();
                                              } else {
//                                                  CheakLogin();
                                                  if (sessionManager.getLoginPass()) {
                                                      Intent intent = new Intent(context, ActLoginPasswordApp.class);
                                                      startActivity(intent);
                                                  } else {
                                                      if (sessionManager.getUser().equals("")){
                                                          Intent intent = new Intent(context, ActWaitSmsValidation.class);
                                                          startActivity(intent);
                                                      }else {
                                                          Intent intent = new Intent(context, ActMainActivity.class);
                                                          startActivity(intent);
                                                      }

                                                  }
                                                  ActSplash.this.finish();
                                              }
                                          }
                                      }
                    , SPLASH_DISPLAY_LUNCHT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    //--------------------------------------------------------cheaklogin----------------------------------
//    private void CheakLogin() {
//        if (!sessionManager.getUser().equals("")){
//            Call<ListCarModel> call = mOService.loginUser(sessionManager.getUser(),sessionManager.getPassword());
//            // Call<ListCarModel> call = mOService.loginUser("4682", "4682");
//            call.enqueue(new Callback<ListCarModel>() {
//                @Override
//                public void onResponse(Call<ListCarModel> call, Response<ListCarModel> response) {
//                    if (response.isSuccessful()) {
//                        if (response.body().getCarModels().size() > 0) {
//                            localDB.SaveCarModel(response.body().getCarModels());
//                            sessionManager.setPerson_Id(response.body().getCarModels().get(0).getPerson_id());
//                        }else{
//                            localDB.DeleteAllCar();
//                        }
//
//                    } else {
//                        if (response.code() == 404) {
//                            Crouton.makeText(Act_spalshload.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
//                        } else {
//                            Crouton.makeText(Act_spalshload.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ListCarModel> call, Throwable t) {
//                    Crouton.makeText(Act_spalshload.this, getText(R.string.error_fail_server).toString(), Style.ALERT).show();
//
//                }
//            });
//        }
//
//    }

}