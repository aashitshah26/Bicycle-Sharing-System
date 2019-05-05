package com.example.dell.cyclepath;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class ActiveRide extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    private static final String TAG = ActiveRide.class.getSimpleName();
    private TimerService timerService;
    private boolean serviceBound;
    TextView ride_time,calories_burnt,pollute;
    Button stop_ride,report;
    ImageButton mylocation;
    double weight=40;
    int ride=1;
    double speed=0,elapsedTime,calculatedSpeed;
    double mets=0;
    double calories=0,distance;
    double pollution=0;
    double fare;
    float time;
    String timer;
    LatLng start_loc,end_loc;
    int count=0;
    int c=1;
    android.support.v7.app.AlertDialog.Builder builder1;
    boolean firstTime=true;

    android.support.v7.app.AlertDialog alertDialog1;
    RelativeLayout ongoing_ride;
    static String startingDate,startingTime,endingDate,endingTime,startingDay ;
    ArrayList<LatLng> stops=new ArrayList<>();
    static SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");
    static SimpleDateFormat stf =  new SimpleDateFormat("HH:mm:ss");
    static SimpleDateFormat sDayf= new SimpleDateFormat("EEEE");
    private final String url="https://psyclepath.000webhostapp.com/stations.php";
    private String url1="https://psyclepath.000webhostapp.com/updaterides.php";
    private String fetchStationId="https://psyclepath.000webhostapp.com/nearest_station.php";
    private String updatewallet="https://psyclepath.000webhostapp.com/updatewalletrides.php";
    static DataInputStream is;
    DecimalFormat df=new DecimalFormat("0.00");
    String to,from;
    String phoneno1="9106468353";
    int end=1;
    // Handler to update the UI every second when the timer is running
    private final Handler mUpdateTimeHandler = new UIUpdateHandler(this);
    // Message type for the handler
    private final static int MSG_UPDATE_TIME = 0;


    class clientSock extends Thread {

        @Override
        public void run() {
            super.run();
            byte b[] = new byte[1024];
            while (c==1) {
            try {
                Log.e(TAG, "Fetching data");

                    is.read(b);
                    String a = b.toString();

                    if(a!=null) {
                        end=0;
                    }
                }catch (Exception e) {
                Log.e("in catch", "exception " + e);
            }
            }
        }
    }

    SharedPreferences sharedPreferences ;
    public void check() {
       if(end==0){
           endRide();
       }
       else{
           Toast.makeText(ActiveRide.this, "Not connected to Dock or Lock not closed", Toast.LENGTH_SHORT).show();
       }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(serviceBound) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("calories", Double.toString(calories));
            editor.putString("pollution", Double.toString(pollution));
            editor.putInt("after_destroy", 1);
            editor.commit();
            Log.e("on destroy", "calories:" + calories + ",pollution:" + pollution);
            Log.e("shared", "calories=" + sharedPreferences.getString("calories", "0") + ",pollution=" + sharedPreferences.getString("pollution", "0") + ",after_destroy=" + sharedPreferences.getInt("after_destroy", 0));
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "Starting and binding service");
        }
        Intent i = new Intent(this, TimerService.class);
        startService(i);
        bindService(i, mConnection, 0);
    }

    public void endRide()
    {
        Log.e("endRide()","ended");
        double from_lat=start_loc.latitude;
        double from_lon=start_loc.longitude;
        double to_lat=end_loc.latitude;
        double to_lon=end_loc.longitude;
        fetchStationId=fetchStationId+"?from_lat="+from_lat+"&from_lon="+from_lon+"&to_lat="+to_lat+"&to_lon="+to_lon;
        fetchNearStation(fetchStationId,this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        updateUIStopRun();
        if (serviceBound) {
            // If a timer is active, foreground the service, otherwise kill the service
            if (timerService.isTimerRunning()) {
                timerService.foreground();
            }
            else {
                stopService(new Intent(this, TimerService.class));
            }
            // Unbind the service
            unbindService(mConnection);
            serviceBound = false;
        }
    }

    public void startRide() {
        if (serviceBound && !timerService.isTimerRunning()) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "Starting timer");
            }
            timerService.startTimer();
            updateUIStartRun();
        }
        else if (serviceBound && timerService.isTimerRunning()) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "Resuming timer");
            }
            //timerService.stopTimer();
            updateUIStartRun();
        }
    }

    /**
     * Updates the UI when a run starts
     */
    private void updateUIStartRun() {
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
    }

    /**
     * Updates the UI when a run stops
     */
    private void updateUIStopRun() {
        mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
    }

    /**
     * Updates the timer readout in the UI; the service must be bound
     */
    private void updateUITimer() {
        if (serviceBound) {
            long sec = timerService.elapsedTime();
            int after_destroy=sharedPreferences.getInt("after_destroy",0);

            //if (timerService.elapsedTime() < 60) {
            //  timerTextView.setText(timerService.elapsedTime() + " seconds");
            //}
            // else if(timerService.elapsedTime()>=60)
            int seconds =(int) sec % 60;
            int minutes = (int)(sec / 60) % 60;
            int hours = (int)sec / 3600;
            timer=String.format("%02d",hours)+":"+String.format("%02d",minutes)+":"+String.format("%02d",seconds);
            ride_time.setText(timer);
            time=(float)((int)sec)/3600;
            Log.e("","secs:"+sec+"speed:"+speed+" mets:"+mets+" time:"+time+" timeformatted:");
            if(after_destroy==1) {
                Double pollution1=Double.parseDouble(sharedPreferences.getString("pollution","0"));
                Double calories1=Double.parseDouble(sharedPreferences.getString("calories","0"));
                Log.e("after_destroy",""+after_destroy);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("after_destroy",0);
                editor.commit();
                calories=calories1;
                pollution=pollution1;
                Log.e("after_destroy","calories:"+calories);
                calories_burnt.setText(df.format(calories));
                Log.e("after_destroy","pollution:"+pollution);
                pollute.setText(df.format(pollution));
            }
            else {
                calories =  (weight * mets * time / 1000);
                Log.e("", "calories:" + calories);
                calories_burnt.setText(df.format(calories));
                Log.e("", "pollution:" + pollution);
                pollute.setText(df.format(pollution));
            }
            fare=hours*10;
            if(minutes<30) {
                fare=fare+5;
            }
            else if(minutes==30)
            {
                if(seconds>0)
                {
                    fare=fare+10;
                }
                else
                {
                    fare=fare+5;
                }
            }
            else
            {
                fare=fare+10;
            }
        }
    }
    /**
     * Callback for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "Service bound");
            }
            TimerService.RunServiceBinder binder = (TimerService.RunServiceBinder) service;
            timerService = binder.getService();
            serviceBound = true;
            // Ensure the service is not in the foreground when bound
            timerService.background();
            // Update the UI if the service is already running the timer
            if (timerService.isTimerRunning()) {
                updateUIStartRun();
            }
            if(ride==1)
            {
                ride++;
                Log.e(TAG,"Ride checked");
                startRide();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "Service disconnect");
            }
            serviceBound = false;
        }
    };

    /**
     * When the timer is running, use this handler to update
     * the UI every second to show timer progress
     */
    static class UIUpdateHandler extends Handler {

        private final static int UPDATE_RATE_MS = 1000;
        private final WeakReference<ActiveRide> activity;

        UIUpdateHandler(ActiveRide activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            if (MSG_UPDATE_TIME == message.what) {
                if (Log.isLoggable(TAG, Log.ERROR)) {
                    Log.e(TAG, "updating time");
                }
                activity.get().updateUITimer();
                sendEmptyMessageDelayed(MSG_UPDATE_TIME, UPDATE_RATE_MS);
            }
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Timer service tracks the start and end time of timer; service can be placed into the
     * foreground to prevent it being killed when the activity goes away
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_ride);
        sharedPreferences = getSharedPreferences("rideDetails", Context.MODE_PRIVATE);
        ongoing_ride=findViewById(R.id.ongoing_ride);
        ongoing_ride.setVisibility(View.INVISIBLE);
        boolean internet = isNetworkAvailable();
        new clientSock().start();
        if(internet == true) {
            process();
        }
        else
        {
            showNoConnectionDialog(this);
        }
        mylocation=(ImageButton)findViewById(R.id.mylocation);
        ride_time = (TextView) findViewById(R.id.ride_time);
        stop_ride = (Button) findViewById(R.id.stop_ride);
        report=(Button)findViewById(R.id.report);
        calories_burnt=(TextView)findViewById(R.id.calories);
        pollute=(TextView)findViewById(R.id.pollute);
        sdf.setTimeZone(TimeZone.getDefault());
        stf.setTimeZone(TimeZone.getDefault());

        is=BluetoothConnect.getInputStream();
        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            new AlertDialog.Builder(this)
                    .setTitle("GPS is not enabled")  // GPS not found
                    .setMessage("You have to enable GPS to enable Maps.") // Want to enable?
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        checkLocationPermission();
        stop_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
                if (serviceBound && timerService.isTimerRunning()) {
                    if (Log.isLoggable(TAG, Log.ERROR)) {
                        Log.e(TAG, "Stopping timer");
                    }
                    timerService.stopTimer();
                    updateUIStopRun();
                    Log.e("Fare calculated",fare+"");
                    //unbindService(mConnection);
                }
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle ext=getIntent().getExtras();
                /*int cid=ext.getExtra("cid");
                Intent i=new Intent(ActiveRide.this,Report.class);
                i.putExtra("cid",cid);
                startActivity(i);*/
            }
        });

        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLocationMarker != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationMarker.getPosition(), 15));
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean internet = isNetworkAvailable();
        if(internet == true && ongoing_ride.getVisibility()==View.INVISIBLE) {
            process();
        }
        else if(internet==false)
        {
            showNoConnectionDialog(this);
        }
        else
        {

        }
    }

    void process() {
        ongoing_ride.setVisibility(View.VISIBLE);
        stations();
    }
    /*public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(false);
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            // mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }
    public void showNoConnectionDialog(Context ctx1) {
        if (alertDialog1 == null) {
            final Context ctx = ctx1;
            builder1 = new android.support.v7.app.AlertDialog.Builder(ctx);
            builder1.setCancelable(false);
            builder1.setMessage("Please turn on internet to connect with us");
            builder1.setTitle("Alert!");
            builder1.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog1.dismiss();
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    intent.setClassName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity");
                    //ctx.startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
                    ctx.startActivity(intent);
                }
            });

            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog1.dismiss();
                }
            });
            alertDialog1 = builder1.create();
        /*builder.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });*/
            alertDialog1.show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("OnLocationChanged", "Location Has been changed");
        Toast.makeText(this, "OnLocationChanged", Toast.LENGTH_SHORT).show();
        if (location != lastLocation) {
            if(lastLocation!=null) {
                elapsedTime = (location.getTime() - lastLocation.getTime()) / 1_000; // Convert milliseconds to seconds
                calculatedSpeed = lastLocation.distanceTo(location) / elapsedTime;
                distance=distance+lastLocation.distanceTo(location);
            }
            lastLocation = location;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            /*if(firstTime) {
                if(start_loc==null)
                {
                    start_loc=new LatLng(location.getLatitude(), location.getLongitude());
                    Log.e("start location",start_loc+"");
                }
                stations();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
            }*/
            end_loc=new LatLng(location.getLatitude(),location.getLongitude());
            Log.e("current location",end_loc+"");
            if (currentLocationMarker != null) {
                currentLocationMarker.setPosition(latLng);
            } else {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current location");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));
                currentLocationMarker = mMap.addMarker(markerOptions);
                //if(firstTime) {
                if(start_loc==null) {
                    start_loc = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.e("start location", start_loc + "");
                }
                //stations();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocationMarker.getPosition()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationMarker.getPosition(),20));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
                //   mMap.animateCamera(CameraUpdateFactory.zoomIn());
                //}
                //firstTime=false;
            }
        }
        if(count<5) {
            double speed1 = location.hasSpeed() && location.getSpeed()>0 ? location.getSpeed() : calculatedSpeed;
            speed = speed + (speed1*1000)/3600;
            count++;
        }
        if(count==5) {
            speed=speed/5;
            count=0;
        }
        if(speed==0)
        {
            mets=0;
        }
        if(speed<=8.5)
        {
            mets=3.5;
        }
        else if(speed>8.9 && speed<=16.1)
        {
            mets=5.8;
        }
        else if(speed>16.1 && speed<=19.2)
        {
            mets=6.8;
        }
        else if(speed>19.2 && speed<=22.5)
        {
            mets=8;
        }
        else if(speed>22.5 && speed<=25.7)
        {
            mets=10;
        }
        else if(speed>25.7 && speed>=32)
        {
            mets=12;
        }
        else if(speed>32)
        {
            mets=15.8;
        }

        pollution=pollution+((((distance/1000)/45)*2392))/1000;

        Toast.makeText(this,"speed:"+speed+"distance:"+(distance/1000),Toast.LENGTH_SHORT).show();

        // Log.e("","speed:"+speed+" mets:"+mets+" time:"+time);
        //calories=weight*mets*time;
        //Log.e("","calories:"+calories);
        //calories_burnt.setText(""+calories);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public static class TimerService extends Service {

        private static final String TAG = TimerService.class.getSimpleName();

        // Start and end times in milliseconds
        private long startTime, endTime;

        // Is the service tracking time?
        private boolean isTimerRunning;

        // Foreground notification id
        private static final int NOTIFICATION_ID = 1;

        // Service binder
        private final IBinder serviceBinder = new RunServiceBinder();

        public class RunServiceBinder extends Binder {
            TimerService getService() {
                return TimerService.this;
            }
        }

        @Override
        public void onCreate() {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "Creating service");
            }
            startTime = 0;
            endTime = 0;
            isTimerRunning = false;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "Starting service");
            }
            return Service.START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "Binding service");
            }
            return serviceBinder;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "Destroying service");
            }
        }

        /**
         * Starts the timer
         */
        public void startTimer() {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            //sdf.setTimeZone(TimeZone.getDefault());
            //String startDateandTime = sdf.format(new Date());
            if (!isTimerRunning) {
                startTime = System.currentTimeMillis();
                isTimerRunning = true;
                startingDate = sdf.format(new Date());
                startingTime = stf.format(new Date());
                startingDay = sDayf.format(new Date());
                //Log.e("started at",""+startTime);
                Log.e("started at",startingDate+","+startingDay+" and "+startingTime);
            } else {
                Log.e(TAG, "startTimer request for an already running timer");
            }
        }
        /**
         * Stops the timer
         */
        public void stopTimer() {
            if (isTimerRunning) {
                endTime = System.currentTimeMillis();
                endingDate=sdf.format(new Date());
                endingTime=stf.format(new Date());
                Log.e("Ended at",endingDate+" and "+endingTime);
                //Log.e("ended at:",endTime+"");
                isTimerRunning = false;
            } else {
                Log.e(TAG, "stopTimer request for a timer that isn't running");
            }
        }

        /**
         * @return whether the timer is running
         */
        public boolean isTimerRunning() {
            return isTimerRunning;
        }

        /**
         * Returns the  elapsed time
         *
         * @return the elapsed time in seconds
         */
        public long elapsedTime() {
            // If the timer is running, the end time will be zero
            return endTime > startTime ?
                    (endTime - startTime) / 1000 :
                    (System.currentTimeMillis() - startTime) / 1000;
        }
        /**
         * Place the service into the foreground
         */
        public void foreground() {
            startForeground(NOTIFICATION_ID, createNotification());
        }
        /**
         * Return the service to the background
         */
        public void background() {
            stopForeground(true);
        }

        /**
         * Creates a notification for placing the service into the foreground
         *
         * @return a notification for interacting with the service when in the foreground
         */
        private Notification createNotification() {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                String channelId = "some_channel_id";
                CharSequence channelName = "Some Channel";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
                notificationManager.createNotificationChannel(notificationChannel);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setContentTitle("Ride Active")
                        .setContentText("Tap to return to Psyclepath")
                        .setChannelId(channelId)
                        .setSmallIcon(R.mipmap.ic_launcher);
                Intent resultIntent = new Intent(this, ActiveRide.class);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(this, 0, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);
                return builder.build();
            }
            else {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setContentTitle("Ride Active")
                        .setContentText("Tap to return to Psyclepath")
                        .setSmallIcon(R.mipmap.ic_launcher);
                Intent resultIntent = new Intent(this, ActiveRide.class);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(this, 0, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(resultPendingIntent);

                return builder.build();
            }
        }
    }

    public void stations() {
        class stop extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                HttpHandler sh = new HttpHandler();
                String jsonStr = sh.makeServiceCall(url);
                if (jsonStr != null) {
                    try {
                        Log.e("details", jsonStr);
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray array = jsonObj.getJSONArray("res");
                        for (int i = 0; i < array.length(); i++) {
                            JSONArray ar1 = array.getJSONArray(i);
                            stops.add(new LatLng(Double.parseDouble(ar1.getString(0)), Double.parseDouble(ar1.getString(1))));
                        }
                    }
                    catch (Exception e) {
                    }
                } else
                    Log.e("doinBackground", "no data received");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.e("details", "OnPostExecute");
                super.onPostExecute(aVoid);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_parking));

                for (int i = 0; i < stops.size(); i++) {
                    markerOptions.position(stops.get(i));
                    mMap.addMarker(markerOptions);
                }
            }
        }
        stop updateTask = new stop();
        updateTask.execute();
    }

    public void getJSON1(final String urlService,final Context context,final ProgressDialog mDialog)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {
            Context context;
            private ProgressDialog mDialog ;
            GetJSON(Context context,ProgressDialog mDialog) {
                this.context=context;
                this.mDialog=mDialog;
            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("Info","Ride ended");
                updatewallet=updatewallet+"?phone="+phoneno1+"&fare="+fare;
                updateWallet(updatewallet,context,mDialog);
                //Intent i=new Intent(EditPa
                //ss.this,Show.class);
                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(i);
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
        GetJSON gtJSON = new GetJSON(context,mDialog);
        gtJSON.execute();
    }

    public void fetchNearStation(final String urlService,final Context context)
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
                }
                try{
                    JSONObject jsonObj=new JSONObject(s);
                    //JSONArray users=jsonObj.getJSONArray("response");
                    //JSONArray users = new JSONArray(s);
                    //JSONObject jsonObj = users.getJSONObject(0);
                    from=jsonObj.getString("from_id");
                    to=jsonObj.getString("to_id");
                    Log.e("before updating ride","from:"+from+",to:"+to+",phone:"+phoneno1+",date:"+startingDate+
                            ",day:"+startingDay+",start:"+startingTime+",end:"+endingTime+",fare:"+fare+",cal:"+ df.format(calories)+",pol:"+df.format(pollution));
                    url1=url1+"?phone="+phoneno1+"&to="+to+"&from="+from+"&date="+startingDate+"&day="+startingDay+"&start="+startingTime+"&end="+endingTime+"&fare="+fare+"&cal="+df.format(calories)+"&pol="+df.format(pollution);
                    getJSON1(url1,context,mDialog);
                }
                catch(Exception e)
                {
                    Log.e("nearby station","error "+e);
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
        GetJSON gtJSON = new GetJSON(context);
        gtJSON.execute();
    }

    public void updateWallet(final String urlService,final Context context,final ProgressDialog mDialog)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {
            Context context;
            private ProgressDialog mDialog ;
            GetJSON(Context context,ProgressDialog mDialog) {
                this.context=context;
                this.mDialog=mDialog;
            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (this.mDialog.isShowing()) {
                    this.mDialog.dismiss();
                }
                Log.e("Info", "Wallet updated");
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    String updated_bal=jsonObj.getString("updated_bal");
                    String fare=jsonObj.getString("fare");
                    Log.e("","updated_bal:"+updated_bal+",fare:"+fare);
                    Intent i=new Intent(ActiveRide.this, Show.class);
                    i.putExtra("fare",fare);
                    i.putExtra("updated_bal",updated_bal);
                    i.putExtra("duration",timer);
                    i.putExtra("pollution",df.format(pollution));
                    i.putExtra("calories",df.format(calories));
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("calories","0");
                    editor.putString("pollution","0");
                    editor.putInt("after_destroy",0);
                    editor.commit();
                    startActivity(i);
                    ActiveRide.this.finish();
                }
                catch(Exception e)
                {
                    Log.e("catch","exception:"+e);
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
        GetJSON gtJSON = new GetJSON(context,mDialog);
        gtJSON.execute();
    }

    @Override
    public void onBackPressed() {
    }
}

