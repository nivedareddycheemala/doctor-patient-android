package com.example.patientapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class WelcomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference reference;
    DatabaseReference reference2;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView tv_patientname;

    FragmentManager manager;
    FragmentTransaction transaction;

    Button materialButton;

    String final_email;
    String final_name;
    String final_age;
    String final_mobile;
    String finale_dob;
    String final_password;
    String final_gender;
    String final_address;

    CircleImageView patient_profile_image;
    TextView patient_header_name;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST=20;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        tv_patientname=(TextView) findViewById(R.id.patient_name);


        toolbar=findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu2);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=(NavigationView) findViewById(R.id.nav_view);

        View view = navigationView.getHeaderView(0);

        patient_profile_image=(CircleImageView) view.findViewById(R.id.patient_profile_image);
        patient_header_name=(TextView) view.findViewById(R.id.header_name);


        getSupportActionBar();

        ActionBarDrawerToggle drawerToggle=
                new ActionBarDrawerToggle(this,drawerLayout,toolbar,0,0);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.white));
        navigationView.setNavigationItemSelectedListener(this);

        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        Home home=new Home();
        transaction.replace(R.id.main_body,home);
        transaction.commit();


        String name = getIntent().getStringExtra("username");
        String email=getIntent().getStringExtra("useremail");
        String age=getIntent().getStringExtra("userage");
        String mobile=getIntent().getStringExtra("usermobile");
        String gender=getIntent().getStringExtra("usergender");
        String dob=getIntent().getStringExtra("userdob");
        String address=getIntent().getStringExtra("useraddress");

        patient_header_name.setText(name);

        final_email=email;
        final_address=address;
        final_age=age;
        final_gender=gender;
        finale_dob=dob;
        final_mobile=mobile;

        final_name=name;

        tv_patientname.setText(name);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.logout){

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

        reference= FirebaseDatabase.getInstance().getReference("Patients").child(mobile);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Patient patient=snapshot.getValue(Patient.class);

                if(patient.getImageURL().equals("default")){
                    patient_profile_image.setImageResource(R.mipmap.ic_launcher);
                }

                else {

                    Glide.with(getApplicationContext()).load(patient.getImageURL()).into(patient_profile_image);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        patient_profile_image.setOnClickListener(new View.OnClickListener() {
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

        storageReference=storageReference.child("PatientImages/"+UUID.randomUUID().toString());

        storageReference.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String fileLocation=uri.toString();

                        reference2=FirebaseDatabase.getInstance().getReference("Patients").child(final_mobile);

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
                Log.i("MainActivity",e.getMessage());
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
        //Toast.makeText(this, "Back Press Disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();


        switch (item.getItemId()){

            case R.id.homeid:
                Home home=new Home();
                transaction.replace(R.id.main_body,home);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;

            case R.id.myprofile:
                profile prof=new profile();
                Bundle bundle = new Bundle();
                bundle.putString("user_email",final_email);
                bundle.putString("user_name",final_name);
                bundle.putString("user_age",final_age);
                bundle.putString("user_mobile",final_mobile);
                bundle.putString("user_gender",final_gender);
                bundle.putString("user_dob",finale_dob);
                bundle.putString("user_address",final_address);
                prof.setArguments(bundle);
                transaction.replace(R.id.main_body,prof);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.search_disease:
                searchdisease srch_dis=new searchdisease();
                Bundle bundle3=new Bundle();
                bundle3.putString("name",final_name);
                bundle3.putString("gender",final_gender);
                srch_dis.setArguments(bundle3);
                transaction.replace(R.id.main_body,srch_dis);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.search_doctor:
                searchdoctor srch_doc=new searchdoctor();
                Bundle bundle2 = new Bundle();
                bundle2.putString("patient_mobile",final_mobile);
                srch_doc.setArguments(bundle2);
                transaction.replace(R.id.main_body,srch_doc);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.feedback:
                feedback fback=new feedback();
                Bundle bundle4=new Bundle();
                bundle4.putString("patient_mobile",final_mobile);
                bundle4.putString("patient_name",final_name);
                fback.setArguments(bundle4);
                transaction.replace(R.id.main_body,fback);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.bookappointment:
                BookAppointment bookAppointment=new BookAppointment();
                Bundle apptbundle=new Bundle();
                apptbundle.putString("patient_name",final_name);
                apptbundle.putString("patient_mobile",final_mobile);
                bookAppointment.setArguments(apptbundle);
                transaction.replace(R.id.main_body,bookAppointment);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;
            case R.id.changepassword:
                ChangePassword changePassword=new ChangePassword();
                Bundle bundle5=new Bundle();
                bundle5.putString("patient_email",final_email);
                changePassword.setArguments(bundle5);
                transaction.replace(R.id.main_body,changePassword);
                transaction.commit();
                drawerLayout.closeDrawers();
                break;
        }
        return false;
    }
}