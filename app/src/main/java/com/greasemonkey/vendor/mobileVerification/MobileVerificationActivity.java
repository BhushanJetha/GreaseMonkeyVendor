package com.greasemonkey.vendor.mobileVerification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.DashobardActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.login.LoginActivity;
import com.greasemonkey.vendor.registration.RegistrationActivity;
import com.greasemonkey.vendor.utility.UserPrefManager;
import com.greasemonkey.vendor.vendor_detail.BikeListActivity;
import com.greasemonkey.vendor.vendor_detail.RegisterAddressActivity;
import com.greasemonkey.vendor.vendor_detail.ServiceDetailActivity;
import com.greasemonkey.vendor.vendor_detail.VendorLocationActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class MobileVerificationActivity extends BaseActivity implements IResponse {

    private Button btnGetOTP;
    private EditText etMobileNumber;
    private String strMobileNumber;
    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);

        init();
        onClick();
    }

    private void init(){
        try{
            etMobileNumber = (EditText)findViewById(R.id.etMobileNumber);
            btnGetOTP = (Button)findViewById(R.id.btnGetOTP);

            userPrefManager= new UserPrefManager(getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onClick(){

        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    JSONObject jsonObject=new JSONObject();
                    strMobileNumber = etMobileNumber.getText().toString();

                    if(strMobileNumber.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Enter Mobile Number !", Toast.LENGTH_LONG).show();
                    }else if(strMobileNumber.length() != 10){
                        Toast.makeText(getApplicationContext(), "Please 10 Digit Mobile Number !", Toast.LENGTH_LONG).show();
                    }else {
                        jsonObject.put("mobile",strMobileNumber);

                        Log.d("Json-->",jsonObject.toString());
                        CommunicationChanel communicationChanel =new CommunicationChanel();
                        communicationChanel.communicateWithServer(MobileVerificationActivity.this,
                                Constant.POST, Constant.mobileRegistrationAPI,jsonObject,"MobileRegistration");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        try {
            Log.d("Mobile Reg Response-->",jsonObject.toString());

            if(jsonObject.has("id")){
                userPrefManager.setVendorId(jsonObject.getString("id"));
            }

            String response = jsonObject.getString("message");

            Log.d("Response ##-->",response);
            if(response.equals("otp sent to your mobile number")){
                Intent i = new Intent(MobileVerificationActivity.this, OTPVerificationActivity.class);
                i.putExtra("mobile",strMobileNumber);
                startActivity(i);
            }else if(jsonObject.has("mobileStatus")){

                String mobileStatus = jsonObject.getString("mobileStatus");
                String personalDetail = jsonObject.getString("personalDetail");
                String addressDetail = jsonObject.getString("addressDetail");
                String providedManufacturer = jsonObject.getString("providedManufacturer");
                String providedService = jsonObject.getString("providedService");

                if (mobileStatus.equals("verified") && personalDetail.equals("true") && addressDetail.equals("true")
                        && providedService.equals("true") && providedManufacturer.equals("true")) {
                    Intent i = new Intent(MobileVerificationActivity.this, LoginActivity.class);
                    startActivity(i);
                } else if (mobileStatus.equals("verified") && personalDetail.equals("true") && addressDetail.equals("true")
                        && providedService.equals("true") && providedManufacturer.equals("false")) {
                    Intent i = new Intent(MobileVerificationActivity.this, BikeListActivity.class);
                    startActivity(i);
                } else if (mobileStatus.equals("verified") && personalDetail.equals("true") && addressDetail.equals("true")
                        && providedService.equals("false") && providedManufacturer.equals("false")) {
                    Intent i = new Intent(MobileVerificationActivity.this, ServiceDetailActivity.class);
                    startActivity(i);
                } else if (mobileStatus.equals("verified") && personalDetail.equals("true") && addressDetail.equals("false")
                        && providedService.equals("false") && providedManufacturer.equals("false")) {
                    Intent i = new Intent(MobileVerificationActivity.this, VendorLocationActivity.class);
                    startActivity(i);
                } else if (mobileStatus.equals("verified") && personalDetail.equals("false") && addressDetail.equals("false")
                        && providedService.equals("false") && providedManufacturer.equals("false")) {
                    Intent i = new Intent(MobileVerificationActivity.this, RegistrationActivity.class);
                    i.putExtra("mobile", strMobileNumber);
                    startActivity(i);
                }
            }


            /*if(jsonObject.has("message")){
                String response = jsonObject.getString("message");
                Log.d("Response ##-->",response);
                if(response.equals("otp sent to your mobile number")){
                    Intent i = new Intent(MobileVerificationActivity.this, OTPVerificationActivity.class);
                    i.putExtra("mobile",strMobileNumber);
                    startActivity(i);
                }else if(response.equals("Vendor is already exists with this mobile number.")) {
                    Toast.makeText(getApplicationContext(),"Looks like this mobile number is already registered !", Toast.LENGTH_LONG).show();
                }
            } else if(jsonObject.has("mobileStatus")) {


                Log.d("p Detail-->",personalDetail);
                Log.d("addressDetail-->",addressDetail);
                Log.d("PM-->",providedManufacturer);
                Log.d("PS-->",providedService);

                if(mobileStatus.equals("verified") && personalDetail.equals("true") && addressDetail.equals("true")
                        && providedService.equals("true") && providedManufacturer.equals("true")){
                    Intent i = new Intent(MobileVerificationActivity.this, DashobardActivity.class);
                    startActivity(i);
                }else if(mobileStatus.equals("verified") && personalDetail.equals("true") && addressDetail.equals("true")
                        && providedService.equals("true") && providedManufacturer.equals("false")){
                    Intent i = new Intent(MobileVerificationActivity.this, BikeListActivity.class);
                    startActivity(i);
                }else if(mobileStatus.equals("verified") && personalDetail.equals("true") && addressDetail.equals("true")
                        && providedService.equals("false") && providedManufacturer.equals("false")){
                    Intent i = new Intent(MobileVerificationActivity.this, ServiceDetailActivity.class);
                    startActivity(i);
                }else if(mobileStatus.equals("verified") && personalDetail.equals("true") && addressDetail.equals("false")
                        && providedService.equals("false") && providedManufacturer.equals("false")){
                    Intent i = new Intent(MobileVerificationActivity.this, VendorLocationActivity.class);
                    startActivity(i);
                } else if(mobileStatus.equals("verified") && personalDetail.equals("false") && addressDetail.equals("false")
                        && providedService.equals("false") && providedManufacturer.equals("false")){
                    Intent i = new Intent(MobileVerificationActivity.this, RegistrationActivity.class);
                    i.putExtra("mobile",strMobileNumber);
                    startActivity(i);
                }*/

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
