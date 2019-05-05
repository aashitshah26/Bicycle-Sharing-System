package com.example.dell.cyclepath;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import static com.example.dell.cyclepath.Subscription_Fragment.final_date;


public class Payment_Subscription extends AppCompatActivity implements Subscription_Fragment.MyInterface {


    FrameLayout frame;
    HttpHandler httpHandler;
    String myresult, web_wallet_balance;
    String cardno, uname, udate,cvvno;
    int bal, wal_bal;
    DBHandler dbHandler;
    SQLiteDatabase db;
    String phoneno;
    int done_transaction=0;
    GetterSetter getterSetter;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        phoneno = sharedpreferences.getString("phone","0");

        frame = findViewById(R.id.dframe);
        dbHandler = new DBHandler(this);
        db = dbHandler.getWritableDatabase();

        Bundle bundle = getIntent().getExtras();
        int which = bundle.getInt("method");
        switch (which) {
            case 0: {

                loadfragment(new Subscription_Fragment(which));

            }
            break;
            case 1:
                loadfragment(new Subscription_Fragment(which));

                break;
            default: {
            }
        }


    }

    @Override
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
                try {
                    JSONObject objectwallet = new JSONObject(web_wallet_balance);
                    JSONObject obwallet = objectwallet.getJSONObject("re");
                    wal_bal = Integer.parseInt(obwallet.getString("bal"));
                    Log.i("walbal", wal_bal + "");
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
                } if(cardno == null){
                    done_transaction = 0;
                }
                 if(udate == null){
                    done_transaction = 0;
                }
                 if(cvvno == null){
                    done_transaction=0;
                }
                else{
                if (Subscription_Fragment.card_name.getText().toString().equals(uname)) {
                    Log.i("done", "name");
                    if (Subscription_Fragment.card_number.getText().toString().equals(cardno)) {
                        if (Subscription_Fragment.cvv.getText().toString().equals(cvvno)) {
                        Log.i("done", "crdno");
                        if (final_date.equals(udate)) {
                            Log.i("done", "date");
                            try {
                                db.beginTransaction();
                                int addamount = Integer.parseInt(Subscription_Fragment.amount.getText().toString());
                                Log.i("done", addamount + "");
                                wal_bal = addamount - 400;
                                bal = bal - addamount;
                                Log.i("balance", addamount + "," + wal_bal + "," + bal);
                                String obupdate = null;
                                String finalresponse = httpHandler.makeServiceCall("https://psyclepath.000webhostapp.com/transaction.php?cardno=" + cardno + "&amount=" + bal + "&uid=" + phoneno + "&amountwall=" + wal_bal + "");

                                try {
                                    Log.i("tr", "try");

                                    JSONObject objectupdate = new JSONObject(finalresponse);
                                    obupdate = objectupdate.getString("res");
                                    Log.i("updateres", obupdate + "");
                                } catch (Exception e) {
                                    Log.i("error", e.getMessage());
                                }
                                String subres = httpHandler.makeServiceCall("https://psyclepath.000webhostapp.com/updatesubscribe.php?uid=" + phoneno);
                                String sub_status = null;
                                try {
                                    JSONObject updatesub = new JSONObject(subres);
                                    sub_status = updatesub.getString("res");
                                    Log.i("substatus", sub_status + "");

                                } catch (Exception e) {
                                }
                                if (obupdate.equals("1") && sub_status.equals("1")) {

                                    String dbname = uname;
                                    String dbmob = phoneno;
                                    String dbmoney = String.valueOf(wal_bal);


                                    getterSetter = new GetterSetter(dbname, dbmoney, dbmob);
                                    dbHandler.opendb();
                                    dbHandler.insertdta(getterSetter);
                                    Cursor c = dbHandler.Cursor();

                                    if (c != null) {
                                        db.setTransactionSuccessful();
                                        done_transaction = 1;
                                        Log.i("newbalance", c + "," + getterSetter);
                                    } else {
                                        Toast.makeText(Payment_Subscription.this, "Try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                            } finally {
                                db.endTransaction();
                            }


                        }
                        else{
                            done_transaction=0;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Check your card date!!!", Toast.LENGTH_SHORT).show();
                                }

                            });

                        }
                    } else {
                            done_transaction=0;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Enter valid CVV no.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                        }
                    else {
                        done_transaction=0;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Enter valid Card Number", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
                else{
                    done_transaction=0;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Enter valid name", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }}catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Subscription_Fragment.progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("result", myresult + "," + wal_bal + "," + web_wallet_balance);
            Log.i("array", uname + "," + cardno + "," + udate + "," + bal);
            Subscription_Fragment.progressBar.setVisibility(View.INVISIBLE);
            if(done_transaction==1) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("subscription", "1");
                editor.putString("balance","100");
                editor.commit();
                Intent intent = new Intent(Payment_Subscription.this, Login.class);
                startActivity(intent);
            }
            else{
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
