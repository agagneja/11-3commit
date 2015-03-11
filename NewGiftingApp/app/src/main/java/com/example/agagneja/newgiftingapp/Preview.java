package com.example.agagneja.newgiftingapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Preview extends Activity {
    ImageView imgView;
    TextView amount;
    Button back;
    Button send;
    EditText msg;
    ListView lst;
    SharedPreferences prefs;
    Bundle bundle;
    String gift_id;
    ChatArrayAdapter chatArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imgView = (ImageView) findViewById(R.id.imgPreviewGift);
        amount = (TextView) findViewById(R.id.giftPreAmount);
        back = (Button) findViewById(R.id.giftPreBack);
        send = (Button)findViewById(R.id.giftPreChatSend);
        msg = (EditText) findViewById(R.id.giftPreChatText);
        lst = (ListView)findViewById(R.id.giftPreChat);
        prefs = getSharedPreferences("Gift",0);
        bundle = getIntent().getBundleExtra("INFO");
        gift_id = bundle.getString("gift_id");
        Log.i("GIFTID IN PREVIEW",gift_id);

    //    LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
       /* if(bundle.getString("gift_id")!= null)
        {
           gift_id = bundle.getString("gift_id");
            Log.i("BUNDLE",gift_id);
            new viewGiftCall().execute();
        }*/
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(),R.layout.single_message);
        lst.setAdapter(chatArrayAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              new getGifts().execute();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = msg.getText().toString();
                chatArrayAdapter.add(new ChatMessage(true,str));
                msg.setText("");
            }
        });
        new viewGiftCall().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preview, menu);
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
    private class viewGiftCall extends AsyncTask<Void, Void, String>
    {
        public String doInBackground(Void... params)
        {

            JSONObject response = ConnectionUtils.viewGift(gift_id,prefs.getString("ACCOUNT",""));
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
                try
                {
                    JSONObject job = new JSONObject(result);
                    JSONObject jobs = job.getJSONObject("sender");
                    JSONObject jobr = job.getJSONObject("receiver");
                    JSONObject joba = job.getJSONObject("amount");
                    JSONArray com = job.getJSONArray("comments");
                    JSONObject como = com.getJSONObject(0);
                    JSONObject media = como.getJSONObject("media");
                    JSONArray images = media.getJSONArray("images");
                    JSONObject imageObj = (JSONObject) images.getJSONObject(0);
                    JSONObject thumb = imageObj.getJSONObject("thumbnail");
                    String imgString = thumb.getString("image");
                    Log.e("Image", imgString);
                    String c = "/9j/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAAUCACgAKAEASIAAhEBAxEBBCIA/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADgQBAAIRAxEEAAA/APn+iiigD5/ooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooooooooAKKKKKKKKACiiiiiiigAooor/2Q==";
                    if(c.equals(imgString))
                    {
                        Log.e("Equal","Equal");

                    }
                    else
                    {
                        Log.e("Not equal","Not equal");
                    }
                    Bitmap bitmap = StringToBitMap(imgString);
                    imgView.setImageBitmap(bitmap);


                    String messa = como.getString("message");
                    String value = joba.getString("value");
                    String cur = joba.getString("currency");
                    String sender = jobs.getString("full_name");
                    String receipient = jobr.getString("full_name");

                    getActionBar().setTitle(sender);
                    Boolean b = jobs.getBoolean("is_me");
                    if(b ==true)
                    {
                        chatArrayAdapter.add(new ChatMessage(false,messa));
                    }
                    else {
                        chatArrayAdapter.add(new ChatMessage(true,messa));
                    }
                    StringBuffer sb = new StringBuffer(value);
                    sb.append(" ");
                    sb.append(cur);
                    amount.setText(sb.toString());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.d("Response:", "failed");
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();


            }

        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
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

    private BroadcastReceiver onNotice= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

          gift_id = intent.getStringExtra("gift_id");
          Log.i("RECIEVER",gift_id);
        //  new viewGiftCall().execute();
        }
    };

}
