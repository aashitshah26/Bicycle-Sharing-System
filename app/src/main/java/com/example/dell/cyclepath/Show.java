package com.example.dell.cyclepath;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Show extends AppCompatActivity {
TextView fare,ride_time,wallet_bal,pollution,calories;
String fare1,ride_time1,wallet_bal1,pollution1,calories1;
Button ok;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context=this;
        setContentView(R.layout.activity_show);
        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        Bundle ext=getIntent().getExtras();
        fare=findViewById(R.id.fare);
        String fare2=fare.getText().toString();
        ride_time=findViewById(R.id.ride_time);
        wallet_bal=findViewById(R.id.wallet_bal);
        String wallet_bal2=wallet_bal.getText().toString();
        pollution=findViewById(R.id.pollution);
        calories=findViewById(R.id.calories);
        fare1=ext.getString("fare");
        ride_time1=ext.getString("duration");
        wallet_bal1=ext.getString("updated_bal");
        pollution1=ext.getString("pollution");
        calories1=ext.getString("calories");
        fare.setText(fare2+fare1);
        ride_time.setText(ride_time1);
        wallet_bal.setText(wallet_bal2+wallet_bal1);
        pollution.setText(pollution1+" kgs of CO2");
        calories.setText(calories1+" kcals");
        ok=findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,Login.class);
                startActivity(i);
            }
        });

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("balance", wallet_bal1);
    }

    public void givefeedback(View view) {
        Intent i = new Intent(this,Feedback.class);
        startActivity(i);
    }
}
