package com.example.agagneja.newgiftingapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity {

    ImageView imgView;
    AutoCompleteTextView email;
    EditText password;
    Button login;
    TextView logo;
    SharedPreferences prefs;
    List<NameValuePair> params;
    EditText account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // imgView = (ImageView)findViewById(R.id.imageView);
        //imgView.setImageResource(R.drawable.original);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);
        account = (EditText) findViewById(R.id.account);
       // logo = (TextView) findViewById(R.id.textView2);
       // Typeface face = Typeface.createFromAsset(LoginActivity.this.getResources().getAssets(),"font/verdana.ttf");
        //logo.setTypeface(face);
        prefs = getSharedPreferences("Gift", 0);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("ACCOUNT",account.getText().toString());
                edit.putString("REG_EMAIL",email.getText().toString());
                edit.putString("REG_PASSWORD",password.getText().toString());
                edit.commit();
               /* String emailId = email.getText().toString();
                if(emailId.equals("cdayanand-cc@paypal.com")) {
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("ACCOUNT", "1453948006982829156");
                    edit.putString("REG_EMAIL",emailId);
                    edit.putString("REG_PASSWORD",password.getText().toString());
                   // Account account = new Account();
                   // account.setAccount("1453948006982829156");
                    edit.commit();
                }
                else
                {
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("ACCOUNT", "1885722654746603501");
                    edit.putString("REG_EMAIL",emailId);
                    edit.putString("REG_PASSWORD",password.getText().toString());
                   // Account account = new Account();
                   // account.setAccount("1885722654746603501");

                    edit.commit();
                }*/

              //  new getGifts().execute();

                new Login().execute();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private class Login extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email.getText().toString()));
            params.add(new BasicNameValuePair("password", password.getText().toString()));
            params.add((new BasicNameValuePair("reg_id",prefs.getString("REG_ID",""))));
            params.add((new BasicNameValuePair("account",prefs.getString("ACCOUNT",""))));
            Log.e("HEY", params.get(0).getValue());
            Log.e("HEY", params.get(1).getValue());
            Log.e("HEY", params.get(2).getValue());
            JSONObject jObj = json.getJSONFromUrl("http://192.168.56.1:8080/login",params);
            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                String res = json.getString("response");
                if(res.equals("Sucessfully Registered")) {
                    new getGifts().execute();

                }else{
                    Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
                    new getGifts().execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
