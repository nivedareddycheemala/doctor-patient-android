package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class DoctorRegistration extends AppCompatActivity {

    int c_date,c_month,c_year;
    private String dateofBirth;

    String gender;
    Button btn_register;
    EditText et1,et2,et3,et4,et5,et6,et7,et8;
    AwesomeValidation awesomeValidation;
    RadioButton r_male;
    RadioButton r_female;
    TextView tv_dob;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog pd;
    FirebaseAuth mAuth;

    List<Doctor> list;
    String imageURL="default";
    String status="offline";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        mAuth=FirebaseAuth.getInstance();

        reference=FirebaseDatabase.getInstance().getReference("Doctors");

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

        awesomeValidation=new AwesomeValidation(BASIC);

        et1=(EditText) findViewById(R.id.fullName);
        et2=(EditText) findViewById(R.id.emailAddress);
        et3=(EditText) findViewById(R.id.mobileNum);
        et4=(EditText) findViewById(R.id.age);
        et5=(EditText) findViewById(R.id.passWord);
        et6=(EditText) findViewById(R.id.rePassword);
        et7=(EditText) findViewById(R.id.locationaddress);
        et8=(EditText) findViewById(R.id.speciality);
        r_male=(RadioButton) findViewById(R.id.male);
        r_female=(RadioButton) findViewById(R.id.female);
        tv_dob=(TextView) findViewById(R.id.dob);

        btn_register=(Button) findViewById(R.id.patientRegister);

        awesomeValidation.addValidation(DoctorRegistration.this, R.id.fullName, "[a-zA-Z\\s]+", R.string.err_name);
        awesomeValidation.addValidation(DoctorRegistration.this, R.id.mobileNum, RegexTemplate.TELEPHONE, R.string.err_tel);
        awesomeValidation.addValidation(DoctorRegistration.this, R.id.emailAddress, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()) {

                    final String name = et1.getText().toString();
                    final String email = et2.getText().toString();
                    final String phone = et3.getText().toString();
                    final String age = et4.getText().toString();
                    final String password = et5.getText().toString();
                    String repassword = et6.getText().toString();
                    final String locationaddress = et7.getText().toString();
                    final String speciality=et8.getText().toString();
                    final String dob=dateofBirth;

                    gender=(r_male.isChecked())?"Male":"Female";

                    final String gend=gender;

                    if(name.isEmpty()||email.isEmpty()||phone.isEmpty()||age.isEmpty()||password.isEmpty()||repassword.isEmpty()||locationaddress.isEmpty()|| speciality.isEmpty()){

                        Toast.makeText(DoctorRegistration.this, "Please Fill All Details", Toast.LENGTH_SHORT).show();
                    }

                    else if(password.length()<8){
                        Toast.makeText(DoctorRegistration.this, "Password Length must be greater than or equal to 8", Toast.LENGTH_SHORT).show();
                    }

                    else if(!(password.equals(repassword))){
                        Toast.makeText(DoctorRegistration.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    }

                    else if(!r_male.isChecked() && !r_female.isChecked()){
                        Toast.makeText(DoctorRegistration.this, "Please Choose Gender", Toast.LENGTH_SHORT).show();
                    }

                    else if(tv_dob.getText().toString().isEmpty()){

                        Toast.makeText(DoctorRegistration.this, "Please Select your Date of Birth", Toast.LENGTH_SHORT).show();
                    }

                    else{

                        int temp=0;

                        for(int i=0;i<list.size();i++){

                            if((phone.equals(list.get(i).getPhone())) || (email.equals(list.get(i).getEmail()))){
                                temp=temp+1;
                                Toast.makeText(DoctorRegistration.this, "Email or Phone Number Already Exists", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        }

                        if(temp==0){

                            pd=new ProgressDialog(DoctorRegistration.this);
                            pd.setMessage("Registering Please Wait...");
                            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            pd.show();
                            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        Doctor doctor= new Doctor(name,email,phone,age,locationaddress,dob,gend,speciality,imageURL,status);

                                        reference.child(phone).setValue(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                pd.dismiss();
                                                Toast.makeText(DoctorRegistration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                Intent i=new Intent(DoctorRegistration.this,MainActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);

                                            }
                                        });
                                    }else{
                                        pd.dismiss();
                                        Toast.makeText(DoctorRegistration.this, "Can't Register with this details", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
//                            Patient patient= new Patient(name,email,phone,age,password,locationaddress,dob,gend);
//                            reference.child(phone).setValue(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    pd.dismiss();
//                                    Toast.makeText(PatientRegistration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                                    Intent i=new Intent(PatientRegistration.this,MainActivity.class);
//                                    startActivity(i);
//                                }
//                            });
                        }

                    }

                }

                else{

                    Toast.makeText(DoctorRegistration.this, "Awesome Validation Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    public void gotoMainPage(View view) {

        Intent i= new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void dobpicker(View view) {
        Calendar c=Calendar.getInstance();
        c_year=c.get(Calendar.YEAR);
        c_month=c.get(Calendar.MONTH);
        c_date=c.get(Calendar.DATE);

        DatePickerDialog pickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dateofBirth="";

                dateofBirth=dateofBirth+dayOfMonth+"-"+(month+1)+"-"+year;
                tv_dob.setText(dateofBirth);

            }
        },c_year,c_month,c_date);

        pickerDialog.show();
    }
}