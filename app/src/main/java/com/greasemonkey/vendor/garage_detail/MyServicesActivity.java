package com.greasemonkey.vendor.garage_detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

import java.util.ArrayList;

public class MyServicesActivity extends BaseActivity implements IResponse{

    private ListView listView;
    private ArrayList<ManufactureModel> manufactureList;
    private ArrayAdapter<ManufactureModel> adapter;
    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);

        setToolbar();
        setBackArrow(true);
        setTitle("Grease Monkey");
        init();
        getManufacturer();
    }

    private void init() {
        userPrefManager= new UserPrefManager(this);
        listView = findViewById(R.id.list);
        manufactureList = new ArrayList<>();
    }

    private void getManufacturer(){
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("vendorId",userPrefManager.getVendorId());

            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(MyServicesActivity.this,
                    Constant.POST, Constant.getVendorDetail,jsonObject,"ManufacturerList");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        Log.d("Manufacturer List-->",jsonObject.toString());
        try {
            if(entity.equals("ManufacturerList")) {
                String response = jsonObject.getString("services");
                Log.d("Response ##-->", response);

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject manufacturerDetail = (JSONObject) jsonArray.get(i);
                    String manufactureName = manufacturerDetail.getString("name");

                    ManufactureModel manufactureModel = new ManufactureModel("1", manufactureName);
                    manufactureList.add(manufactureModel);
                }

                adapter = new ArrayAdapter<ManufactureModel>(this,
                        android.R.layout.simple_dropdown_item_1line, manufactureList);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(adapter);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
