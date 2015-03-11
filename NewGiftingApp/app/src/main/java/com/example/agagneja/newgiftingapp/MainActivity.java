package com.example.agagneja.newgiftingapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;

import java.io.IOException;
public class MainActivity extends Activity {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = "491879869618";
    static final String TAG = "L2C";
    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;
    String regid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("Gift", 0);
        context = getApplicationContext();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            System.out.println("*** My thread is now configured to allow connection");
        }
        /*if(!prefs.getString("REG_EMAIL","").isEmpty()){
            Log.e("email",prefs.getString("REG_EMAIL",""));
           /* if(prefs.getString("REG_EMAIL","").equals("cdayanand-cc@paypal.com")) {
                Account account = new Account();
                account.setAccount("1453948006982829156");
            }
            else
            {
                Account account = new Account();
                account.setAccount("1885722654746603501");
            }
           new getGifts().execute();
         //   startLogin();
        }else if(!prefs.getString("REG_ID", "").isEmpty()){
          startLogin();
        }else */
        if(!prefs.getString("REG_EMAIL","").isEmpty())
        {
            new getGifts().execute();
        }
         else   if(checkPlayServices()){
            new Register().execute();
        }else{
            Toast.makeText(getApplicationContext(),"This device is not supported",Toast.LENGTH_SHORT).show();
        }
    }
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    private class Register extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                    regid = gcm.register(SENDER_ID);
                    Log.e("RegId",regid);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("REG_ID", regid);
                    edit.commit();
                }
                return  regid;
            } catch (IOException ex) {
                Log.e("Error", ex.getMessage());
                return "Fails";
            }
        }
        @Override
        protected void onPostExecute(String json) {
            startLogin();
        }
    }

    public void startLogin()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private class getGifts extends AsyncTask<Void, Void, String>
    {
        public String doInBackground(Void... params)
        {

            JSONArray response = ConnectionUtils.getGifts(prefs.getString("ACCOUNT",""));
            if(response!=null)
            {
                return response.toString();
            }
            else {
                return null;
            }
        }

        public void onPostExecute(String result)
        {
            if(result!=null) {
                launchActivity(result);
            }
            else
            {
                Log.d("Response:","failed");
                Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();


            }

        }
    }

    public void launchActivity(String result)
    {
        // Intent myIntent = new Intent(this,ChooseContact.class);
        Intent myIntent = new Intent(this,GetGifts.class);
        myIntent.putExtra("response",result);
        startActivity(myIntent);
    }

}