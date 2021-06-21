package com.example.patientapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Models.Appointmentdata;

public class DetailsforAppointment extends AppCompatActivity {

    AppCompatSpinner locationspinner,specialityspinner;
    ImageButton searchbtn;
    String selectedLocation,selectedSpeciality;
    List<Doctor> doctorList;
    List<Appointmentdata> appointmentdataList;
    List<String> locationList;
    List<String> specialityList;
    List<String> appointeddoctorsList;
    List<String> appointedpatientsList;
    TextView tv_doctorname,tv_doctornumber,appointmentdateview;
    int c_date,c_month,c_year;
    private String appointmentdate="";
    Button appointmentdatepicker,bookbuttonfinal,cancelbutton;
    DatabaseReference databaseReference;
    String apptstatus="Pending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailsfor_appointment);

        databaseReference=FirebaseDatabase.getInstance().getReference();

        appointmentdatepicker=(Button) findViewById(R.id.appointmentdatepicker);
        bookbuttonfinal=(Button) findViewById(R.id.bookbuttonfinal);
        cancelbutton=(Button) findViewById(R.id.cancelbutton);

        tv_doctorname=(TextView) findViewById(R.id.doctorname);
        tv_doctornumber=(TextView) findViewById(R.id.doctornumber);
        appointmentdateview=(TextView) findViewById(R.id.appointmentdateview);

        locationspinner=findViewById(R.id.locationspinner);
        specialityspinner=findViewById(R.id.specialityspinner);
        searchbtn=(ImageButton) findViewById(R.id.searchbtn);
        doctorList=new ArrayList<>();
        appointmentdataList=new ArrayList<>();
        appointeddoctorsList=new ArrayList<>();
        appointedpatientsList=new ArrayList<>();
        locationList=new ArrayList<>();
        specialityList=new ArrayList<>();
        locationList.add("Please select location");
        specialityList.add("Please Select Speciality");

        final String patientname = getIntent().getStringExtra("patient_name");
        final String patientmobile = getIntent().getStringExtra("patient_mobile");

        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference("Doctors");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                doctorList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Doctor doctors=dataSnapshot.getValue(Doctor.class);

                    doctorList.add(doctors);
                }

                for(int i=0;i<doctorList.size();i++){

                    String location=doctorList.get(i).getLocationaddress();
                    String speciality=doctorList.get(i).getSpeciality();
                    locationList.add(location);
                    specialityList.add(speciality);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final ArrayAdapter<String> locationadapter = new ArrayAdapter<String>(DetailsforAppointment.this, android.R.layout.simple_spinner_dropdown_item,locationList);
        locationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationspinner.setAdapter(locationadapter);

        locationspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0){
                    selectedLocation=locationspinner.getItemAtPosition(position).toString();
                }
                else{
                    selectedLocation="";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<String> specialityadapter = new ArrayAdapter<String>(DetailsforAppointment.this, android.R.layout.simple_spinner_dropdown_item,specialityList);
        specialityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialityspinner.setAdapter(specialityadapter);

        specialityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position!=0){
                    selectedSpeciality=specialityspinner.getItemAtPosition(position).toString();
                }
                else{
                    selectedSpeciality="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedLocation.isEmpty()){
                    tv_doctornumber.setText("");
                    tv_doctorname.setText("");
                    Toast.makeText(DetailsforAppointment.this, "Please Select Location", Toast.LENGTH_SHORT).show();
                }

                else if(selectedSpeciality.isEmpty()){
                    tv_doctornumber.setText("");
                    tv_doctorname.setText("");
                    Toast.makeText(DetailsforAppointment.this, "Please Select Speciality", Toast.LENGTH_SHORT).show();
                }

                else{
                    int temp=0;
                    for(int i=0;i<doctorList.size();i++){

                        if(doctorList.get(i).getLocationaddress()==selectedLocation && doctorList.get(i).getSpeciality()==selectedSpeciality){
                            temp=temp+1;
                            String doctorname=doctorList.get(i).getName();
                            String doctornumber=doctorList.get(i).getPhone();
                            tv_doctorname.setText(doctorname);
                            tv_doctornumber.setText(doctornumber);
                            break;
                        }
                    }

                    if(temp==0){
                        Toast.makeText(DetailsforAppointment.this, "Doctor not found", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        appointmentdatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                c_year=c.get(Calendar.YEAR);
                c_month=c.get(Calendar.MONTH);
                c_date=c.get(Calendar.DATE);

                DatePickerDialog pickerDialog=new DatePickerDialog(DetailsforAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        appointmentdate="";

                        appointmentdate=appointmentdate+dayOfMonth+"-"+(month+1)+"-"+year;
                        appointmentdateview.setText(appointmentdate);

                    }
                },c_year,c_month,c_date);

                pickerDialog.show();
            }
        });

        DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference("Appointments");

        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                appointmentdataList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Appointmentdata appointmentdata =dataSnapshot.getValue(Appointmentdata.class);

                    appointmentdataList.add(appointmentdata);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        bookbuttonfinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedLocation.isEmpty()){
                    Toast.makeText(DetailsforAppointment.this, "Please Choose Location", Toast.LENGTH_SHORT).show();
                }

                else if(selectedSpeciality.isEmpty()){
                    Toast.makeText(DetailsforAppointment.this, "Please Choose Speciality", Toast.LENGTH_SHORT).show();
                }

                else if(tv_doctorname.getText().toString().isEmpty() || tv_doctornumber.getText().toString().isEmpty()){
                    Toast.makeText(DetailsforAppointment.this, "Please Find Doctor First", Toast.LENGTH_SHORT).show();
                }

                else if(appointmentdate.isEmpty()){
                    Toast.makeText(DetailsforAppointment.this, "Please Choose appointment date", Toast.LENGTH_SHORT).show();
                }

                else{
                    int temp=0;
                    for(int i=0;i<appointmentdataList.size();i++){

                        String doctorsname=appointmentdataList.get(i).getDoctorname();
                        String patientsname=appointmentdataList.get(i).getPatientname();
                        if(doctorsname.equals(tv_doctorname.getText().toString()) && patientsname.equals(patientname)){
                            temp=temp+1;
                            Toast.makeText(DetailsforAppointment.this, "Already appointed with this doctor", Toast.LENGTH_SHORT).show();
                            break;

                        }
                    }

                    if(temp==0){

                        Appointmentdata appointmentdata = new Appointmentdata(patientname, tv_doctorname.getText().toString(), appointmentdate, tv_doctornumber.getText().toString(), patientmobile,apptstatus);

                        databaseReference.child("Appointments").push().setValue(appointmentdata).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(DetailsforAppointment.this, "Appointment Booked Successfully", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        });
                    }
                }

            }
        });


        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}