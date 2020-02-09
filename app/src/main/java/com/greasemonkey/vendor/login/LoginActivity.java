package com.greasemonkey.vendor.login;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.greasemonkey.vendor.DashobardActivity;
import com.greasemonkey.vendor.HomeActivity;
import com.greasemonkey.vendor.R;
import com.greasemonkey.vendor.SplashActivity;
import com.greasemonkey.vendor.common.Constant;
import com.greasemonkey.vendor.comunication.CommunicationChanel;
import com.greasemonkey.vendor.comunication.IResponse;
import com.greasemonkey.vendor.mobileVerification.MobileVerificationActivity;
import com.greasemonkey.vendor.utility.JWTUtils;
import com.greasemonkey.vendor.utility.UserPrefManager;
import com.greasemonkey.vendor.vendor_detail.RegisterAddressActivity;
import com.greasemonkey.vendor.vendor_detail.ServiceDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class LoginActivity extends AppCompatActivity implements IResponse,GoogleApiClient.OnConnectionFailedListener {

    private EditText etMobileNumber,etPassword;
    private String strMobileNumber, strPassword;
    private TextView registartionLink, tvForgotPassword;
    private Button btnLogin;
    private GoogleApiClient googleApiClient;
   // private SignInButton signInButton;
    private static final int RC_SIGN_IN = 1;

    private static final String EMAIL = "email";

    LoginButton loginButton;

    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private LocationManager locationManager;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private UserPrefManager userPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        facbookLogin();
        //googleLogin();
        onClick();
        checkLocationPermission();


        /*forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.forgot_password_dialog, null);
                //final EditText etUsername = alertLayout.findViewById(R.id.et_username);
                final EditText etEmail = alertLayout.findViewById(R.id.et_email);
                //final CheckBox cbToggle = alertLayout.findViewById(R.id.cb_show_pass);

                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle("Info");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // String user = etUsername.getText().toString();
                        String pass = etEmail.getText().toString();
                        Toast.makeText(getBaseContext(),  " Email: " + pass, Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });*/
    }

    private void init(){
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        registartionLink = (TextView) findViewById(R.id.registrationLink);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        btnLogin = (Button) findViewById(R.id.loginBtn);
        //loginButton = (LoginButton) findViewById(R.id.login_button);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        accessToken = AccessToken.getCurrentAccessToken();
        callbackManager = CallbackManager.Factory.create();

        userPrefManager= new UserPrefManager(getApplicationContext());

        if(!isLocationEnabled(getApplicationContext())){
            showLocationDialog();
        }

        String mystring=new String("Don't have account? Register here");
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        registartionLink.setText(content);
    }

    private void onClick(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    JSONObject jsonObject=new JSONObject();

                    strMobileNumber = etMobileNumber.getText().toString();
                    strPassword = etPassword.getText().toString();

                    if(strMobileNumber.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Register Mobile Number !", Toast.LENGTH_LONG).show();
                    }else if(strPassword.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Enter Your Password !", Toast.LENGTH_LONG).show();
                    }else {
                        jsonObject.put("mobile",strMobileNumber);
                        jsonObject.put("password",strPassword);
                        jsonObject.put("firebaseId",userPrefManager.getFCMId());

                    Log.d("Json-->",jsonObject.toString());
                    CommunicationChanel communicationChanel =new CommunicationChanel();
                    communicationChanel.communicateWithServer(LoginActivity.this,
                    Constant.POST, Constant.loginAPI,jsonObject,"Login");

                   /* Intent i = new Intent(LoginActivity.this, DashobardActivity.class);
                    startActivity(i);
                    finish();*/
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        registartionLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isLocationEnabled(getApplicationContext())){
                    Intent i = new Intent(LoginActivity.this, MobileVerificationActivity.class);
                    startActivity(i);
                }else {
                    showLocationDialog();
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(LoginActivity.this); // Context, this, etc.
                dialog.setContentView(R.layout.forgot_password_dialog);
                dialog.setTitle("Forgot Password");
                dialog.show();

                final EditText etMobileNumbere = dialog.findViewById(R.id.etMobileNumber);
                Button btnSendPassword =  dialog.findViewById(R.id.btnSendPassword);
                Button btnCancel =  dialog.findViewById(R.id.btnCancel);

                btnSendPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strMobileNumber = etMobileNumbere.getText().toString();

                        if(strMobileNumber.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Please enter 10 digit mobile number !!",Toast.LENGTH_LONG).show();
                        } else if(strMobileNumber.length() < 10){
                            Toast.makeText(getApplicationContext(),"Please enter 10 digit mobile number !!",Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject jsonObject=new JSONObject();
                                dialog.dismiss();
                                jsonObject.put("userType","vendor");
                                jsonObject.put("mobile",strMobileNumber);

                                Log.d("Json-->",jsonObject.toString());
                                CommunicationChanel communicationChanel =new CommunicationChanel();
                                communicationChanel.communicateWithServer(LoginActivity.this,
                                        Constant.POST, Constant.forgotPassword,jsonObject,"forgotPassword");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    /*private void googleLogin(){
        try{
            GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            googleApiClient=new GoogleApiClient.Builder(this)
                    .enableAutoManage(this,this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                    .build();

            signInButton=(SignInButton)findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(intent,RC_SIGN_IN);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }*/

    @Override
    public void onRequestComplete(JSONObject jsonObject, String entity) {
        try {

            Log.d("Login Response-->", jsonObject.toString());

            if(entity.equals("Login")){
                if(jsonObject.has("personalDetail")){
                    String response = jsonObject.getString("personalDetail");
                    if(response.equals("true")){

                        if(jsonObject.has("token")) {
                            String token = jsonObject.getString("token");
                            String tokenData = JWTUtils.decoded(token);
                            Log.d("Token-->", token);
                            JSONObject jsonObject1 = new JSONObject(tokenData);

                            String vendorId = jsonObject1.getString("vendorId");
                            String verificationStatus = jsonObject1.getString("verificationStatus");
                            String onlineStatus = jsonObject1.getString("onlineStatus");
                            String pickUpDropStatus = jsonObject1.getString("pickupDropStatus");
                            String tommarows_status = jsonObject1.getString("tommarows_status");

                            userPrefManager.setVendorId(vendorId);
                            userPrefManager.setVendorVerificationStatus(verificationStatus);
                            userPrefManager.setOnlineStatus(onlineStatus);
                            userPrefManager.setPickupDropStatus(pickUpDropStatus);
                            userPrefManager.setPickupDropStatus(pickUpDropStatus);
                            userPrefManager.setTommarowStatus(tommarows_status);
                            userPrefManager.setLoginStatus("LoginSuccess");

                            if(userPrefManager.getVendorVerificationStatus().equals("active")){
                                Intent i = new Intent(LoginActivity.this, DashobardActivity.class);
                                startActivity(i);
                                finish();
                            }else {
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    }
                } else if(jsonObject.has("personalDetail")){
                    String response = jsonObject.getString("message");
                    Log.d("Response ##-->",response);

                    if(response.equals("Vendor Not Found.")){
                        Toast.makeText(getApplicationContext(),"Please register first!",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Please register your garage first!",Toast.LENGTH_LONG).show();
                }
            }else if(entity.equals("forgotPassword")){

            }


        }catch (JSONException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }*/
    }



   /* private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
            GoogleSignInAccount account=result.getSignInAccount();

            Log.d("Display Name:-->",account.getDisplayName() );
            Log.d("Display Name:-->",account.getEmail());
            Log.d("Display Name:-->",account.getId());

            userPrefManager.setUserName(account.getDisplayName());
            userPrefManager.setEmailId(account.getEmail());

            Intent i = new Intent(LoginActivity.this,MobileVerificationActivity.class);
            startActivity(i);

        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }
    private void gotoProfile(){

    }*/


    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(
                AccessToken oldAccessToken,
                AccessToken currentAccessToken) {

            if (currentAccessToken == null) {
                Log.d("Facebook:-->", "User logged out successfully");
            }
        }
    };



    private void facbookLogin(){

        //loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
//        loginButton.setReadPermissions(Arrays.asList(new String[]{"email", "user_birthday", "user_hometown"}));
        callbackManager = CallbackManager.Factory.create();

        if (accessToken != null) {
            getProfileData();
        } else {
            //rlProfileArea.setVisibility(View.GONE);
        }

        // Callback registration
       /* loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook-->", "User login successfully");
                getProfileData();
            }

            @Override
            public void onCancel() {
                Log.d("Facebook-->", "User login Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("Facbook-->", "User login Error");
            }
        });*/

    }

    public void getProfileData() {
        try {
            accessToken = AccessToken.getCurrentAccessToken();
            //rlProfileArea.setVisibility(View.VISIBLE);
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            Log.d("Facebook", "Graph Object :" + object);
                            try {
                                String name = object.getString("name");
                                

                                Log.d("Facebook--->", "Name :" + name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,birthday,gender,email");
            request.setParameters(parameters);
            request.executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("User Location")
                        .setMessage("Grease monkey wants to access your current location.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LoginActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    private void showLocationDialog(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            new AlertDialog.Builder(this)
                    .setTitle("GPS Not Enable!!")  // GPS not found
                    .setMessage("Request you to enable your location setting for register you garage in our system !!") // Want to enable?
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                           startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}
