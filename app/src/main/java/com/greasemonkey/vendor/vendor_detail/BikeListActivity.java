package com.greasemonkey.vendor.vendor_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.DashobardActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.utility.UserPrefManager;
import com.greasemonkey.vendor.vendor_detail.model.ManufactureModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BikeListActivity extends BaseActivity implements View.OnClickListener, IResponse {

    private Button button;
    private ListView listView;
    private ArrayList<ManufactureModel> manufactureList;
    private ArrayAdapter<ManufactureModel> adapter;
    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_list);

        setToolbar();
        init();
        getManufacturer();

        button.setOnClickListener(this);
    }

    private void init() {
        listView = (ListView) findViewById(R.id.list);
        button = (Button) findViewById(R.id.btnNext);

        userPrefManager = new UserPrefManager(getApplicationContext());
        manufactureList = new ArrayList<>();
    }

    public void setToolbar(){
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manufacturers");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
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
                    ManufactureModel manufacturereMaster = manufactureList.get(position);
                    selectedItem +=  manufacturereMaster.getManufactureName() + ",";
                    jsonObject.put("vendorId",userPrefManager.getVendorId());
                    jsonObject.put("manufactureId",manufacturereMaster.getManufactureId());
                    jsonArray.put(jsonObject);
                }
            }
            jsonService.put("Services",jsonArray);

            Log.d("Selected Item", selectedItem);
            Log.d("Service Json--->", jsonService.toString());
            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(BikeListActivity.this,
                    Constant.POST, Constant.registerManufacturereAPI,jsonService,"RegisterManufacturer");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getManufacturer(){
        CommunicationChanel communicationChanel =new CommunicationChanel();
        communicationChanel.communicateWithServer(BikeListActivity.this,
                Constant.GET, Constant.manufacturerAPI,null,"ManufacturerList");
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        Log.d("Ser List Response-->",jsonObject.toString());
        try {
            if(entity.equals("ManufacturerList")) {
                String response = jsonObject.getString("data");
                Log.d("Response ##-->", response);

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject manufacturerDetail = (JSONObject) jsonArray.get(i);
                    String manufactureId = manufacturerDetail.getString("manufactureId");
                    String manufactureName = manufacturerDetail.getString("manufacture");

                    ManufactureModel manufactureModel = new ManufactureModel(manufactureId, manufactureName);
                    manufactureList.add(manufactureModel);
                }

                adapter = new ArrayAdapter<ManufactureModel>(this,
                        android.R.layout.simple_list_item_multiple_choice, manufactureList);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(adapter);
            }else  if(entity.equals("RegisterManufacturer")){
                String response = jsonObject.getString("message");
                Log.d("Response ##-->", response);

                if(response.equals("Services saved successfully")){
                    Intent i = new Intent(BikeListActivity.this,  DashobardActivity.class);
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
