package com.randi_horace.skillshare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class newMissionActivity extends AppCompatActivity {

    private ImageButton mSelecImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitBtn;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    double longitude;
    double latitude;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mission);

        TextView txtclose;
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Mission");


        txtclose = findViewById(R.id.txtclose);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSelecImage = (ImageButton) findViewById(R.id.imageSelect);

        mPostTitle = (EditText) findViewById(R.id.titleField);
        mPostDesc = (EditText) findViewById(R.id.descField);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);

        mProgress = new ProgressDialog(this);

        mSelecImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });

        Spinner mySpinner = (Spinner) findViewById(R.id.districtSpinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(newMissionActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.district));// in res/values/string.xml
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    //Central and Western
                    latitude = 22.2724967;
                    longitude = 114.1350874;
                }
                else if (i == 1) {
                    //Eastern
                    latitude = 22.2756648;
                    longitude = 114.2059835;
                }
                else if (i == 2) {
                    //Southern
                    latitude = 22.2395616;
                    longitude = 114.1537401;
                }
                else if (i == 3) {
                    //Wan Chai
                    latitude = 22.2773499;
                    longitude = 114.1696255;
                }
                else if (i == 4) {
                    //Sham Shui Po
                    latitude = 22.3290706;
                    longitude = 114.1522559;
                }
                else if (i == 5) {
                    //Kowloon City
                    latitude = 22.3219813;
                    longitude = 114.1718194;
                }
                else if (i == 6) {
                    //Kwun Tong
                    latitude = 22.3120123;
                    longitude = 114.2193389;
                }
                else if (i == 7) {
                    //Wong Tai Sin
                    latitude = 22.3420553;
                    longitude = 114.1898863;
                }
                else if (i == 8) {
                    //Yau Tsim Mong
                    latitude = 22.3099511;
                    longitude = 114.1588463;
                }
                else if (i == 9) {
                    //Islands
                    latitude = 22.3548163;
                    longitude = 114.0001601;
                }
                else if (i == 10) {
                    //Kwai Tsing
                    latitude = 22.3534523;
                    longitude = 114.0958334;
                }
                else if (i == 11) {
                    //North
                    latitude = 22.5124981;
                    longitude = 114.1181291;
                }
                else if (i == 12) {
                    //Sai Kung
                    latitude = 22.4081311;
                    longitude = 114.2468085;
                }
                else if (i == 13) {
                    //Sha Tin
                    latitude = 22.3887767;
                    longitude = 114.1820634;
                }
                else if (i == 14) {
                    //Tai Po
                    latitude = 22.4459487;
                    longitude = 114.1462521;
                }
                else if (i == 15) {
                    //Tsuen Wan
                    latitude = 22.3707447;
                    longitude = 114.0949557;
                }
                else if (i == 16) {
                    //Tuen Mun
                    latitude = 22.395427;
                    longitude = 113.9315835;
                }
                else if (i == 17) {
                    //Yuen Long
                    latitude = 22.4458533;
                    longitude = 114.0142113;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //no action
            }
        });

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            currentUserID = mFirebaseUser.getUid();
        }


    }

    private void startPosting(){

        mProgress.setMessage("Posing...");

        final String latitude_val = Double.toString(latitude);
        final String longitude_val = Double.toString(longitude);
        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri !=null ){

            mProgress.show();

            StorageReference filepath = mStorage.child("Mission_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    DatabaseReference newPost = mDatabase.push();

                    newPost.child("latitude").setValue(latitude_val);
                    newPost.child("longitude").setValue(longitude_val);
                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    newPost.child("uid").setValue(currentUserID);
                    newPost.child("image").setValue(downloadUrl.toString());

                    mProgress.dismiss();

                    startActivity(new Intent(newMissionActivity.this,MainActivity.class));

                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mSelecImage.setImageURI(mImageUri);
        }
    }
}
