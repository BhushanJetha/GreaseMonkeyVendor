package com.greasemonkey.vendor.servicing_request;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.DashobardActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;


public class GenerateBillActivity extends BaseActivity implements IResponse{


    private TextView tvOrderId, tvLabourCharges, tvBikePartCharges, tvGstAmount, tvTotalAmount, tvEstimateAmount;
    private Button btnSendBill;
    private String strPartName, strPartAmount, strLabourCharges = "",strEsitimateBill = "";
    private CardView cvBill;
    private String orderId = "", strGmOrderId = "";
    float gstAmount = 0, totalamount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_bill);

        setToolbar();
        setBackArrow(true);
        setTitle("Bike Servicing Bill");
        init();
        onClick();
        getBikePartChangeCharges();
    }

    private void init() {

        tvOrderId = findViewById(R.id.tvOrderId);
        tvLabourCharges = findViewById(R.id.tvLabourCharges);
        tvBikePartCharges = findViewById(R.id.tvBikePartCharges);
        tvGstAmount = findViewById(R.id.tvGSTAmount);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnSendBill = findViewById(R.id.btnSendBill);
        tvEstimateAmount = findViewById(R.id.tvEstimateBill);


        orderId = getIntent().getStringExtra("OrderId");
        strGmOrderId = getIntent().getStringExtra("GmOrderId");
        strEsitimateBill = getIntent().getStringExtra("EstimateBill");

        tvOrderId.setText(strGmOrderId);
        tvEstimateAmount.setText(strEsitimateBill);


        /*gstAmount = ((Integer.parseInt(strEsitimateBill) * 18)/100);
        Log.d("Gst Amount-->",String.valueOf(gstAmount));
        if(gstAmount==0){
            tvGstAmount.setText("0");
        }else {
            tvGstAmount.setText(String.valueOf(gstAmount));
        }
*/
    }

    private void onClick(){

        btnSendBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    JSONObject jsonObject=new JSONObject();

                    Log.d("Order Id->>", orderId);

                    jsonObject.put("orderId",orderId);
                    jsonObject.put("labourCharges",tvLabourCharges.getText().toString());
                    jsonObject.put("bikePartsName","");
                    jsonObject.put("bikePartsCharges",tvBikePartCharges.getText().toString());
                    jsonObject.put("gstAmount",tvGstAmount.getText().toString());
                    jsonObject.put("totalAmountPaid", tvTotalAmount.getText().toString());

                    Log.d("Json-->",jsonObject.toString());
                    CommunicationChanel communicationChanel =new CommunicationChanel();
                    communicationChanel.communicateWithServer(GenerateBillActivity.this,
                            Constant.POST, Constant.sendBikeServicingBill,jsonObject,"updateOrderStatus");

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getBikePartChangeCharges(){
        try{
            JSONObject jsonObject=new JSONObject();

            Log.d("Order Id->>", orderId);
            jsonObject.put("orderId",orderId);

            Log.d("Json-->",jsonObject.toString());
            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(GenerateBillActivity.this,
                    Constant.POST, Constant.getBikePartChangeCharges,jsonObject,"BikePartChange");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {

        try {
            if(entity.equals("updateOrderStatus")){
                Log.d("Order Detail-->",jsonObject.toString());
                String response = jsonObject.getString("message");
                Log.d("Response ##-->",response);

                if(response.equals("Order updated successfully.")){
                    Toast.makeText(getApplicationContext(),"Bill send to user successfully!!",Toast.LENGTH_LONG).show();
                    Intent i=new Intent(getApplicationContext(),DashobardActivity.class);
                    startActivity(i);
                }
            } else if (entity.equals("BikePartChange")) {
                Log.d("Bike part -->",jsonObject.toString());
                String response = jsonObject.getString("data");
                JSONArray jsonArray = new JSONArray(response);

                int laboutAmount = 0, partCharges = 0;
                float gstAmount = 0, totalamount = 0;

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject manufacturerDetail = (JSONObject) jsonArray.get(i);
                    String partPrice = manufacturerDetail.getString("partPrice");
                    String labourCharges = manufacturerDetail.getString("labourCharges");
                    String status = manufacturerDetail.getString("status");

                    if(status.equals("Accepted")){
                        laboutAmount += Integer.parseInt(labourCharges);
                        partCharges += Integer.parseInt(partPrice);
                    }
                }

                strLabourCharges = String.valueOf(laboutAmount);
                strPartAmount = String.valueOf(partCharges);



                gstAmount = (((laboutAmount+Integer.parseInt(strEsitimateBill)) * 18)/100);

                if(laboutAmount==0){
                    tvLabourCharges.setText("0");
                }else {
                    tvLabourCharges.setText(String.valueOf(laboutAmount));
                }

                if(partCharges==0){
                    tvBikePartCharges.setText("0");
                }else {
                    tvBikePartCharges.setText(String.valueOf(strPartAmount));
                }

                if(gstAmount==0){
                    tvGstAmount.setText("0");
                }else {
                    tvGstAmount.setText(String.valueOf(gstAmount));
                }

                if(!strEsitimateBill.isEmpty()){
                    int estimateAmount = Integer.parseInt(strEsitimateBill);
                    totalamount = partCharges + laboutAmount + estimateAmount + gstAmount;
                }

                tvTotalAmount.setText(String.valueOf(totalamount));

                Log.d("Response ##-->",response);
            }
        } catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
