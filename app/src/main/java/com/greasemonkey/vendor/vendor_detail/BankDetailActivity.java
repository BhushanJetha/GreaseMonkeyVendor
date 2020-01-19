package com.greasemonkey.vendor.vendor_detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.utility.UserPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class BankDetailActivity extends AppCompatActivity implements IResponse {

    private EditText etBankName, etIfscCode, etBranchName, etAccountNo, etConfirmAccountNumber, etBeneficiaryName;
    private String strBankName, strIfscCode, strBranchName, strAccountNo, strConfirmAccNo, strBeneficiaryName;
    private Button btnSkip, btnSave;

    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_detail);

        init();
        onClick();
    }

    private void init(){

        etBankName = findViewById(R.id.etBankName);
        etIfscCode = findViewById(R.id.etIfscCode);
        etBranchName = findViewById(R.id.etBranchName);
        etAccountNo = findViewById(R.id.etAccountNumber);
        etConfirmAccountNumber = findViewById(R.id.etConfirmAccountNumber);
        etBeneficiaryName = findViewById(R.id.etBenficiaryName);

        btnSave = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);

        userPrefManager = new UserPrefManager(getApplicationContext());
    }

    private void onClick(){
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BankDetailActivity.this, ServiceDetailActivity.class);
                startActivity(i);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    JSONObject jsonObject=new JSONObject();
                    strBankName = etBankName.getText().toString();
                    strIfscCode = etIfscCode.getText().toString();
                    strBranchName = etBranchName.getText().toString();
                    strAccountNo = etAccountNo.getText().toString();
                    strConfirmAccNo = etConfirmAccountNumber.getText().toString();
                    strBeneficiaryName = etBeneficiaryName.getText().toString();

                    if(strBankName.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter bank name!", Toast.LENGTH_LONG).show();
                    }else  if(strIfscCode.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter IFSC code!", Toast.LENGTH_LONG).show();
                    }else  if(strBranchName.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter branch name!", Toast.LENGTH_LONG).show();
                    }else  if(strAccountNo.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter account number!", Toast.LENGTH_LONG).show();
                    }else  if(strConfirmAccNo.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter confirm account number code!", Toast.LENGTH_LONG).show();
                    }else  if(strBeneficiaryName.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter beneficiary name!", Toast.LENGTH_LONG).show();
                    }else if(!strAccountNo.equals(strConfirmAccNo)){
                        Toast.makeText(getApplicationContext(), "Account number and confirm account number not match!", Toast.LENGTH_LONG).show();
                    }else {
                        jsonObject.put("vendorId",userPrefManager.getVendorId());
                        jsonObject.put("ifscCode",strIfscCode);
                        jsonObject.put("branchName",strBranchName);
                        jsonObject.put("bankName",strBankName);
                        jsonObject.put("beneficiaryName",strBeneficiaryName);
                        jsonObject.put("accountNo",strAccountNo);

                        Log.d("Json-->",jsonObject.toString());
                        CommunicationChanel communicationChanel =new CommunicationChanel();
                        communicationChanel.communicateWithServer(BankDetailActivity.this,
                                Constant.POST, Constant.bankDetailAPI,jsonObject,"bankDetail");

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
            String response = jsonObject.getString("message");
            Log.d("BAnk Response ##-->",response);
            if(response.equals("Bank details added successfully")){
                Intent i = new Intent(BankDetailActivity.this, ServiceDetailActivity.class);
                startActivity(i);
                finish();
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
