package com.greasemonkey.vendor.servicing_request;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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


    private TextView tvOrderId, tvLabourCharges, tvBikePartCharges, tvPartsLabourCharges, tvEngineOil, tvWashing,
            tvPickUpDrop, tvOtherCharges, tvGstAmount, tvTotalAmount, tvSubTotal;
    private Button btnSendBill;
    private int mypartAmount, mylabourCharges, mycoupon_discount_amount, myreferalBalance, myengine_oil_price, mywashing,
            mypick_up_drop_price, myother_charges, mypartsLabourCharges ;
    private String orderId = "", strGmOrderId = "";
    float fGstAmount = 0, fTotalamount = 0, totalAmountWithOffer, mytotalAmountPaid, totalAmountWithoutOffer;
    private LinearLayout llLabourCharges, llPartsLabourCharges, llPartsCharges, llEngineOil, llWashing, llPickUpDrop, llOtherCharges;



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
        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvPartsLabourCharges = findViewById(R.id.tvPartsLabourCharges);
        tvEngineOil= findViewById(R.id.tvEngineOilCharges);
        tvWashing = findViewById(R.id.tvWashing);
        tvPickUpDrop = findViewById(R.id.tvPickUpDropCharges);
        tvOtherCharges = findViewById(R.id.tvOtherCharges);

        llLabourCharges  = findViewById(R.id.llLabourCharges);
        llPartsLabourCharges  = findViewById(R.id.llBikePartsLabourCharges);
        llPartsCharges  = findViewById(R.id.llPartsCharges);
        llEngineOil = findViewById(R.id.llEngineOilCharges);
        llWashing = findViewById(R.id.llWashing);
        llPickUpDrop  = findViewById(R.id.llPickUpDropCharges);
        llOtherCharges  = findViewById(R.id.llOtherCharges);

        orderId = getIntent().getStringExtra("OrderId");
        strGmOrderId = getIntent().getStringExtra("GmOrderId");
        //strEsitimateBill = getIntent().getStringExtra("EstimateBill");

        tvOrderId.setText(strGmOrderId);


    }

    private void onClick(){

        btnSendBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    JSONObject jsonObject=new JSONObject();

                    Log.d("Order Id->>", orderId);

                    jsonObject.put("orderId",orderId);
                    jsonObject.put("labourCharges",mylabourCharges);
                    jsonObject.put("bikePartsName",mypartsLabourCharges);
                    jsonObject.put("bikePartsCharges",mypartAmount);
                    jsonObject.put("gstAmount",fGstAmount);
                    jsonObject.put("totalAmountPaid", totalAmountWithOffer);

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

    private void getInvoiceByOrderId(){
        try {
            JSONObject jsonObject=new JSONObject();

            Log.d("Order Id->>", orderId);
            jsonObject.put("orderId",orderId);

            Log.d("Json-->",jsonObject.toString());
            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(GenerateBillActivity.this,
                    Constant.POST, Constant.getInvoiceByOrderId,jsonObject,"GetBillDetail");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
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
            } else if (entity.equals("GetBillDetail")){
                Log.d("Bill Detail -->",jsonObject.toString());
                String response = jsonObject.getString("data");
                JSONArray jsonArray = new JSONArray(response);

                JSONObject billDetail = (JSONObject) jsonArray.get(0);
                String referalBalance = billDetail.getString("referalBalance");
                String labourCharges = billDetail.getString("labourCharges");
                String engine_oil_price = billDetail.getString("engine_oil_price");
                String pick_up_drop_price = billDetail.getString("pick_up_drop_price");
                String other_charges = billDetail.getString("other_charges");
                String coupon_discount_amount = billDetail.getString("coupon_discount_amount");
                String washing = billDetail.getString("washing");

                int gstApplicableAmount = 0;

                if(referalBalance != null && !referalBalance.isEmpty()){
                    myreferalBalance = Integer.parseInt(referalBalance);
                }else  {
                    myreferalBalance = 0;
                }

                if(labourCharges != null && !labourCharges.isEmpty()){
                    mylabourCharges = Integer.parseInt(labourCharges);
                }else  {
                    mylabourCharges = 0;
                }

                if(engine_oil_price != null && !engine_oil_price.isEmpty() && !engine_oil_price.equals("0")){
                    myengine_oil_price = Integer.parseInt(engine_oil_price);
                }else  {
                    myengine_oil_price = 0;
                    llEngineOil.setVisibility(View.GONE);
                }

                if(pick_up_drop_price != null && !pick_up_drop_price.isEmpty() && !pick_up_drop_price.equals("0")){
                    mypick_up_drop_price = Integer.parseInt(pick_up_drop_price);
                }else  {
                    mypick_up_drop_price = 0;
                    llPickUpDrop.setVisibility(View.GONE);
                }

                if(other_charges != null && !other_charges.isEmpty() && !other_charges.equals("0")){
                    myother_charges = Integer.parseInt(other_charges);
                }else  {
                    myother_charges = 0;
                    llOtherCharges.setVisibility(View.GONE);
                }

                if(coupon_discount_amount != null && !coupon_discount_amount.isEmpty()){
                    mycoupon_discount_amount = Integer.parseInt(coupon_discount_amount);
                }else  {
                    mycoupon_discount_amount = 0;
                }

                if(washing != null && !washing.isEmpty() && !washing.equals("0")){
                    mywashing = Integer.parseInt(washing);
                }else  {
                    mywashing = 0;
                    llWashing.setVisibility(View.GONE);
                }

                gstApplicableAmount = mylabourCharges + mypartsLabourCharges +
                        mywashing + myother_charges;

                Log.d("GST App Amount-->", String.valueOf(gstApplicableAmount));
                fGstAmount = ((gstApplicableAmount * 18)/100);
                totalAmountWithoutOffer = mypartsLabourCharges + mypartAmount + mylabourCharges + myengine_oil_price + mypick_up_drop_price +
                        myother_charges + mywashing + fGstAmount;


                totalAmountWithOffer = totalAmountWithoutOffer - mycoupon_discount_amount - myreferalBalance;

                int subTotal = mypartsLabourCharges + mypartAmount + mylabourCharges + myengine_oil_price + mypick_up_drop_price +
                        myother_charges + mywashing;

                if(mypartsLabourCharges!= 0){
                    tvPartsLabourCharges.setText(String.valueOf(mypartsLabourCharges));
                }else {
                    llPartsLabourCharges.setVisibility(View.GONE);
                }

                if(mypartAmount!= 0){
                    tvBikePartCharges.setText(String.valueOf(mypartAmount));
                }else {
                    llPartsCharges.setVisibility(View.GONE);
                }

                mytotalAmountPaid = totalAmountWithoutOffer;
                tvLabourCharges.setText(String.valueOf(mylabourCharges));

                tvBikePartCharges.setText(String.valueOf(mypartAmount));
                tvEngineOil.setText(String.valueOf(myengine_oil_price));
                tvPickUpDrop.setText(String.valueOf(mypick_up_drop_price));
                tvWashing.setText(String.valueOf(mywashing));
                tvOtherCharges.setText(String.valueOf(myother_charges));
                tvGstAmount.setText(String.valueOf(fGstAmount));
                tvTotalAmount.setText(String.valueOf(mytotalAmountPaid));
                tvSubTotal.setText(String.valueOf(subTotal));

            } else if (entity.equals("BikePartChange")) {
                Log.d("Bike part -->",jsonObject.toString());
                String response = jsonObject.getString("data");
                JSONArray jsonArray = new JSONArray(response);

                getInvoiceByOrderId();
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

                mypartsLabourCharges =  laboutAmount;
                mypartAmount = partCharges;
            }
        } catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
