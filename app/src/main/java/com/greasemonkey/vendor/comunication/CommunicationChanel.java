package com.greasemonkey.vendor.comunication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

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
import com.greasemonkey.vendor.utility.NetworkDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by dell on 12/11/2017.
 */
public  class CommunicationChanel {

    IResponse iResponse;

    public  void communicateWithServer(Context context, int methodType, String url, JSONObject data, final String entity){

        try{
            if(NetworkDialog.isOnline(context)){
                final int REQUEST_TIMEOUT_MS = 6000000;
                final ProgressDialog progressRing = ProgressDialog.show(context, "Progressing", "please wait...", true);
                progressRing.setCancelable(false);

                iResponse= (IResponse) context;
                RequestQueue queue = Volley.newRequestQueue(context);

                JsonObjectRequest request = new JsonObjectRequest(methodType, url, data,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // TODO Auto-generated method stub

                                Log.d("Server --->", "response::" + response.toString());
                                progressRing.dismiss();

                                iResponse.onRequestComplete(response,entity);
                                //responseJsonObject = response;

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

                                        iResponse.onRequestComplete(obj,entity);
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
                NetworkDialog.showDialog(context);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
