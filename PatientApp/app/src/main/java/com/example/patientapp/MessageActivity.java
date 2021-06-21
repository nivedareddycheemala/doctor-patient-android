package com.example.patientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapter.MessageAdapter;
import Models.Chat;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView tv_doctonname;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;
    ImageButton button_send;
    EditText text_send;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference reference2;
    DatabaseReference reference3;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    ValueEventListener seenListener;

    String patientmobileforstatus;

    String doctormobileforcall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference2=firebaseDatabase.getReference();
        Toolbar toolbar= findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu3);
        getSupportActionBar();
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.callDoctor){

                    Uri u= Uri.parse("tel:"+doctormobileforcall);
                    Intent i=new Intent(Intent.ACTION_DIAL,u);
                    startActivity(i);

                }
                return false;
            }
        });

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image=(CircleImageView) findViewById(R.id.profile_image);
        tv_doctonname=(TextView) findViewById(R.id.doctorname);
        button_send=(ImageButton) findViewById(R.id.btn_send);
        text_send=(EditText) findViewById(R.id.text_send);
        intent=getIntent();
        final String doctormobile=intent.getStringExtra("mobileno");
        final String patientmobile=intent.getStringExtra("patient_mobile");
        patientmobileforstatus=patientmobile;
        doctormobileforcall=doctormobile;

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=text_send.getText().toString();
                if(!msg.equals("")){

                    sendMessagetoDoctor(patientmobile,doctormobile,msg);
                }
                else{
                    Toast.makeText(MessageActivity.this, "Please write something to send", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });


        reference= FirebaseDatabase.getInstance().getReference("Doctors").child(doctormobile);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Doctor doctor=snapshot.getValue(Doctor.class);
                tv_doctonname.setText(doctor.getName());
                if(doctor.getImageURL().equals("default")){

                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }

                else{
                    Glide.with(getApplicationContext()).load(doctor.getImageURL()).into(profile_image);

                }
                ReadMessage(patientmobile,doctormobile,doctor.getImageURL());

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void seenMessage(final String userid){
        reference3=FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Chat chat=dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(patientmobileforstatus) && chat.getSender().equals(userid)){

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        dataSnapshot.getRef().updateChildren(hashMap);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        reference2.child("Patients").child(patientmobileforstatus).child("status").setValue("online");
//    }

    private void sendMessagetoDoctor(String sender, String receiver, String message){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);

        reference.child("Chats").push().setValue(hashMap);
    }

    private void ReadMessage(final String mymobile, final String doctormobile, final String imageURL){

        mChat=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mChat.clear();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Chat chat=dataSnapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(mymobile) && chat.getSender().equals(doctormobile) ||
                    chat.getReceiver().equals(doctormobile) && chat.getSender().equals(mymobile)){

                        mChat.add(chat);
                    }

                    messageAdapter=new MessageAdapter(MessageActivity.this,mChat,doctormobile,imageURL);
                    recyclerView.setAdapter(messageAdapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onResume() {

        seenMessage(doctormobileforcall);

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference3.removeEventListener(seenListener);
    }
}