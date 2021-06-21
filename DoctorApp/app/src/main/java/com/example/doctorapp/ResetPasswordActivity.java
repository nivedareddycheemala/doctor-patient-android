package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText materialEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth=FirebaseAuth.getInstance();
        materialEditText=(EditText) findViewById(R.id.resetEmailDoctor);
    }

    @Override
    public void onBackPressed() {

    }

    public void gotoMainPage(View view) {
        Intent i= new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void resetPassword(View view) {

        String doctor_email=materialEditText.getText().toString();

        if(doctor_email.isEmpty()){
            Toast.makeText(this, "Please Enter email address", Toast.LENGTH_SHORT).show();

        }

        else{

            mAuth.sendPasswordResetEmail(doctor_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        Toast.makeText(ResetPasswordActivity.this, "Please Open Your mail to Reset Password", Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(ResetPasswordActivity.this,MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }

                    else{

                        Toast.makeText(ResetPasswordActivity.this, "Please Enter correct Email address", Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }

    }
}