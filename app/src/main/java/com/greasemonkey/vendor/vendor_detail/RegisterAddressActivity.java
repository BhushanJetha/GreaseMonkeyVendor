package com.greasemonkey.vendor.vendor_detail;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Interpolator;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;


import android.graphics.Interpolator;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.utility.UserPrefManager;
import com.greasemonkey.vendor.vendor_detail.model.CityMaster;
import com.greasemonkey.vendor.vendor_detail.model.StateMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class RegisterAddressActivity extends BaseActivity implements IResponse, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerDragListener {

    private EditText etLandmark, etAddressLine1, etAddressLine2, etPincode;
    private Spinner spinnerState, spinnerCity;
    private Button btnNext;
    private TextView tvLocateOnMap;

    private String strStateId, strCityId, strLandmark, strAddressLine1, strAddressLine2, strPincode;
    private ArrayList<StateMaster> stateList;
    private ArrayList<CityMaster> cityList;

    //Google Map
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;

    private String finalLatitude, finalLongitude;
    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address_address);

        init();
        onClick();
        getState();
    }

    private void init() {
        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);

        etLandmark = (EditText) findViewById(R.id.etLandmark);
        etAddressLine1 = (EditText) findViewById(R.id.etAddressLine1);
        etAddressLine2 = (EditText) findViewById(R.id.etAddressLine2);
        etPincode = (EditText) findViewById(R.id.etPincode);

        btnNext = (Button) findViewById(R.id.btnNext);

        tvLocateOnMap = (TextView) findViewById(R.id.locateOnMap);

        stateList = new ArrayList<>();
        cityList = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        try{
            userPrefManager = new UserPrefManager(getApplicationContext());

            Bundle bundle = getIntent().getExtras();
            finalLongitude = bundle.getString("Longitude");
            finalLatitude = bundle.getString("Latitude");

            Log.d("Lat--",finalLatitude);
            Log.d("Long--",finalLongitude);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onClick() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    JSONObject jsonObject = new JSONObject();
                    strLandmark = etLandmark.getText().toString();
                    strAddressLine1 = etAddressLine1.getText().toString();
                    strAddressLine2 = etAddressLine2.getText().toString();
                    strPincode = etPincode.getText().toString();

                    if (strStateId.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Select State !", Toast.LENGTH_LONG).show();
                    } else if (strCityId.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Select City !", Toast.LENGTH_LONG).show();
                    } else if (strLandmark.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter Landmark !", Toast.LENGTH_LONG).show();
                    } else if (strAddressLine1.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter Address Line1 !", Toast.LENGTH_LONG).show();
                    } else if (strPincode.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Enter Pin-Code !", Toast.LENGTH_LONG).show();
                    } else if (strPincode.length() != 6) {
                        Toast.makeText(getApplicationContext(), "Please Enter 6 Digit Pin-Code !", Toast.LENGTH_LONG).show();
                    } else if (finalLatitude == null && finalLongitude == null) {
                        Toast.makeText(getApplicationContext(), "Please locate your garage on google map !", Toast.LENGTH_LONG).show();
                    } else {

                        jsonObject.put("vendorId", userPrefManager.getVendorId());
                        jsonObject.put("addressLine1", strAddressLine1);
                        jsonObject.put("addressLine2", strAddressLine2);
                        jsonObject.put("landmark", strLandmark);
                        jsonObject.put("pincode", strPincode);
                        jsonObject.put("stateId", strStateId);
                        jsonObject.put("cityId", strCityId);
                        jsonObject.put("longitude", finalLongitude);
                        jsonObject.put("latitude", finalLatitude);

                        Log.d("Json-->", jsonObject.toString());
                        CommunicationChanel communicationChanel = new CommunicationChanel();
                        communicationChanel.communicateWithServer(RegisterAddressActivity.this,
                                Constant.POST, Constant.garageAddressDetail, jsonObject, "AddressDetail");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                StateMaster stateMaster = stateList.get(position);
                //strStateId = parent.getItemAtPosition(position).toString();
                strStateId = stateMaster.getStateId();
                getCity(stateMaster.getStateId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                //strCityId = parent.getItemAtPosition(position).toString();

                CityMaster cityMaster = cityList.get(position);
                strCityId = cityMaster.getCityId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvLocateOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // selectLocationDialogBox();
            }
        });
    }

    private void getState() {
        CommunicationChanel communicationChanel = new CommunicationChanel();
        communicationChanel.communicateWithServer(RegisterAddressActivity.this,
                Constant.GET, Constant.stateAPI, null, "state");
    }

    private void getCity() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("stateId", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommunicationChanel communicationChanel = new CommunicationChanel();
        communicationChanel.communicateWithServer(RegisterAddressActivity.this,
                Constant.POST, Constant.cityAPI, jsonObject, "city");
    }

    private void getCity(String stateId) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("stateId", stateId);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommunicationChanel communicationChanel = new CommunicationChanel();
        communicationChanel.communicateWithServer(RegisterAddressActivity.this,
                Constant.POST, Constant.cityAPI, jsonObject, "city");
    }


   /* private void selectLocationDialogBox() {

        try {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();

            View dialogView = null;

            dialogView = inflater.inflate(R.layout.select_location_layout, null);

            dialogBuilder.setView(dialogView);

            dialogBuilder.setTitle("Select Your Location");
            dialogBuilder.setMessage("Hold the marker and drag to your garage location !!");

            Button btnDoneLocation = (Button) dialogView.findViewById(R.id.btnDoneLocation);
            final SupportMapFragment mapFragment = (SupportMapFragment)
                    getSupportFragmentManager()
                            .findFragmentById(R.id.map);


            mapFragment.getMapAsync(this);

            final AlertDialog b = dialogBuilder.create();
            b.show();

            btnDoneLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();

                    b.dismiss();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }*/



    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        try {
            Log.d("State Response-->",jsonObject.toString());

            if(entity.equals("state")){
                String response = jsonObject.getString("data");
                Log.d("Response ##-->",response);


                JSONArray jsonArray=new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject userDetail= (JSONObject) jsonArray.get(i);
                    String stateId=userDetail.getString("stateId");
                    String stateName=userDetail.getString("stateName");

                    StateMaster stateMaster=new StateMaster(stateId,stateName);
                    stateList.add(stateMaster);
                }

                ArrayAdapter<StateMaster> stateAdapter = new ArrayAdapter<StateMaster>(this, android.R.layout.simple_spinner_item, stateList);
                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerState.setAdapter(stateAdapter);
                spinnerState.setSelection(stateAdapter.getPosition(stateList.get(0)));
                getCity();
            }else if(entity.equals("city")){
                String response = jsonObject.getString("data");
                Log.d("Response ##-->",response);

                cityList.clear();
                JSONArray jsonArray=new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject cityDetail= (JSONObject) jsonArray.get(i);
                    String stateId=cityDetail.getString("stateId");
                    String cityId=cityDetail.getString("cityId");
                    String cityName=cityDetail.getString("cityName");

                    CityMaster cityMaster=new CityMaster(stateId,cityId,cityName);
                    cityList.add(cityMaster);
                }

                ArrayAdapter<CityMaster> stateAdapter = new ArrayAdapter<CityMaster>(this, android.R.layout.simple_spinner_item, cityList);
                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCity.setAdapter(stateAdapter);
                spinnerCity.setSelection(stateAdapter.getPosition(cityList.get(0)));
            }else if(entity.equals("AddressDetail")) {
                String response = jsonObject.getString("message");
                if (response.equals("Address saved successfully")) {
                    Intent i = new Intent(RegisterAddressActivity.this, BankDetailActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Something went wrong, Please try again !",Toast.LENGTH_LONG).show();
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Showing Current Location Marker on Map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + latLng + "," + subLocality + "," + state
                            + "," + country);

                    finalLatitude = String.valueOf(latitude);
                    finalLongitude = String.valueOf(longitude);

                    Log.d("Address-->","State: "+state + " Lat: "+latitude +" Lon: "+ latitude);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        mMap.setOnMarkerDragListener(this);
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
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
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng latLng= marker.getPosition();

        Log.d("Lat-->", String.valueOf(latLng.latitude));
        Log.d("Long-->", String.valueOf(latLng.longitude));

        finalLatitude = String.valueOf(latLng.latitude);
        finalLongitude = String.valueOf(latLng.longitude);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng latLng= marker.getPosition();

        Log.d("Lat-->", String.valueOf(latLng.latitude));
        Log.d("Long-->", String.valueOf(latLng.longitude));

        finalLatitude = String.valueOf(latLng.latitude);
        finalLongitude = String.valueOf(latLng.longitude);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        LatLng latLng= marker.getPosition();

        Log.d("Lat-->", String.valueOf(latLng.latitude));
        Log.d("Long-->", String.valueOf(latLng.longitude));

        finalLatitude = String.valueOf(latLng.latitude);
        finalLongitude = String.valueOf(latLng.longitude);

    }
}
