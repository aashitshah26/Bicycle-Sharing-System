package com.example.dell.cyclepath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Subscription extends AppCompatActivity {

    Button btn;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.subscription_main);
        btn=findViewById(R.id.dbtnsubsmoney);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Subscription.this, Payment_Subscription.class);
                intent.putExtra("method", 0);
                startActivity(intent);

            }
        });

    }
}
