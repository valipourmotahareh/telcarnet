package com.thimat.sockettelkarnet.localDb;

import android.content.Context;

import com.thimat.sockettelkarnet.models.AgentModel;
import com.thimat.sockettelkarnet.models.CarModel;
import com.thimat.sockettelkarnet.models.GeofenceModel;
import com.thimat.sockettelkarnet.models.GuideModel;
import com.thimat.sockettelkarnet.models.LatLonCar;
import com.thimat.sockettelkarnet.models.MessageModel;
import com.thimat.sockettelkarnet.models.MessageTicketModel;
import com.thimat.sockettelkarnet.models.NewsModel;
import com.thimat.sockettelkarnet.models.RengeDateModel;
import com.thimat.sockettelkarnet.models.WarningModel;
import com.thimat.sockettelkarnet.models.carOfflineModel;
import com.thimat.sockettelkarnet.models.carOnlineModel;
import com.thimat.sockettelkarnet.models.contactModel;
import com.thimat.sockettelkarnet.models.subjectModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Valipour.Motahareh on 2/14/2018.
 */

public class LocalDB {
    Context context;
    Realm realm;

    public LocalDB(Context context) {
        this.context = context;
        realm = Realm.getDefaultInstance();
    }

    //---------------------------------------------- save Lat&Lon
    public void SaveLatLon(List<LatLonCar> latLonCars) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(LatLonCar.class);
        realm.copyToRealmOrUpdate(latLonCars);
        realm.commitTransaction();
        realm.close();
    }

    //-------------------------------------------- get Lat&Lon
    public RealmResults<LatLonCar> GetLatLonCar() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LatLonCar> results = realm.where(LatLonCar.class).findAll();
        realm.close();
        return results;
    }

    //--------------------------------------------- add CarModel
    public void SaveCarModel(List<CarModel> carModels) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if (realm.where(CarModel.class) != null) {
            if (realm.where(CarModel.class).equalTo("sync", 0).findAll().size() <= 0) {
                realm.where(CarModel.class).findAll().deleteAllFromRealm();
            }
        }
        for (int i = 0; i < carModels.size(); i++) {
            if (realm.where(CarModel.class).equalTo("car_code", carModels.get(i).getCar_code()) != null) {
                CarModel searchmodel = realm.where(CarModel.class).equalTo("car_code", carModels.get(i).getCar_code()).findFirst();
                if (searchmodel != null) {
                    searchmodel.deleteFromRealm();
                }
            }
            carModels.get(i).setSync(1);
//            if (!carModels.get(i).getPhone().equals("")) {
//                if (carModels.get(i).getPhone().substring(0, 1).equals("0")) {
//                    carModels.get(i).setPhone(carModels.get(i).getPhone().trim().replace(" ", "").replaceFirst("0", "+98"));
//                } else {
//                    carModels.get(i).setPhone(carModels.get(i).getPhone().replace(" ", ""));
//                }
//            }
            realm.copyToRealmOrUpdate(carModels.get(i));
        }
        //realm.copyToRealmOrUpdate(carModels);
        realm.commitTransaction();
        realm.close();
    }

    //--------------------------------------------- add CarModel
    public void SaveCar(CarModel carModels) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if (realm.where(CarModel.class).equalTo("car_code", carModels.getCar_code()) != null) {
            CarModel searchmodel = realm.where(CarModel.class).equalTo("car_code", carModels.getCar_code()).findFirst();
            if (searchmodel != null) {
                searchmodel.deleteFromRealm();
            }
        }
        carModels.setSync(1);
        if (!carModels.getPhone().equals("")) {
            if (carModels.getPhone().charAt(0) == '0') {
                carModels.setPhone(carModels.getPhone().trim().replace(" ", "").replaceFirst("0", "+98"));
            } else {
                carModels.setPhone(carModels.getPhone().replace(" ", ""));
            }
        }
        realm.copyToRealmOrUpdate(carModels);
        //realm.copyToRealmOrUpdate(carModels);
        realm.commitTransaction();
        realm.close();
    }

    //---------------------------------------------- add new car local
    public void SaveLocalCar(String car_name, String phone, int model, int sync,
                             int personid) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        CarModel carModel = new CarModel();
        int id = 1;
        if (realm.where(CarModel.class) != null) {
            if (realm.where(CarModel.class).findAll().size() != 0) {
                carModel = realm.where(CarModel.class).contains("phone", phone).findFirst();
                if (carModel == null) {
                    id = realm.where(CarModel.class).findAll().last().getId() + 1;
                    carModel = new CarModel();
                    carModel.setId(id);
                }
            }
        }
        carModel.setCar_name(car_name);
        carModel.setPhone(phone);
        carModel.setModel(model);
        carModel.setSync(sync);
        carModel.setPerson_id(personid);
        realm.copyToRealmOrUpdate(carModel);
        realm.commitTransaction();
        realm.close();
    }

    //-------------------------------------------- search in CarModel using phone and set car_code
    public void SearhCarModel(String cellphone, String car_code) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if (realm.where(CarModel.class) != null) {
            CarModel carModel = realm.where(CarModel.class).contains("phone", cellphone).findFirst();
            if (carModel != null) {
                carModel.setCar_code(car_code);
                realm.copyToRealmOrUpdate(carModel);
            }

        }
        realm.commitTransaction();
        realm.close();
    }

    //-------------------------------------------- search cellphone in CarModel
    public String Searchcellphone(String cellphone) {
        Realm realm = Realm.getDefaultInstance();
        String phone = "";
        if (realm.where(CarModel.class) != null) {
            if (realm.where(CarModel.class).findAll().size() > 0) {
                CarModel carModel = realm.where(CarModel.class).contains("phone" +
                        "" +
                        "", cellphone).findFirst();
                if (carModel != null) {
                    phone = carModel.getPhone();
                }

            }
        }

        realm.close();
        return phone;
    }

    //-------------------------------------------- search cellphone in CarModel and return name
    public String CarName(String cellphone) {
        Realm realm = Realm.getDefaultInstance();
        String phone = "";
        if (realm.where(CarModel.class) != null) {
            if (realm.where(CarModel.class).findAll().size() > 0) {
                CarModel carModel = realm.where(CarModel.class).contains("phone", cellphone).findFirst();
                if (carModel != null) {
                    phone = carModel.getCar_name();
                }

            }
        }

        realm.close();
        return phone;
    }

    //-------------------------------------------- search IDCAR in CarModel and return name
    public String GetCarName(int id) {
        Realm realm = Realm.getDefaultInstance();
        String namecar = "بدون نام";
        if (realm.where(CarModel.class) != null) {
            if (realm.where(CarModel.class).findAll().size() > 0) {
                CarModel carModel = realm.where(CarModel.class).equalTo("id", id).findFirst();
                if (carModel != null) {
                    namecar = carModel.getCar_name();
                }

            }
        }

        realm.close();
        return namecar;
    }
    //-------------------------------------------- search car_code in CarModel and return name
    public String GetCarName(String car_code) {
        Realm realm = Realm.getDefaultInstance();
        String namecar = "بدون نام";
        if (realm.where(CarModel.class) != null) {
            if (realm.where(CarModel.class).findAll().size() > 0) {
                CarModel carModel = realm.where(CarModel.class).equalTo("car_code",car_code).findFirst();
                if (carModel != null) {
                    namecar = carModel.getCar_name();
                }

            }
        }

        realm.close();
        return namecar;
    }
    //-------------------------------------------- get CarModel
    public RealmResults<CarModel> GetCars() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CarModel> realmResults = realm.where(CarModel.class).findAll();
        realm.close();
        return realmResults;
    }

    //-------------------------------------------- get CarModel
    public int GetpersonID() {
        int personid = 0;
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<CarModel> realmResults = realm.where(CarModel.class)
                    .equalTo("sync", 1).notEqualTo("person_id", 0).findAll();
            realm.close();
            personid = realmResults.get(0).getPerson_id();
        } catch (Exception ex) {
            personid = 0;
        }
        return personid;
    }

    //-------------------------------------------- get last row carmodel using car_code
    public CarModel Searchcarlast(String car_code) {
        CarModel carModel = new CarModel();
        try {
            Realm realm = Realm.getDefaultInstance();
            if (realm.where(CarModel.class).equalTo("car_code", car_code).findAll().size() > 0) {
                carModel = realm.where(CarModel.class).equalTo("car_code", car_code).findAll().sort("tstmp", Sort.DESCENDING).last();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return carModel;
    }

    //-------------------------------------------- save WarningModel
    public void SaveWarningModel(String car_code, int id, String name, String date, String txtwarning) {
        Realm realm = Realm.getDefaultInstance();
        WarningModel warningModel = new WarningModel();
        warningModel.setCar_code(car_code);
        if (realm.where(WarningModel.class).findAll().size() > 0) {
            int lastid = realm.where(WarningModel.class).findAll().last().getId() + 1;
            warningModel.setId(lastid);
        } else {
            warningModel.setId(1);
        }
        warningModel.setIdcar(id);
        warningModel.setCar_name(name);
        warningModel.setDate(date);
        warningModel.setTxtwarning(txtwarning);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(warningModel);
        realm.commitTransaction();
        realm.close();
    }

    //-------------------------------------------- show WarningModel
    public RealmResults<WarningModel> GetWarning() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<WarningModel> warningModel = realm.where(WarningModel.class).findAll().sort("date",
                Sort.DESCENDING);
        return warningModel;
    }

    //--------------------------------------------- delete warning
    public void DeleteWarning(int id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(WarningModel.class).equalTo("id", id).findFirst().deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    //--------------------------------------------- delete all warning
    public void DeleteAllWarning() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(WarningModel.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    //-------------------------------------------- get CarModel
    public int GetCod_ticket() {
        int codeticket = 0;
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<CarModel> realmResults = realm.where(CarModel.class).equalTo("sync", 1).findAll();
            realm.close();
            codeticket = realmResults.get(0).getCod_ticket();
        } catch (Exception ex) {
            ex.printStackTrace();
            codeticket = 0;
        }
        return codeticket;
    }

    //------------------------------------------------ get MessageTicketModel
    public subjectModel GetReadTicketus(int id) {
        Realm realm = Realm.getDefaultInstance();
        subjectModel readTickets = realm.where(subjectModel.class).equalTo("id", id).findFirst();
        realm.close();
        return readTickets;
    }

    //-------------------------------------------- get carmodeloffline
    public RealmResults<CarModel> GetCars(int sync) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CarModel> carModels = realm.where(CarModel.class).equalTo("sync", sync)
                .isNotNull("car_code").findAll();
        return carModels;
    }

    //------------------------------------------- rename car
    public void RenameCar(int car_id, String namecar) {
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(CarModel.class).equalTo("id", car_id).findFirst() != null) {
            CarModel carModel = realm.where(CarModel.class).equalTo("id", car_id).findFirst();
            realm.beginTransaction();
            carModel.setCar_name(namecar);
            realm.copyToRealmOrUpdate(carModel);
            realm.commitTransaction();
            realm.close();
        }

    }

    //------------------------------------------ delete car
    public void DeleteCar(int car_id) {
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(CarModel.class).equalTo("id", car_id).findFirst() != null) {
            realm.beginTransaction();
            realm.where(CarModel.class).equalTo("id", car_id).findAll().deleteAllFromRealm();
            realm.commitTransaction();
            realm.close();
        }
    }

    //--------------------------------------------- delete all car--------------------
    public void DeleteAllCar() {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.where(CarModel.class).equalTo("sync", 1).findAll().deleteAllFromRealm();
            realm.commitTransaction();
            realm.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //-------------------------------------------- get CarModel filter code
    public String Getcellphone(int car_id) {
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(CarModel.class) != null) {
            CarModel realmResults = realm.where(CarModel.class).equalTo("id", car_id).findFirst();
            realm.close();
            return realmResults.getPhone();
        } else {
            return "";
        }

    }

    //-------------------------------------------- get CarModel filter code
    public CarModel GetCarsID(int id) {
        Realm realm = Realm.getDefaultInstance();
        CarModel realmResults = new CarModel();
        if (realm.where(CarModel.class).equalTo("id", id) != null) {
            realmResults = realm.where(CarModel.class).equalTo("id", id).findFirst();
        }
        realm.close();
        return realmResults;
    }

    //-------------------------------------------- save online in realm
    public void SaveOnline(List<carOnlineModel> onlineModels) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(carOnlineModel.class);
        realm.copyToRealmOrUpdate(onlineModels);
        realm.commitTransaction();
        realm.close();
    }

    //-------------------------------------------- get online from realm
    public RealmResults<carOnlineModel> GetOnline(int car_code) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<carOnlineModel> results = realm.where(carOnlineModel.class).equalTo("car_id", car_code).findAll();
        realm.close();
        return results;
    }

    //-------------------------------------------- get online from realm
    public carOnlineModel Getcar_onlineModel(int car_code) {
        Realm realm = Realm.getDefaultInstance();
        carOnlineModel results = realm.where(carOnlineModel.class).equalTo("car_id", car_code).findFirst();
        realm.close();
        return results;
    }

    //-------------------------------------------- get CarModel filter code
    public CarModel GetCarCode(int car_code) {
        Realm realm = Realm.getDefaultInstance();
        CarModel realmResults = null;
        try {
            realmResults = realm.where(CarModel.class).equalTo("id", car_code).findFirst();
            realm.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return realmResults;
    }

    //-------------------------------------------- save car_offlineModel in realm
    public void SaveCarOffline(List<carOfflineModel> car_offlineModels) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(carOfflineModel.class);
        realm.copyToRealmOrUpdate(car_offlineModels);
        realm.commitTransaction();
        realm.close();
    }

    //----------------------------------------- delete car_offlineModel in realm
    public void Deletecar_offlineModel() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(carOfflineModel.class);
        realm.commitTransaction();
        realm.close();
    }

    //---------------------------------------- get car_offlineModel
    public RealmResults<carOfflineModel> Getcar_offline() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<carOfflineModel> realmResults = realm.where(carOfflineModel.class).findAll();
        realm.close();
        return realmResults;
    }

    //----------------------------------------  save news--------------------
    public void SaveNews(List<NewsModel> newsModels) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(NewsModel.class);
        realm.copyToRealmOrUpdate(newsModels);
        realm.commitTransaction();
        realm.close();
    }

    //--------------------------------------------- get news-----------------
    public RealmResults GetNews() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults realmResults = realm.where(NewsModel.class).findAll().sort("priority");
        realm.close();
        return realmResults;
    }

    //-------------------------------------------- save agent--------------------
    public void SaveAgent(List<AgentModel> agentModels) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(AgentModel.class);
        realm.copyToRealmOrUpdate(agentModels);
        realm.commitTransaction();
        realm.close();
    }

    //------------------------------------------ get agent-----------------------
    public RealmResults<AgentModel> GetAgent() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults realmResult = realm.where(AgentModel.class).findAll().sort("cod_city").sort("priority");
        realm.close();
        return realmResult;
    }

    //------------------------------------------ get agent city_name-----------------------
    public RealmResults<AgentModel> GetAgentcity() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults realmResult = realm.where(AgentModel.class).distinct("city").findAll();
        realm.close();
        return realmResult;
    }

    //------------------------------------------ get agent search-----------------------
    public RealmResults<AgentModel> GetSearchAgent(int code_city) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults realmResult = realm.where(AgentModel.class).equalTo("cod_city", code_city)
                .findAll().sort("cod_city").sort("priority");
        realm.close();
        return realmResult;
    }

    //------------------------------------------- get agent using id-----------------
    public AgentModel GetAgentFilter(int id) {
        Realm realm = Realm.getDefaultInstance();
        AgentModel agentModel = realm.where(AgentModel.class).equalTo("id", id).findFirst();
        realm.close();
        return agentModel;
    }

    //------------------------------------------ save GuideModel---------------------
    public void SaveGuide(List<GuideModel> guideModels) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(GuideModel.class);
        realm.copyToRealm(guideModels);
        realm.commitTransaction();
        realm.close();
    }

    //-------------------------------------------- get guideModels
    public RealmResults<GuideModel> GetGuide() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<GuideModel> realmResults = realm.where(GuideModel.class).findAll();
        realm.close();
        return realmResults;
    }

    //---------------------------------------- save contactListModel-----------------
    public void SavecontactModel(List<contactModel> contactModels) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(contactModel.class);
        realm.copyToRealm(contactModels);
        realm.commitTransaction();
        realm.close();
    }

    //--------------------------------------------- get contactModel--------------------
    public RealmResults<contactModel> GetcontactModel() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<contactModel> contactModels = realm.where(contactModel.class).findAll();
        realm.close();
        return contactModels;
    }

    //--------------------------------------------- save MessageModel
    public void SaveMessage(String type, String message, String sendernum, String date, String time,
                            boolean read) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        MessageModel messageModel = new MessageModel();
        if (realm.where(MessageModel.class).findAll() != null) {
            if (realm.where(MessageModel.class).findAll().size() > 0) {
                int id = realm.where(MessageModel.class).findAll().last().getId() + 1;
                messageModel.setId(id);
            } else {
                messageModel.setId(1);
            }
        } else {
            messageModel.setId(1);
        }

        messageModel.setType(type);
        messageModel.setMessage(message);
        messageModel.setDate(date);
        messageModel.setTime(time);
        messageModel.setSendernum(sendernum);
        messageModel.setRead(read);
        realm.copyToRealmOrUpdate(messageModel);
        realm.commitTransaction();
        realm.close();
    }

    //----------------------------------------------- update messageModel
    public void UpdateMessage(int id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        MessageModel messageModel = realm.where(MessageModel.class).equalTo("id", id).findFirst();
        messageModel.setRead(true);
        realm.copyToRealmOrUpdate(messageModel);
        realm.commitTransaction();
        realm.close();
    }

    //-------------------------------------------------- get messagemodel
    public RealmResults<MessageModel> GetMessages() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MessageModel> messageModels = realm.where(MessageModel.class).sort("Time", Sort.DESCENDING).findAll();
        realm.close();
        return messageModels;
    }

    //-------------------------------------------------- delete messageModel
    public void DeleteMessage() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(MessageModel.class);
        realm.commitTransaction();
        realm.close();
    }

    //-------------------------------------------------- save MessageTicketModel
    public void SaveReadTicket(List<MessageTicketModel> readTickets, List<subjectModel> subjectModels) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(MessageTicketModel.class);
        realm.delete(subjectModel.class);
        realm.copyToRealmOrUpdate(subjectModels);
        realm.copyToRealmOrUpdate(readTickets);
        realm.commitTransaction();
        realm.close();
    }

    //------------------------------------------------ get MessageTicketModel
    public RealmResults<subjectModel> GetReadTicket() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<subjectModel> readTickets = realm.where(subjectModel.class).findAll()
                .sort("tstmp", Sort.DESCENDING);
        realm.close();
        return readTickets;
    }

    //------------------------------------------------ get ticket using id
    public RealmResults<MessageTicketModel> GetTicket(int id) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MessageTicketModel> readTicket = realm.where(MessageTicketModel.class).equalTo("ticket_id", id).findAll()
                .sort("tstmp", Sort.DESCENDING);
        realm.close();
        return readTicket;
    }

    //------------------------------------------------ delete All Data
    public void DeleteAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        realm.close();
    }

    //--------------------------------------------------- save GeofenceModel
    public void SaveGeofencing(String id, String car_code, Double Lat, Double Lon, Double Radius, String name) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        GeofenceModel geofenceModel = new GeofenceModel();
        geofenceModel.setId(id);
        geofenceModel.setCar_code(car_code);
        geofenceModel.setLat(Lat);
        geofenceModel.setLon(Lon);
        geofenceModel.setRadius(Radius);
        geofenceModel.setNamegeo(name);
        geofenceModel.setFlag(false);
        realm.copyToRealmOrUpdate(geofenceModel);
        realm.commitTransaction();
        realm.close();
    }
    //----------------------------------------------- update flag geo
    public void UpdateFlag(String id,Boolean flag){
        Realm realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        if (realm.where(GeofenceModel.class).equalTo("id", id).findFirst() != null) {
            GeofenceModel geofenceModel=realm.where(GeofenceModel.class).equalTo("id", id).findFirst();
            geofenceModel.setFlag(flag);
            realm.copyToRealmOrUpdate(geofenceModel);
        }

        realm.commitTransaction();
        realm.close();
    }

    //------------------------------------------------ search in Geofencing
    public RealmResults<GeofenceModel> SearchGeo(String car_code) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<GeofenceModel> geofenceModels=
                realm.where(GeofenceModel.class).equalTo("car_code", car_code).equalTo("actdeac",true)
                        .findAll();
        realm.close();
        return geofenceModels;
    }

    //---------------------------------------------------------delete GeofenceModel
    public void DeleteGeo(String id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if (realm.where(GeofenceModel.class).findAll() != null) {
            if (realm.where(GeofenceModel.class).equalTo("id", id).findFirst() != null) {
                realm.where(GeofenceModel.class).equalTo("id", id).findFirst().deleteFromRealm();
            }
        }
        realm.commitTransaction();
        realm.close();
    }
    //--------------------------------------------------------get geofencings
    public RealmResults<GeofenceModel> GetGeofencing(){
        Realm realm=Realm.getDefaultInstance();
        RealmResults<GeofenceModel> geofenceModels=
                realm.where(GeofenceModel.class).findAll();
        realm.close();
        return  geofenceModels;
    }
    //----------------------------------------------- update actdeac geo
    public void Updateactdeac(String id,Boolean actdeac){
        try{
            Realm realm=Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (realm.where(GeofenceModel.class).equalTo("id", id).findFirst() != null) {
                        GeofenceModel geofenceModel=realm.where(GeofenceModel.class).equalTo("id", id).findFirst();
                        geofenceModel.setActdeac(actdeac);
                        realm.copyToRealmOrUpdate(geofenceModel);
                    }
                }
            });
            realm.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    //----------------------------------------------------------- save Renge Date
    public void InsertRengeDate(List<String> list){
        Realm realm=Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(RengeDateModel.class);
        for (int i=0;i<list.size();i++){
            RengeDateModel rengeDateModel=new RengeDateModel();
            rengeDateModel.setTitle(list.get(i));
            rengeDateModel.setID(i);
            realm.copyToRealm(rengeDateModel);
        }
        realm.commitTransaction();
        realm.close();
    }

    //-------------------------------------------------------------- get Renge Date
    public RealmResults<RengeDateModel> GetRengeDate(){
        Realm realm=Realm.getDefaultInstance();
        RealmResults<RengeDateModel> rengeDateModels=realm.where(RengeDateModel.class).findAll();
        realm.close();
        return rengeDateModels;
    }
}
