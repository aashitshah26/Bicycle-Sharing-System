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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyRides extends AppCompatActivity {
    private static String url = "https://psyclepath.000webhostapp.com/rides.php?user=";
    private ArrayList<Rides> rides = new ArrayList<>();
    ArrayList<String> a = new ArrayList<>();
    ListView listView;
    Myrides_adapter myAdapter;
    ProgressBar progressBar;
    String user;
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rides);
        listView = findViewById(R.id.list1);
        progressBar=findViewById(R.id.progress);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu_white);
        navigationView.getMenu().getItem(3).setChecked(true);
        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        if(sharedpreferences.contains("phone")) {
            user=sharedpreferences.getString("phone","");
            Toast.makeText(this, String.valueOf(user), Toast.LENGTH_SHORT).show();
        }

        View v = navigationView.getHeaderView(0);
        TextView name = v.findViewById(R.id.name);
        name.setText("Welcome "+sharedpreferences.getString("name","Client")+"!");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if(item.toString().equals("Home")) {
                    Intent i = new Intent(MyRides.this,Login.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Wallet")) {
                    Intent i = new Intent(MyRides.this,Wallet.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Edit Profile")){
                    Intent i = new Intent(MyRides.this, Profile.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Report Cycle")){
                    Intent i = new Intent(MyRides.this,Report.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Logout")){
                    getApplicationContext().getSharedPreferences("mypref", 0).edit().clear().commit();
                    Intent i = new Intent(MyRides.this,MainActivity.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                return true;
            }
        });
        new getRides().execute();
    }

    @Override
    public void onBackPressed() {

            if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START,true);
            }
            else {
                super.onBackPressed();
            }
    }


    @Override
    protected void onStart() {
        super.onStart();
        navigationView.getMenu().getItem(3).setChecked(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class getRides extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            url = url+user;
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            url="https://psyclepath.000webhostapp.com/rides.php?user=";
            if(jsonStr!=null)
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray array = jsonObj.getJSONArray("res");
                    for(int i=0;i<array.length();i++)
                    {
                        Log.e("Array", String.valueOf(array.get(i)));
                        JSONObject object = array.getJSONObject(i);
                        String startloc=object.getString("startloc");
                        String endloc=object.getString("endloc");
                        String day=object.getString("day");
                        String start=object.getString("start");
                        String end=object.getString("end");
                        String date=object.getString("date");
                        String fare=object.getString("fare");
                        String cal=object.getString("cal");
                        String pol=object.getString("pol");
                        Rides abc = new Rides(startloc,endloc,day,start,end,date,fare,cal,pol);
                        rides.add(abc);
                    }
                }
                catch (Exception e)
                { }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setClickable(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MyRides.this, String.valueOf(rides.size()), Toast.LENGTH_SHORT).show();
            myAdapter = new Myrides_adapter(MyRides.this,rides);
            listView.setAdapter(myAdapter);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
