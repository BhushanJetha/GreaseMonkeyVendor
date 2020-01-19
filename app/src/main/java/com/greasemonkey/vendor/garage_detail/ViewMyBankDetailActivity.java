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

public class ViewMyBankDetailActivity extends BaseActivity implements IResponse{

    private TextView tvBankName, tvIFSCCode, tvBranchName, tvAccountNumber, tvBeneficiaryName;
    private Button btnUpdateBankDetail;
    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_detail2);

        setToolbar();
        setBackArrow(true);
        setTitle("Grease Monkey");
        init();
        getBankDetail();
    }

    private void init(){
        userPrefManager= new UserPrefManager(this);
        tvBankName = findViewById(R.id.tvBankName);
        tvIFSCCode = findViewById(R.id.tvIFSCCode);
        tvBranchName = findViewById(R.id.tvBranchName);
        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvBeneficiaryName = findViewById(R.id.tvBeneficiaryName);

       // btnUpdateBankDetail = findViewById(R.id.btnUpdateBankDetailRequest);
    }

    private void getBankDetail(){
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("vendorId",userPrefManager.getVendorId());

            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(ViewMyBankDetailActivity.this,
                    Constant.POST, Constant.getBankDetail,jsonObject,"bankDetail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        Log.d("bank Detail-->",jsonObject.toString());
        try {
            if(entity.equals("bankDetail")) {
                String response = jsonObject.getString("data");
                Log.d("Response ##-->", response);

                JSONArray jsonArray = new JSONArray(response);
                JSONObject object = jsonArray.getJSONObject(0);
                String bankDetailId = object.getString("bankDetailId");
                String bankName = object.getString("bankName");
                String vendorId = object.getString("vendorId");
                String ifscCode = object.getString("ifscCode");
                String branchName = object.getString("branchName");
                String accountNo = object.getString("accountNo");
                String beneficiaryName = object.getString("beneficiaryName");

                tvBankName.setText(bankName);
                tvIFSCCode.setText(ifscCode);
                tvBranchName.setText(branchName);
                tvAccountNumber.setText(accountNo);
                tvBeneficiaryName.setText(beneficiaryName);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
