package com.example.dell.cyclepath;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothConnect extends AppCompatActivity {

    BluetoothDevice mBluetoothDevice;
    String mac="D8:32:E3:49:06:E3";
    BluetoothAdapter mBluetoothAdapter;
    ArrayList<BluetoothDevice> mDevices;
    Set<BluetoothDevice> pairedDevices;
    private static final String TAG = "MainActivity";
    DataOutputStream os;
    ProgressBar mProgress;
    String cid;
    BluetoothDevice mDevice=null;
    private static String url = "https://psyclepath.000webhostapp.com/mac.php?";
    int pStatus = 0,i=0;
    private Handler handler = new Handler();
    TextView tv;
    static DataInputStream is;



    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.e(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){

                Log.e(TAG,"Action found");
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                Log.e(TAG,"Device found="+device.getName());
                mDevices.add(device);

                Log.e(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());

                if(device.getAddress().equals(mac))
                {
                    mDevice = device;
                    getBonded(device);
                }
            }
        }
    };


    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    mDevice=device;
                    mProgress.setProgress(40);
                    tv.setText(40 + "%");
                    createConnection(mDevice);

                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            if(mProgress.getProgress()<40) {
                                mProgress.setProgress(40);
                                tv.setText(40 + "%");
                            }
                        }
                    });
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                    Discover();
                }
            }
        }
    };







    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);
        mac = getIntent().getStringExtra("mac");
        cid=mac;
        Log.e("Mac id",mac);
        tv = (TextView) findViewById(R.id.tv);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mDevices = new ArrayList<>();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);


        new getMac().execute();

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
         mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void abc() {
        mBluetoothAdapter.enable();
        Log.e(TAG,"Bluetooth is enabled");
        pairedDevices =  mBluetoothAdapter.getBondedDevices();
        Log.e(TAG,"Getting paired devices");
        for(BluetoothDevice device: pairedDevices)
        {
            String devicename = device.getName();
            String Address = device.getAddress();

            if(Address.equals(mac))
            {
                Log.e(TAG,"Device found");
                mDevice = device;
                mProgress.setProgress(40);
                tv.setText(40 + "%");
                Log.e(TAG,"The device is paired. Device name:"+devicename +" Device address:"+Address);
            }
        }

        if(mDevice==null)
        {
            Log.e(TAG,"Discovering");
            Discover();
            Log.e(TAG,"Discovering ended");


        }

        if(mDevice!=null)
        {
            createConnection(mDevice);
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mBroadcastReceiver);
            unregisterReceiver(mBroadcastReceiver4);
        }catch(Exception e){

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Discover() {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
        }
        if(!mBluetoothAdapter.isDiscovering()){

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver, discoverDevicesIntent);
        }
        mBluetoothAdapter.cancelDiscovery();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getBonded(BluetoothDevice device) {

        Log.e(TAG,"The bluetooth is bonding with Name:"+device.getName()+"Address:"+device.getAddress());
        device.createBond();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }


    private void createConnection(BluetoothDevice device1) {
        try {
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(device1.getAddress());
            Method m  = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
            BluetoothSocket clientSocket =  (BluetoothSocket) m.invoke(device, 1);

            clientSocket.connect();

            os = new DataOutputStream(clientSocket.getOutputStream());
            is = new DataInputStream(clientSocket.getInputStream());


            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if(mProgress.getProgress()<100) {
                        mProgress.setProgress(80);
                        tv.setText(80 + "%");
                    }
                }
            });

            handler.postDelayed(null,300);

            new clientSock().start();



        } catch (Exception e) {
            Log.e(TAG,"The error 1 is:"+ e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Intent intent=new Intent(BluetoothConnect.this,Login.class);
                    //Toast.makeText(BluetoothConnect.this, "Unable to connect with Cycle", Toast.LENGTH_SHORT).show();
                    //startActivity(intent);
                }
            });
        }
    }

    public static DataInputStream getInputStream(){
        return is;
    }


    private class clientSock extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                Log.e(TAG,"Sending data");
                os.writeBytes("1");
                os.flush();

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if(mProgress.getProgress()<100) {
                            mProgress.setProgress(100);
                            tv.setText(100 + "%");
                        }
                    }
                });

                handler.postDelayed(null,300);

                Intent i = new Intent(BluetoothConnect.this,ActiveRide.class);
                i.putExtra("cid",cid);

                  startActivity(i);


            } catch (IOException e) {

                Log.e(TAG,"The error 2 is :"+e.getMessage());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Intent intent=new Intent(BluetoothConnect.this,Login.class);
                       //Toast.makeText(BluetoothConnect.this, "Unable to connect with Cycle", Toast.LENGTH_SHORT).show();
                        //startActivity(intent);
                    }
                });

            }
        }
    }

    private class getMac extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            url = url+"bike="+mac;
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            url="https://psyclepath.000webhostapp.com/mac.php?";

            if(jsonStr!=null)
            {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    mac = jsonObj.getString("res");
                }
                catch (Exception e)
                {

                }

            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if(mProgress.getProgress()<10) {
                        mProgress.setProgress(10);
                        tv.setText(10 + "%");
                    }
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //call method here
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if(mProgress.getProgress()<20) {
                        mProgress.setProgress(20);
                        tv.setText(20 + "%");
                    }
                }
            });
            abc();
        }
    }
}
