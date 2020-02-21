package com.greasemonkey.vendor.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.greasemonkey.vendor.DashobardActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.request_detail.RequestDetailActivity;
import com.greasemonkey.vendor.servicing_request.RequestStatusActivity;
import com.greasemonkey.vendor.utility.NetworkDialog;
import com.greasemonkey.vendor.utility.UserPrefManager;
import com.greasemonkey.vendor.utility.recyclerview_click_handler.RecyclerTouchListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private ArrayList<ServiceRequestModel> todaysOrdersList;
    private RecyclerView mRecyclerView;
    private ServiceRequestAdapter mAdapter;
    private SwitchCompat sShopTodaysStatus, sPickUpDropStatus, sShopTommarowsStatus;
    private LinearLayout llTomorowsStatus;
    private UserPrefManager userPrefManager;
    private int notificationCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        todaysOrdersList=new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.rvServiceRequest);
        sShopTodaysStatus = view.findViewById(R.id.switcherShopStatus);
        sPickUpDropStatus = view.findViewById(R.id.switcherPickUpDropStatus);
        sShopTommarowsStatus = view.findViewById(R.id.switcherNextDayStatus);
        llTomorowsStatus = view.findViewById(R.id.llTomorrowsStatus);
        userPrefManager= new UserPrefManager(this.getContext());

        String onlineStatus = userPrefManager.getOnlineStatus();
        String pickUpDropStatus = userPrefManager.getPickupDropStatus();
        String tommaroesStatus = userPrefManager.getTommarowStatus();
        Log.d("Shop status", onlineStatus);

        Log.d("Status-->", onlineStatus);
        if(onlineStatus.equals("active")){
            sShopTodaysStatus.setChecked(true);
        }else {
            sShopTodaysStatus.setChecked(false);
        }

        if (pickUpDropStatus.equals("yes")){
            sPickUpDropStatus.setChecked(true);
        }else {
            sPickUpDropStatus.setChecked(false);
        }

        if (tommaroesStatus.equals("yes")){
            sShopTommarowsStatus.setChecked(true);
        }else {
            sShopTommarowsStatus.setChecked(false);
        }


       /* TextView tv = view.findViewById(R.id.notificationsBadgeTextView);
        tv.setText("22");*/


        getRequests();
        onClick();

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ServiceRequestModel requests= todaysOrdersList.get(position);
                Log.d("Order Id-->",requests.getOrderId());
                if(requests.getOrderStatus().equals("Fresh Order")){
                    Intent i=new Intent(getContext(),RequestDetailActivity.class);
                    i.putExtra("OrderId",requests.getOrderId());
                    i.putExtra("GMOrderId",requests.getGmOrderId());
                    startActivity(i);
                }else {
                    Intent i=new Intent(getContext(),RequestStatusActivity.class);
                    i.putExtra("OrderId",requests.getOrderId());
                    i.putExtra("UserId",requests.getUserId());
                    i.putExtra("GMOrderId",requests.getGmOrderId());
                    startActivity(i);
                }
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));

        return view;
    }

    private void onClick(){
        sShopTodaysStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llTomorowsStatus.setVisibility(View.GONE);
                    setShopOnlineStatus(Constant.setShopOnlineStatus, "active");
                    userPrefManager.setOnlineStatus("active");
                } else {
                    llTomorowsStatus.setVisibility(View.VISIBLE);
                    setShopOnlineStatus(Constant.setShopOnlineStatus,"deactive");
                    userPrefManager.setOnlineStatus("deactive");
                }
            }
        });

        sPickUpDropStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setShopOnlineStatus(Constant.setPickUpDropStatus, "yes");
                    userPrefManager.setPickupDropStatus("yes");
                } else {
                    setShopOnlineStatus(Constant.setPickUpDropStatus, "no");
                    userPrefManager.setPickupDropStatus("no");
                }
            }
        });

        sShopTommarowsStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setShopOnlineStatus(Constant.setShopTommarowsStatus, "yes");
                    userPrefManager.setTommarowStatus("yes");
                } else {
                    setShopOnlineStatus(Constant.setShopTommarowsStatus, "no");
                    userPrefManager.setTommarowStatus("no");
                }
            }
        });
    }

    private void getRequests() {
        try{
            if(NetworkDialog.isOnline(getActivity())){
                final int REQUEST_TIMEOUT_MS = 6000000;
                final ProgressDialog progressRing = ProgressDialog.show(getActivity(), "Progressing", "please wait...", true);
                progressRing.setCancelable(false);

                final JSONObject jsonObject=new JSONObject();
                jsonObject.put("vendorId",userPrefManager.getVendorId());

                Log.d("Request-->",jsonObject.toString());

                RequestQueue queue = Volley.newRequestQueue(getActivity());

                JsonObjectRequest request = new JsonObjectRequest(Constant.POST, Constant.getServiceRequest, jsonObject,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // TODO Auto-generated method stub

                                Log.d("Server --->", "response::" + response.toString());
                                progressRing.dismiss();
                                notificationCount = 0;

                                try {
                                    String strResponse = response.getString("data");
                                    Log.d("Response -->", strResponse);
                                    JSONArray jsonArray = new JSONArray(strResponse);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String orderId = jsonObject1.getString("orderId");
                                        String userId = jsonObject1.getString("userId");
                                        String serviceType = jsonObject1.getString("serviceType");
                                        String pickupAndDrop = jsonObject1.getString("pickupAndDrop");
                                        String orderDate = jsonObject1.getString("orderDate");
                                        String orderTime = jsonObject1.getString("orderTime");
                                        String userName = jsonObject1.getString("userName");
                                        String gmOrderId = jsonObject1.getString("gmOrderId");
                                        String orderStatus = jsonObject1.getString("orderStatus");


                                        if(orderStatus.equals("Fresh Order")){
                                            notificationCount++;
                                        }
                                        todaysOrdersList.add(new ServiceRequestModel(orderId,userId,serviceType,pickupAndDrop,"-",orderDate,orderTime,userName,orderStatus,gmOrderId));
                                    }

                                    if(todaysOrdersList.size()>0){

                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        mAdapter = new ServiceRequestAdapter(todaysOrdersList);
                                        mRecyclerView.setAdapter(mAdapter);

                                        ((DashobardActivity) getActivity()).setCount(notificationCount);
                                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                                        mRecyclerView.setLayoutManager(llm);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressRing.dismiss();
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                                Log.d("Response--->",res);
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                                Log.d("Login", "onErrorResponse: " + obj.toString());

                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }

                    }

                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        return params;
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request.setShouldCache(false);
                queue.add(request);
            }
            else
                NetworkDialog.showDialog(getActivity());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setShopOnlineStatus(String api, String status) {
        try{
            if(NetworkDialog.isOnline(getActivity())){
                final int REQUEST_TIMEOUT_MS = 6000000;
                final ProgressDialog progressRing = ProgressDialog.show(getActivity(), "Progressing", "please wait...", true);
                progressRing.setCancelable(false);

                final JSONObject jsonObject=new JSONObject();
                jsonObject.put("vendorId",userPrefManager.getVendorId());
                if(api.equals(Constant.setPickUpDropStatus)){
                    jsonObject.put("pickupDropStatus",status);
                   // jsonObject.put("onlineStatus",status);
                }else if(api.equals(Constant.setShopTommarowsStatus)){
                    jsonObject.put("status",status);
                }

                Log.d("Request-->", jsonObject.toString());
                Log.d("API -->", api);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                JsonObjectRequest request = new JsonObjectRequest(Constant.POST, api, jsonObject,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // TODO Auto-generated method stub

                                Log.d("Server --->", "response::" + response.toString());
                                progressRing.dismiss();

                                try {
                                    String strResponse = response.getString("status");
                                    Log.d("Response -->", strResponse);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressRing.dismiss();
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));

                                Log.d("Response--->",res);
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                                Log.d("Login", "onErrorResponse: " + obj.toString());

                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                    }

                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        return params;
                    }
                };

                request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request.setShouldCache(false);
                queue.add(request);
            }
            else
                NetworkDialog.showDialog(getActivity());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
