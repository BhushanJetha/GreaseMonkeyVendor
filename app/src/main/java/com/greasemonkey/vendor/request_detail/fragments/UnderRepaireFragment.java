package com.greasemonkey.vendor.request_detail.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.notification.NotificationAdapter;
import com.greasemonkey.vendor.notification.NotificationModel;
import com.greasemonkey.vendor.servicing_request.RequestStatusActivity;
import com.greasemonkey.vendor.utility.NetworkDialog;
import com.greasemonkey.vendor.utility.recyclerview_click_handler.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UnderRepaireFragment extends Fragment {

    ArrayList<NotificationModel> notifications;
    private RecyclerView mRecyclerView;
    private NotificationAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_under_repaire, container, false);

        notifications=new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.rvServiceList);
        getRequests();

        mRecyclerView.addOnItemTouchListener(new
                RecyclerTouchListener(this.getContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                NotificationModel requests= notifications.get(position);
                Intent i=new Intent(getContext(),RequestStatusActivity.class);
                i.putExtra("OrderId",requests.getOrderId());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    private void getRequests() {
        try{
            if(NetworkDialog.isOnline(getActivity())){
                final int REQUEST_TIMEOUT_MS = 6000000;
                final ProgressDialog progressRing = ProgressDialog.show(getActivity(), "Progressing", "please wait...", true);
                progressRing.setCancelable(false);

                final JSONObject jsonObject=new JSONObject();
                jsonObject.put("vendorId","6");
                jsonObject.put("orderStatus","Work In Progress");

                RequestQueue queue = Volley.newRequestQueue(getActivity());

                JsonObjectRequest request = new JsonObjectRequest(Constant.POST, Constant.getOrderByStatus, jsonObject,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // TODO Auto-generated method stub

                                Log.d("Server --->", "response::" + response.toString());
                                progressRing.dismiss();

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

                                        notifications.add(new NotificationModel(orderId,userId,serviceType,pickupAndDrop,"-",orderDate,orderTime,userName));
                                    }

                                    if(notifications.size()>0){

                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        mAdapter = new NotificationAdapter(notifications);
                                        mRecyclerView.setAdapter(mAdapter);
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
}
