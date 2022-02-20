package com.thimat.sockettelkarnet;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.thimat.sockettelkarnet.classes.Jalali;
import com.thimat.sockettelkarnet.classes.SessionManager;
import com.thimat.sockettelkarnet.classes.Util;
import com.thimat.sockettelkarnet.geofencing.GeofenceTransitionsIntentService;
import com.thimat.sockettelkarnet.localDb.LocalDB;
import com.thimat.sockettelkarnet.models.CarModel;
import com.thimat.sockettelkarnet.models.GeofenceModel;
import com.thimat.sockettelkarnet.models.ListCarModel;
import com.thimat.sockettelkarnet.rest.ClientConfigs;
import com.thimat.sockettelkarnet.rest.OperationService;
import com.thimat.sockettelkarnet.rest.ServiceProvider;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarningService extends Service {
    Context context;
    SessionManager sessionManager;
    private OperationService mOService;
    LocalDB localDB;
    private final String IP = "94.130.71.170";
    private final int PORT = 8080;
    //------------------------------------------
    private PrintWriter output;
    private BufferedReader input;
    Socket socket;

    public WarningService(Context applicationContext) {
        super();
        this.context = applicationContext;

    }

    public WarningService() {
    }

    public int counter = 0;
    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 10 minutes
        timer.schedule(timerTask, 0,10000); //
//        timer.schedule(timerTask, 0, 100000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
                GetCars();

            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //--------------------------------------------------- get online
    private void GetCars() {
        sessionManager = new SessionManager(getApplicationContext());
        localDB = new LocalDB(getApplicationContext());
        //---------------------------------------------------
        ServiceProvider mSProvider = new ServiceProvider(ServiceProvider.WEBSERVICE_GENERAL, ClientConfigs.timeout);
        mOService = mSProvider.getOpService();
        if (!sessionManager.getUser().equals("")) {
            Call<ListCarModel> call = mOService.loginUser(sessionManager.getUser(), sessionManager.getPassword());
            call.enqueue(new Callback<ListCarModel>() {
                @Override
                public void onResponse(Call<ListCarModel> call, Response<ListCarModel> response) {
                    try {
                        if (response.isSuccessful()) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss");
                            Date date1 = new Date();
                            sessionManager.setPerson_Id(response.body().getCarModels().get(0).getPerson_id());
                            //---------------------------------------------
                            if (response.body().getCarModels().size() > 0) {
                                for (int i = 0; i < response.body().getCarModels().size(); i++) {
                                    CarModel Newcarmodel = response.body().getCarModels().get(i);
                                    CarModel carModel = localDB.Searchcarlast(Newcarmodel.getCar_code());
                                    try {
                                        date1 = simpleDateFormat.parse(Newcarmodel.getTstmp());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    //--------------------------------------------- for cheack geofencing
                                    RealmResults<GeofenceModel> geofenceModel = localDB.SearchGeo(Newcarmodel.getCar_code());
                                    if (geofenceModel.size()>0){
                                        for (int geo=0;geo<geofenceModel.size();geo++){
                                                if (geofenceModel.get(geo).getLat() != null) {
                                                    float[] results = new float[1];
                                                    Intent intent =new  Intent(getApplicationContext(), GeofenceTransitionsIntentService.class);
                                                    PendingIntent.getService(
                                                            getApplicationContext(),
                                                            0,
                                                            intent,
                                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                                    Location.distanceBetween(geofenceModel.get(geo).getLat(), geofenceModel.get(geo).getLon(),
                                                            Newcarmodel.getLat(), Newcarmodel.getLng(), results);
                                                    float distanceInMeters = results[0];
                                                    if (distanceInMeters<geofenceModel.get(geo).getRadius()-10 && Newcarmodel.getStart()==1
                                                    && !geofenceModel.get(geo).getFlag()) { // if distance < 0.1
                                                        //   launch the activity
                                                        localDB.SaveWarningModel(Newcarmodel.getCar_code(), Newcarmodel.getId(), Newcarmodel.getCar_name(),
                                                                Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1],
                                                                getApplicationContext().getString(R.string.warning_Enter)+" "+geofenceModel.get(geo).getNamegeo());
                                                        localDB.UpdateFlag(geofenceModel.get(geo).getId(),true);
                                                        if (sessionManager.getActDeactNotif()) {
                                                            Util.sendNotification(getApplicationContext(), getApplicationContext().getString(R.string.warning_Enter)
                                                                    , Newcarmodel.getCar_name() + "   " + Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1]);
                                                        }
                                                    }
                                                    else if (distanceInMeters>geofenceModel.get(geo).getRadius()+10  && Newcarmodel.getStart()==1
                                                            && geofenceModel.get(geo).getFlag()){
                                                        localDB.SaveWarningModel(Newcarmodel.getCar_code(), Newcarmodel.getId(), Newcarmodel.getCar_name(),
                                                                Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1],
                                                                getApplicationContext().getString(R.string.warning_exit)+" "+geofenceModel.get(geo).getNamegeo());
                                                        localDB.UpdateFlag(geofenceModel.get(geo).getId(),false);
                                                        if (sessionManager.getActDeactNotif()) {
                                                            Util.sendNotification(getApplicationContext(), getApplicationContext().getString(R.string.warning_exit)
                                                                    , Newcarmodel.getCar_name() + "   " + Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1]);
                                                        }
                                                    }
                                                }
                                        }
                                    }
                                    //-------------------------------------------------------------------------------Start
                                    if (carModel.getStart() == 0 && Newcarmodel.getStart() == 1) {
                                        localDB.SaveWarningModel(Newcarmodel.getCar_code(), Newcarmodel.getId(), Newcarmodel.getCar_name(),
                                                Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1],
                                                getApplicationContext().getString(R.string.warning_switch_on));
                                        if (sessionManager.getActDeactNotif()) {
                                            Util.sendNotification(getApplicationContext(), getApplicationContext().getString(R.string.warning_switch_on)
                                                    , Newcarmodel.getCar_name() + "   " + Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1]);
                                        }
                                    } else if (carModel.getStart() == 1 && Newcarmodel.getStart() == 0) {
                                        localDB.SaveWarningModel(Newcarmodel.getCar_code(), Newcarmodel.getId(), Newcarmodel.getCar_name(),
                                                Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1],
                                                getApplicationContext().getString(R.string.warning_switch_off));
                                        if (sessionManager.getActDeactNotif()) {
                                            Util.sendNotification(getApplicationContext(), getApplicationContext().getString(R.string.warning_switch_off)
                                                    , Newcarmodel.getCar_name() + "   " + Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1]);
                                        }
                                    }
                                    //-------------------------------------------------------------------------------Ultra_sensor
                                    if (carModel.getUltra_sensor() == 0 && Newcarmodel.getUltra_sensor() == 1) {
                                        localDB.SaveWarningModel(Newcarmodel.getCar_code(), Newcarmodel.getId(), Newcarmodel.getCar_name(),
                                                Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1],
                                                getApplicationContext().getString(R.string.warning_Ultra_sensor));
                                        if (sessionManager.getActDeactNotif()) {
                                            Util.sendNotification(getApplicationContext(), getApplicationContext().getString(R.string.warning_Ultra_sensor)
                                                    , Newcarmodel.getCar_name() + "   " + Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1]);
                                        }
                                    }
                                    //--------------------------------------------------------------------------------Shock_sensor
                                    if (carModel.getShock_sensor() == 0 && Newcarmodel.getShock_sensor() == 1) {
                                        localDB.SaveWarningModel(Newcarmodel.getCar_code(), Newcarmodel.getId(), Newcarmodel.getCar_name(),
                                                Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1],
                                                getApplicationContext().getString(R.string.warning_getShock_sensor));
                                        if (sessionManager.getActDeactNotif()) {
                                            Util.sendNotification(getApplicationContext(), getApplicationContext().getString(R.string.warning_getShock_sensor)
                                                    , Newcarmodel.getCar_name() + "   " + Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1]);
                                        }
                                    }
                                    //---------------------------------------------------------------------------------battery_connected
                                    if (carModel.getIs_battery_connected() == 0 && Newcarmodel.getIs_battery_connected() == 1) {
                                        localDB.SaveWarningModel(Newcarmodel.getCar_code(), Newcarmodel.getId(), Newcarmodel.getCar_name(),
                                                Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1],
                                                getApplicationContext().getString(R.string.warning_connect_battery));
                                        if (sessionManager.getActDeactNotif()) {
                                            Util.sendNotification(getApplicationContext(), getApplicationContext().getString(R.string.warning_connect_battery)
                                                    , Newcarmodel.getCar_name() + "   " + Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1]);
                                        }
                                    } else if (carModel.getIs_battery_connected() == 1 && Newcarmodel.getIs_battery_connected() == 0) {
                                        localDB.SaveWarningModel(Newcarmodel.getCar_code(), Newcarmodel.getId(), Newcarmodel.getCar_name(),
                                                Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1],
                                                getApplicationContext().getString(R.string.warning_disconnect_battery));
                                        if (sessionManager.getActDeactNotif()) {
                                            Util.sendNotification(getApplicationContext(), getApplicationContext().getString(R.string.warning_disconnect_battery)
                                                    , Newcarmodel.getCar_name() + "   " + Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1]);
                                        }
                                    }
                                    //---------------------------------------------------------------------------------getDoor
                                    if (carModel.getDoor() == 0 && Newcarmodel.getDoor() == 1) {
                                        localDB.SaveWarningModel(Newcarmodel.getCar_code(), Newcarmodel.getId(), Newcarmodel.getCar_name(),
                                                Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1],
                                                getApplicationContext().getString(R.string.warning_open_door));
                                        if (sessionManager.getActDeactNotif()) {
                                            Util.sendNotification(getApplicationContext(), getApplicationContext().getString(R.string.warning_open_door)
                                                    , Newcarmodel.getCar_name() + "   " + Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1]);
                                        }
                                    } else if (carModel.getDoor() == 1 && Newcarmodel.getDoor() == 0) {
                                        localDB.SaveWarningModel(Newcarmodel.getCar_code(), Newcarmodel.getId(), Newcarmodel.getCar_name(),
                                                Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1],
                                                getApplicationContext().getString(R.string.warning_close_door));
                                        if (sessionManager.getActDeactNotif()) {
                                            Util.sendNotification(getApplicationContext(), getApplicationContext().getString(R.string.warning_close_door)
                                                    , Newcarmodel.getCar_name() + "   " + Jalali.getFarsiDate(date1) + " " + Newcarmodel.getTstmp().split(" ")[1]);
                                        }
                                    }

                                }
                            }
                            localDB.SaveCarModel(response.body().getCarModels());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ListCarModel> call, Throwable t) {
                    t.getMessage();
                }
            });
        }

    }

    private double distance(double lat1, double lng1, double lat2, double lng2, double redius) {

        double earthRadius = redius; // in miles, change to 6371 for kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist;
    }


}
