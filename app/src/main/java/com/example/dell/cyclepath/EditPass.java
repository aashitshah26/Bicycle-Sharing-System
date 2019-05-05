package com.example.dell.cyclepath;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditPass extends AppCompatActivity {
    EditText oldpass,newpass,repass;
    Button done;
    String oldpass1,newpass1,repass1;
    String phoneno1="9106468353";
    String url="https://psyclepath.000webhostapp.com/fetch.php";
    String url1="https://psyclepath.000webhostapp.com/updatepass.php";
    SharedPreferences sharedpreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);
        oldpass=(EditText)findViewById(R.id.oldpass);






        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        phoneno1 = sharedpreferences.getString("phone","");
        newpass=(EditText)findViewById(R.id.newpass);
        repass=(EditText)findViewById(R.id.repass);
        done=(Button)findViewById(R.id.edit_pass_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpass1 = oldpass.getText().toString();
                newpass1 = newpass.getText().toString();
                repass1 = repass.getText().toString();
                if (oldpass1.isEmpty() || newpass1.isEmpty() || repass1.isEmpty()) {
                    Toast.makeText(EditPass.this, "All fields are necessary", Toast.LENGTH_SHORT).show();
                } else if (!repass1.equals(newpass1))
                {
                    Toast.makeText(EditPass.this,"Passwords must match",Toast.LENGTH_SHORT).show();
                } else {
                    url=url+"?phoneno="+phoneno1+"&oldpass="+oldpass1;
                    getJSON(url,EditPass.this);
                }
            }
        });
    }
    public void getJSON(final String urlService,final Context context)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {
            Context context;
            private ProgressDialog mDialog = null;
            GetJSON(Context context) {
                this.setDialog(context);
                this.context=context;
            }

            private void setDialog(Context context) {
                this.mDialog = new ProgressDialog(context);
                this.mDialog.setMessage("Loading..");
                this.mDialog.setCancelable(false);
            }
            @Override
            protected void onPreExecute() {
                this.mDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONArray users = new JSONArray(s);
                    JSONObject jsonObj = users.getJSONObject(0);
                    String error = jsonObj.getString("error");
                    if (error.equals("false")) {
                        url1=url1+"?newpass="+newpass1+"&phoneno="+phoneno1;
                        getJSON1(url1, context);
                    }
                    else
                    {
                        Toast.makeText(context,"Enter correct old password",Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e)
                {

                }
                if (this.mDialog.isShowing()) {
                    this.mDialog.dismiss();
                }
            }
            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                try {
                    String response = handler.makeServiceCall(urlService);
                    Log.e("","Response from url in string is "+response);
                    return response;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        class showLoading<Params, Progress, Result> extends
                AsyncTask<Params, Progress, Result> {
            Context context;
            private final String DIALOG_MESSAGE = "Loading..";
            private ProgressDialog mDialog = null;

            private void setDialog(Context context) {
                this.mDialog = new ProgressDialog(context);
                this.mDialog.setMessage(DIALOG_MESSAGE);
                this.mDialog.setCancelable(false);
            }

            public showLoading(Context context) {
                this.setDialog(context);
                this.context=context;
            }

            @Override
            protected void onPreExecute() {
                this.mDialog.show();
            }

            @Override
            protected Result doInBackground(Params... arg0) {
                // Place your background executed method here
                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            GetJSON gtJSON = new GetJSON(context);
                            gtJSON.execute();
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Result result) {

                // Update the UI if u need to
                // And then dismiss the dialog
                if (this.mDialog.isShowing()) {
                    this.mDialog.dismiss();
                }
            }
        }
        showLoading<Void,Void,Void> updateTask = new showLoading<>(context);
        updateTask.execute();
    }

    public void getJSON1(final String urlService,final Context context)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {
            Context context;
            private ProgressDialog mDialog = null;
            GetJSON(Context context) {
                this.setDialog(context);
                this.context=context;
            }

            private void setDialog(Context context) {
                this.mDialog = new ProgressDialog(context);
                this.mDialog.setMessage("Loading..");
                this.mDialog.setCancelable(false);
            }
            @Override
            protected void onPreExecute() {
                this.mDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (this.mDialog.isShowing()) {
                    this.mDialog.dismiss();
                }
                Toast.makeText(context,"Password updated successfully",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(EditPass.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            @Override
            protected String doInBackground(Void... voids) {
                HttpHandler handler = new HttpHandler();
                try {
                    String response = handler.makeServiceCall(urlService);
                    Log.e("","Response from url in string is "+response);
                    return response;
                } catch (Exception e) {
                    return null;
                }
            }
        }
        class showLoading<Params, Progress, Result> extends
                AsyncTask<Params, Progress, Result> {
            Context context;
            private final String DIALOG_MESSAGE = "Loading..";
            private ProgressDialog mDialog = null;

            private void setDialog(Context context) {
                this.mDialog = new ProgressDialog(context);
                this.mDialog.setMessage(DIALOG_MESSAGE);
                this.mDialog.setCancelable(false);
            }

            public showLoading(Context context) {
                this.setDialog(context);
                this.context=context;
            }

            @Override
            protected void onPreExecute() {
                this.mDialog.show();
            }

            @Override
            protected Result doInBackground(Params... arg0) {
                // Place your background executed method here
                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            GetJSON gtJSON = new GetJSON(context);
                            gtJSON.execute();
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Result result) {

                // Update the UI if u need to
                // And then dismiss the dialog
                if (this.mDialog.isShowing()) {
                    this.mDialog.dismiss();
                }
            }
        }
        showLoading<Void,Void,Void> updateTask = new showLoading<>(context);
        updateTask.execute();
    }

}
