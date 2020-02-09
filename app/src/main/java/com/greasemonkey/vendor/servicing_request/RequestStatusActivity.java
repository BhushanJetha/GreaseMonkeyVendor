package com.greasemonkey.vendor.servicing_request;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.greasemonkey.vendor.BaseActivity;
import com.greasemonkey.vendor.DashobardActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.login.LoginActivity;
import com.greasemonkey.vendor.utility.ConvertAddressToLatLong;
import com.greasemonkey.vendor.utility.UserPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RequestStatusActivity extends BaseActivity implements IResponse, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Button btnSubmit, btnSendRequest, btnAddress;
    private TextView tvUserName, tvMobileNumber, tvManufacturer, tvModel, tvServivceType, tvAMC, tvPickupDrop,
            tvPickUpDateTime, tvAddress, tvComment, tvOrderId, tvRegistrationNumber, tvOrderStatus,
            tvEstemateDeliveryDate, tvEstimateDeliveryTime, tvGMSupport;

    private Spinner OrderSpinner;
    private String orderId = "", strOrderStatus = "", strEstimateBill, strUserId = "", strGMOrderId = "", strEngineCC;
    private LinearLayout llOrderStatus;
    private ScrollView mScrollView;

    //Google Map
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int MY_PERMISSIONS_REQUEST_call = 22;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker, mUserLocationMarker;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private String finalLatitude, finalLongitude;
    private LatLng latLng;
    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);

        setToolbar();
        setBackArrow(true);
        setTitle("Request Status");
        init();
        onClick();
        getOrderDetail();
        checkCallingPermission();
    }

    private void init() {
        userPrefManager = new UserPrefManager(this);

        orderId = getIntent().getStringExtra("OrderId");
        strUserId = getIntent().getStringExtra("UserId");
        strGMOrderId = getIntent().getStringExtra("GMOrderId");

        mScrollView = findViewById(R.id.svServiceDetail);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSendRequest = findViewById(R.id.btnSendRequest);
        tvUserName = findViewById(R.id.tvUserName);
        tvMobileNumber = findViewById(R.id.tvMobileNumber);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvRegistrationNumber = findViewById(R.id.tvRegistrationNumber);
        tvManufacturer = findViewById(R.id.tvManufacturere);
        tvModel = findViewById(R.id.tvModel);
        tvServivceType = findViewById(R.id.tvServiceType);
        tvAMC = findViewById(R.id.tvAMCType);
        tvPickupDrop = findViewById(R.id.tvPickUpAndDrop);
        tvPickUpDateTime = findViewById(R.id.tvPickupDateTime);
        tvAddress = findViewById(R.id.tvAddress);
        tvComment = findViewById(R.id.tvCommnent);
        tvEstemateDeliveryDate = findViewById(R.id.tvEstemateDate);
        tvEstimateDeliveryTime = findViewById(R.id.tvEstimateTime);
        OrderSpinner = findViewById(R.id.orderStatus);
        btnAddress = findViewById(R.id.btnAddress);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);

        llOrderStatus = findViewById(R.id.llOrderStatus);
        tvGMSupport = findViewById(R.id.tvGMSupport);

        if (tvMobileNumber != null) {
            Linkify.addLinks(tvMobileNumber, Patterns.PHONE, "tel:", Linkify.sPhoneNumberMatchFilter, Linkify.sPhoneNumberTransformFilter);
            tvMobileNumber.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }

    private void onClick() {

        tvMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tvMobileNumber.getText().toString()));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });

        tvGMSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tvGMSupport.getText().toString()));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String btnText = btnSubmit.getText().toString();

                if(btnText.equals("Generate Bill")){
                    Intent i = new Intent(getApplicationContext(), GenerateBillActivity.class);
                    i.putExtra("OrderId",orderId);
                    i.putExtra("GmOrderId",strGMOrderId);
                    i.putExtra("EstimateBill",strEstimateBill);
                    startActivity(i);
                }else {
                    String orderStatus = OrderSpinner.getSelectedItem().toString();

                    if(!orderStatus.equals("Select")){
                        sendOrderStatus(orderStatus);
                    }
                }
            }
        });

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        PartChnageRequestActivity.class);
                i.putExtra("OrderId",orderId);
                i.putExtra("UserId",strUserId);
                i.putExtra("GmOrderId",strUserId);
                i.putExtra("EngigneCC", strEngineCC);
                startActivity(i);
            }
        });

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RequestAddressActivity.class);
                i.putExtra("Latitude",finalLatitude);
                i.putExtra("Longitude",finalLongitude);
                startActivity(i);
            }
        });
    }

    public boolean checkCallingPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

                new AlertDialog.Builder(this)
                        .setTitle("Calling Permission")
                        .setMessage("Grease monkey wants to access your current location.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(RequestStatusActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        MY_PERMISSIONS_REQUEST_call);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_call);
            }
            return false;
        } else {
            return true;
        }
    }

    private void getOrderDetail() {
        try{
            JSONObject jsonObject=new JSONObject();

            jsonObject.put("orderId",orderId);
            jsonObject.put("vendorId",userPrefManager.getVendorId());

            Log.d("Json-->",jsonObject.toString());
            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(RequestStatusActivity.this,
                    Constant.POST, Constant.getOrderDetail,jsonObject,"orderDetail");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendOrderStatus(String orderStatus) {
        try{
            JSONObject jsonObject=new JSONObject();

            jsonObject.put("orderId",orderId);
            jsonObject.put("orderStatus",orderStatus);

            Log.d("Json-->",jsonObject.toString());
            CommunicationChanel communicationChanel =new CommunicationChanel();
            communicationChanel.communicateWithServer(RequestStatusActivity.this,
                    Constant.POST, Constant.sendOrderStatus,jsonObject,"updateOrderStatus");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {

        try {
            if(entity.equals("orderDetail")){
                Log.d("Order Detail-->",jsonObject.toString());
                String response = jsonObject.getString("data");
                Log.d("Response ##-->",response);

                JSONArray jsonArray = new JSONArray(response);
                //JSONObject object = new JSONObject(response);
                JSONObject object = jsonArray.getJSONObject(0);

                String serviceType = object.getString("serviceType");
                String serviceDetail = object.getString("serviceDetail");
                String pickupAndDrop = object.getString("pickupAndDrop");
                String gstAmount = object.getString("gstAmount");
                strEstimateBill = object.getString("totalAmountPaid");
                strOrderStatus = object.getString("orderStatus");
                String orderDate = object.getString("orderDate");
                String orderTime = object.getString("orderTime");

                tvOrderStatus.setText(strOrderStatus);

                Log.d("Service Detail -->", serviceDetail);

                if(!serviceDetail.isEmpty()){
                    JSONObject serviceDetailJson = new JSONObject(serviceDetail);

                    if(serviceDetailJson.has("is_oil_change")) {
                        String is_oil_change = serviceDetailJson.getString("is_oil_change");
                        String is_oil_type = serviceDetailJson.getString("is_oil_type");
                        String is_oil_brand = serviceDetailJson.getString("is_oil_brand");
                        String is_wash = serviceDetailJson.getString("is_wash");
                        String is_pickup = serviceDetailJson.getString("is_pickup");

                        String strServiceDetail = "Oil Change - " + is_oil_change + "\n" + "Oil Type - " + is_oil_type + "\n" +
                                "Oil Brand - " + is_oil_brand + "\n" + "Bike Wash - " + is_wash;
                        tvComment.setText(strServiceDetail);
                    }

                }
                String additionalInfo = "", state ="", city="",addressLine1="", addressLine2="", landmark="", pincode="",
                        username = "", pickupDate ="",pickupTime="",manufacture="", modelName="", mobile="",
                        vehicleRegisterNo="";

                if(object.has("vehicleRegisterNo")) {
                    vehicleRegisterNo = object.getString("vehicleRegisterNo");
                }
                if(object.has("username")){
                    username = object.getString("username");
                }else if(object.has("userName")){
                    username = object.getString("userName");
                }
                if(object.has("mobile")) {
                    mobile = object.getString("mobile");
                }
                if(object.has("manufacture")) {
                    manufacture = object.getString("manufacture");
                }
                if(object.has("modelName")) {
                    modelName = object.getString("modelName");
                }
                if(object.has("pickupDate")) {
                    pickupDate = object.getString("pickupDate");
                }
                if(object.has("pickupTime")) {
                    pickupTime = object.getString("pickupTime");
                }
                if(object.has("additionalInfo")) {
                    additionalInfo = object.getString("additionalInfo");
                }
                if(object.has("state")) {
                    state = object.getString("state");
                }
                if(object.has("city")) {
                    city = object.getString("city");
                }
                if(object.has("line1")) {
                    addressLine1 = object.getString("line1");
                }
                if(object.has("line2")) {
                    addressLine2 = object.getString("line2");
                }
                if(object.has("landmark")) {
                    landmark = object.getString("landmark");
                }
                if(object.has("pincode")) {
                    pincode = object.getString("pincode");
                }

                if(object.has("engineCC")){
                    strEngineCC = object.getString("engineCC");
                }

                String address = addressLine1 + ", " + addressLine2 + ", " + landmark + ", " + state + ", " + city + ", " + pincode;
                String dateTime = pickupDate + " " +  pickupTime;


                tvOrderId.setText(strGMOrderId);
                tvRegistrationNumber.setText(vehicleRegisterNo);
                tvUserName.setText(username);
                tvMobileNumber.setText(mobile);
                tvManufacturer.setText(manufacture);
                tvModel.setText(modelName);
                tvServivceType.setText(serviceType);
                tvAMC.setText("RS."+strEstimateBill);
                tvPickupDrop.setText(pickupAndDrop);
                tvPickUpDateTime.setText(dateTime);
               // tvComment.setText(additionalInfo);


                if(pickupDate.isEmpty()){
                    tvPickUpDateTime.setText("-");
                }else
                    tvPickUpDateTime.setText(dateTime);

               /* if(additionalInfo.isEmpty()){
                    tvComment.setText("-");
                }else
                    tvComment.setText(additionalInfo);*/

                if(addressLine1.isEmpty()){
                    tvAddress.setText("-");
                }else {
                    tvAddress.setText(address);
                    latLng = ConvertAddressToLatLong.getLocationFromAddress(getApplicationContext(),address);

                    finalLatitude = String.valueOf(latLng.latitude);
                    finalLongitude = String.valueOf(latLng.longitude);
                    Log.d("Latitude--->", String.valueOf(latLng.latitude));
                    Log.d("Longitude--->", String.valueOf(latLng.longitude));
                }

                Log.d("Order Status-->",strOrderStatus);
                if(strOrderStatus.equals("Work in Progress")){
                    btnSendRequest.setVisibility(View.VISIBLE);
                }else if(strOrderStatus.equals("Completed")){
                    llOrderStatus.setVisibility(View.GONE);
                    btnSubmit.setText("Generate Bill");
                }

            }else if (entity.equals("updateOrderStatus")){
                Log.d("Order Status-->",jsonObject.toString());
                String response = jsonObject.getString("message");

                if(response.equals("Order updated successfully.")){
                    Intent i = new Intent(getApplicationContext(), DashobardActivity.class);
                    startActivity(i);
                    finish();
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create()
        .setInterval(1000)
        .setFastestInterval(1000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        if(this.latLng != null)
        {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            MarkerOptions userLocationMarker = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.draggable(true);

            userLocationMarker.position(this.latLng);
            userLocationMarker.draggable(true);


            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            String provider = locationManager.getBestProvider(new Criteria(), true);
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mCurrLocationMarker = mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                        this);
            }

            //userLocationMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mUserLocationMarker = mMap.addMarker(userLocationMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
        //Showing Current Location Marker on Map
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }

                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_call: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                    }
                }
                return;
            }
        }
    }

}
