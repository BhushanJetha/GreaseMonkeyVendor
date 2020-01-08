package com.greasemonkey.vendor.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.greasemonkey.vendor.DashobardActivity;
import com.greasemonkey.vendor.HomeActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.ContactUsActivity;
import com.greasemonkey.vendor.common.ReferAndEarnActivity;
import com.greasemonkey.vendor.garage_detail.ChartListForLabourChargesActivity;
import com.greasemonkey.vendor.garage_detail.GarageDetailActivity;
import com.greasemonkey.vendor.garage_detail.MyProvidedManufacturerActivity;
import com.greasemonkey.vendor.garage_detail.MyServicesActivity;
import com.greasemonkey.vendor.garage_detail.VendorAddressDetailActivity;
import com.greasemonkey.vendor.garage_detail.ViewMyBankDetailActivity;
import com.greasemonkey.vendor.request_detail.RequestDetailActivity;
import com.greasemonkey.vendor.vendor_detail.BankDetailActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ProfileFragment extends Fragment {

    private LinearLayout llGarageDetail, llAddressDetail, llBankDetail, llMyServices,
            llmyBikes, llLabourCharges, llReferAndEarn, llContactUs;

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
                /*Intent i=new Intent(getContext(),ChartListForLabourChargesActivity.class);
                startActivity(i);*/

                CopyReadAssets();
               /* File fileBrochure = new File(Environment.getExternalStorageDirectory() + "/" + "labour_charges.pdf");
                if (!fileBrochure.exists())
                {
                    CopyAssetsBrochure();
                }*/

                /** PDF reader code */
               /* File file = new File(Environment.getExternalStorageDirectory() + "/" + "labour_charges.pdf");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(
                        Uri.fromFile(file),
                        "application/pdf");

                getContext().startActivity(intent);*/

                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try
                {
                    getContext().startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(getActivity(), "NO Pdf Viewer", Toast.LENGTH_SHORT).show();
                }*/
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
    }

    private void CopyAssetsBrochure() {
        Log.d("Inside -->","Download File");
        AssetManager assetManager = getActivity().getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        for(int i=0; i<files.length; i++) {
            String fStr = files[i];

            if(fStr.equalsIgnoreCase("labour_charges.pdf")) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    Log.d("Inside -->","Download File");
                    in = assetManager.open(files[i]);
                    out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + files[i]);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                    break;
                }
                catch(Exception e) {
                    Log.e("tag", e.getMessage());
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    //latest
    private void CopyReadAssets()
    {
        AssetManager assetManager = getActivity().getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getActivity().getFilesDir(), "labour_charges.pdf");
        try
        {
            in = assetManager.open("labour_charges.pdf");
            out = getActivity().openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e)
        {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getActivity().getFilesDir() + "/labour_charges.pdf"),
                "application/pdf");

        startActivity(intent);
    }


}
