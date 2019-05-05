package com.example.dell.cyclepath;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.dell.cyclepath.Wallet_Fragment.final_date;

public class Payment_Wallet extends AppCompatActivity implements Wallet_Fragment.MyInterface {

    FrameLayout frame;
    HttpHandler httpHandler;
    String myresult, web_wallet_balance;
    String cardno, uname, udate,cvvno;
    int bal, wal_bal;
    DBHandler dbHandler;
    SQLiteDatabase db;
    String phoneno;
    int done_transaction=0;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        phoneno = sharedpreferences.getString("phone","0");
        frame = findViewById(R.id.dframe);
        dbHandler = new DBHandler(this);
        dbHandler.opendb();
        db=dbHandler.getWritableDatabase();


        Bundle bundle = getIntent().getExtras();
        int which = bundle.getInt("method");
        switch (which) {
            case 0: {

                loadfragment(new Wallet_Fragment(which));

            }
            break;
            case 1:
                loadfragment(new Wallet_Fragment(which));

                break;
            default: {
            }
        }


    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void loadfragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fr = fm.beginTransaction();
        fr.replace(R.id.dframe, fragment);
        fr.commit();

    }

    @Override
    public void getData(String url) {
        new classA(url).execute();
    }

    private class classA extends AsyncTask<Void, Void, Void> {
        String url;

        public classA(String url) {
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... voids) {



            httpHandler = new HttpHandler();
            myresult = httpHandler.makeServiceCall(url);
            web_wallet_balance = httpHandler.makeServiceCall("https://psyclepath.000webhostapp.com/userdatas.php?phonenum="+phoneno);

            try {
                JSONObject object = new JSONObject(myresult);
                JSONObject ob1 = object.getJSONObject("res");
                if(ob1==null){
                    Toast.makeText(Payment_Wallet.this, "Something went Wrong!!!", Toast.LENGTH_SHORT).show();
                }
                else{
                try {
                    JSONObject objectwallet = new JSONObject(web_wallet_balance);
                    JSONObject obwallet = objectwallet.getJSONObject("re");
                    if(ob1==null){
                        Toast.makeText(Payment_Wallet.this, "Something went Wrong!!!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        wal_bal = Integer.parseInt(obwallet.getString("bal"));
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                uname = ob1.getString("name");
                cardno = ob1.getString("cardno");
                udate = ob1.getString("expdate");
                bal = Integer.parseInt(ob1.getString("bal"));
                cvvno=ob1.getString("cvv");

                Log.i("array", uname + "," + cardno + "," + udate + "," + bal);
                Log.i("done", final_date);
                    if (uname == null) {
                        done_transaction = 0;
                    } else if(cardno == null){
                        done_transaction = 0;
                    }
                    else if(udate == null){
                        done_transaction = 0;
                    }
                    else{

                if (Wallet_Fragment.card_name.getText().toString().equals(uname)) {
                    if (Wallet_Fragment.card_number.getText().toString().equals(cardno)) {
                        if (Wallet_Fragment.cvv.getText().toString().equals(cvvno)) {
                        if (final_date.equals(udate)) {
                            try {
                                db.beginTransaction();
                                int addamount = Integer.parseInt(Wallet_Fragment.amount.getText().toString());
                                wal_bal = wal_bal + addamount;
                                bal = bal - addamount;
                                Log.i("balance", wal_bal + "," + bal);
                                String obupdate = null;
                                String finalresponse = httpHandler.makeServiceCall("https://psyclepath.000webhostapp.com/transaction.php?cardno=" + cardno + "&amount=" + bal + "&uid=" + phoneno + "&amountwall=" + wal_bal + "");

                                try {
                                    Log.i("tr", "try");

                                    JSONObject objectupdate = new JSONObject(finalresponse);
                                    Log.i("objupdate", objectupdate + "ab");
                                    obupdate = objectupdate.getString("res");
                                    if (obupdate == null) {
                                        Toast.makeText(Payment_Wallet.this, "Something went Wrong!!!", Toast.LENGTH_SHORT).show();
                                    } else {
                                    }
                                } catch (Exception e) {
                                    Log.i("error", e.getMessage());
                                }

                                if (obupdate.equals("1")) {

                                    dbHandler.opendb();
                                    int get = dbHandler.updatewal(String.valueOf(wal_bal), phoneno);

                                    if (get == 1) {
                                        db.setTransactionSuccessful();
                                        done_transaction = 1;

                                        Log.i("newbalance", get + "");
                                    } else {
                                        Toast.makeText(Payment_Wallet.this, "Try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                            } finally {
                                db.endTransaction();

                            }


                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Payment_Wallet.this, "Enter Valid Date!!!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    }
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Payment_Wallet.this, "Enter Valid Card Number!!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Payment_Wallet.this, "Enter Valid Name!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }}} catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Wallet_Fragment.progressBar.setVisibility(View.VISIBLE);



        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("result", myresult + "," + wal_bal + "," + web_wallet_balance);
            Log.i("array", uname + "," + cardno + "," + udate + "," + bal);
            //Toast.makeText(Payment_Wallet.this, "there", Toast.LENGTH_SHORT).show();
           if(done_transaction==1) {
               Wallet_Fragment.progressBar.setVisibility(View.INVISIBLE);
               Intent intent = new Intent(Payment_Wallet.this, Wallet.class);
               intent.putExtra("new_balance", wal_bal);
               startActivity(intent);
           }
           else{
               Wallet_Fragment.progressBar.setVisibility(View.INVISIBLE);
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(getApplicationContext(), "Transaction Failed First Check Your All Details!!!", Toast.LENGTH_SHORT).show();
                   }
               });

           }
        }
    }
}
