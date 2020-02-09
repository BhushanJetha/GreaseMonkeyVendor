package com.greasemonkey.vendor.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.ContactUsActivity;
import com.greasemonkey.vendor.common.ReferAndEarnActivity;
import com.greasemonkey.vendor.garage_detail.GarageDetailActivity;
import com.greasemonkey.vendor.garage_detail.MyProvidedManufacturerActivity;
import com.greasemonkey.vendor.garage_detail.MyServicesActivity;
import com.greasemonkey.vendor.garage_detail.VendorAddressDetailActivity;
import com.greasemonkey.vendor.garage_detail.ViewMyBankDetailActivity;
import com.greasemonkey.vendor.login.LoginActivity;
import com.greasemonkey.vendor.utility.UserPrefManager;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


public class ProfileFragment extends Fragment {

    private LinearLayout llGarageDetail, llAddressDetail, llBankDetail, llMyServices, llTermsAndCondition,
            llmyBikes, llLabourCharges, llReferAndEarn, llContactUs, llLogout;

    private UserPrefManager userPrefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        llGarageDetail = view.findViewById(R.id.llGarageDetail);
        llAddressDetail = view.findViewById(R.id.llAddressDetail);
        llBankDetail = view.findViewById(R.id.llBankDetail);
        llMyServices = view.findViewById(R.id.llMyServices);
        llmyBikes = view.findViewById(R.id.llMyBikes);
        llLabourCharges = view.findViewById(R.id.llLabourCharges);
        llTermsAndCondition = view.findViewById(R.id.llTermsAndCondition);

        llReferAndEarn = view.findViewById(R.id.llReferAndEarn);
        llContactUs = view.findViewById(R.id.llContactUs);
        llLogout = view.findViewById(R.id.llLogout);

        userPrefManager= new UserPrefManager(this.getActivity());

        onClick();

        return view;
    }

    private void onClick(){
        llGarageDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),GarageDetailActivity.class);
                startActivity(i);
            }
        });

        llAddressDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),VendorAddressDetailActivity.class);
                startActivity(i);
            }
        });

        llBankDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),ViewMyBankDetailActivity.class);
                startActivity(i);
            }
        });

        llMyServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),MyServicesActivity.class);
                startActivity(i);
            }
        });

        llmyBikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),MyProvidedManufacturerActivity.class);
                startActivity(i);
            }
        });

        llLabourCharges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://157.245.99.66:9000/getLabourCharges.pdf"));
                startActivity(browserIntent);
            }
        });

        llTermsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback();
            }
        });

        llReferAndEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),ReferAndEarnActivity.class);
                startActivity(i);
            }
        });

        llContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),ContactUsActivity.class);
                startActivity(i);
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPrefManager.setLoginStatus("Logout");
                Intent i=new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
    }

    private void feedback() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.terms_and_condition, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        dialogBuilder.setTitle("Terms & Conditions");
        //dialogBuilder.setMessage("please send me to your feedback.");

        WebView webView;
        webView = (WebView) dialogView.findViewById(R.id.simpleWebView);
        // displaying content in WebView from html file that stored in assets folder
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_res/raw/" + "guidelines_and_policies.html");

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                b.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                b.dismiss();
            }
        });


        b.show();
    }

}
