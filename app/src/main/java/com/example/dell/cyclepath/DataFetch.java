package com.example.dell.cyclepath;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONObject;

public class DataFetch extends AsyncTask<Void, Void, Void> {
    HttpHandler httpHandler;
    String allresult = "", uname, walbal, mobil;
    String uri;
    DBHandler dbHandler;
    Context context;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    GetterSetter getterSetter;
    ProgressBar progressBar;
    Cursor c;

    DataFetch(String url, Context context, ProgressBar progressBar) {
        this.uri = url;
        this.context = context;
        this.progressBar=progressBar;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {

        /*try {
            URL url=new URL("https://psyclepath.000webhostapp.com/rides.php?user=8141945051");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
            httpHandler = new HttpHandler();
            allresult = httpHandler.makeServiceCall(uri);
            Log.i("result", allresult);
            JSONObject object = new JSONObject(allresult);
            JSONObject ob1 = object.getJSONObject("re");
            uname = ob1.getString("name");
            walbal = ob1.getString("bal");
            mobil = ob1.getString("mob");
            dbHandler = new DBHandler(context);
            getterSetter = new GetterSetter(uname, walbal, mobil);
            dbHandler.opendb();

            c = dbHandler.balanceCursor(mobil);
            Log.i("here",c+"");
            if (c != null && c.getCount()>0) {
                dbHandler.updateuser(walbal, uname, mobil);
                c = dbHandler.balanceCursor(mobil);
                Log.i("hereupdate",c+","+"here");
            } else {
                dbHandler.insertdta(getterSetter);
                c = dbHandler.balanceCursor(mobil);
                Log.i("hereinsert",c+","+"here");
            }

            Log.i("result", allresult);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (c != null) {
            c.moveToFirst();

            Wallet.name.setText(c.getString(c.getColumnIndex("user_name")));
            Wallet.balance.setText(c.getString(c.getColumnIndex("wal_money")));
            Log.i("datas", c + "");
        } else {

        }
        progressBar.setVisibility(View.INVISIBLE);

    }


}
