package com.greasemonkey.vendor.comunication;

import org.json.JSONObject;

/**
 * Created by dell on 12/14/2017.
 */
public interface IResponse {

    void onRequestComplete(JSONObject jsonObject, String entity);
}
