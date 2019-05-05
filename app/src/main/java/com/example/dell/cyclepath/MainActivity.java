package com.example.dell.cyclepath;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    ImageView img;
    Intent i;
    String phonenumber,password;
    EditText phone, pass;
    private ProgressDialog pDialog;
    private static String url = "https://psyclepath.000webhostapp.com/login.php?";
    SharedPreferences sharedpreferences;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        if(sharedpreferences.contains("phone")) {
            url=url+"user_name="+sharedpreferences.getString("phone","")+"&password="+sharedpreferences.getString("password","");
            Log.e("Shared Prefrence","Connecting");
            //new GetLoginDetail().execute();
            i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
        }
        

        img = (ImageView) findViewById(R.id.img);
        img.setClipToOutline(true);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, 1);
        }
    }

    public void forgotPassword(View view) {
        i = new Intent(MainActivity.this, ForgetPass.class);
        startActivity(i);
    }

    public void Submit(View view) {
        phone = (EditText) findViewById(R.id.number);
        pass = (EditText) findViewById(R.id.mobile);

        if (phone.getText().length() < 10 || phone.getText().length() > 10)
            Toast.makeText(MainActivity.this, "Enter valid mobile number!", Toast.LENGTH_SHORT).show();
        else if (pass.getText().length() == 0)
            Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show();
        else if (pass.getText().length() < 6 || pass.getText().length() > 12)
            Toast.makeText(this, "Enter password of length between 6 and 12", Toast.LENGTH_SHORT).show();
        else {
            phonenumber=phone.getText().toString();
            password=pass.getText().toString();
           url=url+"user_name="+phonenumber+"&password="+password;
            new GetLoginDetail().execute();

        }
    }


    public void Signup(View view) {

        i = new Intent(MainActivity.this, Signup.class);
        startActivity(i);
    }


    private class GetLoginDetail extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            Log.e("url",url);
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    Log.e("obj", jsonObj.toString());

                    JSONArray array = jsonObj.getJSONArray("res");
                    Log.e("array",array.toString());

                    if (array!=null) {
                       String success = array.getString(0);
                        String balance = array.getString(1);
                        String name = array.getString(2);
                        String block = array.getString(3);
                        if(Integer.parseInt(block)==1){
                            Toast.makeText(MainActivity.this, "You have been blocked from Psyclepath!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("phone", phonenumber);
                            editor.putString("password", password);
                            editor.putString("subscription", success);
                            editor.putString("balance", balance);
                            editor.putString("name", name);
                            Log.e("subs", success);
                            Log.e("balance", balance);
                            Log.e("name", name);
                            editor.commit();
                            url = "https://psyclepath.000webhostapp.com/login.php?";
                            i = new Intent(MainActivity.this, Login.class);
                            startActivity(i);
                        }

                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                final Toast toast = Toast.makeText(MainActivity.this, "Invalid user name/Password",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                } catch (final Exception e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Server is down please wait",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }

             else
               {
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

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            url = "https://psyclepath.000webhostapp.com/login.php?";

        }
    }

    @Override
    public void onBackPressed() {
            finishAffinity();
    }
}
