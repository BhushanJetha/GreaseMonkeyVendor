package com.greasemonkey.vendor.garage_detail;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.utility.UserPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VendorAddressDetailActivity extends BaseActivity implements IResponse{

    private TextView tvState, tvcity, tvAddressLine1, tvAddressLine2, tvLandmark, tvPincode;
    private Button btnUpdateaddress;
    private UserPrefManager userPrefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_address_detail);

        setToolbar();
        setBackArrow(true);
        setTitle("Grease Monkey");
        init();
        getVendorDetail();
    }


    private void init(){
        userPrefManager= new UserPrefManager(this);
        tvState = findViewById(R.id.tvState);
        tvcity = findViewById(R.id.tvCity);
        tvAddressLine1 = findViewById(R.id.tvAddressLine1);
        tvAddressLine2 = findViewById(R.id.tvAddressLine2);
        tvLandmark = findViewById(R.id.tvLandmark);
        tvPincode = findViewById(R.id.tvPincode);

        //btnUpdateaddress = findViewById(R.id.btnUpdateAddressRequest);
    }

    private void getVendorDetail(){
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("vendorId",userPrefManager.getVendorId());

            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(VendorAddressDetailActivity.this,
                    Constant.POST, Constant.getVendorDetail,jsonObject,"ManufacturerList");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        Log.d("Manufacturer List-->",jsonObject.toString());
        try {
            if(entity.equals("ManufacturerList")) {
                String response = jsonObject.getString("details");
                Log.d("Response ##-->", response);

                JSONObject object = new JSONObject(response);
                String line1 = object.getString("line1");
                String line2 = object.getString("line2");
                String city = object.getString("city");
                String landmark = object.getString("landmark");
                String latitude = object.getString("latitude");
                String longitude = object.getString("longitude");
                String pincode = object.getString("pincode");

                tvState.setText("Maharashtra");
                tvcity.setText(city);
                tvAddressLine1.setText(line1);
                tvAddressLine2.setText(line2);
                tvLandmark.setText(landmark);
                tvPincode.setText(pincode);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
