package com.example.dell.cyclepath;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Wallet extends AppCompatActivity implements Wallet_Fragment.MyInterface {


    Intent intent;
    Button btnaddmoney;
    public static TextView name, balance;
    DataFetch dataFetch;
    String phno;
    String url;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences sharedpreferences;
    Context context;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        phno = sharedpreferences.getString("phone","0");
        url= "https://psyclepath.000webhostapp.com/userdatas.php?phonenum="+phno;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        progressBar=findViewById(R.id.progress);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
         context = Wallet.this;
        // actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        navigationView.getMenu().getItem(1).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if (item.toString().equals("Home")) {
                    Intent i = new Intent(context,Login.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if (item.toString().equals("Edit Profile")) {
                            Intent i = new Intent(context,Profile.class);
                            startActivity(i);
                            mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if (item.toString().equals("My Rides")) {
                            Intent i = new Intent(context,MyRides.class);
                            startActivity(i);
                            mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if (item.toString().equals("Logout")) {
                    getApplicationContext().getSharedPreferences("mypref", MODE_PRIVATE).edit().clear().commit();
                            Intent i = new Intent(context,MainActivity.class);
                            startActivity(i);
                            mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if (item.toString().equals("Report Cycle")) {
                            Intent i =new Intent(context,Report.class);
                            startActivity(i);
                }


                return true;
            }
        });

        dataFetch = new DataFetch(url, this,progressBar);
        dataFetch.execute();
        btnaddmoney = findViewById(R.id.dbtnaddmoney);
        name = findViewById(R.id.dtxtname);
        balance = findViewById(R.id.dbalance);


        btnaddmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Wallet.this, Payment_Wallet.class);
                intent.putExtra("method", 0);
                startActivity(intent);


            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void getData(String url) {
        Toast.makeText(this, "hii there", Toast.LENGTH_SHORT).show();

    }
}


