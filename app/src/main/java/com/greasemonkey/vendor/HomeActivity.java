package com.greasemonkey.vendor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.garage_detail.GarageDetailActivity;
import com.greasemonkey.vendor.login.LoginActivity;
import com.greasemonkey.vendor.utility.UserPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity implements IResponse {

    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setToolbar();
        userPrefManager= new UserPrefManager(this);
        getVendorDetail();
    }

    public void setToolbar(){
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Grease Monkey");

    }

    private void getVendorDetail(){
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("vendorId",userPrefManager.getVendorId());

            Log.d("Request -->",jsonObject.toString());
            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(HomeActivity.this,
                    Constant.POST, Constant.getVendorDetail,jsonObject,"ProfileDetail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        Log.d("vendor List-->",jsonObject.toString());
        try {
            if(entity.equals("ProfileDetail")) {
                String response = jsonObject.getString("vendor");
                Log.d("Response ##-->Co", response);

                JSONObject object = new JSONObject(response);

                String verification = object.getString("verification");
                userPrefManager.setVendorVerificationStatus(verification);

                if(verification.equals("active")){
                    Intent i = new Intent(HomeActivity.this, DashobardActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
