package com.greasemonkey.vendor;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import com.greasemonkey.vendor.login.LoginActivity;
import com.greasemonkey.vendor.utility.UserPrefManager;

import androidx.appcompat.app.AppCompatActivity;

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
                    if(userPrefManager.getVendorVerificationStatus().equals("active")){
                        Intent i = new Intent(SplashActivity.this, DashobardActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                    Intent i = new Intent(SplashActivity.this,   LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
