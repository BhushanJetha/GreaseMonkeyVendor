package com.greasemonkey.vendor.garage_detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.notification.NotificationAdapter;
import com.greasemonkey.vendor.notification.NotificationModel;
import com.greasemonkey.vendor.request_detail.RequestDetailActivity;
import com.greasemonkey.vendor.servicing_request.LabourCharges;
import com.greasemonkey.vendor.servicing_request.LabourChargesActivity;
import com.greasemonkey.vendor.utility.recyclerview_click_handler.RecyclerTouchListener;
import com.greasemonkey.vendor.vendor_detail.model.ManufactureModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChartListForLabourChargesActivity extends BaseActivity implements IResponse {

    private ArrayList<LabourChargesModel> labourChargesList;
    private RecyclerView mRecyclerView;
    private LabourChargesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_list_for_labour_charges);

        setToolbar();
        setBackArrow(true);
        setTitle("Labour Charges");
        labourChargesList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.rvLabourCharges);
        getLabourCharges();

    }

    private void getLabourCharges(){
        CommunicationChanel communicationChanel =new CommunicationChanel();
        communicationChanel.communicateWithServer(ChartListForLabourChargesActivity.this,
                Constant.GET, Constant.getLabourCharges,null,"LabourCharges");
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        Log.d("LabourCharges-->",jsonObject.toString());
        try {
            if(entity.equals("LabourCharges")) {
                String response = jsonObject.getString("data");
                Log.d("Response ##-->", response);

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject manufacturerDetail = (JSONObject) jsonArray.get(i);
                    String labourChargesId = manufacturerDetail.getString("labourChargesId");
                    String serviceName = manufacturerDetail.getString("serviceName");
                    String bikecc1 = manufacturerDetail.getString("bikecc1");
                    String bikecc2 = manufacturerDetail.getString("bikecc2");
                    String bikecc3 = manufacturerDetail.getString("bikecc3");
                    String bikecc4 = manufacturerDetail.getString("bikecc4");
                    String bikecc5 = manufacturerDetail.getString("ktm");

                    LabourChargesModel labourCharges = new LabourChargesModel(labourChargesId, serviceName, bikecc1, bikecc2, bikecc3, bikecc4, bikecc5,"");
                    labourChargesList.add(labourCharges);
                }

                if(labourChargesList.size()>0){

                    mRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter = new LabourChargesAdapter(labourChargesList);
                    mRecyclerView.setAdapter(mAdapter);
                    LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                    mRecyclerView.setLayoutManager(llm);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
