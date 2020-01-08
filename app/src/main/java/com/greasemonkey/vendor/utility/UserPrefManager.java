package com.greasemonkey.vendor.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by bhushan on 20/12/16.
 */

public class UserPrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "GreaseMonkey";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private String KEY_FCM_ID = "fcm_id";

    public UserPrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLoginStatus(String isLogin) {
        editor.putString("LoginStatus", isLogin);
        editor.commit();
    }

    public String getLoginStatus() {
        return pref.getString("LoginStatus", "");
    }

    public void setVendorId(String vendorId){
        editor.putString("vendorId",vendorId);
        editor.commit();
    }

    public String getVendorId() {
        return pref.getString("vendorId", "");
    }

    public String getIsPasswordChange() {
        return pref.getString("changePassword", "");
    }

    public  void setUserName(String name){
        editor.putString("userName", name);
        editor.commit();
    }

    public String getUserName(){
        return  pref.getString("userName", "");
    }

    public void setVendorVerificationStatus(String verificationStatus) {
        editor.putString("verificationStatus",verificationStatus);
        editor.commit();
    }

    public String getVendorVerificationStatus(){
        return pref.getString("verificationStatus", "");
    }

    public void setOnlineStatus(String onlineStatus) {
        editor.putString("onlineStatus",onlineStatus);
        editor.commit();
    }

    public String getOnlineStatus(){
        return pref.getString("onlineStatus", "");
    }

    public void setPickupDropStatus(String pickupDropStatus) {
        editor.putString("pickupDropStatus",pickupDropStatus);
        editor.commit();
    }

    public String getPickupDropStatus(){
        return pref.getString("pickupDropStatus", "");
    }

    public void setTommarowStatus(String tommarowStatus) {
        editor.putString("tommarowStatus",tommarowStatus);
        editor.commit();
    }

    public String getTommarowStatus(){
        return pref.getString("tommarowStatus", "");
    }

    public String getEmailId(){
        return pref.getString("emailId", "");
    }

    public void setEmailId(String emailId) {
        editor.putString("emailId",emailId);
        editor.commit();
    }

    public void setFCMId(String s) {
        editor.putString(KEY_FCM_ID, s);
        editor.commit();
    }

    public String getFCMId(){
        return pref.getString(KEY_FCM_ID, "");
    }

}


