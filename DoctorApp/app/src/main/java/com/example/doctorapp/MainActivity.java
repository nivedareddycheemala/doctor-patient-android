package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText et_email,et_password;
    DatabaseReference reference;
    List<Doctor> list;
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        et_email=(EditText) findViewById(R.id.doctorEmail);
        et_password=(EditText) findViewById(R.id.doctorPassword);
        reference= FirebaseDatabase.getInstance().getReference("Doctors");
        list=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Doctor doctor=dataSnapshot.getValue(Doctor.class);
                    list.add(doctor);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void DoctorSignIn(View view) {

        pd=new ProgressDialog(MainActivity.this);
        pd.setMessage("Logging in");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();

        final String useremail = et_email.getText().toString();
        final String userpass = et_password.getText().toString();

        if ((useremail.isEmpty()) || (userpass.isEmpty())) {

            pd.dismiss();

            Toast.makeText(this, "Please Fill the details", Toast.LENGTH_SHORT).show();
        } else {


        firebaseAuth.signInWithEmailAndPassword(useremail, userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    for (int i = 0; i < list.size(); i++) {

                        if ((useremail.equals(list.get(i).getEmail()))) {

                            pd.dismiss();
                            String u_name = list.get(i).getName();
                            String u_mobile = list.get(i).getPhone();
                            String u_email=list.get(i).getEmail();
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            intent.putExtra("username", u_name);
                            intent.putExtra("usermobile", u_mobile);
                            intent.putExtra("useremail",u_email);

                            startActivity(intent);
                            break;

                        }

                    }

                } else {

                    pd.dismiss();

                    Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
    }

    public void doctorSignUp(View view) {
        Intent i=new Intent(this,DoctorRegistration.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void resetPassword(View view) {
        Intent i=new Intent(this,ResetPasswordActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

}