package com.greasemonkey.vendor.request_detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.DashobardActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.registration.RegistrationActivity;
import com.greasemonkey.vendor.utility.UserPrefManager;
import com.greasemonkey.vendor.vendor_detail.VendorLocationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestDetailActivity extends BaseActivity implements IResponse {

    private Button btnAccept, btnReject;
    private TextView tvUserName, tvMobileNumber, tvManufacturer, tvModel, tvServivceType, tvAMC, tvPickupDrop,
            tvPickUpDateTime, tvAddress, tvComment, tvOrderId, tvRegistrationNumber,
            tvEstemateDeliveryDate, tvEstimateDeliveryTime;

    private String orderId = "";
    private String orderStatus = "";
    private String strGMOrderId = "";
    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail2);

        setToolbar();
        setBackArrow(true);
        setTitle("Request Detail");

        init();
        onClick();
    }

    private void init(){

        userPrefManager= new UserPrefManager(this);
        orderId = getIntent().getStringExtra("OrderId");
        strGMOrderId = getIntent().getStringExtra("GMOrderId");
        getOrderDetail(orderId);

        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);

        tvUserName = findViewById(R.id.tvUserName);
        tvMobileNumber = findViewById(R.id.tvMobileNumber);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvRegistrationNumber = findViewById(R.id.tvRegistrationNumber);
        tvManufacturer = findViewById(R.id.tvManufacturere);
        tvModel = findViewById(R.id.tvModel);
        tvServivceType = findViewById(R.id.tvServiceType);
        tvAMC = findViewById(R.id.tvEstimatePrice);
        tvPickupDrop = findViewById(R.id.tvPickUpAndDrop);
        tvPickUpDateTime = findViewById(R.id.tvPickupDateTime);
        tvAddress = findViewById(R.id.tvAddress);
        tvComment = findViewById(R.id.tvCommnent);
        tvEstemateDeliveryDate = findViewById(R.id.tvEstemateDate);
        tvEstimateDeliveryTime = findViewById(R.id.tvEstimateTime);

    }

    private void onClick(){

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderStatus = "Accepted";
                sendOrderStatus(orderStatus);
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderStatus = "Declined";
                sendOrderStatus(orderStatus);
            }
        });

    }

    private void getOrderDetail(String orderId) {
        try{
            JSONObject jsonObject=new JSONObject();

            jsonObject.put("vendorId",userPrefManager.getVendorId());
            jsonObject.put("orderId",orderId);

            Log.d("Json-->",jsonObject.toString());
            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(RequestDetailActivity.this,
                        Constant.POST, Constant.getOrderDetail,jsonObject,"orderDetail");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendOrderStatus(String orderStatus) {
        try{
            JSONObject jsonObject=new JSONObject();

            jsonObject.put("orderId",orderId);
            jsonObject.put("orderStatus",orderStatus);

            Log.d("Json-->",jsonObject.toString());
            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(RequestDetailActivity.this,
                    Constant.POST, Constant.sendOrderStatus,jsonObject,"updateOrderStatus");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        try {

            if(entity.equals("orderDetail")){
                Log.d("Order Detail-->",jsonObject.toString());
                String response = jsonObject.getString("data");
                Log.d("Response ##-->",response);

                JSONArray jsonArray = new JSONArray(response);
                //JSONObject object = new JSONObject(response);
                JSONObject object = jsonArray.getJSONObject(0);

                String serviceType = object.getString("serviceType");
                String serviceDetail = object.getString("serviceDetail");
                String pickupAndDrop = object.getString("pickupAndDrop");
                String gstAmount = object.getString("gstAmount");
                String totalAmountPaid = object.getString("totalAmountPaid");
                String orderStatus = object.getString("orderStatus");
                String orderDate = object.getString("orderDate");
                String orderTime = object.getString("orderTime");

                String additionalInfo = "", state ="", city="",addressLine1="", addressLine2="", landmark="", pincode="",
                        username = "", pickupDate ="",pickupTime="",manufacture="", modelName="", mobile="",
                        vehicleRegisterNo="";




                if(object.has("vehicleRegisterNo")) {
                    vehicleRegisterNo = object.getString("vehicleRegisterNo");
                }
                if(object.has("username")){
                    username = object.getString("username");
                }else if(object.has("userName")){
                    username = object.getString("userName");
                }
                if(object.has("mobile")) {
                    mobile = object.getString("mobile");
                }
                if(object.has("manufacture")) {
                    manufacture = object.getString("manufacture");
                }
                if(object.has("modelName")) {
                    modelName = object.getString("modelName");
                }
                if(object.has("pickupDate")) {
                    pickupDate = object.getString("pickupDate");
                }
                if(object.has("pickupTime")) {
                    pickupTime = object.getString("pickupTime");
                }
                if(object.has("additionalInfo")) {
                    additionalInfo = object.getString("additionalInfo");
                }
                if(object.has("state")) {
                    state = object.getString("state");
                }
                if(object.has("city")) {
                    city = object.getString("city");
                }
                if(object.has("line1")) {
                    addressLine1 = object.getString("line1");
                }
                if(object.has("line2")) {
                    addressLine2 = object.getString("line2");
                }
                if(object.has("landmark")) {
                    landmark = object.getString("landmark");
                }
                if(object.has("pincode")) {
                    pincode = object.getString("pincode");
                }

                String address = addressLine1 + ", " + addressLine2 + ", " + landmark + ", " + state + ", " + city + ", " + pincode;
                String dateTime = pickupDate + " " +  pickupTime;

                tvOrderId.setText(strGMOrderId);
                tvRegistrationNumber.setText(vehicleRegisterNo);
                tvUserName.setText(username);
                tvMobileNumber.setText(mobile);
                tvManufacturer.setText(manufacture);
                tvModel.setText(modelName);
                tvServivceType.setText(serviceType);
                tvAMC.setText( "Rs." + totalAmountPaid);
                tvPickupDrop.setText(pickupAndDrop);
                tvPickUpDateTime.setText(dateTime);
                tvComment.setText(additionalInfo);


                if(pickupDate.isEmpty()){
                    tvPickUpDateTime.setText("-");
                }else
                    tvPickUpDateTime.setText(dateTime);

                if(additionalInfo.isEmpty()){
                    tvComment.setText("-");
                }else
                    tvComment.setText(additionalInfo);

                if(addressLine1.isEmpty()){
                    tvAddress.setText("-");
                }else
                    tvAddress.setText(address);

            }else  if(entity.equals("updateOrderStatus")){
                Log.d("Order Status-->",jsonObject.toString());
                String response = jsonObject.getString("message");

                if(response.equals("Order updated successfully.")){
                    Intent i = new Intent(getApplicationContext(), DashobardActivity.class);
                    startActivity(i);
                    finish();
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
