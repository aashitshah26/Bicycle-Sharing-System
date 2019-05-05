package com.example.dell.cyclepath;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
public class Login extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    private GoogleMap mMap;
    LocationTrack locationTrack;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private  Location lastLocation;
    private Marker currentLocationMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    String bike;
    int subscription,balance;
    int onetimeonly=0,oto=0;
    Handler handler;
    private ArrayList<LatLng> stops = new ArrayList<>();
    private final String url="https://psyclepath.000webhostapp.com/stations.php";


    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START,true);
        }
        else {
            finishAffinity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.getMenu().getItem(0).setChecked(true);
          new stop().execute();
        locationTrack = new LocationTrack(Login.this);
        if (locationTrack.canGetLocation()) {

        }
        else {
            locationTrack.showSettingsAlert();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
            checkLocationPermission();
        SharedPreferences sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        subscription = Integer.parseInt(sharedpreferences.getString("subscription","0"));
        balance = Integer.parseInt(sharedpreferences.getString("balance","0"));
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu_white);
        SupportMapFragment mapFragment1 = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment1.getMapAsync(this);

        View v = navigationView.getHeaderView(0);
        TextView name = v.findViewById(R.id.name);
        name.setText("Welcome "+sharedpreferences.getString("name","Client")+"!");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if(item.toString().equals("Wallet")) {
                    Intent i = new Intent(Login.this,Wallet.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Edit Profile")){
                    Intent i = new Intent(Login.this,Profile.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("My Rides")){
                    Intent i = new Intent(Login.this,MyRides.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Logout")){
                    getApplicationContext().getSharedPreferences("mypref", MODE_PRIVATE).edit().clear().commit();
                    Intent i = new Intent(Login.this,MainActivity.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Report Cycle")){
                    Intent i =new Intent(Login.this,Report.class);
                    startActivity(i);
                }


                return true;
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        if(client==null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Scan(View view) {
        if(subscription==1) {
            if(balance<100){
                Toast.makeText(Login.this, "U should have more than 100 rs in your wallet.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Login.this,Wallet.class);
                startActivity(i);
            }
            else {
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();
            }
        }
        else{
           Intent i = new Intent(this,Subscription.class);
           startActivity(i);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Intent i = new Intent(Login.this,BluetoothConnect.class);
                i.putExtra("mac",result.getContents());
                startActivity(i);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        buildGoogleApiClient();

    }

    protected  synchronized void buildGoogleApiClient()
    {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        client.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client,locationRequest,this);

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("OnLocationChanged","Location Has been changed");
        //Toast.makeText(this, "OnLocationChanged", Toast.LENGTH_SHORT).show();
        if(location!=lastLocation) {
            /*try {
                    Toast.makeText(Login.this, String.valueOf(location.getSpeed()), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                Log.e("Speed",e.getMessage());
            }*/
            lastLocation = location;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            if (currentLocationMarker != null) {
                currentLocationMarker.setPosition(latLng);
                if(onetimeonly==0){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    onetimeonly=1;
                }
            }

            else {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current location");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location));
                currentLocationMarker = mMap.addMarker(markerOptions);
                if(onetimeonly==0){
                   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                   onetimeonly=1;
            }
          }
        }
    }

    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else
            {
                locationTrack.showSettingsAlert();
            }
            return false;
        }
        return true;
    }

    public void current(View view) {
        if(currentLocationMarker!=null)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationMarker.getPosition(), 15));
    }

    private class stop extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            if(jsonStr!=null){
                try{
                    Log.e("details",jsonStr+"huihiu");
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray array = jsonObj.getJSONArray("res");
                    for(int i=0;i<array.length();i++)
                    {
                        JSONArray ar1=array.getJSONArray(i);
                        stops.add(new LatLng(Double.parseDouble(ar1.getString(0)),Double.parseDouble(ar1.getString(1))));
                    }
                }
                catch (Exception e){

                }
            }
            else
                Log.e("doinBackground","no data received");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("details","OnPostExecute");
            super.onPostExecute(aVoid);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_parking));

            for (int i=0;i<stops.size();i++){
                markerOptions.position(stops.get(i));
                mMap.addMarker(markerOptions);
            }
        }
    }
}

