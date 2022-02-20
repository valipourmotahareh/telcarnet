package com.thimat.sockettelkarnet.classes;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Valipour.Motahareh on 5/23/2018.
 */

public class SessionManager {
    // Sharedpref file name
    private static final String PREF_NAME = "ThimatSession";
    public static final String KEY_LOGIN_STATE = "loginstate";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONENUMBR = "phonenumber";
    public static final String KEY_RECEIVESMS = "recivesms";
    public static final String KEY_VER2 = "ver2";
    public static final String KEY_VER3 = "ver3";
    public static final String KEY_ALLGPRS = "allgprs";
    public static final String KEY_DEACTIVEGPRS = "deactivegprs";
    public static final String KEY_TCN_V300 = "Tcn_v300";
    public static final String KEY_TCN_V200 = "Tcn_v200";
    public static final String KEY_FIRSTRUN = "FIRSTRUN";
    public static final String KEY_LOGINPASS="loginpass";
    public static final String KEY_LOGINPASSWORD="loginpassword";
    public static final String KEY_warnin_gopen_door="warningopendoor";
    public static final String KEY_warning_start="warningstart";
    public static final String KEY_warning_Impact_Sensor="warningImpactSensor";
    public static final String KEY_warning_Move_Sensor="warningMoveSensor";
    public static final String KEY_warning_Stealing_battery="warningStealingbattery";
    public static final String KEY_warning_Unauthorized_speed="warningUnauthorizedspeed";
    public static final String KEY_warning_Towing_shift_warning="warningTowingshiftwarning";
    public static final String KEY_ACTIVE_DEACTIVE_NOTIF="activeDeactiveNotif";
    public static final String KEY_Person_Id="person_id";
    public static final String KEY_Car_Code="car_code";
    public static final String KEY_SPEED="speed";
    public static final String KEY_MainPhoneNumber="mainphonenumber";
    public static final String KEY_Last_Select_ID="lastidcar";
    public static final String KEY_Speed_car="speedcar";
    public static final String KEY_SETTING_SOUND_BUTTON="soundbutton";
    public static final String KEY_SETTING_SOUND_ALARM="soundalarm";
    public static final String KEY_SETTING_CONNECT_TYPE="connectType";



    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    //---------------------------------------------------------- get and set KEY_SETTING_SOUND_BUTTON
    public void SetSoundButton(boolean sound)
    {
        editor.putBoolean(KEY_SETTING_SOUND_BUTTON,sound);
        editor.commit();
    }
    public boolean GetSoundButton(){
        return pref.getBoolean(KEY_SETTING_SOUND_BUTTON,true);
    }
    //---------------------------------------------------------- get and set KEY_SETTING_SOUND_ALARM
    public void SetSoundAlarm(boolean sound){
        editor.putBoolean(KEY_SETTING_SOUND_ALARM,sound);
        editor.commit();
    }
    public boolean GetSoundAlarm(){
        return pref.getBoolean(KEY_SETTING_SOUND_ALARM,true);
    }
    //---------------------------------------------------------- get and set KEY_Speed_car
    public void Setspeed_car(int speed){
        editor.putInt(KEY_Speed_car,speed);
        editor.commit();
    }
    public int GetSpeed_car(){
        return pref.getInt(KEY_Speed_car,3);
    }
    //---------------------------------------------------------- get and set KEY_Last_Select_ID
    public void setlastid(int lastid){
        editor.putInt(KEY_Last_Select_ID,lastid);
        editor.commit();
    }
    public int GetLastId(){
        return  pref.getInt(KEY_Last_Select_ID,0);
    }
    //---------------------------------------------------------- get and set KEY_MainPhoneNumber
    public void SetMainPhone(String mainphonenumber){
        editor.putString(KEY_MainPhoneNumber,mainphonenumber);
        editor.commit();
    }
    public String GetMainPhone(){
        return pref.getString(KEY_MainPhoneNumber,"");
    }
    //---------------------------------------------------------- get and set KEY_SPEED
    public void setSpeed(int speed){
        editor.putInt(KEY_SPEED,speed);
        editor.commit();
    }
    public int getspeed(){
        return pref.getInt(KEY_SPEED,0);
    }
    //----------------------------------------------------------- get and set KEY_Person_Id
    public void setPerson_Id(int Person_Id){
        editor.putInt(KEY_Person_Id,Person_Id);
        editor.commit();
    }
    public int getPerson_Id(){
        return pref.getInt(KEY_Person_Id,0);
    }


