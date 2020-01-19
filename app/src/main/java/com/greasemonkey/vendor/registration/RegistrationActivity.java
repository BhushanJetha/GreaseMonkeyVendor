package com.greasemonkey.vendor.registration;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.utility.UserPrefManager;
import com.greasemonkey.vendor.vendor_detail.RegisterAddressActivity;
import com.greasemonkey.vendor.vendor_detail.VendorLocationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;


public class RegistrationActivity extends BaseActivity implements IResponse {

    private Button btnRegistration;
    private TextView tvTermsAndCondition;
    private EditText etGarageName, etContactPersonName, etEmailId, etPancard, etGstNumber, etPassword,
            etConfirmPassword, etAnotherContactNumber;
    private String strMobile, strGarageName, strContactPersonName, strEmailId, strPancard,
            strGstNumber, strPassword, strConfirmPassword, strAnotherContactNumber;
    private UserPrefManager userPrefManager;
    private CheckBox cbTermsAndCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
        onClick();

    }

    private void init(){

        try {

            tvTermsAndCondition = (TextView) findViewById(R.id.tvTermsAndCondition);
            etGarageName = (EditText)findViewById(R.id.etGarageName);
            etContactPersonName = (EditText)findViewById(R.id.etContactPersonName);
            etAnotherContactNumber = (EditText)findViewById(R.id.etAnotherContactNumber);
            etEmailId = (EditText)findViewById(R.id.etEmailId);
            etPancard = (EditText)findViewById(R.id.etPancard);
            etGstNumber = (EditText)findViewById(R.id.etGstNumber);
            etPassword = (EditText)findViewById(R.id.etPassword);
            etConfirmPassword = (EditText)findViewById(R.id.etConfirmPassword);
            btnRegistration = (Button)findViewById(R.id.btnRegistration);

            cbTermsAndCondition = findViewById(R.id.cbTermsAndCondition);

            userPrefManager = new UserPrefManager(getApplicationContext());

            if(userPrefManager.getUserName() != null){
                etContactPersonName.setText(userPrefManager.getUserName());
            }

            if(userPrefManager.getEmailId() != null){
                etEmailId.setText(userPrefManager.getEmailId());
            }

            Bundle bundle = getIntent().getExtras();
            strMobile = bundle.getString("mobile");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onClick(){
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    JSONObject jsonObject=new JSONObject();

                    strGarageName = etGarageName.getText().toString();
                    strContactPersonName = etContactPersonName.getText().toString();
                    strAnotherContactNumber = etAnotherContactNumber.getText().toString();
                    strEmailId = etEmailId.getText().toString();
                    strPancard = etPancard.getText().toString();
                    strGstNumber = etGstNumber.getText().toString();
                    strPassword = etPassword.getText().toString();
                    strConfirmPassword = etConfirmPassword.getText().toString();

                    if(strGarageName.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Enter Garage Name !", Toast.LENGTH_LONG).show();
                    }else if(strContactPersonName.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Enter Contact Person Name !", Toast.LENGTH_LONG).show();
                    }else if(strPancard.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Enter Pan-card Number !", Toast.LENGTH_LONG).show();
                    }else if(strPassword.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Enter Password !", Toast.LENGTH_LONG).show();
                    }else if(strConfirmPassword.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Enter Confirm Password !", Toast.LENGTH_LONG).show();
                    }else if(!strPassword.equals(strConfirmPassword)){
                        Toast.makeText(getApplicationContext(), "Password And Confirm Password Not Matched !", Toast.LENGTH_LONG).show();
                    }else if(!cbTermsAndCondition.isChecked()){
                        Toast.makeText(getApplicationContext(), "Please accept our Terms and Condition !", Toast.LENGTH_LONG).show();
                    }
                    else {
                        jsonObject.put("contactPerson",strContactPersonName);
                        jsonObject.put("anotherMobile",strAnotherContactNumber);
                        jsonObject.put("email",strEmailId);
                        jsonObject.put("password",strPassword);
                        jsonObject.put("garageName",strGarageName);
                        jsonObject.put("pancard",strPancard);
                        jsonObject.put("gstNumber",strGstNumber);
                        jsonObject.put("mobile",strMobile);
                        jsonObject.put("firebaseId",userPrefManager.getFCMId());

                        Log.d("Json-->",jsonObject.toString());
                        CommunicationChanel communicationChanel =new CommunicationChanel();
                        communicationChanel.communicateWithServer(RegistrationActivity.this,
                                Constant.POST, Constant.vendorPersonalDetail,jsonObject,"VendorRegistration");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        tvTermsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               feedback();
            }
        });
    }

    private void feedback() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.terms_and_condition, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Terms & Conditions");
        //dialogBuilder.setMessage("please send me to your feedback.");

        WebView webView;
        webView = (WebView) dialogView.findViewById(R.id.simpleWebView);
        // displaying content in WebView from html file that stored in assets folder
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_res/raw/" + "guidelines_and_policies.html");

        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        try {
            Log.d("Mobile Reg Response-->",jsonObject.toString());
            String response = jsonObject.getString("message");
            Log.d("Response ##-->",response);
            String vendorId = jsonObject.getString("vendorId");
            Log.d("Vendor ID ##-->",vendorId);

            userPrefManager.setVendorId(vendorId);

            Intent i = new Intent(RegistrationActivity.this, VendorLocationActivity.class);
            startActivity(i);
            finish();
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}
