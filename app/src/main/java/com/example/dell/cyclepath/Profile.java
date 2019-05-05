package com.example.dell.cyclepath;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends AppCompatActivity {
    TextView phoneno,cam,gallery;
    CircleImageView imageView;
    EditText fullName, userEmailid, dob,subscribed,wallet_bal,weight;
    RadioButton male, female;
    ImageView profile_pic;
    ProgressBar progressBar;
    Button profile_edit,pass_edit,subscribeyn,addwallet,edit_profile_done;
    LinearLayout wallet,subscribe,options,edit_options;
    AlertDialog.Builder builder;
    AlertDialog.Builder builder1;
    LayoutInflater inflater;
    View dialogView;
    AlertDialog alertDialog;
    Bitmap pic;
    String picturePath,fileName=null,outPath,name1,email1,dob1,gender1,phoneno1,path=null,weight1,pic_url;
    Uri outuri,selectedImage;
    ScrollView profile;
    AlertDialog alertDialog1;
    int pic_id=0;
    int CAMERA_REQUEST=1,PICK_FROM_GALLERY=2,cam_permit_code=3,write_permit_code=4;
    String url = "https://psyclepath.000webhostapp.com/fetch.php?phone_num=";
    String url1="https://psyclepath.000webhostapp.com/update.php";
    String number;
    int flag=0;
    SharedPreferences sharedpreferences;
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.getMenu().getItem(2).setChecked(true);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

       progressBar=findViewById(R.id.progresspic);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu_white);

        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);

                if(item.toString().equals("Home")) {
                    Intent i = new Intent(Profile.this,Login.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }

                if(item.toString().equals("Wallet")) {
                    Intent i = new Intent(Profile.this,Wallet.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Edit Profile")){
                    /*Intent i = new Intent(Profile.this,Profile.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);*/
                }
                if(item.toString().equals("My Rides")){
                    Intent i = new Intent(Profile.this,MyRides.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Logout")){
                    getApplicationContext().getSharedPreferences("mypref", MODE_PRIVATE).edit().clear().commit();
                    Intent i = new Intent(Profile.this,MainActivity.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                if(item.toString().equals("Report Cycle")){
                    Intent i =new Intent(Profile.this,Report.class);
                    startActivity(i);
                    mDrawerLayout.closeDrawer(GravityCompat.START,true);
                }
                return true;
            }
        });


        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
         number = sharedpreferences.getString("phone","");

        View v = navigationView.getHeaderView(0);
        TextView name = v.findViewById(R.id.name);
        name.setText("Welcome "+sharedpreferences.getString("name","Client")+"!");
        boolean internet = isNetworkAvailable();
        profile=(ScrollView)findViewById(R.id.profile);
        profile.setVisibility(View.GONE);
        if(internet == true) {
            process();
        }
        else
        {
            showNoConnectionDialog(this);
        }
    }
    protected void onResume() {
        super.onResume();
        boolean internet = isNetworkAvailable();
        if(profile.getVisibility()==View.GONE)
        {
            if(internet==true) {
                process();
            }
            else
            {
                showNoConnectionDialog(this);
            }
        }
        else
        {
            //profile.setVisibility(View.GONE);
        }
    }

    void process ()
    {
        fullName = (EditText) findViewById(R.id.fullName);
        userEmailid = (EditText) findViewById(R.id.userEmailId);
        dob = (EditText) findViewById(R.id.dob);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        subscribed = (EditText) findViewById(R.id.subscribed);
        wallet_bal = (EditText) findViewById(R.id.wallet_bal);
        subscribeyn = (Button) findViewById(R.id.subscribeyn);
        addwallet = (Button) findViewById(R.id.addwallet);
        profile_edit = (Button) findViewById(R.id.profile_edit);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        pass_edit = (Button) findViewById(R.id.pass_edit);
        wallet = (LinearLayout) findViewById(R.id.wallet);
        subscribe = (LinearLayout) findViewById(R.id.subscribe);
        options = (LinearLayout) findViewById(R.id.options);
        edit_options = (LinearLayout) findViewById(R.id.edit_options);
        phoneno = (TextView) findViewById(R.id.phoneno);
        weight=(EditText)findViewById(R.id.weight);
        edit_profile_done = (Button) findViewById(R.id.edit_profile_done);
        profile_pic.setEnabled(false);
        fullName.setEnabled(false);
        weight.setEnabled(false);
        userEmailid.setEnabled(false);
        dob.setEnabled(false);
        male.setClickable(false);
        female.setClickable(false);
        getJSON(url+number, this);
        builder = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.profilepic_dialog, null);
        builder.setView(dialogView);
        cam = (TextView) dialogView.findViewById(R.id.cam);
        gallery = (TextView) dialogView.findViewById(R.id.gallery);
        alertDialog = builder.create();
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_pic.setEnabled(true);
                fullName.setEnabled(true);
                userEmailid.setEnabled(true);
                dob.setEnabled(true);
                male.setClickable(true);
                female.setClickable(true);
                weight.setEnabled(true);
                wallet.setVisibility(View.GONE);
                subscribe.setVisibility(View.GONE);
                options.setVisibility(View.GONE);
                edit_options.setVisibility(View.VISIBLE);
                flag=1;
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                DatePickerDialog datepickdialog = new DatePickerDialog(Profile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datepickdialog.show();
            }
        });
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            int i = permissioncheckcam();
                            Toast.makeText(Profile.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                            if (i != 1) {
                                requestpermissioncam(i);
                            }
                            else {
                                startCam();
                            }
                        }
                        alertDialog.dismiss();
                    }
                });
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !permissioncheckstorage()) {
                            requestpermissionstorage();
                        } else {
                            startGallery();
                        }
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        edit_profile_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("","edit done");
                name1 = fullName.getText().toString();
                email1 = userEmailid.getText().toString();
                dob1 = dob.getText().toString();
                phoneno1=phoneno.getText().toString();
                weight1=weight.getText().toString();
                if (male.isChecked()) {
                    gender1 = "male";
                }
                else {
                    gender1= "female";
                }
                if(pic_id==1) {
                    path=outPath;
                }
                else if(pic_id==2) {
                    path=getPath(selectedImage);
                }
                else {
                    path=null;
                }
                Log.e("","done clicked");
                updateProfile(url1,Profile.this);

            }
        });
                /*back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                });*/
    }
    public void onBackPressed()
    {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START,true);
        }
        else if(flag==1){

            Intent i = new Intent(Profile.this, Profile.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            flag=0;
        }
       else if(flag==0){
            super.onBackPressed();
        }
    }

    public void showNoConnectionDialog(Context ctx1) {
        if (alertDialog1 == null) {
            final Context ctx = ctx1;
            builder1 = new AlertDialog.Builder(ctx);
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

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    void startCam() {
        fileName=fileName+System.currentTimeMillis()+".jpg";
        Log.e("",""+fileName);
        String file=Environment.getExternalStorageDirectory().getAbsolutePath() +"/photos";
        File file1=new File(file);
        if(!file1.exists())
        {
            file1.mkdir();
        }
        outPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/photos/"+ fileName;
        Log.e("",""+outPath);
        File outFile = new File(outPath);
        //if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O_MR1)
        outuri= FileProvider.getUriForFile(Profile.this,BuildConfig.APPLICATION_ID + ".provider",outFile);
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(i, CAMERA_REQUEST);
    }

    void startGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
    }

    private int permissioncheckcam() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int result1=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED) {
            return 1;
        } else if(result==PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_DENIED) {
            return 2;
        }
        else if(result==PackageManager.PERMISSION_DENIED && result1==PackageManager.PERMISSION_GRANTED){
            return 3;
        }
        else {
            return 4;
        }
    }

    private boolean permissioncheckstorage() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestpermissioncam(int i) {
        if (i == 2) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, cam_permit_code);
        }
        if(i==3){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},cam_permit_code);
        }
        if(i==4)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},cam_permit_code);
        }
    }

    private void requestpermissionstorage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, write_permit_code);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == cam_permit_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCam();
            } else {
                Toast.makeText(this, "Cannot Open Camera. Permission denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == write_permit_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGallery();
            } else {
                Toast.makeText(this, "Gallery cannot be accessed. Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (outuri != null) {
                if (outPath != null) {
                    selectedImage = outuri;
                    profile_pic.setImageURI(selectedImage);
                    if(profile_pic!=null)
                    {
                        pic = ((BitmapDrawable)profile_pic.getDrawable()).getBitmap();
                        try {
                            ExifInterface ei = new ExifInterface(Environment.getExternalStorageDirectory().getAbsolutePath()+"/photos"+fileName);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    pic = rotateImage(pic, 90);
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    pic =  rotateImage(pic, 180);
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    pic = rotateImage(pic, 270);
                                    break;
                                default:
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        pic_id=1;
                        profile_pic.setImageBitmap(pic);
                        profile_pic.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            pic_id=2;
            profile_pic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        //text.setText("");
    }

    private Bitmap rotateImage(Bitmap img, int i) {
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, pic.getWidth(), pic.getHeight(), matrix, true);
        return rotatedImg;
    }

    public void updateProfile(final String UPLOAD_URL,final Context context)
    {
        class showUpdating<Params, Progress, Result> extends
                AsyncTask<Params, Progress, Result> {
            Context context;
            private final String DIALOG_MESSAGE = "Loading..";
            private ProgressDialog mDialog = null;

            private void setDialog(Context context) {
                this.mDialog = new ProgressDialog(context);
                this.mDialog.setMessage(DIALOG_MESSAGE);
                this.mDialog.setCancelable(false);
            }

            public showUpdating(Context context) {
                this.setDialog(context);
                this.context=context;
            }

            @Override
            protected void onPreExecute() {
                this.mDialog.show();
            }

            @Override
            protected Result doInBackground(Params... arg0) {
                // Place your background executed method here
                try {
                    /*runOnUiThread(new Runnable() {
                        public void run() {*/
                            try {
                                String uploadId = UUID.randomUUID().toString();
                                //Creating a multi part request
                                /*if (path.equals(null)) {
                                    Bitmap default1 = BitmapFactory.decodeResource(Profile.this.getResources(),
                                            R.drawable.man);
                                    saveImage(default1);
                                    path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/profile pic/default.jpg";
                                }*/
                                Log.e("", path + "," + name1 + "," + email1 + "," + dob1 + "," + gender1 + "," + number + "," +weight1);
                                if(path!=null) {
                                    final MultipartUploadRequest request=
                                            new MultipartUploadRequest(Profile.this, uploadId, UPLOAD_URL)
                                            .addFileToUpload(path, "image") //Adding file
                                            .addParameter("name", name1)
                                            .addParameter("email", email1)//Adding text parameter to the request
                                            .addParameter("dob", dob1)
                                            .addParameter("gender", gender1)
                                            .addParameter("phoneno", number)
                                            .addParameter("weight", weight1);
                                            request.startUpload();
                                            //finish();
                                            //Starting the upload
                                }
                                else
                                {
                                    String update=url1+"?name="+name1+"&email="+email1+"&dob="+dob1+"&gender="+gender1+"&phoneno="+number+"&weight="+weight1;
                                   /*final MultipartUploadRequest request=
                                           new MultipartUploadRequest(Profile.this, uploadId, UPLOAD_URL)
                                            //Adding file
                                            .addParameter("name", name1)
                                            .addParameter("email", email1)//Adding text parameter to the request
                                            .addParameter("dob", dob1)
                                            .addParameter("gender", gender1)
                                            .addParameter("phoneno", number)
                                            .addParameter("weight", weight1);
                                            request.startUpload();
                                            //finish();//Starting the upload*/
                                   getJSON1(update,context);
                                }
                            }
                            catch
                            (Exception exc) {
                                Log.e("error:", exc.getMessage());
                            }

                        //}
                    //});
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Result result) {
                //Toast.makeText(context,"Updated Successfully",Toast.LENGTH_SHORT).show();
                // Update the UI if u need to
                // And then dismiss the dialog
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent i=new Intent(context,Profile.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        });
                    }
                },2000);
                if (this.mDialog.isShowing()) {
                    this.mDialog.dismiss();
                }
            }
        }
        showUpdating<Void,Void,Void> updateTask = new showUpdating<>(context);
        updateTask.execute();
    }


    public void getJSON(final String urlService,final Context context)
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
                this.mDialog.setMessage("Loading Profile..");
                this.mDialog.setCancelable(false);
            }
            @Override
            protected void onPreExecute() {
                this.mDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    setText(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (this.mDialog.isShowing()) {
                    this.mDialog.dismiss();
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
        class showLoading<Params, Progress, Result> extends
                AsyncTask<Params, Progress, Result> {
            Context context;
            private final String DIALOG_MESSAGE = "Loading Profile";
            private ProgressDialog mDialog = null;

            private void setDialog(Context context) {
                this.mDialog = new ProgressDialog(context);
                this.mDialog.setMessage(DIALOG_MESSAGE);
                this.mDialog.setCancelable(false);
            }

            public showLoading(Context context) {
                this.setDialog(context);
                this.context=context;
            }

            @Override
            protected void onPreExecute() {
                this.mDialog.show();
            }

            @Override
            protected Result doInBackground(Params... arg0) {
                // Place your background executed method here
                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            GetJSON gtJSON = new GetJSON(context);
                            gtJSON.execute();
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Result result) {

                // Update the UI if u need to
                // And then dismiss the dialog
                if (this.mDialog.isShowing()) {
                    this.mDialog.dismiss();
                }
            }
        }
        showLoading<Void,Void,Void> updateTask = new showLoading<>(context);
        updateTask.execute();
    }
    public void getJSON1(final String urlService,final Context context)
    {
        class GetJSON1 extends AsyncTask<Void, Void, String> {
            Context context;
            private ProgressDialog mDialog = null;
            GetJSON1(Context context) {
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
                try {
                    setText(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (this.mDialog.isShowing()) {
                    this.mDialog.dismiss();
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
        class showLoading<Params, Progress, Result> extends
                AsyncTask<Params, Progress, Result> {
            Context context;
            private final String DIALOG_MESSAGE = "Loading..";
            private ProgressDialog mDialog = null;

            private void setDialog(Context context) {
                this.mDialog = new ProgressDialog(context);
                this.mDialog.setMessage(DIALOG_MESSAGE);
                this.mDialog.setCancelable(false);
            }

            public showLoading(Context context) {
                this.setDialog(context);
                this.context=context;
            }

            @Override
            protected void onPreExecute() {
                this.mDialog.show();
            }

            @Override
            protected Result doInBackground(Params... arg0) {
                // Place your background executed method here
                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            GetJSON1 gtJSON = new GetJSON1(context);
                            gtJSON.execute();
                        }
                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Result result) {

                // Update the UI if u need to
                // And then dismiss the dialog
                if (this.mDialog.isShowing()) {
                    this.mDialog.dismiss();
                }
            }
        }
        showLoading<Void,Void,Void> updateTask = new showLoading<>(context);
        updateTask.execute();
    }
    public void setText(String json) throws JSONException {
        profile.setVisibility(View.VISIBLE);
        JSONArray users = new JSONArray(json);
        JSONObject jsonObj = users.getJSONObject(0);
        phoneno1=jsonObj.getString("phone");
        Log.e("",""+phoneno1);
        phoneno.setText(phoneno1);

        pic_url=jsonObj.getString("pic_url");

        if(!pic_url.equals(null) && !pic_url.equals("")) {
            Log.e("","url not empty");
            Glide.with(Profile.this).load(pic_url).centerCrop().skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.INVISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressBar.setVisibility(View.INVISIBLE);
                    return false;
                }
            }).into(profile_pic);

        }
        String fullName1=jsonObj.getString("user_name");
        Log.e("",""+fullName1);
        fullName.setText(fullName1);
        String userEmailid1=jsonObj.getString("email");
        Log.e("",""+userEmailid1);
        userEmailid.setText(userEmailid1);
        String dob1=jsonObj.getString("dob");
        Log.e("",""+dob1);
        dob.setText(dob1);
        String weight1=jsonObj.getString("weight");
        Log.e("",""+weight);
        weight.setText(weight1);
        String gender1=jsonObj.getString("gender");
        Log.e("",""+gender1);
        if(gender1.equals("female"))
        {
            female.setChecked(true);
        }
        else
        {
            male.setChecked(true);
        }
        String subscribe1=jsonObj.getString("subscription_detail");
        Log.e("",""+subscribe1);
        subscribed.setText(subscribe1);
        if(subscribe1.equals("1"))
        {
            subscribeyn.setVisibility(View.INVISIBLE);
            subscribed.setText("Subscribed");
            wallet.setVisibility(View.VISIBLE);
            String wallet_bal1=jsonObj.getString("wallet_balance");
            Log.e("",""+wallet_bal1);
            wallet_bal.setText(wallet_bal1);
        }
        else {
            subscribed.setText("Not Subscribed yet.");
        }
    }
    private void saveImage(Bitmap data) {
        String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/profile pic";
        File createFolder = new File(path1);
        if (!createFolder.exists())
            createFolder.mkdir();
        File saveImage = new File(createFolder, "default.jpg");
        path = path1 + "/default.jpg";
        if (saveImage.exists()) {
        } else {
            try {
                OutputStream outputStream = new FileOutputStream(saveImage);
                data.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void changepassword(View view) {
        Intent i = new Intent(this,EditPass.class);
        startActivity(i);
    }
}
