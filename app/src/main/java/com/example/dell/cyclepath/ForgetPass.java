package com.example.dell.cyclepath;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForgetPass extends AppCompatActivity {
    EditText edt;
    static String url = "https://psyclepath.000webhostapp.com/change.php?number=";
    String msg;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        edt=findViewById(R.id.mobile_forget);
        progressBar=findViewById(R.id.progress);
        progressBar.bringToFront();
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void change(View view) {
        String phone = edt.getText().toString();
        if (phone!=null && phone!="" && !phone.isEmpty() ) {
              if(phone.length()!=10){
                  Toast.makeText(this, "Phone number should be of length 10", Toast.LENGTH_SHORT).show();
              }
              else {
                  url = url + phone;
                  msg = "Visit this link to change your Password:"+url;
                  new msg(phone,msg,progressBar,"Link for password change has been sent to your Number!",this).execute();
              }
        }
        else{
            Toast.makeText(this, "Phone number should not be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
