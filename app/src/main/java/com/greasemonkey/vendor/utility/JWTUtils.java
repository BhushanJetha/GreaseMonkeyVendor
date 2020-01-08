package com.greasemonkey.vendor.utility;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by dell on 12/2/2019.
 */

public class JWTUtils {
    public static String decoded(String JWTEncoded) throws Exception {
        String token = "";
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));

            token = getJson(split[1]);
        } catch (UnsupportedEncodingException e) {
            //Error
        }

        return token;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
