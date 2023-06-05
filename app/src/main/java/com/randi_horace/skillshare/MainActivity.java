package com.randi_horace.skillshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    BottomAppBar bar;



    Dialog myDialog;

    String currentUserID;

    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;
    private TextView NavProfileUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //currentUserID = mAuth.getCurrentUser().getUid();

        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            currentUserID = mFirebaseUser.getUid();
        }

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        myDialog = new Dialog(this);

        bar = findViewById(R.id.bottomAppBar);
        bar.replaceMenu(R.menu.bottom_bar_nav_menu);





        bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.mission:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MissionFragment()).commit();    //open fragment
                        break;

                    case R.id.mapButton:
                        Intent i = new Intent(MainActivity.this,MapsActivity.class);
                        startActivity(i);
                }
                return false;
            }
        });

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNavDrawer();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MissionFragment()).commit();
    }

    private void openNavDrawer(){

        final View bottomNavDrawer = getLayoutInflater().inflate(R.layout.bottom_bar_nav_view, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(bottomNavDrawer);
        bottomSheetDialog.show();

        NavigationView navigationView = bottomNavDrawer.findViewById(R.id.nav_view);

        View header = navigationView.inflateHeaderView(R.layout.nav_header);

        NavProfileUserName = header.findViewById(R.id.UserName123);
        NavProfileImage = header.findViewById(R.id.UserPhoto123);
        NavProfileUserEmail = header.findViewById(R.id.UserEmail123);

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("userName"))
                    {
                        String userName = dataSnapshot.child("userName").getValue().toString();
                        NavProfileUserName.setText(userName);
                    }

                    if (dataSnapshot.hasChild("email"))
                    {
                        String email = dataSnapshot.child("email").getValue().toString();
                        NavProfileUserEmail.setText(email);
                    }

                    if(dataSnapshot.hasChild("profileimage"))
                    {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).into(NavProfileImage);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Profile name do not exists...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_logout:
                        mAuth.signOut();
                        SendUserToLoginActivity();
                        break;

                    case R.id.nav_member:
                        startActivity(new Intent(MainActivity.this, MemberActivity.class));
                        break;

                }

                return false;
            }
        });

    }

    public void initNavDrawer(){

    }


    public void newMission(View v) {
        Intent i = new Intent(MainActivity.this,newMissionActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
        {
            CheckUserExistence();
        }
    }



    private void CheckUserExistence()
    {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild(current_user_id))
                {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(MainActivity.this, SetUpActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }


    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

}
