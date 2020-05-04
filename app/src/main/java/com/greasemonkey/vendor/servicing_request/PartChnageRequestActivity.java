package com.greasemonkey.vendor.servicing_request;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.garage_detail.LabourChargesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PartChnageRequestActivity extends BaseActivity implements IResponse {

    private EditText etPartsName, etPartsPrice;
    private Spinner spinnerLabouCharges;
    private Button sendRequest;
    private ArrayList<LabourCharges> labourChargesList;
    private String orderId = "", strUserId = "", strGmOrderId = "";
    private String strPartName, strPartAmount, strLabourCharges, strEngineCC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_chnage_request);

        setToolbar();
        setBackArrow(true);
        setTitle("Part Change Request");

        init();
        onClick();
        getLabourCharges();

    }

    private void init(){
        strUserId = getIntent().getStringExtra("UserId");
        strEngineCC = getIntent().getStringExtra("EngigneCC");
        Log.d("Engine CC-->", strEngineCC);

        etPartsName = findViewById(R.id.etBikePartName);
        etPartsPrice = findViewById(R.id.etBikePartCharges);

        labourChargesList = new ArrayList<>();
        sendRequest = findViewById(R.id.btnSendRequest);
        spinnerLabouCharges = findViewById(R.id.spinnerLabourCharges);
        orderId = getIntent().getStringExtra("OrderId");
        strUserId = getIntent().getStringExtra("UserId");
        strGmOrderId = getIntent().getStringExtra("GmOrderId");
    }

    private void onClick(){

        spinnerLabouCharges.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // String selectedItem = parent.getItemAtPosition(position).toString();

                LabourCharges lb = labourChargesList.get(position);
                if(strEngineCC.equals("80cc to 135cc")){
                    strLabourCharges = lb.bikecc1;
                } else if(strEngineCC.equals("136cc to 180cc")){
                    strLabourCharges = lb.bikecc2;
                } else if(strEngineCC.equals("181cc to 300cc")){
                    strLabourCharges = lb.bikecc3;
                } else if(strEngineCC.equals("301cc to 500cc")){
                    strLabourCharges = lb.bikecc4;
                } else if(strEngineCC.equals("KTM")){
                    strLabourCharges = lb.bikecc5;
                } else {
                    strLabourCharges = lb.bikecc1;
                }
            }   // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                    strPartName = etPartsName.getText().toString();
                    strPartAmount = etPartsPrice.getText().toString();

                    if(!strPartName.isEmpty() && !strPartAmount.isEmpty()){
                        JSONObject jsonObject=new JSONObject();

                        jsonObject.put("userId",strUserId);
                        jsonObject.put("orderId",orderId);
                        jsonObject.put("partName",strPartName);
                        jsonObject.put("partPrice",strPartAmount);
                        jsonObject.put("labourCharges",strLabourCharges);
                        jsonObject.put("status","Requested");

                        Log.d("Json-->",jsonObject.toString());
                        CommunicationChanel communicationChanel =new CommunicationChanel();
                        communicationChanel.communicateWithServer(PartChnageRequestActivity.this,
                                Constant.POST, Constant.sndPartChangeRequest,jsonObject,"sendPartChangeRequest");
                    }else {
                        Toast.makeText(getApplicationContext(),"Please enter part name & part Price first!",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getLabourCharges(){
        CommunicationChanel communicationChanel =new CommunicationChanel();
        communicationChanel.communicateWithServer(PartChnageRequestActivity.this,
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

                    LabourCharges labourCharges = new LabourCharges(labourChargesId, serviceName, bikecc1, bikecc2, bikecc3, bikecc4, bikecc5);
                    labourChargesList.add(labourCharges);
                }

                Collections.sort(labourChargesList, new Comparator<LabourCharges>() {
                    @Override
                    public int compare(LabourCharges lhs, LabourCharges rhs) {
                        return lhs.getServiceName().compareTo(rhs.getServiceName());
                    }
                });

                ArrayAdapter<LabourCharges> adapter = new ArrayAdapter<LabourCharges> (getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, labourChargesList);
                spinnerLabouCharges.setAdapter(adapter);

            }else if(entity.equals("sendPartChangeRequest")){
                String response = jsonObject.getString("message");
                Log.d("Response ##-->", response);
                if(response.equals("Bike part details added successfully.")){
                    Toast.makeText(getApplicationContext(),"Bike part details added successfully",Toast.LENGTH_LONG).show();
                    Intent i=new Intent(this,RequestStatusActivity.class);
                    i.putExtra("OrderId",orderId);
                    i.putExtra("UserId",strUserId);
                    i.putExtra("GMOrderId",strGmOrderId);
                    startActivity(i);
                    finish();
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
