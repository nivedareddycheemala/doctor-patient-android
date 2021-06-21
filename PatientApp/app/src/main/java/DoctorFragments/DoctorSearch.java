package DoctorFragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.patientapp.Doctor;
import com.example.patientapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import Adapter.DoctorAdapter;


public class DoctorSearch extends Fragment {

    AppCompatSpinner spinner;
    EditText materialEditText;
    MaterialButton materialButton;
    ProgressDialog pd;

    String category;

    RecyclerView rv;

    List<Doctor> doctorList;
    DoctorAdapter doctorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String patientmobile = getArguments().getString("patient_mobile");

        View view = inflater.inflate(R.layout.fragment_doctor_search, container, false);

        spinner= view.findViewById(R.id.searchcategory);
        materialEditText=(EditText) view.findViewById(R.id.details);
        materialButton=(MaterialButton) view.findViewById(R.id.submit);

        rv=(RecyclerView) view.findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        doctorList=new ArrayList<>();

        ArrayAdapter<CharSequence> searchcatAdapter= ArrayAdapter.createFromResource(getActivity(),R.array.DoctorSearch,R.layout.spinner_list);

        spinner.setAdapter(searchcatAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    category=spinner.getItemAtPosition(position).toString();
                }
                else{
                    category="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String details=materialEditText.getText().toString();
                if(category.equals("")){
                    Toast.makeText(getActivity(), "Please Select Category", Toast.LENGTH_SHORT).show();
                }

                else if(details.isEmpty()){
                    Toast.makeText(getActivity() ,"Please Enter details", Toast.LENGTH_SHORT).show();
                }

                else{
                    pd=new ProgressDialog(getActivity());
                    pd.setMessage("Please wait");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.show();

                    final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Doctors");

                    if(category.equals("Name")){


                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                doctorList.clear();
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                                    Doctor doctors=dataSnapshot.getValue(Doctor.class);

                                    if((doctors.getName().toLowerCase()).contains(details.toLowerCase())){
                                        doctorList.add(doctors);
                                    }
                                }

                                doctorAdapter =new DoctorAdapter(getContext(),doctorList,patientmobile);
                                rv.setAdapter(doctorAdapter);
                                pd.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    else if(category.equals("Mobile")){

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                doctorList.clear();
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                                    Doctor doctors=dataSnapshot.getValue(Doctor.class);

                                    if(doctors.getPhone().equals(details)){
                                        doctorList.add(doctors);
                                    }
                                }

                                doctorAdapter =new DoctorAdapter(getContext(),doctorList,patientmobile);
                                rv.setAdapter(doctorAdapter);
                                pd.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    else if(category.equals("Email")){


                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                doctorList.clear();
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                                    Doctor doctors=dataSnapshot.getValue(Doctor.class);

                                    if(doctors.getEmail().equals(details)){
                                        doctorList.add(doctors);
                                    }
                                }

                                doctorAdapter =new DoctorAdapter(getContext(),doctorList,patientmobile);
                                rv.setAdapter(doctorAdapter);
                                pd.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    else if(category.equals("Location")){


                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                doctorList.clear();
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                                    Doctor doctors=dataSnapshot.getValue(Doctor.class);

                                    if((doctors.getLocationaddress().toLowerCase()).contains(details.toLowerCase())){
                                        doctorList.add(doctors);
                                    }
                                }

                                doctorAdapter =new DoctorAdapter(getContext(),doctorList,patientmobile);
                                rv.setAdapter(doctorAdapter);
                                pd.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    else {


                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                doctorList.clear();
                                for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                                    Doctor doctors=dataSnapshot.getValue(Doctor.class);

                                    if((doctors.getSpeciality().toLowerCase()).contains(details.toLowerCase())){
                                        doctorList.add(doctors);
                                    }
                                }

                                doctorAdapter =new DoctorAdapter(getContext(),doctorList,patientmobile);
                                rv.setAdapter(doctorAdapter);
                                pd.dismiss();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });

        return  view;
    }
}