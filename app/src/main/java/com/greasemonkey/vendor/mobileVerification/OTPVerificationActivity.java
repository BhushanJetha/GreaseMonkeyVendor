package com.greasemonkey.vendor.mobileVerification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.registration.RegistrationActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class OTPVerificationActivity extends BaseActivity implements IResponse {

    private Button btnVerify;
    private EditText etOTP;
    private TextView tvResendOTP;
    private String strOTP, strMobileNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        init();
        onClick();
    }

    private void init(){
        try {
            etOTP = (EditText) findViewById(R.id.etOTP);
            btnVerify = (Button) findViewById(R.id.btnVerify);
            tvResendOTP = (TextView) findViewById(R.id.tvResendOTP);

            Bundle bundle = getIntent().getExtras();
            strMobileNumber = bundle.getString("mobile");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onClick(){

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    JSONObject jsonObject=new JSONObject();
                    strOTP = etOTP.getText().toString();

                    if(strOTP.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Enter OTP !", Toast.LENGTH_LONG).show();
                    }else if(strOTP.length() != 4){
                        Toast.makeText(getApplicationContext(), "Please 4 Digit OTP !", Toast.LENGTH_LONG).show();
                    }else {
                        jsonObject.put("mobile",strMobileNumber);
                        jsonObject.put("otp",strOTP);
                        Log.d("Json-->",jsonObject.toString());

                        CommunicationChanel communicationChanel =new CommunicationChanel();
                        communicationChanel.communicateWithServer(OTPVerificationActivity.this,
                                Constant.POST, Constant.otpValidationAPI,jsonObject,"OTPVerification");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject=new JSONObject();

                    jsonObject.put("mobile",strMobileNumber);

                    Log.d("Json-->",jsonObject.toString());
                    CommunicationChanel communicationChanel =new CommunicationChanel();
                    communicationChanel.communicateWithServer(OTPVerificationActivity.this,
                            Constant.POST, Constant.mobileRegistrationAPI,jsonObject,"resendOTP");
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
            String response = jsonObject.getString("message");
            Log.d("Response ##-->",response);

            if(entity.equals("resendOTP")){
                if(response.equals("otp sent to your mobile number")){
                    Toast.makeText(getApplicationContext(), "OTP send on your mobile number !", Toast.LENGTH_LONG).show();
                }
            }else if(entity.equals("OTPVerification")){
                if(response.equals("You have entered wrong otp.")){
                    Toast.makeText(getApplicationContext(), "Please 4 Digit OTP !", Toast.LENGTH_LONG).show();
                }else  if(response.equals("Thank you for verifying your mobile.")){
                    Intent i = new Intent(OTPVerificationActivity.this,  RegistrationActivity.class);
                    i.putExtra("mobile",strMobileNumber);
                    startActivity(i);
                    finish();
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
