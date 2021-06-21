package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

import Nav_Fragments.ChangePassword;
import Nav_Fragments.Feedback;
import Nav_Fragments.Home;
import Nav_Fragments.Patients_Chat;
import Nav_Fragments.profile;
import de.hdodenhof.circleimageview.CircleImageView;

public class WelcomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference reference;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView tv_doctorname;
    TextView doctor_header_name;

    FragmentManager manager;
    FragmentTransaction transaction;

    Button materialButton;

    String final_name;
    String final_mobile;
    String final_email;

    CircleImageView doctor_profile_image;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST=20;
    FirebaseStorage storage;
    DatabaseReference reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        tv_doctorname = (TextView) findViewById(R.id.doctor_name);

        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu2);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View view = navigationView.getHeaderView(0);

        doctor_profile_image=(CircleImageView) view.findViewById(R.id.doctor_profile_image);
        doctor_header_name=(TextView) view.findViewById(R.id.header_name);


        getSupportActionBar();

        ActionBarDrawerToggle drawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.white));
        navigationView.setNavigationItemSelectedListener(this);

        String name = getIntent().getStringExtra("username");
        String mobile = getIntent().getStringExtra("usermobile");
        String email=getIntent().getStringExtra("useremail");

        doctor_header_name.setText(name);

        final_mobile = mobile;
        final_name = name;
        final_email=email;

        tv_doctorname.setText(name);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        Home home = new Home();
        Bundle hmebundle1=new Bundle();
        hmebundle1.putString("doctor_mobile",final_mobile);
        hmebundle1.putString("doctor_name",final_name);
        hmebundle1.putString("doctor_email",final_email);
        home.setArguments(hmebundle1);
        transaction.replace(R.id.main_body, home);
        transaction.commit();

        reference = FirebaseDatabase.getInstance().getReference();



        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.logout) {

                    FirebaseAuth.getInstance().signOut();

                    Intent i = new Intent(WelcomeActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                    return true;
                }
                return false;
            }
        });

        reference= FirebaseDatabase.getInstance().getReference("Doctors").child(mobile);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Doctor doctor=snapshot.getValue(Doctor.class);

                if(doctor.getImageURL().equals("default")){
                    doctor_profile_image.setImageResource(R.mipmap.ic_launcher);
                }

                else {

                    Glide.with(getApplicationContext()).load(doctor.getImageURL()).into(doctor_profile_image);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        doctor_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImage();

            }
        });


    }

    private void openImage() {

        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,IMAGE_REQUEST);

    }

    private void uploadImage(Uri u){

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading File Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.show();

        storageReference=storageReference.child("DoctorImages/"+ UUID.randomUUID().toString());

        storageReference.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String fileLocation=uri.toString();

                        reference2=FirebaseDatabase.getInstance().getReference("Doctors").child(final_mobile);
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("imageURL",fileLocation);
                        reference2.updateChildren(map);

                    }
                });

                Toast.makeText(WelcomeActivity.this, "Image Uploaded successfullyy", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Welcome",e.getMessage());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double d = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setProgress((int)d);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST) {

            if (resultCode == RESULT_OK) {

                Uri u = data.getData();

                uploadImage(u);
            }

        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back Press Disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();


        switch (item.getItemId()) {

            case R.id.homeid:
                Home home = new Home();
                Bundle homebundle=new Bundle();
                homebundle.putString("doctor_mobile",final_mobile);
                homebundle.putString("doctor_name",final_name);
                homebundle.putString("doctor_mobile",final_mobile);
                home.setArguments(homebundle);
                transaction.replace(R.id.main_body, home);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;

            case R.id.myprofile:
                profile prof = new profile();
                Bundle bundle = new Bundle();
                bundle.putString("doctor_mobile", final_mobile);
                prof.setArguments(bundle);
                transaction.replace(R.id.main_body, prof);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.patient:
                Patients_Chat patients_chat = new Patients_Chat();
                Bundle bundle2 = new Bundle();
                bundle2.putString("doctor_mobile", final_mobile);
                patients_chat.setArguments(bundle2);
                transaction.replace(R.id.main_body, patients_chat);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.feedback:
                Feedback fback = new Feedback();
                Bundle bundle3=new Bundle();
                bundle3.putString("doctor_mobile",final_mobile);
                bundle3.putString("doctor_name",final_name);
                fback.setArguments(bundle3);
                transaction.replace(R.id.main_body, fback);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;

            case R.id.changepassword:
                ChangePassword changePassword=new ChangePassword();
                Bundle bundle5=new Bundle();
                bundle5.putString("doctor_email",final_email);
                bundle5.putString("doctor_name",final_name);
                bundle5.putString("doctor_mobile",final_mobile);
                changePassword.setArguments(bundle5);
                transaction.replace(R.id.main_body,changePassword);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;
        }
        return false;
    }

    private void status(String status){
        reference2=FirebaseDatabase.getInstance().getReference("Doctors").child(final_mobile);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference2.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}