    //---------------------------------------------------------- get and set KEY_Car_Code
    public void setcar_code(String car_code){
        editor.putString(KEY_Car_Code,car_code);
        editor.commit();
    }
    public String getCar_code(){
        return pref.getString(KEY_Car_Code,"");
    }
    //---------------------------------------------------------- get and set KEY_warning_Unauthorized_speed
    public void SetwarningTowingshiftwarning(Boolean warningTowingshiftwarning){
        editor.putBoolean(KEY_warning_Towing_shift_warning,warningTowingshiftwarning);
        editor.commit();
    }
    public boolean getwarningTowingshiftwarning(){
        return  pref.getBoolean(KEY_warning_Towing_shift_warning,false);
    }
    //---------------------------------------------------------- get and set KEY_ACTIVE_DEACTIVE_NOTIF
    public void setActDeactNotif(Boolean notif){
        editor.putBoolean(KEY_ACTIVE_DEACTIVE_NOTIF,notif);
        editor.commit();
    }
    public boolean getActDeactNotif(){
        return pref.getBoolean(KEY_ACTIVE_DEACTIVE_NOTIF,true);
    }
    //---------------------------------------------------------- get and set KEY_warning_Unauthorized_speed
    public void SetwarningUnauthorizedspeed(Boolean warningUnauthorizedspeed){
        editor.putBoolean(KEY_warning_Unauthorized_speed,warningUnauthorizedspeed);
        editor.commit();
    }
    public boolean getwarningUnauthorizedspeed(){
        return  pref.getBoolean(KEY_warning_Unauthorized_speed,false);
    }
    //---------------------------------------------------------- get and set KEY_warning_Stealing_battery
    public void SetwarningStealingbattery(Boolean warningStealingbattery){
        editor.putBoolean(KEY_warning_Stealing_battery,warningStealingbattery);
        editor.commit();
    }
    public boolean getwarningStealingbattery(){
        return  pref.getBoolean(KEY_warning_Stealing_battery,false);
    }
    //---------------------------------------------------------- get and set KEY_warning_Impact_Sensor
    public void SetwarningImpactSensor(Boolean warningImpactSensor){
        editor.putBoolean(KEY_warning_Impact_Sensor,warningImpactSensor);
        editor.commit();
    }
    public boolean getwarningImpactSensor(){
        return  pref.getBoolean(KEY_warning_Impact_Sensor,false);
    }

    //---------------------------------------------------------- get and set KEY_warning_Impact_Sensor
    public void SetWarningMoveSensor(Boolean warningMoveSensor){
        editor.putBoolean(KEY_warning_Move_Sensor,warningMoveSensor);
        editor.commit();
    }
    public boolean getWarningMoveSensor(){
        return  pref.getBoolean(KEY_warning_Move_Sensor,false);
    }
    //---------------------------------------------------------- get and set KEY_warning_start
    public void Setwarningstart(Boolean warningstart){
        editor.putBoolean(KEY_warning_start,warningstart);
        editor.commit();
    }
    public boolean getwarningstart(){
        return  pref.getBoolean(KEY_warning_start,false);
    }
    //---------------------------------------------------------- get and set KEY_warnin_gopen_door
    public void Setwarningopendoor(Boolean warningopendoor){
        editor.putBoolean(KEY_warnin_gopen_door,warningopendoor);
        editor.commit();
    }
    public boolean getwarningopendoor(){
        return  pref.getBoolean(KEY_warnin_gopen_door,false);
    }
    //---------------------------------------------------------- get and set KEY_LOGINPASSWORD
    public void setLoginPassword(String loginpass){
        editor.putString(KEY_LOGINPASSWORD,loginpass);
        editor.commit();
    }
    public String getLoginPassword(){
        return pref.getString(KEY_LOGINPASSWORD,"");
    }
    //---------------------------------------------------------- get and set KEY_LOGINPASS
    public void setLoginPass(boolean loginpass){
        editor.putBoolean(KEY_LOGINPASS,loginpass);
        editor.commit();
    }
    public boolean getLoginPass(){
        return pref.getBoolean(KEY_LOGINPASS,false);
    }
    //---------------------------------------------------------- get and set KEY_FIRSTRUN
    public boolean getfirstrun() {
        return pref.getBoolean(KEY_FIRSTRUN, false);
    }

