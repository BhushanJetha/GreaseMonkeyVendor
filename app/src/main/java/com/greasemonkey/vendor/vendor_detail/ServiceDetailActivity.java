package com.greasemonkey.vendor.vendor_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.utility.UserPrefManager;
import com.greasemonkey.vendor.vendor_detail.model.ServicesMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ServiceDetailActivity extends BaseActivity implements OnClickListener,IResponse {

    private Button button;
    private ListView listView;
    private ArrayList<ServicesMaster> servicesList;
    private  ArrayAdapter<ServicesMaster> adapter;
    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        setToolbar();
        init();
        getState();

        button.setOnClickListener(this);
    }

    public void setToolbar(){
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Offered Services");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void init() {
        listView = (ListView) findViewById(R.id.list);
        button = (Button) findViewById(R.id.btnNext);

        servicesList = new ArrayList<>();
        userPrefManager = new UserPrefManager(getApplicationContext());
    }

    public void onClick(View v) {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        String selectedItem = "";
        try {
                JSONObject jsonService = new JSONObject();
                JSONArray jsonArray=new JSONArray();

                for (int i = 0; i < checked.size(); i++) {
                    int position = checked.keyAt(i);
                    JSONObject jsonObject=new JSONObject();
                    if (checked.valueAt(i)){
                        ServicesMaster servicesMaster = servicesList.get(position);
                        selectedItem +=  servicesMaster.getServiceName() + ",";
                        jsonObject.put("vendorId",userPrefManager.getVendorId());
                        jsonObject.put("serviceId",servicesMaster.getServiceId());
                        jsonArray.put(jsonObject);
                    }
                }

                jsonService.put("Manufacturer",jsonArray);

                Log.d("Selected Item", selectedItem);
                Log.d("Service Json--->", jsonService.toString());
                CommunicationChanel communicationChanel =new CommunicationChanel();
                communicationChanel.communicateWithServer(ServiceDetailActivity.this,
                        Constant.POST, Constant.registerServicesAPI,jsonService,"RegisterServices");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getState(){
        CommunicationChanel communicationChanel =new CommunicationChanel();
        communicationChanel.communicateWithServer(ServiceDetailActivity.this,
                Constant.GET, Constant.servicesListAPI,null,"ServicesList");
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        Log.d("Ser List Response-->",jsonObject.toString());
        try {
        if(entity.equals("ServicesList")) {
            String response = jsonObject.getString("data");
            Log.d("Response ##-->", response);

            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userDetail = (JSONObject) jsonArray.get(i);
                String serviceId = userDetail.getString("serviceTypeId");
                String serviceTypeId = userDetail.getString("serviceName");
                String serviceName = userDetail.getString("createdDate");
                String servicePrice = userDetail.getString("createTime");

                Log.d("Service Name-->",serviceName);
                ServicesMaster serviceMaster = new ServicesMaster(serviceId, serviceTypeId, serviceName, servicePrice);
                servicesList.add(serviceMaster);
            }

            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_multiple_choice, servicesList);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            listView.setAdapter(adapter);
        } else  if(entity.equals("RegisterServices")){
            String response = jsonObject.getString("message");
            Log.d("Response ##-->", response);

            if(response.equals("Services saved successfully")){
                Intent i = new Intent(ServiceDetailActivity.this, BikeListActivity.class);
                startActivity(i);
                finish();
            }else {
                Toast.makeText(getApplicationContext(),"Something went wrong, Please Tray again !!",Toast.LENGTH_LONG).show();
            }
        }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
