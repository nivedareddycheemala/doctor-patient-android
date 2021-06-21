package com.example.patientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    MaterialButton signin;
    DatabaseReference reference;
    List<Patient> list;
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        et_email=(EditText) findViewById(R.id.patientEmail);
        et_password=(EditText) findViewById(R.id.patientPassword);
        signin=(MaterialButton) findViewById(R.id.buttonSignIn);
        reference= FirebaseDatabase.getInstance().getReference("Patients");
        list=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Patient patient=dataSnapshot.getValue(Patient.class);
                    list.add(patient);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void patientSignUp(View view) {

        Intent i=new Intent(this,PatientRegistration.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void resetPassword(View view) {
        Intent i=new Intent(this,ResetPasswordActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void PatientSignIn(View view) {
        pd=new ProgressDialog(MainActivity.this);
        pd.setMessage("Logging in");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();
        final String useremail = et_email.getText().toString();
        final String userpass = et_password.getText().toString();

        if (useremail.isEmpty() || userpass.isEmpty()) {
            pd.dismiss();
            Toast.makeText(this, "Please Enter details", Toast.LENGTH_SHORT).show();
        }

        else{

        firebaseAuth.signInWithEmailAndPassword(useremail, userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    int temp = 0;

                    for (int i = 0; i < list.size(); i++) {

                        if ((useremail.equals(list.get(i).getEmail()))) {

                            pd.dismiss();
                            temp = temp + 1;
                            String u_name = list.get(i).getName();
                            String u_age = list.get(i).getAge();
                            String u_mobile = list.get(i).getPhone();
                            String u_gender = list.get(i).getGend();
                            String u_dob = list.get(i).getDateofbirth();
                            String u_address = list.get(i).getLocationaddress();
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            intent.putExtra("useremail", useremail);
                            intent.putExtra("username", u_name);
                            intent.putExtra("userage", u_age);
                            intent.putExtra("usermobile", u_mobile);
                            intent.putExtra("usergender", u_gender);
                            intent.putExtra("userdob", u_dob);
                            intent.putExtra("useraddress", u_address);
                            startActivity(intent);
                            break;

                        }

                    }

                    if (temp == 0) {

                        pd.dismiss();

                        Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    pd.dismiss();

                    Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

//        for(int i=0;i<list.size();i++){
//
//            if((useremail.equals(list.get(i).getEmail())) && (userpass.equals(list.get(i).getPassword())) ){
//
//                temp=temp+1;
//                String u_name=list.get(i).getName();
//                String u_age=list.get(i).getAge();
//                String u_mobile=list.get(i).getPhone();
//                String u_password=list.get(i).getPassword();
//                String u_gender=list.get(i).getGend();
//                String u_dob=list.get(i).getDateofbirth();
//                String u_address=list.get(i).getLocationaddress();
//                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(MainActivity.this,WelcomeActivity.class);
//                intent.putExtra("useremail", useremail);
//                intent.putExtra("username", u_name);
//                intent.putExtra("userage", u_age);
//                intent.putExtra("usermobile", u_mobile);
//                intent.putExtra("userpassword", u_password);
//                intent.putExtra("usergender", u_gender);
//                intent.putExtra("userdob", u_dob);
//                intent.putExtra("useraddress", u_address);
//                startActivity(intent);
//                break;
//
//            }
//
//        }
//
//        if(temp==0){
//
//            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
//        }

    }
}