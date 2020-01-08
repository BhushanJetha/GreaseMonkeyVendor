package com.greasemonkey.vendor.common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.greasemonkey.vendor.R;

public class ReferAndEarnActivity extends AppCompatActivity {

    Button btnConfirmBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);

        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);


        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(
                        Intent.EXTRA_TEXT, "Download Grease Monkey App from this link https://bit.ly/2qX5raI");
                startActivity(Intent.createChooser(shareIntent, "send to"));
            }
        });


    }

}
