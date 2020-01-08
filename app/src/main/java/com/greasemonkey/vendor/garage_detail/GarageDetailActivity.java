package com.greasemonkey.vendor.garage_detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.utility.UserPrefManager;
import com.greasemonkey.vendor.vendor_detail.model.ManufactureModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GarageDetailActivity extends BaseActivity implements IResponse{

    private TextView tvGarageId, tvGarageName, tvContactPerName, tvContPerMobile, tvOtherContNumber, tvEmailId,
            tvPancarNumber, tvGST, tvVerificationStatus;
    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_detail);

        setToolbar();
        setBackArrow(true);
        setTitle("Grease Monkey");
        init();
        getVendorDetail();
    }

    private void init(){
        userPrefManager= new UserPrefManager(this);
        tvGarageId = findViewById(R.id.tvGarageId);
        tvGarageName = findViewById(R.id.tvGarageName);
        tvContactPerName = findViewById(R.id.tvContactPersonName);
        tvContPerMobile = findViewById(R.id.tvContactPersonMobileNumber);
        tvOtherContNumber = findViewById(R.id.tvOtherContactNumber);
        tvEmailId = findViewById(R.id.tvEmailId);
        tvPancarNumber = findViewById(R.id.tvPancardNumber);
        tvGST = findViewById(R.id.tvGSTNumber);
        tvVerificationStatus = findViewById(R.id.tvVerificationStatus);

    }

    private void getVendorDetail(){
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("vendorId",userPrefManager.getVendorId());

            Log.d("Request -->",jsonObject.toString());
            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(GarageDetailActivity.this,
                    Constant.POST, Constant.getVendorDetail,jsonObject,"ManufacturerList");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        Log.d("vendor List-->",jsonObject.toString());
        try {
            if(entity.equals("ManufacturerList")) {
                String response = jsonObject.getString("vendor");
                Log.d("Response ##-->", response);

                JSONObject object = new JSONObject(response);
                String vendorId = object.getString("vendorId");
                String contactPerson = object.getString("contactPerson");
                String garageName = object.getString("garageName");
                String mobile = object.getString("mobile");

                String email = object.getString("email");
                String pancard = object.getString("pancard");
                String gst = object.getString("gst");

                String verification = object.getString("verification");
                String onlineStatus = object.getString("onlineStatus");

                tvGarageId.setText(vendorId);
                tvGarageName.setText(garageName);
                tvContactPerName.setText(contactPerson);
                tvContPerMobile.setText(mobile);
                tvOtherContNumber.setText(mobile);
                tvEmailId.setText(email);
                tvPancarNumber.setText(pancard);
                tvGST.setText(gst);
                tvVerificationStatus.setText(verification);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