    public void setfirstrun(boolean firstrun) {
        editor.putBoolean(KEY_FIRSTRUN, firstrun);
        editor.commit();
    }

    //----------------------------------------------------------- get and set KEY_TCN_V300 ردیاب-دزدگیر modelcar = 2
    public boolean getTcn_v300() {
        return pref.getBoolean(KEY_TCN_V300, false);
    }

    public void setTcn_v300(boolean tcn_v300) {
        editor.putBoolean(KEY_TCN_V300, tcn_v300);
        editor.commit();
    }

    //----------------------------------------------------------- get and set KEY_TCN_V200 ردیاب modelcar = 1
    public boolean getTcn_v200() {
        return pref.getBoolean(KEY_TCN_V200, false);
    }

    public void setTcn_v200(boolean tcn_v200) {
        editor.putBoolean(KEY_TCN_V200, tcn_v200);
        editor.commit();
    }

    // ---------------------------------------------------------- get and set LoginState
    public boolean getLoginState() {
        return pref.getBoolean(KEY_LOGIN_STATE, false);
    }

    public void setLoginState(boolean state) {
        editor.putBoolean(KEY_LOGIN_STATE, state);
        editor.commit();
    }

    //------------------------------------------------------- get and set KEY_ALLGPRS
    public boolean getallgprs() {
        return pref.getBoolean(KEY_ALLGPRS, false);
    }

    public void setallgprs(boolean allgprs) {
        editor.putBoolean(KEY_ALLGPRS, allgprs);
        editor.commit();
    }

    //-------------------------------------------------------- get and set KEY_DEACTIVEGPRS
    public boolean getdeactivegprs() {
        return pref.getBoolean(KEY_DEACTIVEGPRS, false);
    }

    public void setdeactivegprs(boolean deactivegprs) {
        editor.putBoolean(KEY_DEACTIVEGPRS, deactivegprs);
        editor.commit();
    }

    //-------------------------------------------------------- get and set UserName
    public void setUser(String name) {
        editor.putString(KEY_USER_NAME,name);
        editor.commit();
    }

    public String getUser() {
        return pref.getString(KEY_USER_NAME,"09111111111");
    }

    //------------------------------------------------------------- get and set password
    public String getPassword() {
        return pref.getString(KEY_PASSWORD,"1111");
    }

    public void setpassword(String password) {
        editor.putString(KEY_PASSWORD,password);
        editor.commit();
    }

    //------------------------------------------------------- get and set phone number
    public String getPhonenumbr() {
        return pref.getString(KEY_PHONENUMBR, "");
    }

    public void setPhonenumber(String phonenumber) {
        editor.putString(KEY_PHONENUMBR, phonenumber);
        editor.commit();
    }

    //----------------------------------------------------- get and set KEY_RECEIVESMS
    public boolean getreceivesms() {
        return pref.getBoolean(KEY_RECEIVESMS, false);
    }

    public void setreceivesms(boolean receivesms) {
        editor.putBoolean(KEY_RECEIVESMS, receivesms);
        editor.commit();
    }

    //-------------------------------------------------- get and set KEY_VER2
    public boolean getver2() {
        return pref.getBoolean(KEY_VER2, false);
    }

    public void setver2(boolean ver2) {
        editor.putBoolean(KEY_VER2, ver2);
        editor.commit();
    }

    //-------------------------------------------------- get and set KEY_VER3
    public boolean getver3() {
        return pref.getBoolean(KEY_VER3, false);
    }

    public void setver3(boolean ver3) {
        editor.putBoolean(KEY_VER3, ver3);
        editor.commit();
    }

    //-------------------------------------------------
    /*
         automatic==2
         sms=1
         socket=0
     */
    public int getConnectType(){
        return  pref.getInt(KEY_SETTING_CONNECT_TYPE,Util.sendSocket);
    }

    public void setConnectType(int connectType){
        editor.putInt(KEY_SETTING_CONNECT_TYPE,connectType);
        editor.commit();
    }

}
