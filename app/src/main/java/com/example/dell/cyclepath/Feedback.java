package com.example.dell.cyclepath;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

public class Feedback extends AppCompatActivity {

    EditText feedback;
    Button submit_feedback;
    private static String url="https://psyclepath.000webhostapp.com/givefeedback.php?phone=";
    SharedPreferences sharedpreferences;
    String phno;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
         sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
         progressBar=findViewById(R.id.progress_feedback);
        phno = sharedpreferences.getString("phone","");
        feedback = findViewById(R.id.feedback);
        submit_feedback = findViewById(R.id.submit_feedback);
    }

    public void feedback(View view) {
        String feedback1 = feedback.getText().toString();
        url=url+"phno"+"&="+feedback1;
        new SendData().execute();
    }

    private class SendData extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler handler = new HttpHandler();
            String string = handler.makeServiceCall(url);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            url="https://psyclepath.000webhostapp.com/givefeedback.php?phone=";
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}

