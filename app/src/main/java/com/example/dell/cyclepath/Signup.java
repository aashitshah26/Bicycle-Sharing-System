package com.example.dell.cyclepath;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Signup extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    String num="";
    String success="";
  private ProgressBar bar;
    private ProgressDialog pDialog;
    EditText number;
    private static String url = "https://psyclepath.000webhostapp.com/check.php?";
    Spinner spin;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        spin =(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Signup.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(myAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.rgb(40,153,223));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void register(View view) {
         number = (EditText) findViewById(R.id.inpmob);
           num = number.getText().toString();
          url = url+"phno="+num;
        Log.e(TAG, "The url is: " + url);
        new getDuplication().execute();
    }

private class getDuplication extends AsyncTask<Void,Void,Void>{

        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Signup.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            url="https://psyclepath.000webhostapp.com/check.php?";
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                     success = jsonObj.getString("res");

                    if(success.equals("exists"))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Number already registered!",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                number.setText("");
                            }
                        });

                    }
                    else if(success.equals("New"))
                    {
                        String numbers = "0123456789";
                        Random rndm_method = new Random();


                        char[] otp = new char[5];

                        for (int i = 0; i < 5; i++)
                        {
                            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
                        }
                        final String O = new String(otp);
                        Intent i = new Intent(Signup.this,OTP.class);
                        i.putExtra("otp",O);
                        i.putExtra("number",num);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new msg(num,"The otp for registering with Psyclepath is:"+O,null,"null",Signup.this).execute();
                            }
                        });



                        startActivity(i);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Signup.this,O, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }



                }
                catch (final Exception e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }

            else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }


    }



}


class msg extends AsyncTask<String,Void,String> {
    String ph, msg,toast;
    ProgressBar bar;
    Context context;

    msg(String ph, String msg, ProgressBar progressBar,String toast, Context context) {
        this.ph = ph;
        this.msg = msg;
        bar=progressBar;
        this.context=context;
        this.toast=toast;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (bar!=null)
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params) {
        String msg1=null;
        try{
        String apiKey = "apikey=" + "MSygOz/RP9Q-X0exKkWa8lJzsaMFImqflyzR3GrG8U";
        String message = "&message=" +msg;
        String sender = "&sender=" + "TXTLCL";
        String numbers = "&numbers=" +ph;

        HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
        String data = apiKey + numbers + message + sender;
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
        conn.getOutputStream().write(data.getBytes("UTF-8"));
        final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuffer stringBuffer = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        rd.close();
        msg1=stringBuffer.toString();
        Log.e("response",msg1);
    } catch (Exception e) {
        Log.e("errorinmsg","Error SMS "+e);
    }
    return msg1;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (bar!=null)
        bar.setVisibility(View.INVISIBLE);
        if(toast!=null)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        //All your UI operation can be performed here
    }
}