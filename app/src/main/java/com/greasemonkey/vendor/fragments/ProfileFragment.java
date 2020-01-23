package com.greasemonkey.vendor.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.fragment.app.Fragment;


public class ProfileFragment extends Fragment {

    private LinearLayout llGarageDetail, llAddressDetail, llBankDetail, llMyServices,
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
}
