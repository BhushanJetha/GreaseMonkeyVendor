package com.greasemonkey.vendor.servicing_request;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LabourChargesActivity extends BaseActivity implements View.OnClickListener,IResponse {

    private Button button;
    private ListView listView;
    private ArrayList<LabourCharges> labourChargesList;
    private ArrayAdapter<LabourCharges> adapter;
    private String orderId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_charges);

        setToolbar();
        setBackArrow(true);
        setTitle("Select Labour Charges");
        init();

        getManufacturer();
    }

    private void init() {
        listView = findViewById(R.id.list);
        button = findViewById(R.id.btnNext);

        labourChargesList = new ArrayList<>();
        button.setOnClickListener(this);
        orderId = getIntent().getStringExtra("OrderId");
    }

    public void onClick(View v) {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        int labourChargesAmount = 0;
        try {
            for (int i = 0; i < checked.size(); i++) {
                int position = checked.keyAt(i);

                if (checked.valueAt(i)){
                    LabourCharges labourCharges = labourChargesList.get(position);
                    labourChargesAmount += Integer.parseInt(labourCharges.getBikecc1());
                }
            }

            Log.d("Amount-->",String.valueOf(labourChargesAmount));
            Intent i=new Intent(getApplicationContext(),GenerateBillActivity.class);
            i.putExtra("labourCharges",String.valueOf(labourChargesAmount));
            i.putExtra("orderId",orderId);
            startActivity(i);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getManufacturer(){
        CommunicationChanel communicationChanel =new CommunicationChanel();
        communicationChanel.communicateWithServer(LabourChargesActivity.this,
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
                    String bikecc5 = manufacturerDetail.getString("bikecc5");

                    LabourCharges labourCharges = new LabourCharges(labourChargesId, serviceName, bikecc1, bikecc2, bikecc3, bikecc4, bikecc5);
                    labourChargesList.add(labourCharges);
                }

                adapter = new ArrayAdapter<LabourCharges>(this,
                        android.R.layout.simple_list_item_multiple_choice, labourChargesList);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(adapter);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
