package com.thimat.sockettelkarnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.thimat.sockettelkarnet.activites.ActMapActivity;
import com.thimat.sockettelkarnet.activites.MapViewActivity;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.Utilities;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.CarModel;
import com.thimat.sockettelkarnet.models.Responce;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.realm.RealmResults;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by motahreh on 8/29/2015.
 */
public class MySmsReceiver extends BroadcastReceiver {
    String PHONENUMBER;
    Context Context = null;
    SessionManager sessionManager;
    LocalDB localDB;
    String senderNum = "";
    OperationService mOservice;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive","onReceiveSMS");

        this.Context = context;
        sessionManager = new SessionManager(context);
        localDB = new LocalDB(context);
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOservice = mSProvider.getOpService();
        //-----------------------------------------

        try {
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");


                String message = "";
                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    senderNum = phoneNumber;
                    message += currentMessage.getDisplayMessageBody();
                    Log.e("msg from", senderNum + "message:" + message);

                } // end for loopSessionManager
                int is = message.indexOf("TCN");
                PhoneNumberUtil pnu = PhoneNumberUtil.createInstance(context);
                PHONENUMBER = localDB.Searchcellphone(senderNum.trim().replace(" ", ""));
                PhoneNumberUtil.MatchType mt = pnu.isNumberMatch(sessionManager.getPhonenumbr(), senderNum);
                if (mt == PhoneNumberUtil.MatchType.NSN_MATCH || mt == PhoneNumberUtil.MatchType.EXACT_MATCH) {
                    try {
                        int resID=context.getResources().getIdentifier("beep", "raw",context.getPackageName());
                         MediaPlayer mPlayer = MediaPlayer.create(context,resID);
                         mPlayer.start();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    returntype type = new returntype();
                    String thistype = type.returntype(message, context);
                    String date = Utilities.getCurrentShamsidate();
                    String time = Get_Time();

                    try {
                        localDB.SaveMessage(thistype, message, senderNum, date,time, false);
                        if (thistype.equals(context.getString(R.string.Current_location)) ||
                                thistype.equals(context.getString(R.string.Last_Location))) {
                            Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,12}\\,\\d{1,3}\\.\\d{1,12}");
                            Matcher match = pattern.matcher(message);
                            String latlon = "";
                            if (match.find()) {
                                latlon = match.group();
                                Log.d("test", latlon);
                                String[] result = latlon.split(",");
                                double lat = Double.parseDouble(result[0]);
                                double lon = Double.parseDouble(result[1]);
                                if (Build.VERSION.SDK_INT > 27) {
                                    Intent intentmapactivity = new Intent(context, MapViewActivity.class);
                                    intentmapactivity.putExtra("lat", lat);
                                    intentmapactivity.putExtra("lon", lon);
                                    intentmapactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intentmapactivity);
                                }else{
                                    Intent intentmapactivity = new Intent(context, ActMapActivity.class);
                                    intentmapactivity.putExtra("lat", lat);
                                    intentmapactivity.putExtra("lon", lon);
                                    intentmapactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intentmapactivity);
                                }


                            }

                        } else if (thistype.equals(context.getString(R.string.txt_user))) {
                            UpdateCar(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
              Log.e("mySms", "error: " + ex.getMessage());
        }
    }

    //-------------------------------------------------------- update car
    public void UpdateCar(String message) {
        String[] arr = message.split("\n");
        String sn = "";
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].contains("use")) {
                String[] user = arr[i].split(":");
                sessionManager.setUser(user[1].trim());
            } else if (arr[i].contains("Pas")) {
                String[] pass = arr[i].split(":");
                sessionManager.setpassword(pass[1].trim());
            } else if (arr[i].contains("SN")) {
                String[] snstring = arr[i].split(":");
                sn = snstring[1].trim();
            }
        }
        localDB.SearhCarModel(sessionManager.getPhonenumbr(), sn);
        SaveCar(CreateJson());
    }

    //---------------------------------------------- send car to webservice
    public void SaveCar(String jsonstring) {
        retrofit2.Call<Responce> call = mOservice.Savecar(jsonstring);
        call.enqueue(new Callback<Responce>() {
            @Override
            public void onResponse(retrofit2.Call<Responce> call, Response<Responce> response) {
                if (response.isSuccessful()) {
                   // CheakLogin();
                }

            }

            @Override
            public void onFailure(retrofit2.Call<Responce> call, Throwable t) {
                t.getMessage();
                //CheakLogin();
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

    //-------------------------------------------------------------- get time
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String Get_Time() {
        Calendar calander = Calendar.getInstance();
        int cHour = calander.get(Calendar.HOUR);
        int cMinute = calander.get(Calendar.MINUTE);
        int cSecond = calander.get(Calendar.SECOND);
        String time=cHour+":"+cMinute+":"+cSecond;
        return time;
    }
    //--------------------------------------------------------cheaklogin----------------------------------
//    private void CheakLogin() {
//        if (!sessionManager.getUser().equals("")){
//            retrofit2.Call<ListCarModel> call = mOservice.loginUser(sessionManager.getUser(),sessionManager.getPassword());
//            // Call<ListCarModel> call = mOService.loginUser("4682", "4682");
//            call.enqueue(new Callback<ListCarModel>() {
//                @Override
//                public void onResponse(retrofit2.Call<ListCarModel> call, Response<ListCarModel> response) {
//                    if (response.isSuccessful()) {
//                        if (response.body().getCarModels().size() > 0) {
//                            localDB.SaveCarModel(response.body().getCarModels());
//                            sessionManager.setPerson_Id(response.body().getCarModels().get(0).getPerson_id());
//                            Intent intent = new Intent(Context, Act_MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.putExtra("EXIT", true);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            Context.startActivity(intent);
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onFailure(retrofit2.Call<ListCarModel> call, Throwable t) {
//                }
//            });
//        }
//
//    }

}

