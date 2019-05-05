package com.example.dell.cyclepath;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.UUID;

public class Register extends AppCompatActivity {
    EditText name,email,dob,weight,pass,repass;
    RadioGroup gender;
    RadioButton male;
    Button signup;
    CheckBox terms;
    DatePickerDialog datepickdialog;
    String name1,email1,dob1,weight1,pass1,repass1,gender1,phoneno1,picturePath,path=null,fileName="image";
    ImageView profile_pic;
    AlertDialog.Builder builder;
    View dialogView;
    LayoutInflater inflater;
    TextView cam,gallery,login;
    AlertDialog alertDialog;
    Uri outuri;
    Bitmap pic;
    int pic_id;
    int CAMERA_REQUEST=1;
    int PICK_FROM_GALLERY=2;
    int cam_permit_code=10;
    int cam_permit_code1=12;
    int write_permit_code=11;
    Uri selectedImage;
    String outPath;
    public static final String UPLOAD_URL = "https://psyclepath.000webhostapp.com/profilepic.php";
    SharedPreferences sharedpreferences;
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=(EditText)findViewById(R.id.fullName);
        email=(EditText)findViewById(R.id.userEmailId);
        dob=(EditText)findViewById(R.id.dob);
        weight=(EditText)findViewById(R.id.weight);
        pass=(EditText)findViewById(R.id.password);
        repass=(EditText)findViewById(R.id.confirmPassword);
        gender=(RadioGroup)findViewById(R.id.gender);
        male=(RadioButton)findViewById(R.id.male);
        signup=(Button)findViewById(R.id.signUpBtn);
        terms=(CheckBox)findViewById(R.id.terms_conditions);
        profile_pic=(ImageView)findViewById(R.id.profile_pic);
        login=(TextView)findViewById(R.id.already_user);
        builder = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.profilepic_dialog, null);
        builder.setView(dialogView);
        cam=(TextView)dialogView.findViewById(R.id.cam);
        gallery=(TextView)dialogView.findViewById(R.id.gallery);
        phoneno1=getIntent().getStringExtra("number");
        alertDialog = builder.create();
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            int i = permissioncheckcam();
                            if (i != 1) {
                                requestpermissioncam(i);
                            } else {
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

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datepickdialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datepickdialog.show();
            }
        });

        //int selectedId = gender.getCheckedRadioButtonId();
        //checkedgender = (RadioButton) findViewById(selectedId);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pic_id == 1) {
                    path = outPath;
                } else if (pic_id == 2) {
                    path = getPath(selectedImage);
                } else {
                    //Uri path1 = Uri.parse("res:/" + R.drawable.defaultpic);
                    //path=path1.toString();
                    Bitmap default1 = BitmapFactory.decodeResource(Register.this.getResources(),
                            R.drawable.man);
                    saveImage(default1);
                }
                Log.e("", "" + path);
                name1 = name.getText().toString();
                email1 = email.getText().toString();
                weight1=weight.getText().toString();
                dob1 = dob.getText().toString();
                if (male.isChecked()) {
                    gender1 = "male";
                } else {
                    gender1 = "female";
                }
                pass1 = pass.getText().toString();
                repass1 = repass.getText().toString();
                if (name1.isEmpty() || email1.isEmpty() || weight1.isEmpty() || dob1.isEmpty() || gender1.isEmpty() || pass1.isEmpty() || repass1.isEmpty()) {
                    Toast.makeText(Register.this, "All fields are necessary.", Toast.LENGTH_SHORT).show();
                } else if(phoneno1.length()!=10) {
                    Toast.makeText(Register.this,"Enter appropriate phone number",Toast.LENGTH_SHORT).show();
                } else if (!pass1.equals(repass1)) {
                    Toast.makeText(Register.this, "Passwords must match", Toast.LENGTH_SHORT).show();
                } else if (!terms.isChecked()) {
                    Toast.makeText(Register.this, "You must agree the terms and conditions", Toast.LENGTH_SHORT).show();
                } else {
                    signUp(UPLOAD_URL,Register.this);
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Register.this,MainActivity.class);
                startActivity(i);
            }
        });


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
        String file= Environment.getExternalStorageDirectory().getAbsolutePath()+"/photos";
        File file1=new File(file);
        if(!file1.exists()) {
            file1.mkdir();
        }

        outPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/photos/"+ fileName;
        File outFile = new File(outPath);
        //if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O_MR1)
        outuri= FileProvider.getUriForFile(Register.this,BuildConfig.APPLICATION_ID + ".provider",outFile);
        Log.e("",""+outuri);
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
        int result1= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED) {
            return 1;
        }
        else if(result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_DENIED) {
            return 2;
        }
        else if(result == PackageManager.PERMISSION_DENIED && result1 == PackageManager.PERMISSION_GRANTED)
        {
            return 3;
        }
        else
        {
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
        if(i==2) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, cam_permit_code);
        }
        else if(i==3)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, cam_permit_code);
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},cam_permit_code1);
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
        if(requestCode == cam_permit_code1)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                startCam();
            } else {
                Toast.makeText(this,"Cannot Open Camera. Permission denied",Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == write_permit_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGallery();
            } else {
                Toast.makeText(this, "Galery cannot be accessed. Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            if (outuri != null) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/photos/" + fileName;
                if (path != null) {
                    File file1 = new File(path);
                    selectedImage = FileProvider.getUriForFile(Register.this, BuildConfig.APPLICATION_ID + ".provider", file1);
                    Log.e("",""+selectedImage);
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
                        } catch (IOException e) {
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
            Log.e("",""+selectedImage);
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
    public void signUp(final String UPLOAD_URL,final Context context)
    {
        class showLoading<Params, Progress, Result> extends
                AsyncTask<Params, Progress, Result> {
            Context context;
            private final String DIALOG_MESSAGE = "Signing Up";
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
                            try {
                                String uploadId = UUID.randomUUID().toString();
                                //Creating a multi part request
                                Log.e("", path + "," + name1 + "," + email1 + "," + dob1 + "," + gender1 + "," + weight1 + "," + phoneno1);
                                new MultipartUploadRequest(Register.this, uploadId, UPLOAD_URL)
                                        .addFileToUpload(path, "image") //Adding file
                                        .addParameter("name", name1)
                                        .addParameter("email", email1)//Adding text parameter to the request
                                        .addParameter("dob", dob1)
                                        .addParameter("weight",weight1)
                                        .addParameter("pass",pass1)
                                        .addParameter("gender", gender1)
                                        .addParameter("phoneno", phoneno1)
                                        .startUpload(); //Starting the upload
                            } catch (Exception exc) {
                                Toast.makeText(Register.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                            }
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
                Toast.makeText(context,"Signup done",Toast.LENGTH_SHORT).show();
                // Update the UI if u need to
                // And then dismiss the dialog
                if (this.mDialog.isShowing()) {
                    this.mDialog.dismiss();
                }
                Intent i = new Intent(Register.this,MainActivity.class);
                startActivity(i);
            }
        }
        showLoading<Void,Void,Void> loadTask = new showLoading<>(context);
        loadTask.execute();
    }
    private void saveImage(Bitmap data) {
        String path1 =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/profile pic";
        File createFolder = new File(path1);
        if (!createFolder.exists())
            createFolder.mkdir();
        File saveImage = new File(createFolder, "default.jpg");
        path=path1+"/default.jpg";
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
}
