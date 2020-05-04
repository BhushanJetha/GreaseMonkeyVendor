package com.greasemonkey.vendor.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.greasemonkey.vendor.servicing_request.RequestStatusActivity;
import com.greasemonkey.vendor.utility.NetworkDialog;
import com.greasemonkey.vendor.utility.UserPrefManager;
import com.greasemonkey.vendor.utility.recyclerview_click_handler.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AccountFragment extends Fragment {

    ArrayList<HistoryModel> notifications;
    private RecyclerView mRecyclerView;
    private HistoryAdapter mAdapter;
    private UserPrefManager userPrefManager;
    private LinearLayout llStartDate, llEndDate;
    private TextView tvStartDate, tvEndDate;
    private Button btnShowHistory;
    private int mYear, mMonth, mDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        notifications=new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.rvServiceList);
        llStartDate = view.findViewById(R.id.llStartDate);
        llEndDate = view.findViewById(R.id.llEndDate);
        btnShowHistory = view.findViewById(R.id.btnShowHistory);
        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);

        userPrefManager= new UserPrefManager(this.getContext());

        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        Log.d("Month-->",dateFormat.format(date));

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = 1;
        c.set(year, month, day);
        int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.d("First Day of month",c.getTime().toString());

        Date startDate = null;
        Date endDate = null;

        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            startDate = formatter.parse(c.getTime().toString());
            Log.e("formated date ", endDate + "");

            String formateDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            Log.v("Start date--> ",formateDate);

            c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth-1);
            endDate = formatter.parse(c.getTime().toString());
            Log.e("formated date ", endDate + "");

            String endFormatedDate = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
            Log.v("End date--> ",endFormatedDate);

            tvStartDate.setText(formateDate);
            tvEndDate.setText(endFormatedDate);
            getRequests(formateDate,endFormatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        onClick();
        return view;
    }

    private void onClick(){

        mRecyclerView.addOnItemTouchListener(new
                RecyclerTouchListener(this.getContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                //HistoryModel requests= notifications.get(position);
                Intent i=new Intent(getContext(),RequestStatusActivity.class);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        llStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String date1 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                Date date = null;
                                try {
                                    date = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
                                    String formateDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                                    tvStartDate.setText(formateDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        llEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String date1 = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                Date date = null;
                                try {
                                    date = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
                                    String formateDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                                    tvEndDate.setText(formateDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnShowHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRequests(tvStartDate.getText().toString(), tvEndDate.getText().toString());
            }
        });
    }

    private void getRequests(String startDate, String endDate) {
        try{
            notifications.clear();
            if(NetworkDialog.isOnline(getActivity())){
                final int REQUEST_TIMEOUT_MS = 6000000;
                final ProgressDialog progressRing = ProgressDialog.show(getActivity(), "Progressing", "please wait...", true);
                progressRing.setCancelable(false);

                final JSONObject jsonObject=new JSONObject();
                jsonObject.put("vendorId",userPrefManager.getVendorId());
                jsonObject.put("startDate",startDate);
                jsonObject.put("endDate",endDate);

                RequestQueue queue = Volley.newRequestQueue(getActivity());

                JsonObjectRequest request = new JsonObjectRequest(Constant.POST, Constant.getOrderHistory, jsonObject,

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
                                        String orderStatus = jsonObject1.getString("orderStatus");
                                        String totalAmountPaid = jsonObject1.getString("totalAmountPaid");

                                        if(orderStatus.equals("Delivered")) {
                                            notifications.add(new HistoryModel(orderId, userId, serviceType, pickupAndDrop,totalAmountPaid , orderDate, orderTime, "",orderStatus));
                                        }
                                    }

                                    if(notifications.size()>0){

                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        mAdapter = new HistoryAdapter(notifications);
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
