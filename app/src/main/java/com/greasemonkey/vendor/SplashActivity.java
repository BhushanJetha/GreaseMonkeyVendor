package com.greasemonkey.vendor;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.Dash;
import com.greasemonkey.vendor.login.LoginActivity;
import com.greasemonkey.vendor.registration.RegistrationActivity;
import com.greasemonkey.vendor.servicing_request.PartChnageRequestActivity;
import com.greasemonkey.vendor.utility.UserPrefManager;
import com.greasemonkey.vendor.vendor_detail.BankDetailActivity;
import com.greasemonkey.vendor.vendor_detail.BikeListActivity;
import com.greasemonkey.vendor.vendor_detail.RegisterAddressActivity;
import com.greasemonkey.vendor.vendor_detail.ServiceDetailActivity;
import com.greasemonkey.vendor.vendor_detail.VendorLocationActivity;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userPrefManager= new UserPrefManager(getApplicationContext());

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                if(userPrefManager.getLoginStatus().equals("LoginSuccess")){
                    Intent i = new Intent(SplashActivity.this, DashobardActivity.class);
                    startActivity(i);
                    //finish();
                } else {
                    Intent i = new Intent(SplashActivity.this,   LoginActivity.class);
                    startActivity(i);
                }


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
