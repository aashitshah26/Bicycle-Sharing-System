package com.example.dell.cyclepath;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Report extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spin;
    ProgressBar progressBar;
    EditText editText;
    String item,des;
    //https://psyclepath.000webhostapp.com/report.php?issue=shivani&description=aashit&userid=8141945051&cycleid=10001
    static String url = "https://psyclepath.000webhostapp.com/report.php?issue=";
    HttpHandler httpHandler;
    String uid,cid;
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu_white);
        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        navigationView.getMenu().getItem(0).setChecked(true);
        View v = navigationView.getHeaderView(0);
        TextView name = v.findViewById(R.id.name);
        name.setText("Welcome "+sharedpreferences.getString("name","Client")+"!");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                if(item.toString().equals("Home")) {
                    Intent i = new Intent(Report.this,Login.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }

                if(item.toString().equals("Wallet")) {
                    Intent i = new Intent(Report.this,Wallet.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Edit Profile")){
                    Intent i = new Intent(Report.this,Profile.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("My Rides")){
                    Intent i = new Intent(Report.this,MyRides.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Logout")){
                    getApplicationContext().getSharedPreferences("mypref", MODE_PRIVATE).edit().clear().commit();
                    Intent i = new Intent(Report.this,MainActivity.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Report Cycle")){
                }
                return true;
            }
        });

        spin = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.progress);
        editText = findViewById(R.id.edt);
        spin.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Brake Down");
        categories.add("Chain malfunction");
        categories.add("Lock Malfunction");
        categories.add("Tyre puncture");
        categories.add("Less air in tyre");
        categories.add("Transaction error");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        uid =getApplicationContext().getSharedPreferences("mypref",MODE_PRIVATE).getString("phone","");
        cid =getIntent().getStringExtra("cid");
        if(cid==null){
            scan();
        }
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START,true);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.getMenu().getItem(4).setChecked(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void scan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(true);
        integrator.setCaptureActivity(CaptureActivityPortrait.class);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Intent i =new Intent(Report.this,Login.class);
                startActivity(i);
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                cid=result.getContents();

            }
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void submit(View view) {
        des=editText.getText().toString();
        new update().execute();
    }
    private class update extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            httpHandler = new HttpHandler();
            String jsonString = httpHandler.makeServiceCall(url+item+"&description="+des+"&userid="+uid+"&cycleid="+cid);

            if(jsonString!=null){
                try {
                    JSONObject object = new JSONObject(jsonString);
                    final int ob = object.getInt("res");
                    if(ob==0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Report.this, "Problem occured", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Report.this, "Your Complaint No. is"+String.valueOf(ob), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
